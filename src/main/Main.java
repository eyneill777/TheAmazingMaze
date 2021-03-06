package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FileDialog;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

public class Main extends JFrame {
	final Dimension mazeDim = new Dimension(100, 100);// Size of the maze
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
	private String filename;
	private JFrame f;
	private static FileWriter writer = null;

	private enum Mode {
		GRAPH, MAZE, TGRAPH;
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
		// initialize the filewriter for statistics

		eh = new EventHandler();
		maze = new Maze(mazeDim);
		solution = new AstarMazeSolver(maze);
		solutionPath = solution.search();
		graph = new Graph(maze, solutionPath);

		// Menu stuff
		Font f = new Font("sans-serif", Font.PLAIN, 20);
		UIManager.put("Menu.font", f);
		menuBar = new JMenuBar();
		// File menu
		JMenu menu = new JMenu("File");
		menu.addMenuListener(new MenuHandler());
		menuBar.add(menu);
		JMenuItem menuItem1 = new JMenuItem("Save");
		menu.add(menuItem1);
		menuItem1.addActionListener(eh);
		JMenuItem menuItem2 = new JMenuItem("Print");
		menu.add(menuItem2);
		menuItem2.addActionListener(eh);
		// Views Menu
		viewMenu = new JMenu("View");
		viewMenu.addMenuListener(new MenuHandler());
		// Generate Menu
		generateMenu = new JMenu("Generate");
		menuBar.add(viewMenu);
		menuBar.add(generateMenu);
		this.add(menuBar, BorderLayout.NORTH);

		// Maze view
		menuItem = new JMenuItem("Maze View");
		menuItem.addActionListener(eh);
		viewMenu.add(menuItem);
		menuItem.setFont(f);

		// Graph view
		JMenuItem menuItem = new JMenuItem("Graph View");
		menuItem.addActionListener(eh);
		viewMenu.add(menuItem);
		menuItem.setFont(f);

		// Tile Graph view
		JMenuItem item = new JMenuItem("Tile Graph View");
		item.addActionListener(eh);
		viewMenu.add(item);
		item.setFont(f);

		// Kruskals Algorithm
		menuItem = new JMenuItem("Kruskals");
		menuItem.addActionListener(eh);
		generateMenu.add(menuItem);
		generateMenu.addMenuListener(new MenuHandler());
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

		// Backtracking
		menuItem = new JMenuItem("BackTracker");
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
		System.out.println(
				String.format("The length of the solution path (in units of cells) is %d ", graph.traversalLength()));
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
		} else if (drawMode == Mode.TGRAPH) {
			graph.draw(g, mazePanel.getSize(), maze.getMazeImage(), true);
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
			// try{
			// writer = new FileWriter("src//main//data.txt");
			// }
			// catch(Exception e1)
			// {
			// e1.printStackTrace();
			// }
			// for(int i = 0;i<100;i++)
			{
				if (e.getActionCommand().equals("Graph View")) {
					drawMode = Mode.GRAPH;
					System.out.println("test");
					repaint();
				} else if (e.getActionCommand().equals("Maze View")) {
					drawMode = Mode.MAZE;
					System.out.println("test");
					repaint();
				} else if (e.getActionCommand().equals("Tile Graph View")) {
					drawMode = Mode.TGRAPH;
					System.out.println("test");
					repaint();
				} else if (e.getActionCommand().equals("Save")) {
					FileDialog chooser = new FileDialog(f, "Use a .png or .jpg extension", FileDialog.SAVE);
					chooser.setVisible(true);
					if (chooser.getFile() != null) {
						save(chooser.getDirectory() + File.separator + chooser.getFile());
					}
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
				} else if (e.getActionCommand().equals("Wilsons")) {
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
				} else if (e.getActionCommand().equals("Sidewinder")) {
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
				} else if (e.getActionCommand().equals("Prim")) {
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
				} else if (e.getActionCommand().equals("Ellers")) {
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
				} else if (e.getActionCommand().equals("BackTracker")) {
					maze.reset();
					generator = new BackTracker(maze); // MazeGenerator here
					startTime = System.currentTimeMillis();
					generator.generateMaze();
					stopTime = System.currentTimeMillis();
					solution = new AstarMazeSolver(maze);
					solutionPath = solution.search();
					graph = new Graph(maze, solutionPath);
					printTheStats();
					repaint();
				}
				// logInfo();
				// System.out.println(i);
			}
			// try {
			// writer.close();
			// } catch (IOException e1) {
			// e1.printStackTrace();
			// }
		}

		@Override
		public void mouseDragged(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseMoved(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		private void logInfo() {
			double time = stopTime - startTime;
			String data = "" + graph.numberOfDeadEnds() + "\t" + graph.numberOfIntersections() + "\t"
					+ graph.traversalLength() + "\t" + graph.solutionComplexity() + "\t" + graph.mazeComplexity() + "\t"
					+ graph.mazeDifficulty() + "\t" + time + "\n";
			try {
				writer.write(data);
				writer.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Save the picture to a file in a standard image format. The filetype must be
	 * .png or .jpg.
	 */
	public void save(String name) {
		save(new File(name));
	}

	/**
	 * Save the picture to a file in a standard image format.
	 */
	public void save(File file) {
		this.filename = file.getName();
		if (f != null) {
			f.setTitle(filename);
		}
		String suffix = filename.substring(filename.lastIndexOf('.') + 1);
		suffix = suffix.toLowerCase();
		if (suffix.equals("jpg") || suffix.equals("png")) {
			try {
				ImageIO.write(maze.getMazeImage(), suffix, file);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			System.out.println("Error: filename must end in .jpg or .png");
		}
	}

	public class MenuHandler implements MenuListener {

		public void menuSelected(MenuEvent e) {

		}

		public void menuDeselected(MenuEvent e) {
			// menuDeselect = true;
			repaint();
		}

		public void menuCanceled(MenuEvent e) {

		}
	}
}
