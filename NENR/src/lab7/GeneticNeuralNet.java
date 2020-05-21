package lab7;

import java.util.Arrays;
import java.util.Random;

import lab5.SigmoidNeuron;

public class GeneticNeuralNet {
	
	private int inputLayerSize;
	private Type1Neuron[] secondLayer;
	private SigmoidNeuron[][] layers;	
	
	public GeneticNeuralNet(String architecture) {
		
		String[] layerDimensions = architecture.split("x");
		inputLayerSize = Integer.parseInt(layerDimensions[0]);
		
		if(inputLayerSize != 2) throw new IllegalArgumentException("This net curently only works with 2D input data, input layer must be size 2!");
		if(Integer.parseInt(layerDimensions[layerDimensions.length - 1]) != 3) throw new IllegalArgumentException("This net currently only works for output layer size 3!");
		
		secondLayer = new Type1Neuron[Integer.parseInt(layerDimensions[1])];
		for(int i = 0; i < secondLayer.length; i++) {
			secondLayer[i] = new Type1Neuron(inputLayerSize);
		}
		
		layers = new SigmoidNeuron[layerDimensions.length - 1][];
		
		for(int i = 0; i < layerDimensions.length - 1; i++) {
			layers[i] = new SigmoidNeuron[Integer.parseInt(layerDimensions[i + 1])];
			for(int j = 0; j < layers[i].length; j++) {
				if(i > 0) {
					layers[i][j] = new SigmoidNeuron(layers[i-1].length);
				}
				else {
					layers[i][j] = new SigmoidNeuron(secondLayer.length);
				}
			}
		}
	}
	
	public GeneticNeuralNet(Type1Neuron[] secondLayer, SigmoidNeuron[][] layers, int inputLayerSize) {
		if(inputLayerSize != 2) throw new IllegalArgumentException("This net curently only works with 2D input data, input layer must be size 2!");
		this.inputLayerSize = inputLayerSize;
		this.secondLayer = secondLayer;
		this.layers = layers;
	}

	public double[] forwardPass(double[] inputs) {
		
		if(inputs.length != inputLayerSize) throw new IllegalArgumentException("Input must match input layer dimension!");
		
		double[] currentLayerOut = Arrays.copyOf(inputs, inputs.length);
		
		double[] previousLayerOut = Arrays.copyOf(currentLayerOut, currentLayerOut.length);
		currentLayerOut = new double[secondLayer.length];
		
		for(int j = 0; j < secondLayer.length; j++) {
			currentLayerOut[j] = secondLayer[j].passThrough(previousLayerOut);
		}
		
		for(int i = 0; i < layers.length; i++) {
			previousLayerOut = Arrays.copyOf(currentLayerOut, currentLayerOut.length);
			currentLayerOut = new double[layers[i].length];
			
			for(int j = 0; j < layers[i].length; j++) {
				currentLayerOut[j] = layers[i][j].passThrough(previousLayerOut);
			}
		}
		return currentLayerOut;
	}
	
	public double getError(String[] samples) {
		double error = 0;
		
		for(String sample : samples) {
			String[] parts = sample.split("\\s+");
			String[] input = Arrays.copyOfRange(parts, 0, parts.length - layers[layers.length - 1].length);
			String[] output = Arrays.copyOfRange(parts, parts.length - layers[layers.length - 1].length, parts.length);
			
			double[] inputParsed = new double[input.length];
			for(int i = 0; i < inputParsed.length; i++) {
				inputParsed[i] = Double.parseDouble(input[i]);
			}
			
			double[] outputParsed = new double[output.length];
			for(int i = 0; i < outputParsed.length; i++) {
				outputParsed[i] = Double.parseDouble(output[i]);
			}
			
			double[] netOut = forwardPass(inputParsed);
			//System.out.println("Net says: " + Arrays.toString(netOut) + ", Correct: " + Arrays.toString(outputParsed));
			for(int i = 0; i < outputParsed.length; i++) {
				error += Math.pow(outputParsed[i] - netOut[i], 2);
			}
		}
		
		return error / samples.length;
	}
	
	public Type1Neuron[] getType1Layer() {
		return secondLayer;
	}
	
	public SigmoidNeuron[][] getSigmoidLayers() {
		return layers;
	}
	
	public int getInputLayerSize() {
		return inputLayerSize;
	}
	
	public void mutation(double mutationProb, double stdDev, boolean type1) {
		
		Random random = new Random();
		
		for(Type1Neuron n : secondLayer) {
			int numberOfConnections = n.getInputConnections();
			for(int i = 0; i < numberOfConnections; i++) {
				if(random.nextDouble() <= mutationProb) {
					if(type1) n.setWeight(i, n.getWeight(i) + (random.nextGaussian() * stdDev));
					else n.setWeight(i, random.nextGaussian() * stdDev);
				}
				if(random.nextDouble() <= mutationProb) {
					if(type1) n.setS(i, n.getS(i) + (random.nextGaussian() * stdDev));
					else n.setS(i, random.nextGaussian() * stdDev);
				}
			}
		}
		
		for(SigmoidNeuron[] layer : layers) {
			for(SigmoidNeuron n : layer) {
				int numberOfConnections = n.getInputConnections();
				for(int i = 0; i < numberOfConnections; i++) {
					if(random.nextDouble() <= mutationProb) {
						if(type1) n.setWeight(i, n.getWeight(i) + (random.nextGaussian() * stdDev));
						else n.setWeight(i, random.nextGaussian() * stdDev);
					}
				}
			}
		}
	}
}
