package lab7;

import java.util.Random;
import lab5.SigmoidNeuron;

public class GeneticNetTrainer {

	private double stdDevM1, stdDevM2, stdDevM3, mutProbM1_M2, mutProbM3;
	private double desirability1, desirability2, desirability3;
	private int populationSize;
	private String[] samples;
	private GeneticNeuralNet[] population;
	private double[] goodnessValues;
	private GeneticNeuralNet bestNet;
	private double bestGoodness = 0;
	
	public GeneticNetTrainer(int populationSize, String architecture, String[] samples,
							 double stdDevM1, double stdDevM2, double stdDevM3, 
							 double mutProbM1_M2, double mutProbM3,
							 double t1, double t2, double t3) {
		if(t1 + t2 + t3 <= 0 || t1 < 0 || t2 < 0 || t3 < 0) throw new IllegalArgumentException("All t values must be >= 0! At least one must be more than 0!");
		else {
			double sum = t1 + t2 + t3;
			desirability1 = t1 / sum;
			desirability2 = t2 / sum;
			desirability3 = t3 / sum;
		}
		this.stdDevM1 = stdDevM1;
		this.stdDevM2 = stdDevM2;
		this.stdDevM3 = stdDevM3;
		this.mutProbM1_M2 = mutProbM1_M2;
		this.mutProbM3 = mutProbM3;
		this.samples = samples;
		this.populationSize = populationSize;
		population = new GeneticNeuralNet[populationSize];
		goodnessValues = new double[populationSize];
		for(int i = 0; i < populationSize; i++) {
			population[i] = new GeneticNeuralNet(architecture);
			goodnessValues[i] = goodness(population[i]);
			if(goodnessValues[i] > bestGoodness) {
				bestGoodness = goodnessValues[i];
				bestNet = population[i];
			}
		}
	}
	
	public GeneticNeuralNet train(int iterations) {
		Random random = new Random();
		for(int iteration = 0; iteration < iterations * populationSize; iteration++) {
			int contestant1 = random.nextInt(population.length);
			int contestant2 = random.nextInt(population.length);
			int contestant3 = random.nextInt(population.length);
			int eliminatedIndex;
			
			GeneticNeuralNet parent1 = null, parent2 = null;
			if(goodnessValues[contestant1] <= goodnessValues[contestant2] && goodnessValues[contestant1] <= goodnessValues[contestant3]) {
				eliminatedIndex = contestant1;
				parent1 = population[contestant2];
				parent2 = population[contestant3];
			}
			else if(goodnessValues[contestant2] <= goodnessValues[contestant1] && goodnessValues[contestant2] <= goodnessValues[contestant3]) {
				eliminatedIndex = contestant2;
				parent1 = population[contestant1];
				parent2 = population[contestant3];
			}
			else if(goodnessValues[contestant3] <= goodnessValues[contestant2] && goodnessValues[contestant3] <= goodnessValues[contestant1]) {
				eliminatedIndex = contestant3;
				parent1 = population[contestant1];
				parent2 = population[contestant2];
			}
			else throw new NullPointerException("Wtf how is noone the worst?!");
			
			population[eliminatedIndex] = cross(parent1, parent2, random.nextDouble() * 3);
			mutate(population[eliminatedIndex]);
			goodnessValues[eliminatedIndex] = goodness(population[eliminatedIndex]);
			if(goodnessValues[eliminatedIndex] > bestGoodness) {
				bestGoodness = goodnessValues[eliminatedIndex];
				bestNet = population[eliminatedIndex];
			}
		}
		return bestNet;
	}
	
	public void mutate(GeneticNeuralNet net) {
		Random random = new Random();
		double selectedMutation = random.nextDouble();
		if(selectedMutation <= desirability1) {
			net.mutation(mutProbM1_M2, stdDevM1, true);
		}
		else if(selectedMutation > desirability1 && selectedMutation <= desirability2 + desirability1) {
			net.mutation(mutProbM1_M2, stdDevM2, true);
		}
		else if(selectedMutation > desirability1 + desirability2 && selectedMutation <= desirability1 + desirability2 + desirability3) {
			net.mutation(mutProbM3, stdDevM3, false);
		}
		else {
			System.err.println("Mutation error, desirability selected NO OPTION!");
		}
	}
	
	public double goodness(GeneticNeuralNet net) {
		return 1.0 / net.getError(samples);
	}
	
