package lab4;

import java.nio.file.Path;
import java.nio.file.Paths;

public class Main4 {

	public static void main(String[] args) {
		
		int popSize = 20;
		int instanceSize = 5;
		double mutationProb = 0.015;
		
		Path path1 = Paths.get("...NENR\\zad4-dataset1.txt");
		//Path path2 = Paths.get("...\\NENR\\zad4-dataset2.txt");
		
		QualityEvaluator evaluator1 = new QualityEvaluator(path1, new GoodnessFunction());
		//QualityEvaluator evaluator2 = new QualityEvaluator(path2, new GoodnessFunction());
		
		/*
		System.out.println("ALGORITHM 1: \n");
		GenerationGeneticAlgorithm algorithm1 = new GenerationGeneticAlgorithm(evaluator1, popSize, instanceSize, mutationProb, true);
		for(int i = 0; i < 1000; i++) {
			algorithm1.generation();
		}
		*/
		
		System.out.println("\nALGORITHM 2: \n");
		EliminationGeneticAlgorithm algorithm2 = new EliminationGeneticAlgorithm(evaluator1, popSize, instanceSize, mutationProb);
		for(int i = 0; i < 1000; i++) {
			algorithm2.generation(popSize);
		}
		
	}

}
