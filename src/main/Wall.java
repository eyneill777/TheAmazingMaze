package main;

public class Wall 
{
	Cell[] dividedCells = new Cell[2];
	boolean outsideWall = false;
	
	public Wall(Cell cell1, Cell cell2)
	{
		dividedCells[0] = cell1;
		dividedCells[1] = cell2;
		if(cell2 == null)
		{
			outsideWall = true;
		}
	}
}
