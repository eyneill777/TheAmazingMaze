package main;

public class RecursiveDivision extends MazeGenerator
{
	
	
	public RecursiveDivision(Maze maze) 
	{
		super(maze);
	}

	@Override
	public void generateMaze() 
	{
		
	}
	
	private void createChamber(int x, int y, int width, int height)
	{
		for(int dy = y;dy<y+height;dy++)
		{
			maze.mazeData[x][dy].addWall(Direction.West);
			maze.mazeData[x+width-1][dy].addWall(Direction.East);
		}
		for(int dx = x;dx<x+width;dx++)
		{
			maze.mazeData[y][dx].addWall(Direction.North);
			maze.mazeData[y+height-1][dx].addWall(Direction.North);
		}
	}
}
