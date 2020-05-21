package lab5;

import java.util.Arrays;

public class NeuralNet {

	private int inputLayerSize;
	private SigmoidNeuron[][] layers;
	
	public NeuralNet(String architecture) {
		
		String[] layerDimensions = architecture.split("x");
		inputLayerSize = Integer.parseInt(layerDimensions[0]);
		
		layers = new SigmoidNeuron[layerDimensions.length - 1][];
		
		for(int i = 0; i < layerDimensions.length - 1; i++) {
			layers[i] = new SigmoidNeuron[Integer.parseInt(layerDimensions[i + 1])];
			for(int j = 0; j < layers[i].length; j++) {
				if(i > 0) {
					layers[i][j] = new SigmoidNeuron(layers[i-1].length);
				}
				else {
					layers[i][j] = new SigmoidNeuron(inputLayerSize);
				}
			}
		}
	}
	
	public double[] forwardPass(double[] inputs) {
		if(inputs.length != inputLayerSize) throw new IllegalArgumentException("Input must match input layer dimension!");
		
		double[] currentLayerOut = Arrays.copyOf(inputs, inputs.length);
		
		for(int i = 0; i < layers.length; i++) {
			double[] previousLayerOut = Arrays.copyOf(currentLayerOut, currentLayerOut.length);
			currentLayerOut = new double[layers[i].length];
			
			for(int j = 0; j < layers[i].length; j++) {
				currentLayerOut[j] = layers[i][j].passThrough(previousLayerOut);
			}
		}
		return currentLayerOut;
	}
	
	public void backprop(int roundsOfLearning, String[] samples, double learningRate) {
		int round = 0;
		while(round < roundsOfLearning) {
			round++;
			double[][][] weightUpdates = new double[layers.length][][];
			for(int a = 0; a < layers.length; a++) {
				weightUpdates[a] = new double[layers[a].length][];
				for(int b = 0; b < weightUpdates[a].length; b++) {
					weightUpdates[a][b] = new double[a > 0 ? layers[a - 1].length : inputLayerSize];
				}
			}
			
			//For every Sample
			for(String sample : samples) {
				//Parse data
				String[] parts = sample.split(",");
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
				
				//Forward Pass
				//double[] netOut = forwardPass(inputParsed);
				forwardPass(inputParsed);
				//System.out.println("Net says: " + Arrays.toString(netOut) + "Correct: " + Arrays.toString(outputParsed));
				
				double[] deltas = new double[layers[layers.length - 1].length];
				
				//last layer weights
				//For every neuron
				for(int j = 0; j < outputParsed.length; j++) {
					SigmoidNeuron n = layers[layers.length - 1][j];
					//System.out.println("neuron before update: " + n.toString());
					//For every connection
					for(int i = 0; i < layers[layers.length - 2].length; i++) {
						
						double delta = n.getOutput() * (1 - n.getOutput()) * (outputParsed[j] - n.getOutput());
						deltas[j] = delta;
						
						weightUpdates[layers.length - 1][j][i] += (learningRate * delta * layers[layers.length - 2][i].getOutput()) / samples.length;
					}
					//System.out.println("neuron after update: " + n.toString() + "\n");
					//System.out.println("neuron: " + n.toString());
				}
				//System.out.println("next round!");
				
				//All other layers
				for(int k = layers.length - 2; k >= 0; k--) {
					//every neuron of layer
					
					double[] oldDeltas = Arrays.copyOf(deltas, deltas.length);
					deltas = new double[layers[k].length];
					
					for(int j = 0; j < layers[k].length; j++) {
						SigmoidNeuron n = layers[k][j];
						//every connection
						for(int i = 0; i < (k > 0 ? layers[k - 1].length : inputLayerSize); i++) {
							
							double deltaForward = 0;
							for(int l = 0; l < oldDeltas.length; l++) {
								deltaForward += oldDeltas[l] * layers[k+1][l].getWeight(j);
							}
							double delta = n.getOutput() * (1 - n.getOutput()) * deltaForward;
							deltas[j] = delta;
							
							weightUpdates[k][j][i] += (learningRate * delta * (k > 0 ? layers[k-1][i].getOutput() : inputParsed[i])) / samples.length;
						}
					}
				}
			}
			for(int a = 0; a < layers.length; a++) {
				for(int b = 0; b < layers[a].length; b++) {
					for(int c = 0; c < (a > 0 ? layers[a - 1].length : inputLayerSize); c++) {
						layers[a][b].setWeight(c, layers[a][b].getWeight(c) + weightUpdates[a][b][c]);
						
						//n.setWeight(i, n.getWeight(i) + (learningRate * delta * (k > 0 ? layers[k-1][i].getOutput() : inputParsed[i])));
					}
				}
			}
			if(round % 100 == 0) System.out.println("Round " + round + " , error: " + error(samples));
		}
	}
	
