/**
 * 
 */
package main;

import java.awt.Dimension;
import java.awt.image.BufferedImage;

/**
 * @author dmx
 *
 */
public class SimpleSampleMaze extends MazeGenerator {
	private static final Dimension fiveByFive = new Dimension(5,5);

	public SimpleSampleMaze(Maze maze) {
		super(maze);
	}

	@Override
	public void generateMaze() {
		// simple 5x5 sample maze
		maze.size = fiveByFive;
		maze.mazeData = new Cell[maze.size.width][maze.size.height];
		maze.mazeImage = new BufferedImage(maze.size.width*maze.cellSize, maze.size.height*maze.cellSize, BufferedImage.TYPE_INT_ARGB);
		maze.reset();
		
		maze.mazeData[0][0].removeWall(Direction.South);
		maze.mazeData[0][1].removeWall(Direction.East);
		maze.mazeData[1][1].removeWall(Direction.South);
		maze.mazeData[1][2].removeWall(Direction.West);
		maze.mazeData[0][2].removeWall(Direction.South);
		maze.mazeData[1][2].removeWall(Direction.East);
		maze.mazeData[2][2].removeWall(Direction.South);
		maze.mazeData[2][3].removeWall(Direction.South);
		maze.mazeData[2][4].removeWall(Direction.West);
		maze.mazeData[1][4].removeWall(Direction.North);
		maze.mazeData[1][4].removeWall(Direction.West);
		maze.mazeData[2][2].removeWall(Direction.East);
		maze.mazeData[3][2].removeWall(Direction.North);
		maze.mazeData[3][1].removeWall(Direction.West);
		maze.mazeData[3][1].removeWall(Direction.North);
		maze.mazeData[3][0].removeWall(Direction.West);
		maze.mazeData[2][0].removeWall(Direction.West);
		maze.mazeData[3][0].removeWall(Direction.East);
		maze.mazeData[4][0].removeWall(Direction.South);
		maze.mazeData[4][1].removeWall(Direction.South);
		maze.mazeData[4][2].removeWall(Direction.South);
		maze.mazeData[4][3].removeWall(Direction.West);
		maze.mazeData[4][3].removeWall(Direction.South);
		maze.mazeData[4][4].removeWall(Direction.West);
		}
	
	

}
