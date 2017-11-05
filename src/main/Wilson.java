/**
 * 
 */
package main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

/**
 * @author Steven Lawrence
 *  Wilson algorithm creates a maze by forming a uniform spanning tree. A spanning tree  connects 
 *  all the vertices of a graph. A uniform spanning tree is any one of the possible spanning trees
 *  of a graph, selected randomly and with equal probability.  Thus Wilson's method will always 
 *  produce connected mazes.  Basically, we start off by adding a random cell to be apart of
 *  the maze or 'universal spanning tree'.  Then we are going to being selecting other cells, or vetices 
 *  at random and perform a random walk until we encounter the cell in the maze. Those cells in the random
 *  walk are added into the maze as well.  Then we pick another random cell and perform a random walk again
 *  until we hit one of those cells in the maze, and so forth.
 */
public class Wilson extends MazeGenerator {
	HashSet<Cell> inMaze, remaining;
	Random rand;
	Maze maze;
	int yBound, xBound;
	
	public Wilson(Maze maze) {
		super(maze);
		this.maze = maze;
		yBound = maze.size.height;
		xBound = maze.size.width;
		remaining = new HashSet<Cell>();
		rand = new Random();
		inMaze = new HashSet<Cell>();
	}
	
	/**
	 * a random walk from a random cell not in the maze to a cell in the maze
	 */
	public void walk() {
		Cell startCell, nextCell, endCell;
		Wall thisWay;
		Direction dirr;
		HashMap<Cell,Direction> walked = new HashMap<Cell,Direction>();
		
		/*  select a number from 0 to size of remaining to use as a stoping index for an iterator to select
		 *  a random cell that is not in the maze already.
		 */
		int index = rand.nextInt(remaining.size());
		Iterator<Cell> iter = remaining.iterator();
		for (int i = 0; i < index; i++) {
		    iter.next();
		}
		startCell = iter.next();
		
		thisWay = startCell.walls[rand.nextInt(4)]; // choose a random wall in the direction to go to next
		while (thisWay.outsideWall) {
			thisWay = startCell.walls[rand.nextInt(4)]; // make sure the wall is not an outside wall
		}
		dirr = thisWay.getRelativeCellPosition();
		walked.put(startCell, dirr); // record the direction of the cell to make the path later
		nextCell = thisWay.getNeighbor();
		
		/*while the next cell is not in the maze continue walking. It doesn't matter if we cross 
		 * over a previously traversed cell as long as we keep the direction up to date. 
		 */
		while (!inMaze.contains(nextCell)) {
			Wall nextWall = nextCell.walls[rand.nextInt(4)];
			while (nextWall.outsideWall || nextWall.getCell2().equals(thisWay.getCell1())) {
				nextWall = nextCell.walls[rand.nextInt(4)]; // same as above but make sure we don't go backwards
			}
			thisWay = nextWall;
			dirr = thisWay.getRelativeCellPosition();
			walked.put(nextCell, dirr);
			nextCell = thisWay.getNeighbor();
		}
		
		endCell = nextCell; // end cell is in the maze
		nextCell = startCell;
		inMaze.add(startCell);
		remaining.remove(startCell);
		
		//create the path from start cell to end cell
		while (!nextCell.equals(endCell)) {
			dirr = walked.get(nextCell);
			thisWay = nextCell.walls[dirr.num];
			nextCell = thisWay.getCell2();
			nextCell.removeWall(Direction.valueOf(dirr.opposite()));
			inMaze.add(nextCell);
			remaining.remove(nextCell);			
		}
	}
	
	/**
	 * Perform walks until the maze is complete
	 */
	public void generateMaze() {
		// to start off add every cell in the maze to remaining
		for (int i = 0; i < maze.mazeData.length; i++) {
			for (int k = 0; k < maze.mazeData[0].length; k++) {
				remaining.add(maze.mazeData[i][k]);
			}
		}
		// select a random cell to add to the maze and then remove it from remaining
		Cell inCell =  maze.mazeData[rand.nextInt(xBound)][rand.nextInt(yBound)];
		inMaze.add(inCell);
		remaining.remove(inCell);
		while (!remaining.isEmpty()) {
			walk();
		}
	}
}