	public void batchBackpop(int roundsOfLearning, String[] samples, double learningRate, int batchSize) {
		
		int round = 0;
		double[][][] weightUpdates = new double[layers.length][][];
		for(int a = 0; a < layers.length; a++) {
			weightUpdates[a] = new double[layers[a].length][];
			for(int b = 0; b < weightUpdates[a].length; b++) {
				weightUpdates[a][b] = new double[a > 0 ? layers[a - 1].length : inputLayerSize];
			}
		}
		while(round < roundsOfLearning) {
			round++;
			
			//For every Sample
			for(int sampleCounter = 0; sampleCounter < samples.length; sampleCounter++) {
				
				String sample = samples[sampleCounter];
				
				if(sampleCounter % batchSize == 0) {
					
					//update weights
					for(int a = 0; a < layers.length; a++) {
						for(int b = 0; b < layers[a].length; b++) {
							for(int c = 0; c < (a > 0 ? layers[a - 1].length : inputLayerSize); c++) {
								layers[a][b].setWeight(c, layers[a][b].getWeight(c) + weightUpdates[a][b][c]);
								
								//n.setWeight(i, n.getWeight(i) + (learningRate * delta * (k > 0 ? layers[k-1][i].getOutput() : inputParsed[i])));
							}
						}
					}
					
					//reset weight updates
					weightUpdates = new double[layers.length][][];
					for(int a = 0; a < layers.length; a++) {
						weightUpdates[a] = new double[layers[a].length][];
						for(int b = 0; b < weightUpdates[a].length; b++) {
							weightUpdates[a][b] = new double[a > 0 ? layers[a - 1].length : inputLayerSize];
						}
					}
				}
				
				//Parse data
				String[] parts = sample.split(",");
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
				
				//Forward Pass
				//double[] netOut = forwardPass(inputParsed);
				forwardPass(inputParsed);
				//System.out.println("Net says: " + Arrays.toString(netOut) + "Correct: " + Arrays.toString(outputParsed));
				
				double[] deltas = new double[layers[layers.length - 1].length];
				
				//last layer weights
				//For every neuron
				for(int j = 0; j < outputParsed.length; j++) {
					SigmoidNeuron n = layers[layers.length - 1][j];
					//System.out.println("neuron before update: " + n.toString());
					//For every connection
					for(int i = 0; i < layers[layers.length - 2].length; i++) {
						
						double delta = n.getOutput() * (1 - n.getOutput()) * (outputParsed[j] - n.getOutput());
						deltas[j] = delta;
						
						weightUpdates[layers.length - 1][j][i] += (learningRate * delta * layers[layers.length - 2][i].getOutput()) / samples.length;
					}
					//System.out.println("neuron after update: " + n.toString() + "\n");
					//System.out.println("neuron: " + n.toString());
				}
				//System.out.println("next round!");
				
				//All other layers
				for(int k = layers.length - 2; k >= 0; k--) {
					//every neuron of layer
					
					double[] oldDeltas = Arrays.copyOf(deltas, deltas.length);
					deltas = new double[layers[k].length];
					
					for(int j = 0; j < layers[k].length; j++) {
						SigmoidNeuron n = layers[k][j];
						//every connection
						for(int i = 0; i < (k > 0 ? layers[k - 1].length : inputLayerSize); i++) {
							
							double deltaForward = 0;
							for(int l = 0; l < oldDeltas.length; l++) {
								deltaForward += oldDeltas[l] * layers[k+1][l].getWeight(j);
							}
							double delta = n.getOutput() * (1 - n.getOutput()) * deltaForward;
							deltas[j] = delta;
							
							weightUpdates[k][j][i] += (learningRate * delta * (k > 0 ? layers[k-1][i].getOutput() : inputParsed[i])) / samples.length;
						}
					}
				}
			}
			if(round % 100 == 0) System.out.println("Round " + round + " , error: " + error(samples));
		}
	}
	
	public void stohasticBackprop(int roundsOfLearning, String[] samples, double learningRate) {
		int round = 0;
		while(round < roundsOfLearning) {
			round++;
			
			//For every Sample
			for(String sample : samples) {
				
				//Parse data
				String[] parts = sample.split(",");
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
				
				//Forward Pass
				//double[] netOut = forwardPass(inputParsed);
				forwardPass(inputParsed);
				//System.out.println("Net says: " + Arrays.toString(netOut) + "Correct: " + Arrays.toString(outputParsed));
				
				double[] deltas = new double[layers[layers.length - 1].length];
				
				//last layer weights
				//For every neuron
				for(int j = 0; j < outputParsed.length; j++) {
					SigmoidNeuron n = layers[layers.length - 1][j];
					//System.out.println("neuron before update: " + n.toString());
					//For every connection
					for(int i = 0; i < layers[layers.length - 2].length; i++) {
						
						double delta = n.getOutput() * (1 - n.getOutput()) * (outputParsed[j] - n.getOutput());
						deltas[j] = delta;
						
						
						n.setWeight(i, n.getWeight(i) + (learningRate * delta * layers[layers.length - 2][i].getOutput()));
					}
					//System.out.println("neuron after update: " + n.toString() + "\n");
					//System.out.println("neuron: " + n.toString());
				}
				//System.out.println("next round!");
				
				//All other layers
				for(int k = layers.length - 2; k >= 0; k--) {
					//every neuron of layer
					
					double[] oldDeltas = Arrays.copyOf(deltas, deltas.length);
					deltas = new double[layers[k].length];
					
					for(int j = 0; j < layers[k].length; j++) {
						SigmoidNeuron n = layers[k][j];
						//every connection
						for(int i = 0; i < (k > 0 ? layers[k - 1].length : inputLayerSize); i++) {
							
							double deltaForward = 0;
							for(int l = 0; l < oldDeltas.length; l++) {
								deltaForward += oldDeltas[l] * layers[k+1][l].getWeight(j);
							}
							double delta = n.getOutput() * (1 - n.getOutput()) * deltaForward;
							deltas[j] = delta;
							
							n.setWeight(i, n.getWeight(i) + (learningRate * delta * (k > 0 ? layers[k-1][i].getOutput() : inputParsed[i])));
						}
					}
				}
			}
			
			if(round % 100 == 0) System.out.println("Round " + round + " , error: " + error(samples));
		}
	}
	
	public double error(String[] samples) {
		double e = 0;
		
		for(String sample : samples) {
			String[] parts = sample.split(",");
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
				e += Math.pow(outputParsed[i] - netOut[i], 2);
			}
		}
		
		return e / (2 * samples.length);
	}
}
