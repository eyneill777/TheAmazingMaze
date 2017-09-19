package main;

public abstract class MazeGenerator 
{
	Maze maze;
	
	public MazeGenerator(Maze maze)
	{
		this.maze = maze;
	}
	
	public abstract void generateMaze();
}
