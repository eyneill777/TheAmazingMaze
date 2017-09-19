package main;

import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Main extends JFrame
{
	final Dimension mazeDim = new Dimension(500,500);//Size of the maze
	static Dimension windowDim;//Size of the window
	Maze maze;
	JPanel mazePanel;//Swing panel to draw the maze in
	MazeGenerator generator; //The algorithm that is used to generate the maze
	
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
		mazePanel = new JPanel();
		this.add(mazePanel);
	}
	
	public void paint(Graphics gfx)
	{
		Graphics2D g = (Graphics2D) mazePanel.getGraphics();
		maze.draw(g, mazePanel.getSize());
	}
}
