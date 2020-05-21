package lab5;

import java.awt.Canvas;
import java.awt.event.MouseEvent;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

public class GestureRecorder extends MouseInputAdapter implements MouseInputListener {
	
	private boolean recordMovement = false;
	private ArrayList<Point> coordinates = new ArrayList<Point>();
	private Canvas drawingCanvas;
	private SampleCollector collector;
	private int samplesPerClass, classes, sampleCounter = 0, classCounter = 0;
	private JFrame frame;
	private BufferedWriter writer;
	
	public GestureRecorder(Canvas canvas, JFrame frame, int samplesPerClass, int classes, int sampleSize, String fileName) throws IOException {
		drawingCanvas = canvas;
		this.samplesPerClass = samplesPerClass;
		this.classes = classes;
		this.frame = frame;
		
		BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, false));
		this.writer = writer;
		collector = new SampleCollector(sampleSize, classes, writer);
		System.out.println("Now recording samples for class 0!");
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
			collector.collect(coordinates, classCounter);
			sampleCounter++;
			if(sampleCounter == samplesPerClass) {
				sampleCounter = 0;
				classCounter++;
				if(classCounter == classes) {
					try {
						writer.close();
					} catch (IOException e1) {
						System.err.println("Problem closing writer, exiting!");
						System.exit(-1);
					}
					frame.dispose();
				}
				System.out.println("Now recording samples for class " + classCounter + "!");
			}
		}
		
		coordinates = new ArrayList<Point>();
		drawingCanvas.repaint();
	}

}
