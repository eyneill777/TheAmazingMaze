/**
 * 
 */
package main;

import java.util.HashSet;
import java.util.Random;

/**
 * @author Steven Lawrence Eller's creates the Maze one row at a time, where
 *         once a row has been generated, the algorithm no longer looks at it.
 *         Each cell in a row is contained in a set, where two cells are in the
 *         same set if there's a path between them through the part of the Maze
 *         that's been made so far.
 */
public class Ellers extends MazeGenerator {
	int setCounter;
	Cell[][] mazeGrid;
	int w, h;
	Random rand;

	public Ellers(Maze maze) {
		super(maze);
		setCounter = 2; // label the different sets by integers
		mazeGrid = maze.mazeData;
		w = (int) maze.size.getWidth();
		h = (int) maze.size.getHeight();
		rand = new Random();
	}

	/**
	 * Connects sets in the same row by a 50/50 probability if its not the last row.
	 * If it is the last row then all sets must be connected to one another in order
	 * to make the maze a perfect maze.
	 * 
	 * @param row
	 *            the row to undergo set adjoining
	 * @param lastRow
	 *            boolean on whether the row is the last row or not
	 */
	private void adjoinRow(int row, boolean lastRow) {
		// if not go through the row checking to see if neighbors are in the same set or
		// not
		for (int x = 0; x < w - 1; x++) {
			Cell leftCell = mazeGrid[x][row];
			Cell rightCell = mazeGrid[x + 1][row];
			if (leftCell.getLabel() != rightCell.getLabel()) {
				// if they are not in the same set then giveng a 50/50 chance adjoin them into
				// one set
				if (rand.nextBoolean() == true || lastRow) {
					leftCell.removeWall(Direction.East);
					rightCell.setLabel(leftCell.getLabel());
				}
			}
		}
	}

	/**
	 * Connects the sets in a row with random chance
	 * 
	 * @param row
	 *            a non last row
	 */
	private void adjoinRow(int row) {
		this.adjoinRow(row, false);
	}

	/**
	 * connects sets in the current row to cells in the rows below. Atleast one
	 * connection must be made per set in the row.
	 * 
	 * @param row
	 *            a non last row
	 */
	private void adjoinVertical(int row) {
		int setStart = 0, setSize, selection;
		Cell chosenCell;
		// find all of the sets in the row
		for (int x = 0; x < w - 1; x++) {
			Cell leftCell = mazeGrid[x][row];
			Cell rightCell = mazeGrid[x + 1][row];
			if (leftCell.getLabel() != rightCell.getLabel() || x == w - 2) {
				setSize = x - setStart + 1;
				if (x == w - 2) { // handle the last cell, it will either be in its own set, or apart of the last
									// set
					if (leftCell.getLabel() != rightCell.getLabel()) {
						rightCell.getSouthWall().getCell2().setLabel(leftCell.getLabel());
						rightCell.removeWall(Direction.South);
					} else {
						if (leftCell.getEastWall() != null) {
							leftCell.removeWall(Direction.East);
						}
						setSize = x - setStart + 2;
					}
				}
				// choose a random cell in the found set to take care of the required one
				// connection per set
				selection = setStart + rand.nextInt(setSize);
				chosenCell = mazeGrid[selection][row];
				chosenCell.getSouthWall().getCell2().setLabel(chosenCell.getLabel());
				chosenCell.removeWall(Direction.South);
				setStart = x + 1; // set the new starting position of the set
			}
		}
	}

	/**
	 * go through a row and define cells that were not broken into from the above
	 * row to their own set
	 * 
	 * @param row
	 *            the row to set
	 */
	private void setRow(int row) {
		for (int x = 0; x < w; x++) {
			if (mazeGrid[x][row].getLabel() == 1) {
				mazeGrid[x][row].setLabel(setCounter);
				setCounter++;
			}
		}
	}

	@Override
	/**
	 * generate the maze by setting the rows, adjoining them horizontally, and
	 * vertically until the last row is reach then all the sets must be connected to
	 * one another.
	 */
	public void generateMaze() {
		// loop through the rows of the maze
		for (int y = 0; y < h - 1; y++) {
			setRow(y);
			adjoinRow(y);
			adjoinVertical(y);
		}

		// take care of the last row
		setRow(h - 1);
		adjoinRow(h - 1, true);
	}
}
