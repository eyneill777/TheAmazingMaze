package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

public class Maze 
{
	Dimension size;
	Cell[][] mazeData;//Data for the maze, true = wall, false = hallway
	BufferedImage mazeImage;//Image the maze is stored in 
	int cellSize;
	
	public Maze(Dimension size)
	{
		this.size = size;
		adjustCellSize();
		mazeData = new Cell[size.width][size.height];
		mazeImage = new BufferedImage(size.width*cellSize, size.height*cellSize, BufferedImage.TYPE_INT_ARGB);
		reset();
	}
	
	private void adjustCellSize()
	{
		int smallestDim = Toolkit.getDefaultToolkit().getScreenSize().height;
		boolean l = true;
		if(Toolkit.getDefaultToolkit().getScreenSize().width<smallestDim)
		{
			smallestDim = Toolkit.getDefaultToolkit().getScreenSize().width;
			l = false;
		}
		if(l = true)
			cellSize = (Toolkit.getDefaultToolkit().getScreenSize().height/size.height)-5;
		else
			cellSize = Toolkit.getDefaultToolkit().getScreenSize().width/size.width;
		System.out.println(size.height);
		System.out.println(cellSize);
	}
	
	public void reset()//Set all maze data to open halls
	{
		for(int x = 0;x < size.width;x++)
		{
			for(int y = 0;y < size.height;y++)
			{
				mazeData[x][y] = new Cell(this, new Point(x,y));
			}
		}
		for(int x = 0;x < size.width;x++)
		{
			for(int y = 0;y < size.height;y++)
			{
				mazeData[x][y].initWalls();
			}
		}
	}
	
	public void draw(Graphics2D g, Dimension panelSize)
	{
		g.setColor(Color.black);
		g.fillRect(0, 0, panelSize.width, panelSize.height);
		Graphics2D gfx = (Graphics2D) mazeImage.getGraphics();
		gfx.fillRect(0, 0, mazeImage.getWidth(), mazeImage.getHeight());   // erase the graph off of the image
		
		//Loop through all the cells and draw them to MazeImage
		for(int x = 0;x<size.width;x++)
		{
			for(int y = 0;y<size.height;y++)
			{
				mazeData[x][y].draw(gfx, cellSize);
			}
		}
		
		//These variables are the location that the maze image will be drawn at
		int drawX = panelSize.width/2-mazeImage.getWidth()/2;
		int drawY = panelSize.height/2-mazeImage.getHeight()/2;
		g.drawImage(mazeImage, drawX, drawY, null);
	}
	
	public Cell getCell(Point position)
	{
		return mazeData[position.x][position.y];
	}

	/**
	 * @return the mazeData
	 */
	public Cell[][] getMazeData() {
		return mazeData;
	}

	public BufferedImage getMazeImage() {
		return mazeImage;
	}
	
}
