package lab5;

import java.awt.Canvas;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.JFrame;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

public class GestureClassifier extends MouseInputAdapter implements MouseInputListener {
	
	private boolean recordMovement = false;
	private ArrayList<Point> coordinates = new ArrayList<Point>();
	private Canvas drawingCanvas;
	private SampleCollector collector;
	private NeuralNet net;

	public GestureClassifier(Canvas canvas, JFrame frame, int sampleSize, NeuralNet net) {
		drawingCanvas = canvas;
		this.net = net;
		collector = new SampleCollector(sampleSize);
	}
	
	@Override
	public void mouseDragged(MouseEvent e) {
		if(recordMovement) {
			coordinates.add(new Point(e.getX(), e.getY()));
			drawingCanvas.getGraphics().drawRect(e.getX(), e.getY(), 1, 1);
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1)	recordMovement = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if(e.getButton() == MouseEvent.BUTTON1)	recordMovement = false;
		
		//send recorded move!
		if(coordinates.size() > 0) {
			String condensedSample = collector.collect(coordinates, 0);
			String[] parts = condensedSample.split(",");
			String[] input = Arrays.copyOfRange(parts, 0, parts.length - 1);	
			double[] inputParsed = new double[input.length];
			for(int i = 0; i < inputParsed.length; i++) {
				inputParsed[i] = Double.parseDouble(input[i]);
			}
			
			double[] prediction = net.forwardPass(inputParsed);
			int gestureClass = -1;
			double certanty = 0;
			for(int i = 0; i < prediction.length; i++) {
				if(prediction[i] > certanty) {
					certanty = prediction[i];
					gestureClass = i;
				}
			}
			System.out.println("Sample belongs to class: " + gestureClass);
		
			coordinates = new ArrayList<Point>();
			drawingCanvas.repaint();
		}
	}
}
