package lab4;

import java.util.Random;

public class EliminationGeneticAlgorithm {
	
	private Instance[] population;
	private double[] goodnessValues;
	private double mutationProbability;
	private int evaluationCount = 0;
	private QualityEvaluator evaluator;
	private Instance allTimeBest;
	private double allTimeBestGoodness = 0;
	
	public EliminationGeneticAlgorithm(QualityEvaluator evaluator, int populationSize, int instanceSize, double mutationProbability) {
		this.mutationProbability = mutationProbability;
		this.population = new Instance[populationSize];
		goodnessValues = new double[populationSize];
		this.evaluator = evaluator;
		
		Random random = new Random();
		for(int i = 0; i < populationSize; i++) {
			double[] tempGenes = new double[instanceSize];
			for(int j = 0; j < instanceSize; j++) {
				tempGenes[j] = -4.0 + (random.nextDouble() * 8);
			}
			population[i] = new Instance(instanceSize, tempGenes);
			goodnessValues[i] = this.evaluator.evaluate(population[i]);
			if(goodnessValues[i] > allTimeBestGoodness) {
				allTimeBest = population[i];
				allTimeBestGoodness = goodnessValues[i];
			}
			evaluationCount++;
		}
		System.out.println("New best solution:\n" + allTimeBest + "\nwith goodness: " + allTimeBestGoodness + "\nat " + evaluationCount + " evaluations\n");
	}
	
	public Instance generation() {
		return generation(1);
	}
	
	public Instance generation(int numEvaluation) {
		
		if(numEvaluation < 1)  throw new IllegalArgumentException("Can't have less than 1 evaluation!");
		Random random = new Random();
		for(int i = 0; i < numEvaluation; i++) {
			int contestant1 = random.nextInt(population.length);
			int contestant2 = random.nextInt(population.length);
			int contestant3 = random.nextInt(population.length);
			int eliminatedIndex;
			Instance parent1 = null, parent2 = null;
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
			population[eliminatedIndex] = InstanceOperations.crossover(parent1, parent2);
			InstanceOperations.mutate(population[eliminatedIndex], mutationProbability, -4, 8);
			goodnessValues[eliminatedIndex] = evaluator.evaluate(population[eliminatedIndex]);
			evaluationCount++;
			if(goodnessValues[eliminatedIndex] > allTimeBestGoodness) {
				allTimeBest = population[eliminatedIndex];
				allTimeBestGoodness = goodnessValues[eliminatedIndex];
				System.out.println("New best solution:\n" + allTimeBest + "\nwith goodness: " + allTimeBestGoodness + "\nat " + evaluationCount + " evaluations\n");
			}
		}
		return allTimeBest;
	}

	public int evaluationCount() {
		return evaluationCount;
	}
}
