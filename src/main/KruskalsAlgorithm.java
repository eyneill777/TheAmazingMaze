package main;

/**
 * @author tln86
 */
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

public class KruskalsAlgorithm extends MazeGenerator {
	private ArrayList<Wall> walls = new ArrayList<Wall>();
	private Set<Cell> visited = new HashSet<Cell>();
	private ArrayList<Cell> cells = new ArrayList<>();

	public KruskalsAlgorithm(Maze maze) {
		super(maze);
	}

	public void generateMaze() {
		Random rand = new Random();

		// adding all the walls.
		int counter = 0;  // If N = the number of cells and we are looping through all cells this computation takes 0(N) 
		for (int i = 0; i < maze.mazeData.length; i++) {
			for (int j = 0; j < maze.mazeData[i].length; j++) {
				maze.mazeData[i][j].setLabel(counter);
				if (maze.mazeData[i][j].hasWall(Direction.North)) {
					if (!walls.contains(maze.mazeData[i][j].getNorthWall())) {
						walls.add(maze.mazeData[i][j].getNorthWall()); // Adding each wall to the arraylist takes: 
					}
				}
				if (maze.mazeData[i][j].hasWall(Direction.East)) {
					if (!walls.contains(maze.mazeData[i][j].getEastWall())) {
						walls.add(maze.mazeData[i][j].getEastWall()); // Adding each wall to the arraylist takes: 
					}
				}
				cells.add(maze.mazeData[i][j]);
				counter++;
			}

		}

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

		// 1. Pick a wall from the list of walls.
		// 2. If both cells have not been visited, mark them as visited and join their sets. Knock the wall down.
		// 3. If one has been visited and the other has not, mark it visited and join it with the larger set. Knock the wall down.
		// 4. If both have been visited and are in different set, knock the wall down and join their sets.
		// 5. If both are visited and in the same sets, keep the wall and toss it out of the list.
		// 6. once all walls have been used, stop.

		while (!walls.isEmpty()) {
			Cell tempCell = walls.get(0).getCell1();
			Cell tempCell2 = walls.get(0).getCell2();
			Direction tempD = walls.get(0).getRelativeCellPosition();
			if (tempCell.getLabel() == tempCell2.getLabel()) {

				walls.remove(0);
			} else if (!visited.contains(tempCell) && !visited.contains(tempCell2)) { // Cells are in their own sets and
				// haven't been visited
				tempCell.removeWall(tempD);
				walls.remove(0);
				visited.add(tempCell);
				visited.add(tempCell2);
				tempCell.setLabel(tempCell2.getLabel()); // Give cell 2 the same label as cell 1. Put them in the same
															// set.
			} else if (!visited.contains(tempCell)) { // Cell 1 is in it's own set. Cell2 is in a larger set.
				visited.add(tempCell);
				tempCell.removeWall(tempD);
				walls.remove(0);
				tempCell.setLabel(tempCell2.getLabel());
			} else if (!visited.contains(tempCell2)) { // Cell 2 is in it's own set. Cell2 is in a larger set.
				visited.add(tempCell2);
				tempCell.removeWall(tempD);
				walls.remove(0);
				tempCell2.setLabel(tempCell.getLabel());
			} else if (visited.contains(tempCell) && visited.contains(tempCell2)) { // cell 2 and cell 1 have been
				walls.remove(0);
				if (tempCell.getLabel() != tempCell2.getLabel()) { // visited but are in different sets.
					tempCell.removeWall(tempD);
				}
				int label = tempCell.getLabel();

				for (Cell c : visited) {
					if (c.getLabel() == label && !c.equals(tempCell)) {
						c.setLabel(tempCell2.getLabel());
					}
				}
				tempCell.setLabel(tempCell2.getLabel());

			}
		}
	}

}