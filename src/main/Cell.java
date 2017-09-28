package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class Cell 
{
	Wall[] walls = new Wall[4]; //The walls surrounding this cell
	Maze maze; //The maze this cell belongs to
	Point position; //This cell's position in the maze
	Color bgColor;
	
	public Cell(Maze maze, Point position)
	{
		this.maze = maze;
		this.position = position;
		bgColor = Color.white;
	}
	
	public void initWalls()
	{
		for(int i = 0;i<walls.length;i++)
		{
			if(walls[i] == null)
			{
				addWall(Direction.valueOf(i));
			}
		}
	}
	
	public void draw(Graphics2D g, int size)
	{
		g.setColor(bgColor);
		g.fillRect(position.x*size, position.y*size, size, size);
		for(int i = 0;i<4;i++)
		{
			Wall w = walls[i];
			g.setColor(w.wallColor);
			if(i == Direction.North.num)
			{
				g.drawLine(position.x*size, position.y*size, (position.x+1)*size, position.y*size);
			}
			else if(i == Direction.East.num)
			{
				g.drawLine((position.x+1)*size, position.y*size, (position.x+1)*size, (position.y+1)*size);
			}
			else if(i == Direction.South.num)
			{
				g.drawLine(position.x*size, (position.y+1)*size, (position.x+1)*size, (position.y+1)*size);
			}
			else if(i == Direction.West.num)
			{
				g.drawLine(position.x*size, position.y*size, position.x*size, (position.y+1)*size);
			}
		}
	}
	
	public void addWall(Direction direction)
	{
		Cell cell2 = null;
		if(direction == Direction.North)
		{
			if(position.y > 0)
			{
				cell2 = maze.getCell(new Point(position.x, position.y-1));
			}
		}
		else if(direction == Direction.East)
		{
			if(position.x < maze.size.width-1)
			{
				cell2 = maze.getCell(new Point(position.x+1, position.y));
			}
		}
		else if(direction == Direction.South)
		{
			if(position.y < maze.size.height-1)
			{
				cell2 = maze.getCell(new Point(position.x, position.y+1));
			}
		}
		else if(direction == Direction.West)
		{
			if(position.x > 0)
			{
				cell2 = maze.getCell(new Point(position.x-1, position.y));
			}
		}
		walls[direction.num] = new Wall(this, cell2);
	}
	
	public void removeWall(Direction direction)
	{
		walls[direction.num] = null;
	}
}
