package lab4;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class QualityEvaluator {
	
	private List<Datapoint> dataset = new ArrayList<Datapoint>();
	private GoodnessFunction function;
	
	public QualityEvaluator(Path dataset, GoodnessFunction function) {
		this.function = function;
		try
	    { 
	      List<String> temp = Files.readAllLines(dataset, StandardCharsets.UTF_8);
	      for(String s : temp) {
    	  String[] parts = s.split("\t");
    	  this.dataset.add(new Datapoint(Double.parseDouble(parts[0]), Double.parseDouble(parts[1]), Double.parseDouble(parts[2])));
	      }
	    }
		catch(IOException e) {
			System.err.println("Caught IOException: " + e.getMessage());
		}
	}
	
	public double evaluate(Instance instance) {
		double error = 0;
		for(Datapoint d : dataset) {
			error += function.goodness(d, instance);
		}
		error /= dataset.size();
		return 1.0 / error;
	}
}
