/**
 * 
 */
package ComplexityRoughDraft;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;

/**
 * @author Steven Lawrence
 *
 */
public class Grid {
	// a 2D array of Cell objects
	private int w;            // width
	private int h;            // height
	private Cell[][] grid;
	private static final int FONT_SIZE = 30;

	/**
	 * Constructor
	 * @param w the width 
	 * @param h the height
	 */
	public Grid(int w, int h) {
		this.w = w;
		this.h = h;	
		grid = new Cell[this.h][this.w];
	
		for (int i = 0; i < grid.length; i++) {        // loop through the rows
			for (int j = 0; j < grid[i].length; j++) { // loop through the columns in that row
				Cell c = new Cell(i,j);                // make a cell for that position 
				grid[i][j] = c;                        // assign it to the grid
			}
		}
	}
	
	/**
	 * draws the grid of cells
	 * @param g the tool to draw the grid with
	 */
	public void display(Graphics2D g) {
		int cellNumber = 1;
		String cellNum;
		
		for (int i = 0; i < grid.length; i++) {         // loop through the rows
			for (int j = 0; j < grid[i].length; j++) {  // loop through the columns in that row
				grid[i][j].detectWallBreaks(grid);
				System.out.println(grid[i][j]);         // for testing purposes
				grid[i][j].draw(g);                     // draw the cell
				
				System.out.println(cellNumber);         // for testing purposes
				
				// draw the cell number on the maze in swing (used for testing purposes)
				cellNum = "" + cellNumber;
				g.setColor(Color.blue);
	            g.setFont(new Font("TimesRoman", Font.PLAIN, FONT_SIZE)); 
				g.drawString(cellNum, grid[i][j].getX(), grid[i][j].getY() + Cell.getWallLength());
				cellNumber += 1;
			}
		}
	}
	
	/**
	 * @return the 2D array of cells
	 */
	public Cell[][] getGrid(){
		return grid;
	}
	
	/**
	 * get the cell based on its row and column position in the grid
	 * @param i the ith row
	 * @param j the jth column
	 * @return the cell at that position
	 */
	// Note to self: Not sure if this method is needed
	public Cell getCell(int i, int j) {
		return grid[i][j];
	}

	/**
	 * @return the width
	 */
	// Note to self: I don't think this method is needed
	public int getW() {
		return w;
	}

	/**
	 * @return the height
	 */
	// Note to self: I don't think this method is needed
	public int getH() {
		return h;
	}
	
	/**
	 * Resets all of the cells in the grid
	 */
	public void resetGrid() {
		for (int i = 0; i < grid.length; i++) {         
			for (int j = 0; j < grid[i].length; j++) {
				grid[i][j].resetCell();
			}
		}
	}
}
