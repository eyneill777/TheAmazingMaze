/**
 * 
 */
package main;

import java.util.Random;

/**
 * @author Steven Lawrence
 *
 */
public class Sidewinder extends MazeGenerator {
	Maze maze;
	int width, height;
	public Sidewinder(Maze maze) {
		super(maze);
		this.maze = maze;
		width = maze.mazeData.length;
		height = maze.mazeData[0].length;
				
	}
	
	public void generateMaze() {
		int runStart, selection;
		Random rand;
		// Clear out the first row in the maze
		for (int topCol = 0; topCol < width - 1; topCol++) {
			maze.mazeData[topCol][0].removeWall(Direction.East);
		} 
		
	    // loop through the rest of the maze
		for(int row = 1; row < height; row++) {
			runStart = 0; // a starting point within a row to decide which cell to break north
			
			for (int col = 0; col < width; col++) {
				rand = new Random(); // probability for deciding when to stop a run and break north
				if ((col + 1 == width) || rand.nextInt(2) == 0) {
					/* open up a pathway through the north
					 * For all the other rows keep going right until you reach the width
					 * or your randomly generate a zero
					 * note to self: not sure what to make the random integer range too.*/
					selection = runStart + rand.nextInt(col - runStart + 1); // select a random cell within that run
					maze.mazeData[selection][row].removeWall(Direction.North);  // break through the north wall of that cell
					runStart = col + 1; // reset the start for the next run
					
				} else if (col + 1 < width) {
					// open up a pathway east as long as its within the bounds
					maze.mazeData[col][row].removeWall(Direction.East);
				} 

			}
		}
	}
}
