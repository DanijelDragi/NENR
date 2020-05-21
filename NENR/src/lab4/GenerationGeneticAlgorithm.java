package lab4;

import java.util.Random;

public class GenerationGeneticAlgorithm {

	private Instance[] population;
	private double[] goodnessValues;
	private double[] rouletteValues;
	private double mutationProbability;
	private int evaluationCount = 0;
	private QualityEvaluator evaluator;
	private boolean elitism;
	private Instance allTimeBest;
	private double allTimeBestGoodness = 0;
	
	public GenerationGeneticAlgorithm(QualityEvaluator evaluator, int populationSize, int instanceSize, double mutationProbability, boolean elitism) {
		this.mutationProbability = mutationProbability;
		this.population = new Instance[populationSize];
		goodnessValues = new double[populationSize];
		this.evaluator = evaluator;
		this.elitism = elitism;
		
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
		
		double goodnessSum = 0;
		this.rouletteValues = new double[populationSize];
		for(double d: goodnessValues) {
			goodnessSum += d;
		}
		rouletteValues[0] = goodnessValues[0] / goodnessSum;
		//System.out.println("Goodness value for " + 0 + ": " + goodnessValues[0] + "\nroulette value : " + rouletteValues[0] + "\n");
		for(int i = 1; i < populationSize; i++) {
			rouletteValues[i] = rouletteValues[i - 1] + goodnessValues[i] / goodnessSum;
			//System.out.println("Goodness value for " + i + ": " + goodnessValues[i] + "\nroulette value : " + rouletteValues[i] + "\n");
		}
	}
	
	public Instance generation() {
		
		Instance[] newPopulation = new Instance[population.length];
		
		if(elitism) {
			Instance elite = null;
			double bestGoodness = Double.MAX_VALUE;
			for(int i = 0; i < population.length; i++) {
				if(goodnessValues[i] < bestGoodness) {
					elite = population[i];
					bestGoodness = goodnessValues[i];
				}
			}
			newPopulation[0] = elite;
		}
		
		Random random = new Random();
		//TODO selection via roulette wheel and crossing with mutation, fill new population! Evaluate them and return the best!
		for(int i = elitism ? 1 : 0; i < population.length; i++) {
			double roulette1 = random.nextDouble();
			double roulette2 = random.nextDouble();
			Instance parent1 = null, parent2 = null;
			for(int j = 0; j < rouletteValues.length; j++) {
				if(rouletteValues[j] >= roulette1 && parent1 == null) parent1 = population[j];
				if(rouletteValues[j] >= roulette2 && parent2 == null) parent2 = population[j];
			}
			newPopulation[i] = InstanceOperations.crossover(parent1, parent2);
			InstanceOperations.mutate(newPopulation[i], mutationProbability, -4.0, 8.0);
			goodnessValues[i] = evaluator.evaluate(newPopulation[i]);
			evaluationCount++;
			if(goodnessValues[i] > allTimeBestGoodness) {
				allTimeBest = population[i];
				allTimeBestGoodness = goodnessValues[i];
				System.out.println("New best solution:\n" + allTimeBest + "\nwith goodness: " + allTimeBestGoodness + "\nat " + evaluationCount + " evaluations\n");
			}
			double goodnessSum = 0;
			rouletteValues = new double[population.length];
			for(double d: goodnessValues) {
				goodnessSum += d;
			}
			rouletteValues[0] = goodnessValues[0] / goodnessSum;
			for(int j = 1; j < population.length; j++) {
				rouletteValues[j] = rouletteValues[j - 1] + goodnessValues[j] / goodnessSum;
			}
		}
		
		return allTimeBest;
	}
	
	public int evaluationCount() {
		return evaluationCount;
	}
}
