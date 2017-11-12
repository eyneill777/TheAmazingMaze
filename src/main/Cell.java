package main;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.util.ArrayList;

public class Cell {
	Wall[] walls = new Wall[4]; // The walls surrounding this cell
	Maze maze; // The maze this cell belongs to
	Point position; // This cell's position in the maze
	Color bgColor;
	ArrayList<Cell> neighbors; // contains neighbor cells from openings, or removed walls
	private int label;

	public Cell(Maze maze, Point position) {
		this.maze = maze;
		this.position = position;
		bgColor = Color.white;
		neighbors = new ArrayList<Cell>();
		label = 1; 
	}
  
	public int getLabel() {
		return label;
	}

	public void setLabel(int label) {
		this.label = label;
	}

	public void initWalls() {
		for (int i = 0; i < walls.length; i++) {
			if (walls[i] == null) {
				addWall(Direction.valueOf(i));
			}
		}
	}

	public void draw(Graphics2D g, int size) {
		g.setColor(bgColor);
		g.fillRect(position.x * size, position.y * size, size, size);
		for (int i = 0; i < 4; i++) {
			Wall w = walls[i];
			if (w == null) { // if the wall is null then skip it
				continue;
			} else {
				g.setColor(w.wallColor);
				if (i == Direction.North.num) {
					g.drawLine(position.x * size, position.y * size, (position.x + 1) * size, position.y * size);
				} else if (i == Direction.East.num) {
					g.drawLine((position.x + 1) * size, position.y * size, (position.x + 1) * size,
							(position.y + 1) * size);
				} else if (i == Direction.South.num) {
					g.drawLine(position.x * size, (position.y + 1) * size, (position.x + 1) * size,
							(position.y + 1) * size);
				} else if (i == Direction.West.num) {
					g.drawLine(position.x * size, position.y * size, position.x * size, (position.y + 1) * size);
				}
			}
		}
	}

	public void addWall(Direction direction) {
		Cell cell2 = null;
		if (direction == Direction.North) {
			if (position.y > 0) {
				cell2 = maze.getCell(new Point(position.x, position.y - 1));
			}
		} else if (direction == Direction.East) {
			if (position.x < maze.size.width - 1) {
				cell2 = maze.getCell(new Point(position.x + 1, position.y));
			}
		} else if (direction == Direction.South) {
			if (position.y < maze.size.height - 1) {
				cell2 = maze.getCell(new Point(position.x, position.y + 1));
			}
		} else if (direction == Direction.West) {
			if (position.x > 0) {
				cell2 = maze.getCell(new Point(position.x - 1, position.y));
			}
		}
		walls[direction.num] = new Wall(this, cell2);

	}

	public boolean hasWall(Direction direction) {
		if (walls[direction.num] != null) {
			return true;
		} else
			return false;
	}

	public void removeWall(Direction direction) {
		if (walls[direction.num] != null) {
			Cell neighbs = walls[direction.num].getNeighbor(); // get the neighbor cell
			neighbors.add(neighbs); // add the new neighboring cell to neighbors
			neighbs.neighbors.add(this); // add current cell to the neighbor's list of the neighboring cells
			walls[direction.num] = null; // delete the wall from its stored locations
			neighbs.walls[direction.opposite()] = null;
		}
	}

	/**
	 * 
	 * @return a boolean based on if the cell is a dead end (only one opening to a
	 *         neighbor)
	 */
	public boolean isDeadEnd() {
		if (neighbors.size() == 1) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Determine if the cell is a corner based on which of its walls are broken
	 * 
	 * @return a boolean of if the cell is a corner vertex, or not
	 */
	public boolean isCorner() {
		if (walls[Direction.North.num] == null && walls[Direction.West.num] == null) {
			return true;
		}
		if (walls[Direction.North.num] == null && walls[Direction.East.num] == null) {
			return true;
		}
		if (walls[Direction.South.num] == null && walls[Direction.West.num] == null) {
			return true;
		}
		if (walls[Direction.South.num] == null && walls[Direction.East.num] == null) {
			return true;
		} else {
			return false;
		}
	}
	public Wall getNorthWall() {
		return walls[Direction.North.num];
	}
	public Wall getWestWall() {
		return walls[Direction.West.num];
	}

	public String toString() {
		return "Cell(x=" + this.position.x + ", y=" + this.position.y + ") " + this.label;
	}

	public Wall getSouthWall() {
		return walls[Direction.South.num];
	}
	public Wall getEastWall() {
		return walls[Direction.East.num]; 
	}

	@Override
	public boolean equals(Object obj) {
		Cell other = (Cell ) obj;
		if  (this.position == other.position &&
				this.label ==other.label) {
			return true;	
		}
		else return false;
	}
}
