/**
 * 
 */
package ComplexityRoughDraft;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.JFrame;

/**
 * @author Steven Lawrence
 *
 */
public class TestSample extends JFrame {     
	private MyMaze displayMaze;                  // JComponent to paint on
	private static final int FRAME_SIZE = 2000;  // dimension of the JFrame
	
	public TestSample(int width, int height) {	
		setLayout(new BorderLayout()); 
		setSize(FRAME_SIZE, FRAME_SIZE);
		setTitle("Sample Maze");
		
		displayMaze = new MyMaze(width, height);
		add(displayMaze);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
		setVisible(true);
	}
	
	/**
	 *The canvas class which the maze is made on
	 */
	private static final class MyMaze extends JComponent {
		int w;
		int h;
		private static final long serialVersionUID = 2L;
		private SampleMaze m;
		
		
		public MyMaze(int w, int h) {
			
			// this would be for inputing width and height, but sample maze is defaulted to 5x5
			this.w = w;
			this.h = h;
			
			// constructor
			setBackground(Color.white); // white background
			m = new SampleMaze();       // no width or height to input
			System.out.println(String.format("The number of dead ends   value: %d Expected: 8", m.getDeadEndCount()));
			System.out.println(String.format("The number of i           value: %d Expected: 6", m.getiCount()));
			System.out.println(String.format("The traversal length      value: %d Expected: 24", m.getTravelLength()));
			System.out.println(String.format("The maze complexity       value: %f Expected: 37.833333", m.getMazeComplexity()));
			m.displayGraph();
		}
		public void paint(Graphics g) {
            m.display((Graphics2D)g);
		}
		
	}
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		TestSample theMaze = new TestSample(5, 5);
	}

}
