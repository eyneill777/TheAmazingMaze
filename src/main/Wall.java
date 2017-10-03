package main;

import java.awt.Color;

public class Wall 
{
	Cell[] dividedCells = new Cell[2];
	boolean outsideWall = false;
	public Color wallColor;
	
	public Wall(Cell cell1, Cell cell2)
	{
		dividedCells[0] = cell1;
		dividedCells[1] = cell2;
		if(cell2 == null)
		{
			outsideWall = true;
		}
		wallColor = Color.GRAY;
	}
	
	/**
	 * @return the neighboring cell
	 */
	public Cell getNeighbor() {
		return dividedCells[1];
	}
}
