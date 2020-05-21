package lab6;

import java.io.IOException;

public class Main6 {
	
	public static double LEARNIGN_RATE_BIG = 0.00001;
	public static double LEARNIGN_RATE_SMALL = 0.00000002;
	public static int NUMBER_OF_RULES = 4;
	
	public static String PATH = "D:\\Danijel\\Shit for college\\5 Godina\\NENR\\lab6 report\\lab6_4RuleLearningError";

	public static void main(String[] args) throws IOException {
		
		SystemTSK tsk = new SystemTSK(NUMBER_OF_RULES, LEARNIGN_RATE_SMALL, LEARNIGN_RATE_BIG);
		
		double[][] trainingData = new double[81][3];
		for(int i = -4; i <= 4; i++) {
			for(int j = -4; j <= 4; j++) {
				trainingData[(i + 4) * 9 + (j + 4)][2] = trainingFunction(i, j);
				trainingData[(i + 4) * 9 + (j + 4)][1] = j;
				trainingData[(i + 4) * 9 + (j + 4)][0] = i;
			}
		}		
		tsk.train(trainingData, false, 25000);

	}
	
	public static double trainingFunction(double x, double y) {
		if(x > 4 || x < -4 || y > 4 || y < -4) throw new IllegalArgumentException("Function only defined for x [-4, 4] and y [-4, 4]");
		return (Math.pow(x - 1, 2) + Math.pow(y + 2, 2) - 5 * x * y + 3.0) * Math.pow(Math.cos(x / 5.0), 2);
	}

}
