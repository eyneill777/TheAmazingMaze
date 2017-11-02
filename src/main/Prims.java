/**
 * 
 */
package main;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;

/**
 * @author Steven Lawrence
 * Prim's algorithm works by starting out with a random cell in the grid.  It then marks all of its
 * neighboring cells as canidates and adds them to a "frontier" set which represents allowable additions
 * to the maze. One of the frontier cells is selected and it is added to the maze and then its neighbors
 * are added into the frontier set, and so forth until a maze is produced.
 */
public class Prims extends MazeGenerator {
	HashSet<Cell> frontier;
	HashSet<Cell> mazeSet;
	Maze maze;
	Random rand;
	int xBound, yBound;

	/**
	 * Start off by adding a random cell with in a grid to a set representing cells in the maze. Then 
	 * add its neighbors to the fronteir set.
	 * @param maze the maze to generate a maze with
	 */
	public Prims(Maze maze) {
		super(maze);
		this.maze = maze;
		rand = new Random();
		mazeSet = new HashSet<Cell>();
		frontier = new HashSet<Cell>();
		yBound = maze.size.height;
		xBound = maze.size.width;
		Cell startCell = maze.mazeData[rand.nextInt(xBound)][rand.nextInt(yBound)];
		mazeSet.add(startCell);
		addFrontier(startCell);
		System.out.println("hi" + startCell);
	}

	@Override
	/**
	 * Continously add cells to the maze and and the frontier set until there is no more canidate cells
	 * left.
	 */
	public void generateMaze() {
		int index;
		Cell chosenCell;
		Wall frontierWall;
		// while there there is still canidate cells to be added to the maze
		
		while (!frontier.isEmpty()){
			// sellect a random frontier cell to add to the maze
			index = rand.nextInt(frontier.size());
			Iterator<Cell> iter = frontier.iterator();
			for (int i = 0; i < index; i++) {
			    iter.next();
			}
			chosenCell = iter.next();
			// add any new frontier cells to the maze
			addFrontier(chosenCell);
			
			//  Select which of the neighboring maze cells walls randomly
			frontierWall = chosenCell.walls[rand.nextInt(4)];
			while (frontierWall.getCell2() == null || !mazeSet.contains(frontierWall.getCell2())) {
				//System.out.println(" c " + chosenCell + " f " +frontierWall.getCell2());
				frontierWall = chosenCell.walls[rand.nextInt(4)];
			}
			chosenCell.removeWall(frontierWall.relativeCellPosition);
			
			// add Chosen Cell to the maze 
			mazeSet.add(chosenCell);
			frontier.remove(chosenCell);
		}

	}

	/**
	 *  loop through the walls of the maze cell, if the wall is an outside wall, or if it was already
	 *  removed then skip it. Otherwise, check to see if the neighbor is not in the maze, and if its not
	 *  add it to the frontier set.
	 * @param mazeCell cell in the maze
	 */
	private void addFrontier(Cell mazeCell) {
		for (Wall w : mazeCell.walls) {
			if (w != null && !w.isOutsideWall()) {
				if (!mazeSet.contains(w.getCell2())) {
					frontier.add(w.getCell2());
				}
			}
		}
	}

}
