package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

public class Maze 
{
	Dimension size;
	boolean[][] mazeData;//Data for the maze, true = wall, false = hallway
	BufferedImage mazeImage;//Image the maze is stored in 
	
	public Maze(Dimension size)
	{
		this.size = size;
		mazeData = new boolean[size.width][size.height];
		reset();
	}
	
	public void reset()//Set all maze data to open halls
	{
		for(int x = 0;x < size.width;x++)
		{
			for(int y = 0;y < size.height;y++)
			{
				mazeData[x][y] = false;
			}
		}
	}
	
	public void resetFilled()//Set all maze data to walls
	{
		for(int x = 0;x < size.width;x++)
		{
			for(int y = 0;y < size.height;y++)
			{
				mazeData[x][y] = true;
			}
		}
	}
	
	public void draw(Graphics2D g, Dimension panelSize)
	{
		g.setColor(Color.black);
		g.fillRect(0, 0, panelSize.width, panelSize.height);
		mazeImage = new BufferedImage(size.width, size.height, BufferedImage.TYPE_INT_ARGB);
		for(int x = 0;x<size.width;x++)
		{
			for(int y = 0;y<size.height;y++)
			{
				if(mazeData[x][y])
				{
					mazeImage.setRGB(x, y, Color.DARK_GRAY.getRGB());
				}
				else
				{
					mazeImage.setRGB(x, y, Color.white.getRGB());
				}
			}
		}
		int drawX = panelSize.width/2-mazeImage.getWidth()/2;
		int drawY = panelSize.height/2-mazeImage.getHeight()/2;
		g.drawImage(mazeImage, drawX, drawY, null);
	}
}
