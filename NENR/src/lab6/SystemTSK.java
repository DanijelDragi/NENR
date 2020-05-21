package lab6;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class SystemTSK {
	
	private int numberOfRules;
	private ExpMemberFunction[][] memberFunctions;
	private RuleConclusionFunction[] conclusionFunctions;
	private double learningRateSmall, learningRateBig;
	
	public SystemTSK(int numberOfRules, double learningRateSmall, double learningRateBig) {
		this.numberOfRules = numberOfRules;
		this.learningRateSmall = learningRateSmall;
		this.learningRateBig = learningRateBig;
		memberFunctions = new ExpMemberFunction[numberOfRules][2];
		conclusionFunctions = new RuleConclusionFunction[numberOfRules];
		for(int i = 0; i < numberOfRules; i++) {
			memberFunctions[i][0] = new ExpMemberFunction();
			memberFunctions[i][1] = new ExpMemberFunction();
			conclusionFunctions[i] = new RuleConclusionFunction();
		}
	}
	
	public double getF(double x, double y) {
		double result = 0;
		
		double[] ruleConclusions = new double[numberOfRules];
		double wTotal = 0;
		for(int i  = 0 ; i < numberOfRules; i++) {
			ruleConclusions[i] = memberFunctions[i][0].getF(x) * memberFunctions[i][1].getF(y);
			wTotal += ruleConclusions[i];
		}
		for(int i  = 0 ; i < numberOfRules; i++) {
			ruleConclusions[i] /= wTotal;
			result += ruleConclusions[i] * conclusionFunctions[i].getF(x, y);
		}
		
		return result;
	}
	
	public ExpMemberFunction[][] getMemberFunctions() {
		return memberFunctions;
	}
	
	public void train(double[][] trainingData, boolean fullGradient, int trainingRounds) throws IOException {
		String PATH = "D:\\Danijel\\Shit for college\\5 Godina\\NENR\\lab6 report\\lab6_4RuleLearningErrorSmallRate.txt";
		BufferedWriter writer = new BufferedWriter(new FileWriter(PATH, false));
		
		int round = 0;
		while(round < trainingRounds) {
			double[] aUpdates = new double[numberOfRules];
			double[] bUpdates = new double[numberOfRules];
			double[] cUpdates = new double[numberOfRules];
			double[] dUpdates = new double[numberOfRules];
			double[] pUpdates = new double[numberOfRules];
			double[] qUpdates = new double[numberOfRules];
			double[] rUpdates = new double[numberOfRules];
			
			for(double[] trainingPoint : trainingData) {
				double x = trainingPoint[0];
				double y = trainingPoint[1];
				double Y = trainingPoint[2];
				double O = getF(x, y);
				//Train across all rules
				for(int i = 0; i < numberOfRules; i++) {
					double wSum = 0;
					double topSum = 0;
					
					for(int j = 0; j < numberOfRules; j++) {
						double wj = memberFunctions[j][0].getF(x) * memberFunctions[j][1].getF(y);
						wSum += wj;
						if(j != i) {
							topSum += wj * (conclusionFunctions[i].getF(x, y) - conclusionFunctions[j].getF(x, y));
						}
					}
					double Ai = memberFunctions[i][0].getF(x);
					double Bi = memberFunctions[i][1].getF(y);
					double ai = memberFunctions[i][0].getA();
					double bi = memberFunctions[i][0].getB();
					double ci = memberFunctions[i][1].getA();
					double di = memberFunctions[i][1].getB();
					
					//Train all member functions and conclusions!
					double wi = Ai * Bi;
					double tempA = - (Y - O) * (topSum / Math.pow(wSum, 2));
					double tempC = - (Y - O) * (topSum / Math.pow(wSum, 2));
					tempA *= Ai * bi * (1 - Ai) * Bi;
					tempC *= Bi * di * (1 - Bi) * Ai;
					aUpdates[i] += tempA;
					cUpdates[i] += tempC;
					
					double tempB = - (Y - O) * (topSum / Math.pow(wSum, 2));
					double tempD = - (Y - O) * (topSum / Math.pow(wSum, 2));
					tempB *= (x - ai) * Ai * (Ai - 1) * Bi;
					tempD *= (x - ci) * Bi * (Bi - 1) * Ai;
					bUpdates[i] += tempB;
					dUpdates[i] += tempD;
					
					pUpdates[i] += - (Y - O) * (wi / wSum) * x;
					qUpdates[i] += - (Y - O) * (wi / wSum) * y;
					rUpdates[i] += - (Y - O) * (wi / wSum);
				}
				if(!fullGradient) {
					for(int i = 0; i < numberOfRules; i++) {
						memberFunctions[i][0].setA(memberFunctions[i][0].getA() - learningRateSmall * aUpdates[i]);
						memberFunctions[i][0].setB(memberFunctions[i][0].getB() - learningRateSmall * bUpdates[i]);
						memberFunctions[i][1].setA(memberFunctions[i][1].getA() - learningRateSmall * cUpdates[i]);
						memberFunctions[i][1].setB(memberFunctions[i][1].getB() - learningRateSmall * dUpdates[i]);
						conclusionFunctions[i].setP(conclusionFunctions[i].getP() - learningRateBig * pUpdates[i]);
						conclusionFunctions[i].setQ(conclusionFunctions[i].getQ() - learningRateBig * qUpdates[i]);
						conclusionFunctions[i].setR(conclusionFunctions[i].getR() - learningRateBig * rUpdates[i]);
						aUpdates[i] = 0;
						bUpdates[i] = 0;
						cUpdates[i] = 0;
						dUpdates[i] = 0;
						pUpdates[i] = 0;
						qUpdates[i] = 0;
						rUpdates[i] = 0;
					}
				}
								
			}
			if(fullGradient) {
				for(int i = 0; i < numberOfRules; i++) {
					memberFunctions[i][0].setA(memberFunctions[i][0].getA() - learningRateSmall * (aUpdates[i] / numberOfRules));
					memberFunctions[i][0].setB(memberFunctions[i][0].getB() - learningRateSmall * (bUpdates[i] / numberOfRules));
					memberFunctions[i][1].setA(memberFunctions[i][1].getA() - learningRateSmall * (cUpdates[i] / numberOfRules));
					memberFunctions[i][1].setB(memberFunctions[i][1].getB() - learningRateSmall * (dUpdates[i] / numberOfRules));
					conclusionFunctions[i].setP(conclusionFunctions[i].getP() - learningRateBig * (pUpdates[i] / numberOfRules));
					conclusionFunctions[i].setQ(conclusionFunctions[i].getQ() - learningRateBig * (qUpdates[i] / numberOfRules));
					conclusionFunctions[i].setR(conclusionFunctions[i].getR() - learningRateBig * (rUpdates[i] / numberOfRules));
					
					//System.out.println("Update of A: " + (-1) * learningRateSmall * (aUpdates[i] / numberOfRules));
					//System.out.println("Update of B: " + (-1) * learningRateSmall * (bUpdates[i] / numberOfRules));
					//System.out.println("Update of C: " + (-1) * learningRateSmall * (cUpdates[i] / numberOfRules));
					//System.out.println("Update of D: " + (-1) * learningRateSmall * (dUpdates[i] / numberOfRules));
					//System.out.println("Update of P: " + (-1) * learningRateBig * (pUpdates[i] / numberOfRules));
					//System.out.println("Update of Q: " + (-1) * learningRateBig * (qUpdates[i] / numberOfRules));
					//System.out.println("Update of R: " + (-1) * learningRateBig * (rUpdates[i] / numberOfRules));
					
					aUpdates[i] = 0;
					bUpdates[i] = 0;
					cUpdates[i] = 0;
					dUpdates[i] = 0;
					pUpdates[i] = 0;
					qUpdates[i] = 0;
					rUpdates[i] = 0;
				}
			}
			if(round % 1 == 0) {
				writer.write(String.valueOf(round)+ ", " + String.valueOf(error(trainingData)));
				writer.newLine();
				
				if(round % 1000 == 0) System.out.println("Error at round " + round + ": " + error(trainingData));
				//System.out.println("Got: a = " + memberFunctions[0][0].getA() + ", b = " + memberFunctions[0][0].getB());
				//System.out.println("Got: c = " + memberFunctions[0][1].getA() + ", d = " + memberFunctions[0][1].getB());
				//System.out.println("Got: p = " + conclusionFunctions[0].getP() + ", q = " + conclusionFunctions[0].getP() + ", r = " + conclusionFunctions[0].getR());
			}
			round++;
		}
		System.out.println("Error at round " + round + ": " + error(trainingData));
		writer.close();
	}
	
	public double error(double[][] trainingData) {
		double error = 0;
		
		for(double[] trainingPoint : trainingData) {
			//System.out.println("Error comparison: Got " + getF(trainingPoint[0], trainingPoint[1]) + ", correct: " + trainingPoint[2]);
			error += Math.abs(Math.pow(getF(trainingPoint[0], trainingPoint[1]) - trainingPoint[2], 2));
		}
		return error / trainingData.length;
	}
}
