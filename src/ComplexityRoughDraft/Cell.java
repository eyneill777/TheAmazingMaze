/**
 * 
 */
package ComplexityRoughDraft;

import java.awt.Graphics2D;
import java.util.HashSet;

/**
 * @author Steven Lawrence
 */
public class Cell {
	/*cell object- a cell on a grid is a space boxed in by four walls*/ 
	private int x, y, row, col;
	private static final int WALL_LENGTH = 200;  // length of the wall (large for the small demo maze)
	private Wall northWall;
	private Wall westWall;
	private Wall southWall;
	private Wall eastWall;
	private final Wall[] WALLS;
	private HashSet<Wall> openings; // openings is a set, so that no duplicates can exist
	
	
	/**
	 * Constructor
	 * A cell has four walls that box it into a square.  Some of the walls of the cell may be broken,
	 * if that is the case then the cell has openings at those walls. 
	 * @param i the row position of the cell
	 * @param j the column position of the cell
	 */
	public Cell(int i, int j) {
		// the x and y coordinate position for swing representing the top left corner of the cell
		x = j * WALL_LENGTH;
		y = i * WALL_LENGTH;
		row = i;  
		col = j;
		// the walls are built based on the x and y coordinates and the wall length
		northWall = new Wall(x, y, x + WALL_LENGTH, y);
		westWall = new Wall(x, y, x, y + WALL_LENGTH);
		southWall = new Wall(x, y + WALL_LENGTH, x + WALL_LENGTH, y + WALL_LENGTH);
		eastWall = new Wall(x + WALL_LENGTH, y, x + WALL_LENGTH, y + WALL_LENGTH);
		WALLS = new Wall[] {northWall, westWall, southWall, eastWall};
		// contains openings of the cell, if any exist, based on if any of its walls are broken
		openings = new HashSet<Wall>();
	}
	/**
	 * draws the cell
	 * @param g the tool to draw the cell with
	 */
	public void draw(Graphics2D g) {
		for (Wall w : WALLS ) {
			w.draw(g);
		}
	}
	
	/**
	 * breaks the north wall and adds it to openings
	 */
	public void breakNorthWall() {
		northWall.breakWall();
		openings.add(northWall);
	}
	
	/**
	 * breaks the east wall and adds it to openings
	 */
	public void breakEastWall() {
		eastWall.breakWall();
		openings.add(eastWall);
	}
	
	/**
	 * breaks the south wall and adds it to openings
	 */
	public void breakSouthWall() {
		southWall.breakWall();
		openings.add(southWall);
	}
	
	/**
	 * breaks the west wall and adds it to the openings
	 */
	public void breakWestWall() {
		westWall.breakWall();
		openings.add(westWall);
	}
	
	/**
	 * @return the walls of the cell
	 */
	public Wall[] getWALLS() {
		return WALLS;
	}
	
	/**
	 * @return the northWall
	 */
	public Wall getNorthWall() {
		return northWall;
	}
	
	/**
	 * @return the westWall
	 */
	public Wall getWestWall() {
		return westWall;
	}
	/**
	 * @return the southWall
	 */
	public Wall getSouthWall() {
		return southWall;
	}
	
	/**
	 * @return the eastWall
	 */
	public Wall getEastWall() {
		return eastWall;
	}
	
	/**
	 * @return the x coordinate on java.swing object (translated x)
	 */
	public int getX() {
		int trans = x + Wall.getTranslation();
		return trans;
	}
	
	/**
	 * @return the y coordinate on the java.swing object (translated y)
	 */
	public int getY() {
		int trans = y + Wall.getTranslation();
		return trans;
	}
	
	/**
	 * @return the wallLength
	 */
	public static int getWallLength() {
		return WALL_LENGTH;
	}
	
	/**
	 * @return all the broken walls in openings in an array data structure
	 */
	public Wall[] getOpenings() {
		Wall[] brokenWalls = new Wall[openings.size()];
		int i = 0;
		for (Wall w: openings) {
			brokenWalls[i] = w;
			i++;
		}
		return  brokenWalls;
	}
	
	/**
	 * gets the neighboring cell based on the current cell's wall
	 * @param cells the 2D grid in which all of the cells lie on
	 * @param neighbs the wall that is in between the cell and its neighbor
	 * @return
	 */
	// Note to self: considering moving this method to the Grid class
	public Cell getNeighbor(Cell[][] cells, Wall neighbs) {
		if (neighbs == northWall) {
			return cells[row - 1][col];
		} else if (neighbs == westWall) {
			return cells[row][col - 1];
		} else if (neighbs == southWall) {
			return cells[row + 1][col];
		} else if (neighbs == eastWall) {
			return cells[row][col + 1];
		}else {
		return null;
		}
	}
	
	/**
	 * Determine if the cell is a corner based on which of its walls are broken
	 * @return a boolean of if the cell is a corner vertex, or not
	 */
	public boolean isCorner() {
		if ((northWall.isBustedWall() && westWall.isBustedWall()) || northWall.isBustedWall() && eastWall.isBustedWall()) {
			return true;
		} else if (southWall.isBustedWall() && westWall.isBustedWall() || southWall.isBustedWall() && eastWall.isBustedWall()) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * 
	 * @return a boolean based on if the cell is a dead end (only one opening to a neighbor)
	 */
	public boolean isDeadEnd() {
		if (openings.size() == 1) {
			return true;
		} else {
			return false;
		}
	}
	
	/**
	 * Determines if its neighbor cells' walls are broken.  If its neighbor's wall is broken then the
	 * corresponding wall of the cell is broken.
	 * @param cells the grid of cells to which the cell lies on
	 */
	// Note to self: Considering moving this to the Grid Class
	public void detectWallBreaks( Cell[][] cells) {
		
		if ((row - 1 >= 0) && (cells[row - 1][col].getSouthWall().isBustedWall())) {  // if the north cell exists and its south wall is broken
			breakNorthWall();
		}
		if ((col - 1 >= 0) && (cells[row][col - 1].getEastWall().isBustedWall())) {   // if the west cell exists and its east wall is broken
			breakWestWall();
		}
		if ((row + 1 < cells.length) && (cells[row + 1][col].getNorthWall().isBustedWall())) {   // if the south cell exists and its north wall is broken
			breakSouthWall();
		}
		if ((col + 1 < cells[0].length) && (cells[row][col + 1].getWestWall().isBustedWall())) {   // if the east cell exists and its west wall is broken
			breakEastWall();
		}
	}
	
	/**
	 * Breaks the walls of the cell's neighbors that correspond to its own broken walls.
	 * @param cells the grid of cells that the cell lies on
	 */
	// Note to self: Considering moving this method to the Grid Class
	public void BreakNeighbsWall(Cell[][] cells) {
		Cell neighborCell;
		
		for (Wall w: openings) {
			neighborCell = getNeighbor(cells, w);
			neighborCell.detectWallBreaks(cells);
		}
	}
	
	/**
	 * Resets the walls to being unbroken, and clears out the openings
	 */
	public void resetCell() {
		for (Wall w: WALLS) {
			w.resetWall();
		}
		openings.clear();
	}
	
	/**
	 * makes the cell object printable (used for testing)
	 */
	public String toString() {
		return "cell( row: " + row + ", col: " + col + ")";
	}
}
