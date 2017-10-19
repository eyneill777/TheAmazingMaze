package main;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JFrame implements MouseListener
{
	final Dimension mazeDim = new Dimension(5,5);//Size of the maze
	static Dimension windowDim;//Size of the window
	Maze maze;
	Graph graph;
	JPanel mazePanel;//Swing panel to draw the maze in
	MazeGenerator generator; //The algorithm that is used to generate the maze
	JComboBox<String> viewFilters; //The drop down menu for selecting the views, switches the operation of the paint function
		
	public static void main(String[] args)//This method creates the window and launches the code in the constructor
	{
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() 
			{
				windowDim = Toolkit.getDefaultToolkit().getScreenSize();//Set windowDim = size of the screen
				Main f = new Main();
				f.setSize(windowDim);
				f.setDefaultCloseOperation(EXIT_ON_CLOSE);
				f.setVisible(true);
			}
		});
	}
	
	public Main()
	{
		addMouseListener(this);
		maze = new Maze(mazeDim);
		graph = new Graph(maze);
		viewFilters = new JComboBox<String>();
		viewFilters.addItem("Maze View");
		viewFilters.addItem("Graph View");
		viewFilters.addMouseListener(this);
		this.add(viewFilters, BorderLayout.NORTH);
		mazePanel = new JPanel();
		this.add(mazePanel);
		System.out.println(graph.numberOfDeadEnds());
		System.out.println(graph.numberOfIntersections());
		System.out.println(graph.traversalLength());
		System.out.println(graph.mazeComplexity());
	}
	
	public void paint(Graphics gfx)
	{
		Graphics2D g = (Graphics2D) mazePanel.getGraphics();
		if(viewFilters.getSelectedItem().equals("Maze View"))
		{
			maze.draw(g, mazePanel.getSize());
		}
		else if(viewFilters.getSelectedItem().equals("Graph View"))
		{
			graph.draw(g, mazePanel.getSize(), maze.getMazeImage());
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) 
	{
		repaint();
		System.out.println("test");
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
}
