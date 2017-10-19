/**
 * 
 */
package main;

import java.util.HashSet;

/**
 * @author Steven Lawrence
 *
 *
  The AStarCell class, along with the AStarMazeSolver class,
  implements a generic A* search algorithm.
 */
public class AstarCell extends Cell {
	int G, H, F;
	AstarCell parent;
	Cell c;
	
	/**
	 * An A* cell has node characteristics including a parent node.  In A* every cell has G which is 
	 * the cost to move from the starting cell to a given cell. H which is an estimation of the cost 
	 * to move from a given cell to the ending cell. To estimate assume no walls are present at all.
	 * F is the sum of G and H and what is minimized in A* search.
	 * @param c the cell of the maze
	 */
	public AstarCell(Cell c) {
		super(c.maze, c.position);
		G = 0; H = 0; F= 0;
		parent = null;
		this.c = c;
		}
	
	/**
	 * Find the possible candidate cells or cells that are possible to move to from this cell
	 * @return
	 */
	public HashSet<AstarCell> findCanidates() {
		HashSet<AstarCell> candidate = new HashSet<AstarCell>();
		for (Cell neighbor : c.neighbors) {
			candidate.add(new AstarCell(neighbor));
		}
		return candidate;
	}
	
	public String toString(){
		return this.c +"-G,H,F = " + G + ","+ H + "," + F;
	}
	
}
