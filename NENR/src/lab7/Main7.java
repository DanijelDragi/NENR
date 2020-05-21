package lab7;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class Main7 {

	public static String PATH = "...\\NENR\\zad7-dataset.txt";
	public static int POPULATION_SIZE = 75;
	public static String ARCHITECTURE = "2x8x3";
	public static double STD_DEV_M1 = 1, STD_DEV_M2 = 3, STD_DEV_M3 = 1.5;
	public static double MUT_PROB_M1_M2 = 0.005, MUT_PROB_M3 = 0.005;
	public static int T1 = 2, T2 = 1, T3 = 2;
	public static int ITERATIONS = 1500;
	
	public static void main(String[] args) {
		
		Path path = Paths.get(PATH);
		String[] samples = null;
		try {
			List<String> list = Files.readAllLines(path, StandardCharsets.UTF_8);
			samples = new String[list.size()];
			for(int i = 0; i < list.size(); i++) {
				samples[i] = list.get(i);
			}
		} catch (IOException e) {
			System.err.println("Error opening file!");
			System.exit(-1);
		}
		
		GeneticNetTrainer trainer = new GeneticNetTrainer(POPULATION_SIZE, ARCHITECTURE, samples,
				STD_DEV_M1, STD_DEV_M2, STD_DEV_M3, MUT_PROB_M1_M2, MUT_PROB_M3, T1, T2, T3);
		GeneticNeuralNet bestNet = trainer.train(ITERATIONS);
		
		Type1Neuron[] secondLayer = bestNet.getType1Layer();
		System.out.println(secondLayer.length);
		for(Type1Neuron neuron : secondLayer) {
			System.out.println(neuron.getWeight(0) + ", " + neuron.getWeight(1));
			System.out.println("Sx: " + neuron.getS(0) + ", Sy: " + neuron.getS(1));
		}
		
		int outputLayerSize = 3, correct = 0;
		for(String sample : samples) {
			String[] parts = sample.split("\\s+");
			String[] input = Arrays.copyOfRange(parts, 0, parts.length - outputLayerSize);
			String[] output = Arrays.copyOfRange(parts, parts.length - outputLayerSize, parts.length);
			
			double[] inputParsed = new double[input.length];
			for(int i = 0; i < inputParsed.length; i++) {
				inputParsed[i] = Double.parseDouble(input[i]);
			}
			
			double[] outputParsed = new double[output.length];
			for(int i = 0; i < outputParsed.length; i++) {
				outputParsed[i] = Double.parseDouble(output[i]);
			}
			
			double[] netOut = bestNet.forwardPass(inputParsed);
			
			boolean mistake = false;
			for(int i = 0; i < netOut.length; i++) {
				netOut[i] = netOut[i] > 0.5 ? 1 : 0;
				if(netOut[i] != outputParsed[i]) mistake = true;
			}
			if(!mistake) correct++;
			
			//System.out.println("Net says: " + Arrays.toString(netOut) + ", Correct: " + Arrays.toString(outputParsed));
		}
		System.out.println("MSE: " + bestNet.getError(samples) + ", correct: " + correct + "/" + samples.length);
	}

}
