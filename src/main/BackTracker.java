package main;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Stack;

/**
 * @author Steven Lawrence
 * The backtracker algorithm carves out the maze with a random walk.  It remembers which cells have been
 * visited. It moves along the grid carving out a path, and only choosing randomly from cells that it has 
 * not visited next to move to.  If none of the neighboring cells have been visited then the algorithm moves
 * back to the previous cell, and checks to see if it has any valid moves left, and so forth until the 
 * algorithm retraces itself back to the starting point.
 */
public class BackTracker extends MazeGenerator {
	Cell[][] mazeData;
	Random rand;
	Stack<Cell> passage;
	static final int VISITED = 2;
	
	/**
	 * Note to self this is a depth first search method.  A stack can be used.  Another option would be 
	 * to do this algorithm recursively.
	 * @param maze the maze to carve from.
	 */
	public BackTracker(Maze maze) {
		super(maze);
		mazeData = maze.mazeData;
		rand = new Random();
		passage = new Stack<Cell>();
	}

	/**
	 * @param theCell the current cell the algorithm is choosing from
	 * @return a set of walls whose neighboring cells represent a valid move
	 */
	private HashSet<Wall> validMoves(Cell theCell) {
		HashSet<Wall> moveSet = new HashSet<Wall>();
		for (Wall w : theCell.walls) {
			if (w == null || w.isOutsideWall()) {
				continue;
			}
			if (w.getCell2().getLabel() == VISITED) {
				continue;
			} else {
				moveSet.add(w);
			}
		}
		return moveSet;
	}
	
	@Override
	/**
	 * continue to move randomly until no valid moves are left, then backtrack to the previous cell, and
	 * repeat this process.
	 */
	public void generateMaze() {
		Iterator<Wall> iter;
		int index, wallDirection;
		Wall goThrough;
		Cell beginCell, c;
		HashSet moves;
		
		// get the starting cell
		beginCell = mazeData[rand.nextInt(mazeData.length)][rand.nextInt(mazeData.length)];
		
		// mark it and put in the maze
		beginCell.setLabel(VISITED);
		passage.push(beginCell);
		
		// loop until their is no more cells left to choose from 
		c = beginCell;
		moves = validMoves(c);
		while (c != beginCell ||  !passage.isEmpty()) {
			if (moves.isEmpty()) {
				c = passage.pop();
				moves = validMoves(c);
				continue;
			}
			
			// randomly select a valid choice
			iter = moves.iterator();
			index = rand.nextInt(moves.size());
			for (int i = 0; i < index; i++) {
				iter.next();
			}
			goThrough = iter.next();
			c = goThrough.getCell2();
			c.setLabel(VISITED);
			passage.push(c);
			wallDirection = goThrough.relativeCellPosition.opposite();
			c.removeWall(Direction.valueOf(wallDirection));
			moves = validMoves(c);
	  }
	}
}

