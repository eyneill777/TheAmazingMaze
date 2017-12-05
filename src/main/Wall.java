package main;

import java.awt.Color;
import java.util.Arrays;

public class Wall {
	Cell[] dividedCells = new Cell[2];
	boolean outsideWall = false;
	public Color wallColor;
	Direction relativeCellPosition;
	int x;
	int y;
	int x2;
	int y2;

	public Wall(Cell cell1, Cell cell2) {
		dividedCells[0] = cell1;
		dividedCells[1] = cell2;
		x = cell1.position.x;
		y = cell1.position.y;

		if (cell2 == null) {
			outsideWall = true;
		} else {
			x2 = cell2.position.x;
			y2 = cell2.position.y;
		}
		relativeCellPosition = getRelativeCellPosition();
		wallColor = Color.BLACK;
	}
	public Cell getCell1() {
		return dividedCells[0];
	}
	public Cell getCell2() {
		return dividedCells[1];
	}
	public boolean isOutsideWall() {
		return outsideWall;
	}
	
	/**
	 * 
	 * @return The position of the first cell relative to the wall and second cell.
	 */
	public Direction getRelativeCellPosition() {
		if (x == x2 && y > y2) {
			relativeCellPosition = Direction.North;
		} else if (x == x2 && y < y2) {
			relativeCellPosition = Direction.South;
		} else if (x > x2 && y == y2) {
			relativeCellPosition = Direction.West;
		} else if (x < x2 && y == y2)
			relativeCellPosition = Direction.East;
		return relativeCellPosition;
	}

	/**
	 * @return the neighboring cell
	 */
	public Cell getNeighbor() {
		return dividedCells[1];
	}

	@Override
	public String toString() {
		return "Wall [dividedCells=" + Arrays.toString(dividedCells) + ", outsideWall=" + outsideWall
				+ ", relativeCellPosition=" + relativeCellPosition + "]";
	}
}
