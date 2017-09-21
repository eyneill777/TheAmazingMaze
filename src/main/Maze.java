package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;

public class Maze 
{
	Dimension size;
	Cell[][] mazeData;//Data for the maze, true = wall, false = hallway
	BufferedImage mazeImage;//Image the maze is stored in 
	int cellSize = 10;
	
	public Maze(Dimension size)
	{
		this.size = size;
		mazeData = new Cell[size.width][size.height];
		reset();
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
		mazeImage = new BufferedImage(size.width*cellSize, size.height*cellSize, BufferedImage.TYPE_INT_ARGB);
		Graphics2D gfx = (Graphics2D) mazeImage.getGraphics();
		
		//Loop through all the cells and draw them to MazeImage
		for(int x = 0;x<size.width;x++)
		{
			for(int y = 0;y<size.height;y++)
			{
				mazeData[x][y].draw(gfx);
			}
		}
		
		//These variables are the location that the maze image will be drawn at
		int drawX = panelSize.width/2-mazeImage.getWidth()/2;
		int drawY = panelSize.height/2-mazeImage.getHeight()/2;
		g.drawImage(mazeImage, drawX, drawY, null);
	}
}
