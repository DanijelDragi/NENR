package lab5;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SampleCollector {
	
	private int sampleSize, classes;
	private BufferedWriter writer;
	
	public SampleCollector(int sampleSize, int classes, BufferedWriter writer) {
		this.sampleSize = sampleSize;
		this.classes = classes;
		this.writer = writer;
	}
	
	public SampleCollector(int sampleSize) {
		this.sampleSize = sampleSize;
		this.classes = 1;
		this.writer = null;
	}
	
	public String collect(List<Point> list, int sampleClass) {
		
		double avgX = 0, avgY = 0, maxX = 0, maxY = 0, max = 0;
		for(Point p : list) {
			avgX += p.getX();
			avgY += p.getY();
		}
		avgX /= list.size();
		avgY /= list.size();
		for(Point p : list) {
			p.setX((p.getX() - avgX));
			p.setY((p.getY() - avgY));
			if(Math.abs(p.getX()) > maxX) maxX = Math.abs(p.getX());
			if(Math.abs(p.getY()) > maxY) maxY = Math.abs(p.getY());
		}
		max = maxX > maxY ? maxX : maxY;
		for(Point p : list) {
			p.setX((p.getX() / max));
			p.setY((p.getY() / max));
		}
		
		double length = 0;
		Point a = null;
		for(Point p : list) {
			if(a == null) a = p;
			else {
				length += Math.sqrt(Math.pow(Math.abs(a.getX()) - Math.abs(p.getX()) , 2) + Math.pow(Math.abs(a.getY()) - Math.abs(p.getY()), 2));
				a = p;
			}
		}

		List<Point> sample = new ArrayList<Point>();
		double d = 0;
		int current = 0;
		a = null;
		for(Point p : list) {
			if(a == null) {
				a = p;
				sample.add(p);
				current++;
			}
			else {
				double diff = Math.sqrt(Math.pow(Math.abs(a.getX()) - Math.abs(p.getX()) , 2) + Math.pow(Math.abs(a.getY()) - Math.abs(p.getY()), 2));
				d += diff;
				
				if(d >= current * length / (sampleSize - 1)) {
					if(d - current * length / (sampleSize - 1) < current * length / (sampleSize - 1) - diff) sample.add(p);
					else sample.add(a);
					current++;
				}
				a = p;
			}
		}
		
		String sampleRep = stringFromList(sample);
		
		for(int i = 0; i < classes; i++) {
			if(i == sampleClass) sampleRep += ", 1";
			else sampleRep += ", 0";
		}
		
		if(writer != null) {
			try {
				writer.write(sampleRep);
				writer.newLine();
			} catch (IOException e) {
				System.out.println("Error writing line! Exiting!");
				System.exit(-1);
			}
		}
		return sampleRep;
	}
	
	private String stringFromList(List<Point> list) {
		String result = "";
		
		for(int i = 0; i < list.size(); i++) {
			if(i > 0) result += ", ";
			result += list.get(i).getX();
			result += ", " + list.get(i).getY();
		}
		
		return result;
	}

}
