package main;

import java.awt.Graphics2D;
import java.awt.Point;

public class Cell 
{
	Wall[] walls = new Wall[4]; //The walls surrounding this cell
	Maze maze; //The maze this cell belongs to
	Point position; //This cell's position in the maze
	
	public Cell(Maze maze, Point position)
	{
		this.maze = maze;
		this.position = position;
	}
	
	public void initWalls()
	{
		for(int i = 0;i<walls.length;i++)
		{
			
		}
	}
	
	public void draw(Graphics2D g)
	{
		
	}
	
	public void addWall(Direction direction)
	{
		
	}
	
	public void removeWall(Direction direction)
	{
		
	}
}