	public GeneticNeuralNet cross(GeneticNeuralNet parent1, GeneticNeuralNet parent2, double type) {
		Random random = new Random();
		//type1 cross
		if(type > 0 && type <= 1) {
			SigmoidNeuron[][] parentLayers;
			if(random.nextBoolean()) {
				parentLayers = parent1.getSigmoidLayers();
			}
			else {
				parentLayers = parent2.getSigmoidLayers();
			}
			SigmoidNeuron[][] newLayers = new SigmoidNeuron[parentLayers.length][]; 
			for(int i = 0; i < parentLayers.length; i++) {
				SigmoidNeuron[] layer = parentLayers[i];
				newLayers[i] = new SigmoidNeuron[layer.length];
				for(int j = 0; j < layer.length; j++) {
					newLayers[i][j] = new SigmoidNeuron(layer[j]);
				}
			}
			
			Type1Neuron[] parent1SecondLayer = parent1.getType1Layer(), parent2SecondLayer = parent2.getType1Layer();
			if(parent1SecondLayer.length != parent2SecondLayer.length) throw new NullPointerException("Parent type1 layers not same dimension!");
			
			Type1Neuron[] newSecondLayer = new Type1Neuron[parent1SecondLayer.length];
			for(int i = 0; i < parent1SecondLayer.length; i++) {
				newSecondLayer[i] = random.nextBoolean() ? new Type1Neuron(parent1SecondLayer[i]) : new Type1Neuron(parent2SecondLayer[i]);
			}
			if(parent1.getInputLayerSize() != parent2.getInputLayerSize()) throw new NullPointerException("Parent input layers not of same dimension!");
			return new GeneticNeuralNet(newSecondLayer, newLayers, parent1.getInputLayerSize());
		}
		
		//type2 cross
		else if(type > 1 && type <= 2) {
			Type1Neuron[] parentSecondLayer;
			if(random.nextBoolean()) {
				parentSecondLayer = parent1.getType1Layer();
			}
			else {
				parentSecondLayer = parent2.getType1Layer();
			}
			Type1Neuron[] newSecondLayer = new Type1Neuron[parentSecondLayer.length]; 
			for(int i = 0; i < parentSecondLayer.length; i++) {
				newSecondLayer[i] = new Type1Neuron(parentSecondLayer[i]);
			}
			
			SigmoidNeuron[][] parent1Layers = parent1.getSigmoidLayers(), parent2Layers = parent2.getSigmoidLayers();
			if(parent1Layers.length != parent2Layers.length) throw new NullPointerException("Parents dont have same amount of sigmoid layers!");
			
			SigmoidNeuron[][] newLayers = new SigmoidNeuron[parent1Layers.length][];
			for(int i = 0; i < parent1Layers.length; i++) {
				SigmoidNeuron[] parent1Layer = parent1Layers[i], parent2Layer = parent2Layers[i];
				if(parent1Layer.length != parent2Layer.length) throw new NullPointerException("Parents' layer " + i + " is not of same dimension!");
				newLayers[i] = new SigmoidNeuron[parent1Layer.length];
				
				for(int j = 0; j < parent1Layer.length; j++) {
					newLayers[i][j] = random.nextBoolean() ? new SigmoidNeuron(parent1Layer[j]) : new SigmoidNeuron(parent2Layer[j]);
				}
			}
			if(parent1.getInputLayerSize() != parent2.getInputLayerSize()) throw new NullPointerException("Parent input layers not of same dimension!");
			return new GeneticNeuralNet(newSecondLayer, newLayers, parent1.getInputLayerSize());
		}
		
		//type3 cross
		else if(type > 2 && type <= 3) {
			SigmoidNeuron[][] parent1Layers = parent1.getSigmoidLayers(), parent2Layers = parent2.getSigmoidLayers();
			if(parent1Layers.length != parent2Layers.length) throw new NullPointerException("Parents dont have same amount of sigmoid layers!");
			
			SigmoidNeuron[][] newLayers = new SigmoidNeuron[parent1Layers.length][];
			for(int i = 0; i < parent1Layers.length; i++) {
				SigmoidNeuron[] parent1Layer = parent1Layers[i], parent2Layer = parent2Layers[i];
				if(parent1Layer.length != parent2Layer.length) throw new NullPointerException("Parents' layer " + i + " is not of same dimension!");
				newLayers[i] = new SigmoidNeuron[parent1Layer.length];
				
				for(int j = 0; j < parent1Layer.length; j++) {
					newLayers[i][j] = random.nextBoolean() ? new SigmoidNeuron(parent1Layer[j]) : new SigmoidNeuron(parent2Layer[j]);
				}
			}
			
			Type1Neuron[] parent1SecondLayer = parent1.getType1Layer(), parent2SecondLayer = parent2.getType1Layer();
			if(parent1SecondLayer.length != parent2SecondLayer.length) throw new NullPointerException("Parent type1 layers not of same dimension!");
			
			Type1Neuron[] newSecondLayer = new Type1Neuron[parent1SecondLayer.length];
			for(int i = 0; i < parent1SecondLayer.length; i++) {
				newSecondLayer[i] = random.nextBoolean() ? new Type1Neuron(parent1SecondLayer[i]) : new Type1Neuron(parent2SecondLayer[i]);
			}
			if(parent1.getInputLayerSize() != parent2.getInputLayerSize()) throw new NullPointerException("Parent input layers not of same dimension!");
			return new GeneticNeuralNet(newSecondLayer, newLayers, parent1.getInputLayerSize());
		}
		
		else {
			System.err.println("Crossing error, user selected NONEXISTING TYPE!");
			throw new IllegalArgumentException("user selected NONEXISTING TYPE!");
		}
	}
}
