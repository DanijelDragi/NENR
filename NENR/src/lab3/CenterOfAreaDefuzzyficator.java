package lab3;

import java.util.InputMismatchException;

import lab2.IFuzzySet;

public class CenterOfAreaDefuzzyficator implements Defuzzyficator {

	private double[] values;
	
	public CenterOfAreaDefuzzyficator(double[] values) {
		this.values = values;
	}
	
	@Override
	public int defuzzyfy(IFuzzySet set) {
		
		if(set.getDomain().getCardinality() != values.length) throw new InputMismatchException();
		
		double top = 0;
		double bottom = 0;
		
		for(int i = 0; i < set.getDomain().getCardinality(); i++) {
			top += values[i] * set.getValueAt(set.getDomain().elementForIndex(i));
			bottom += set.getValueAt(set.getDomain().elementForIndex(i));
			
			//System.out.println("values " + i + " " + values[i] + ": " + set.getValueAt(set.getDomain().elementForIndex(i)));
		}
		
		//System.out.println("output: " + top + "/" + bottom);
		return (int) Math.round(top / bottom);
	}

}
