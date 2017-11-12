package main;

import java.util.Random;

public class RecursiveDivision extends MazeGenerator
{
	Random rand;
	
	public RecursiveDivision(Maze maze) 
	{
		super(maze);
		rand = new Random();
	}

	@Override
	public void generateMaze() 
	{
		maze.clear();
		createChamber(0, 0, maze.size.width, maze.size.height, 0);
	}
	
	private void createChamber(int x, int y, int width, int height, int count)
	{
		if(count < 4)
		{
			int fx = x+width, fy = y+height;
			System.out.println("Count: "+count);
			count++;
			System.out.println("Create Chamber: "+x+","+y+" : "+fx+","+fy);
			for(int dy = y;dy<y+height;dy++)
			{
				maze.mazeData[x][dy].addWall(Direction.West);
				maze.mazeData[fx-1][dy].addWall(Direction.East);
			}
			for(int dx = x;dx<x+width;dx++)
			{
				maze.mazeData[dx][y].addWall(Direction.North);
				maze.mazeData[dx][fy-1].addWall(Direction.South);
			}
			if(width > 1 && height > 1)
			{
				int rx = rand.nextInt(width-1)+x+1;
				int ry = rand.nextInt(height-1)+y+1;
				System.out.print("Quartile: ");
				System.out.println(0);
				createChamber(x, y, rx-x, ry-y, count);
				System.out.println(1);
				createChamber(rx, y, width-rx, ry-y, count);
				System.out.println(2);
				createChamber(x, ry, rx-x, height-ry, count);
				System.out.println(3);
				createChamber(rx, ry, width-rx, height-ry, count);
				System.out.println(rx+" "+ry);
				openWalls(rx, ry, ry-y, y+height-ry, rx-x, x+width-rx);
			}
		}
		//if(count == 0)
		{
			maze.mazeData[0][0].removeWall(Direction.North);
			maze.mazeData[maze.size.width-1][maze.size.height-1].removeWall(Direction.South);
		}
	}
	
	private void openWalls(int wallX, int wallY, int spaceAbove, int spaceBelow, int spaceLeft, int spaceRight)
	{
		int n = rand.nextInt(4);
		for(int i = 0;i<4;i++)
		{
			if(i != n)
			{
				if(i == Direction.North.num)
				{
					int y = wallY-rand.nextInt(spaceAbove);
					maze.mazeData[wallX][y].removeWall(Direction.West);
				}
				else if(i == Direction.South.num)
				{
					int y = wallY+rand.nextInt(spaceBelow);
					maze.mazeData[wallX][y].removeWall(Direction.West);
				}
				else if(i == Direction.East.num)
				{
					int x = wallX-rand.nextInt(spaceLeft);
					maze.mazeData[x][wallY].removeWall(Direction.North);
				}
				else if(i == Direction.West.num)
				{
					int x = wallX+rand.nextInt(spaceRight);
					maze.mazeData[x][wallY].removeWall(Direction.North);
				}
			}
		}
	}
}
