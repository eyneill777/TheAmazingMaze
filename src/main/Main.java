package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.UIManager;

public class Main extends JFrame {
	final Dimension mazeDim = new Dimension(50, 50);// Size of the maze 
	static Dimension windowDim;// Size of the window
	Maze maze;
	Graph graph;
	JPanel mazePanel;// Swing panel to draw the maze in
	MazeGenerator generator; // The algorithm that is used to generate the maze
	JComboBox<String> viewFilters; // The drop down menu for selecting the views, switches the operation of the
									// paint function
	AstarMazeSolver solution;
	ArrayList<Cell> solutionPath;
	private JMenuBar menuBar;
	private JMenu viewMenu, generateMenu;
	private AbstractButton menuItem;
	private EventHandler eh;
	private Mode drawMode = Mode.MAZE; // default to maze
	private double startTime, stopTime;

	private enum Mode {
		GRAPH, MAZE;
	}

	public static void main(String[] args)// This method creates the window and launches the code in the constructor
	{
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				windowDim = Toolkit.getDefaultToolkit().getScreenSize();// Set windowDim = size of the screen
				Main f = new Main();
				f.setSize(windowDim);
				f.setDefaultCloseOperation(EXIT_ON_CLOSE);
				f.setVisible(true);
			}
		});
	}

	public Main() {
		eh = new EventHandler();
		maze = new Maze(mazeDim);
		generator = new KruskalsAlgorithm(maze); // MazeGenerator here
		startTime = System.currentTimeMillis();
		generator.generateMaze();
		stopTime = System.currentTimeMillis();
		solution = new AstarMazeSolver(maze);
		solutionPath = solution.search();
		graph = new Graph(maze, solutionPath);

		// Menu stuff
		Font f = new Font("sans-serif", Font.PLAIN, 20);
		UIManager.put("Menu.font", f);
		menuBar = new JMenuBar();
		viewMenu = new JMenu("View");
		generateMenu = new JMenu("Generate");
		menuBar.add(viewMenu);
		menuBar.add(generateMenu);
		this.add(menuBar, BorderLayout.NORTH);
		
		// Graph view
		JMenuItem menuItem = new JMenuItem("Graph View");
		menuItem.addActionListener(eh);
		viewMenu.add(menuItem);
		menuItem.setFont(f);
		// Maze view
		menuItem = new JMenuItem("Maze View");
		menuItem.addActionListener(eh);
		viewMenu.add(menuItem);
		menuItem.setFont(f);
		
		// Kruskals Algorithm
		menuItem = new JMenuItem("Kruskals");
		menuItem.addActionListener(eh);
		generateMenu.add(menuItem);
		menuItem.setFont(f);

		// Recursive Division
				menuItem = new JMenuItem("Recursive");
				menuItem.addActionListener(eh);
				generateMenu.add(menuItem);
				menuItem.setFont(f);
		
		// Wilson's Algorithm
		menuItem = new JMenuItem("Wilsons");
		menuItem.addActionListener(eh);
		generateMenu.add(menuItem);
		menuItem.setFont(f);
		
		// Prim's Algorithm
		menuItem = new JMenuItem("Prim");
		menuItem.addActionListener(eh);
		generateMenu.add(menuItem);
		menuItem.setFont(f);
		
		// SideWinder Algorithm
		menuItem = new JMenuItem("Sidewinder");
		menuItem.addActionListener(eh);
		generateMenu.add(menuItem);
		menuItem.setFont(f);
		
		// Eller 's algorithm
		menuItem = new JMenuItem("Ellers");
		menuItem.addActionListener(eh);
		generateMenu.add(menuItem);
		menuItem.setFont(f);

		mazePanel = new JPanel();
		mazePanel.addMouseListener(eh);
		this.add(mazePanel);
		printTheStats();
	}
	private void printTheStats() {
		System.out.println("New Maze");
		System.out.println(String.format("The number of dead ends in the maze is %d ", graph.numberOfDeadEnds()));
		System.out.println(String.format("The number of intesections in the maze %d ", graph.numberOfIntersections()));
		System.out.println("The solution path found is:");
		System.out.println(solutionPath);
		System.out.println(String.format("The length of the solution path (in units of cells) is %d ", graph.traversalLength()));
		System.out.println(String.format("The complexity of the solution path is %f", graph.solutionComplexity()));
		System.out.println(String.format("The maze complexity is %f ", graph.mazeComplexity()));
		System.out.println(String.format("The maze difficulty is %f ", graph.mazeDifficulty()));
		System.out.println(String.format("Generation took %f ms", stopTime - startTime));
	}

	public void paint(Graphics gfx) {
		super.paint(gfx);
		Graphics2D g = (Graphics2D) mazePanel.getGraphics();

		if (drawMode == Mode.MAZE) {
			maze.draw(g, mazePanel.getSize());

		} else if (drawMode == Mode.GRAPH) {
			graph.draw(g, mazePanel.getSize(), maze.getMazeImage());
		}
	}

	private class EventHandler implements ActionListener, MouseListener, MouseMotionListener {
		@Override
		public void mouseClicked(MouseEvent e) {

		}

		@Override
		public void mousePressed(MouseEvent e) {
			repaint();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseEntered(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseExited(MouseEvent e) {
			// TODO Auto-generated method stub

		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getActionCommand().equals("Graph View")) {
				drawMode = Mode.GRAPH;
				System.out.println("test");
				repaint();
			}
			else if (e.getActionCommand().equals("Maze View")) {
				drawMode = Mode.MAZE;
				System.out.println("test");
				repaint();
			}
			else if (e.getActionCommand().equals("Kruskals")) {
				maze.reset();
				generator = new KruskalsAlgorithm(maze); // MazeGenerator here
				startTime = System.currentTimeMillis();
				generator.generateMaze();
				stopTime = System.currentTimeMillis();
				solution = new AstarMazeSolver(maze);
				solutionPath = solution.search();
				graph = new Graph(maze, solutionPath);
				printTheStats();
				repaint();
			}
			else if (e.getActionCommand().equals("Wilsons")) {
				maze.reset();
				generator = new Wilson(maze); // MazeGenerator here
				startTime = System.currentTimeMillis();
				generator.generateMaze();
				stopTime = System.currentTimeMillis();
				solution = new AstarMazeSolver(maze);
				solutionPath = solution.search();
				graph = new Graph(maze, solutionPath);
				printTheStats();
				repaint();
			}
			else if (e.getActionCommand().equals("Recursive")) {
				maze.reset();
				generator = new RecursiveDivision(maze); // MazeGenerator here
				generator.generateMaze();
				repaint();
			}
			else if (e.getActionCommand().equals("Sidewinder")) {
				maze.reset();
				generator = new Sidewinder(maze); // MazeGenerator here
				startTime = System.currentTimeMillis();
				generator.generateMaze();
				stopTime = System.currentTimeMillis();
				solution = new AstarMazeSolver(maze);
				solutionPath = solution.search();
				graph = new Graph(maze, solutionPath);
				printTheStats();
				repaint();
			}
			else if (e.getActionCommand().equals("Prim")) {
				maze.reset();
				generator = new Prims(maze); // MazeGenerator here
				startTime = System.currentTimeMillis();
				generator.generateMaze();
				stopTime = System.currentTimeMillis();
				solution = new AstarMazeSolver(maze);
				solutionPath = solution.search();
				graph = new Graph(maze, solutionPath);
				printTheStats();
				repaint();
			}
			else if (e.getActionCommand().equals("Ellers")) {
				maze.reset();
				generator = new Ellers(maze); // MazeGenerator here
				startTime = System.currentTimeMillis();
				generator.generateMaze();
				stopTime = System.currentTimeMillis();
				solution = new AstarMazeSolver(maze);
				solutionPath = solution.search();
				graph = new Graph(maze, solutionPath);
				printTheStats();
				repaint();
			}
		}

		@Override
		public void mouseDragged(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}
	}
}
