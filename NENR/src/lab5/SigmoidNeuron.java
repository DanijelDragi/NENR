package lab5;

import java.util.Arrays;
import java.util.Random;

public class SigmoidNeuron {

	private double[] weights; 
	private int inputConnections;
	private double output = Double.NaN;
	
	public SigmoidNeuron(int inputConnections) {
		this.weights = new double[inputConnections];
		Random random = new Random();
		for(int i = 0; i < weights.length; i++) {
			weights[i] = -0.4 + random.nextDouble() * 0.8;
		}
		this.inputConnections = inputConnections;
	}
	
	public SigmoidNeuron(SigmoidNeuron other) {
		inputConnections = other.getInputConnections(); 
		weights = new double[inputConnections];
		for(int i = 0; i < inputConnections; i++) {
			weights[i] = other.getWeight(i);
		}
	}

	public void setWeight(int connection, double weight) {
		weights[connection] = weight;
	}
	
	public double getWeight(int index) {
		return weights[index];
	}
	
	public int getInputConnections() {
		return inputConnections;
	}
	
	public double passThrough(double[] inputs) {
		if(inputs.length != inputConnections) throw new IllegalArgumentException("Input must contain " + inputConnections + " connections!");
		double sum = 0;
		for(int i = 0; i < inputConnections; i++) {
			sum += inputs[i] * weights[i];
		}
		output = sigmoid(sum);
		return sigmoid(sum);
	}
	
	public double getOutput() {
		return output;
	}
	
	public double sigmoid(double sum) {
		return 1.0 / (1.0 + Math.pow(Math.E, -sum));
	}
	
	public String toString() {
		return Arrays.toString(weights);
	}
}
