package lab5;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import javax.swing.JFrame;

public class Main5 {
	
	public static int SAMPLES_PER_CLASS = 20, CLASSES = 5, SAMPLE_SIZE = 10;
	public static String PATH = "...\\NENR\\lab5_Samples.txt";

	public static void main(String[] args) {
		/*
		try {
			gestureRecorder(PATH);
		} catch (IOException e) {
			System.err.println("Error recording gestures!");
		}
		*/
		
		Path path1 = Paths.get("...\\NENR\\lab5_Samples.txt");
		String[] samples = null;
		try {
			List<String> list = Files.readAllLines(path1, StandardCharsets.UTF_8);
			samples = new String[list.size()];
			for(int i = 0; i < list.size(); i++) {
				samples[i] = list.get(i);
			}
		} catch (IOException e) {
			System.err.println("Error opening file!");
			System.exit(-1);
		}
		
		NeuralNet net = new NeuralNet("20x10x5");
				
		net.stohasticBackprop(2000, samples, 0.05);
		//net.backprop(5000, samples, 0.05);
		//net.batchBackpop(5000, samples, 0.05, 7);
		
		gestureClassifier(net);
	}

	public static void gestureRecorder(String fileName) throws IOException {
		
		JFrame frame = new JFrame("Gesture recorder");
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.out.println("Force stopped sample collecting!");
				frame.dispose();
				System.exit(1);
	        }
		});
		
	    Canvas canvas = new Canvas();
	    canvas.setBackground(Color.WHITE);
	    
	    GestureRecorder recorder = new GestureRecorder(canvas, frame, SAMPLES_PER_CLASS, CLASSES, SAMPLE_SIZE, PATH);
	    canvas.addMouseListener(recorder);
	    canvas.addMouseMotionListener(recorder);
	    
	    canvas.setSize(800, 600);
	    frame.add(canvas);
	    frame.pack();
	    frame.setVisible(true);
	}
	
	public static void gestureClassifier(NeuralNet net) {
		
		JFrame frame = new JFrame("Gesture classifier");
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.out.println("Force stopped gesture classifier!");
				frame.dispose();
				System.exit(1);
	        }
		});
		
		Canvas canvas = new Canvas();
	    canvas.setBackground(Color.WHITE);
	    
	    GestureClassifier classifier = new GestureClassifier(canvas, frame, SAMPLE_SIZE, net);
	    canvas.addMouseListener(classifier);
	    canvas.addMouseMotionListener(classifier);
	    
	    canvas.setSize(800, 600);
	    frame.add(canvas);
	    frame.pack();
	    frame.setVisible(true);
	}
}
