package lab7;
import java.util.Random;

public class Type1Neuron {
	
	private double[] weights;
	private double[] s;
	private int inputConnections;
	private double output = Double.NaN;
	
	public Type1Neuron(int inputConnections) {
		weights = new double[inputConnections];
		s = new double[inputConnections];
		Random random = new Random();
		for(int i = 0; i < weights.length; i++) {
			weights[i] = -0.4 + random.nextDouble() * 0.8;
			s[i] = 0.2 + random.nextDouble() * 2;
		}
		this.inputConnections = inputConnections;
	}
	
	public Type1Neuron(Type1Neuron other) {
		inputConnections = other.getInputConnections(); 
		weights = new double[inputConnections];
		s = new double[inputConnections];
		for(int i = 0; i < inputConnections; i++) {
			weights[i] = other.getWeight(i);
			s[i] = other.getS(i);
		}
	}

	public void setWeight(int connection, double weight) {
		weights[connection] = weight;
	}
	
	public void setS(int connection, double s) {
		this.s[connection] = s;
	}
	
	public double getWeight(int index) {
		return weights[index];
	}
	
	public double getS(int index) {
		return s[index];
	}
	
	public int getInputConnections() {
		return inputConnections;
	}
	
	public double passThrough(double[] inputs) {
		if(inputs.length != inputConnections) throw new IllegalArgumentException("Input must contain " + inputConnections + " connections!");
		double temp = 1;
		for(int i = 0; i < inputs.length; i++) {
			temp += similarity(i, inputs[i]);
		}
		output = 1.0 / temp;
		return output;
	}
	
	public double similarity(int index, double input) {
		return Math.abs(weights[index] - input) / Math.abs(s[index]);
	}
	
	public double getOutput() {
		return output;
	}
	
	public String toString() {
		String representation = "[";
		for(int i = 0; i < inputConnections; i++) {
			representation += "[w = " + weights[i] + ", s = " + s[i] + "]";
		}
		return representation + "]";
	}

}
