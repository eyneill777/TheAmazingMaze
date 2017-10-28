package main;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class KruskalsAlgorithm extends MazeGenerator {
	private ArrayList<Wall> walls = new ArrayList<Wall>();
	private static final Dimension fiveByFive = new Dimension(5, 5);
	private Set<Cell> hash_Set = new HashSet<Cell>();

	public KruskalsAlgorithm(Maze maze) {
		super(maze);
	}

	public void generateMaze() {
		Random rand = new Random();
		maze.size = fiveByFive;
		maze.mazeData = new Cell[maze.size.width][maze.size.height];
		maze.mazeImage = new BufferedImage(maze.size.width * maze.cellSize, maze.size.height * maze.cellSize,
				BufferedImage.TYPE_INT_ARGB);
		maze.reset();

		// adding all the walls.
		for (int i = 0; i < maze.mazeData.length; i++) {
			for (int j = 0; j < maze.mazeData[i].length; j++) {
				
				if (maze.mazeData[i][j].hasWall(Direction.North)) {
					if (!walls.contains(maze.mazeData[i][j].getNorthWall())) {
						walls.add(maze.mazeData[i][j].getNorthWall());
					}
				}
				if (maze.mazeData[i][j].hasWall(Direction.South)) {
					if (!walls.contains(maze.mazeData[i][j].getSouthWall())) {
						walls.add(maze.mazeData[i][j].getSouthWall());
					}
				}
				if (maze.mazeData[i][j].hasWall(Direction.East)) {
					if (!walls.contains(maze.mazeData[i][j].getEastWall())) {
						walls.add(maze.mazeData[i][j].getEastWall());
					}
				}
				if (maze.mazeData[i][j].hasWall(Direction.West)) {
					if (!walls.contains(maze.mazeData[i][j].getWestWall())) {
						walls.add(maze.mazeData[i][j].getWestWall());
					}
				}
			}
		}
		System.out.println(hash_Set);
		// Removing all the outside walls
		for (int i = walls.size() - 1; i >= 0; i--) {
			if (walls.get(i).isOutsideWall())
				walls.remove(i);
		}



		// Will shuffle the walls
		for (int i = 0; i < walls.size(); i++) {
			int ind = rand.nextInt(walls.size());
			int ind2 = rand.nextInt(walls.size());
			Collections.swap((List<Wall>) walls, ind, ind2);
		}
		

		// Playing with how I am going to delete the walls. The following does not
		// reflect Kruskal's at all.
		// How do i give each cell its own set?
		while(!walls.isEmpty()) {
			Cell tempCell = walls.get(0).getCell1();
			Cell tempCell2 = walls.get(0).getCell2();
			if (!hash_Set.contains(tempCell) && !hash_Set.contains(tempCell2)) { // Cells are in their own sets
				Direction tempD = walls.get(0).getRelativeCellPosition();
				tempCell.removeWall(tempD);
				walls.remove(0);
				hash_Set.add(tempCell);
				hash_Set.add(tempCell2);
			} else if (!hash_Set.contains(tempCell)) { // Cell 1 is in it's own set. Cell2 is in a larger set.
				Direction tempD = walls.get(0).getRelativeCellPosition();
				tempCell.removeWall(tempD);
				walls.remove(0);
				hash_Set.add(tempCell);
			} else if (!hash_Set.contains(tempCell2)) { // Cell 2 is in it's own set. Cell2 is in a larger set.
				Direction tempD = walls.get(0).getRelativeCellPosition();
				tempCell.removeWall(tempD);
				walls.remove(0);
				hash_Set.add(tempCell2);
			} else walls.remove(0);
		}
	}
}