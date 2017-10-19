package main;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;

public class AstarMazeSolver {
	HashSet<Point> closedList;
	HashSet<Point> openList;
	Queue<AstarCell> priorityQueue;
	AstarCell startCell;
	AstarCell endCell;
	
    /**
     * A* calculates the distance between current node and the goal node, whil also considering the current path distance.
     * The path changes by choosing from the allowable moves to a cell with the smalles F value.  The path changes by setting
     * the nodes of the A* cells.
     * @param maze the maze that is being solved
     */
	public AstarMazeSolver(Maze maze) {
		startCell = new AstarCell(maze.mazeData[0][0]);
		endCell = new AstarCell(maze.mazeData[maze.mazeData[0].length - 1][maze.mazeData.length - 1]);
		calculateCost(startCell);
		
		// create the lists to keep track of which nodes can and can't be visitied
		closedList = new HashSet<Point>();
		openList = new HashSet<Point>();
		
		// intialize the queue used to make the path and add the starting cell to it and the open list
		priorityQueue = initQueue();
		priorityQueue.add(startCell);
		openList.add(startCell.position);
		
	}
	
	/**
	 * Calculates the heuristic distance to reach the end goal and the F value of the A* cell
	 * @param a the A* cell
	 */
	private void calculateCost(AstarCell a) {
		// traditional to move up,down,left, or right one cell the cost is considered to be 10
		a.H = 10 * ((endCell.position.x - a.position.x) + (endCell.position.y - a.position.y));
		a.F = a.G + a.H;
	}
	
	/**
	 * constructs the path from the A* search using the parent nodes of the A& cells
	 * @param node the end of the a* path
	 * @return an array list of Cell objects ordered from the start cell to the end cell
	 */
	private ArrayList<Cell> constructPath(AstarCell node){
		ArrayList<Cell> path = new ArrayList<Cell>();
		while (node.parent != null) {
			path.add(0, node.c);
			node = node.parent;
		}
		path.add(0, startCell.c);
		return path;
	}
	
	/**
	 * priority queue used to to get the minimum F values
	 * @return a priority queue to be used in the A* search
	 */
	private PriorityQueue<AstarCell> initQueue() {
		return new PriorityQueue<AstarCell>(10, new Comparator<AstarCell>() {
			public int compare(AstarCell c1, AstarCell c2) {
				if (c1.F < c2.F) {
					return -1;
				}
				if (c1.F > c2.F) {
					return 1;
				}
				return 0;
			};
		});
	}
	
	/**
	 * Searches for the solution path in the maze
	 * @return An array list fo Cells representing the solution path, if no solution exists then null is returned
	 */
	public ArrayList<Cell> search() {
		AstarCell current = null;
		
		// while there are still possible moves left
		while(!priorityQueue.isEmpty()) {
			current = priorityQueue.remove();
			openList.remove(current.position);
			
			if (!closedList.contains(current.position)) {
				// add to the closed list so we don't process visited cells twice
				closedList.add(current.position);
				
				if (current.position.equals(endCell.position)) {
					return constructPath(current);
				}
				
				for (AstarCell candidate : current.findCanidates()) {
					if (!closedList.contains(candidate.position)) {
						// calculate G based on parent cell (10 is the arbitrary cost to move from one cell)
						if(openList.contains(candidate.position)) {
							/*if the candidate cell already exists in the open list
							 * check to see if current path is better than the previously found path*/
							if (candidate.G > current.G + 10) {
								candidate.G = current.G + 10;     
								calculateCost(candidate);
								candidate.parent = current;
							}
						} else {
							// then its an unvisited cell 
							candidate.G = current.G + 10;        
							calculateCost(candidate);
							candidate.parent = current;
							// add to the queue and open list
							priorityQueue.add(candidate);
							openList.add(candidate.position);
						}
					}	
				}
			}
			/*System.out.println("New Current = " + current);
			System.out.println(priorityQueue);*/
		} return null; // path not found
	}
}
