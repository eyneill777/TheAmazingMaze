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
		maze = new Maze(mazeDim);
		// MazeGenerator here 
		/* sample maze, probably should have put this in its separte class
		 * Note make cell size 200 and change maze dimensions to 5x5
		maze.mazeData[0][0].removeWall(Direction.South);
		maze.mazeData[0][1].removeWall(Direction.East);
		maze.mazeData[1][1].removeWall(Direction.South);
		maze.mazeData[1][2].removeWall(Direction.West);
		maze.mazeData[0][2].removeWall(Direction.South);
		maze.mazeData[1][2].removeWall(Direction.East);
		maze.mazeData[2][2].removeWall(Direction.South);
		maze.mazeData[2][3].removeWall(Direction.South);
		maze.mazeData[2][4].removeWall(Direction.West);
		maze.mazeData[1][4].removeWall(Direction.North);
		maze.mazeData[1][4].removeWall(Direction.West);
		maze.mazeData[2][2].removeWall(Direction.East);
		maze.mazeData[3][2].removeWall(Direction.North);
		maze.mazeData[3][1].removeWall(Direction.West);
		maze.mazeData[3][1].removeWall(Direction.North);
		maze.mazeData[3][0].removeWall(Direction.West);
		maze.mazeData[2][0].removeWall(Direction.West);
		maze.mazeData[3][0].removeWall(Direction.East);
		maze.mazeData[4][0].removeWall(Direction.South);
		maze.mazeData[4][1].removeWall(Direction.South);
		maze.mazeData[4][2].removeWall(Direction.South);
		maze.mazeData[4][3].removeWall(Direction.West);
		maze.mazeData[4][3].removeWall(Direction.South);
		maze.mazeData[4][4].removeWall(Direction.West);
		*/
		
		graph = new Graph(maze);
		viewFilters = new JComboBox<String>();
		viewFilters.addItem("Maze View");
		viewFilters.addItem("Graph View");
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
