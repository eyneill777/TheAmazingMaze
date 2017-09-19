package main;

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
		
	}
}
