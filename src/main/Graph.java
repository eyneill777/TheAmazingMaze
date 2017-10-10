/**
 * 
 */
package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * A graph is a set of vertices and edges.  An edge is just a pair of vertices in the graph.  
 * @author Steven Lawrence
 */
public class Graph {
	private HashMap<Cell, ArrayList<Cell>> graphDict;
	private HashSet<Cell> intersections;
	private HashSet<Cell> deadEnds;
	private ArrayList<ArrayList<Cell>> allHallways;
	private Maze theMaze;
	private Cell[][] mazeInfo;
	private BufferedImage graphImage;

	
	/**
	 * Constructor
	 * The graph of a maze treats each cell as a vertex in the graph.  A dictionary is used to create
	 * an adjacency list to represent the edges, or connections between the cells.  Cells with degrees
	 * of 1 are end points, cells with degrees of 3 or 4 are intersection points. The set of connected cells
	 * between intersection, or dead end points are hallways of the maze.   
	 * @param maze the maze object that the graph is being constructed from
	 */
	public Graph(Maze maze) {
		graphDict = new HashMap<Cell, ArrayList<Cell>>();    // holds the mapping of cells to their neighbors
		allHallways = new ArrayList<ArrayList<Cell>>();      // holds all the hallways in the maze
		intersections = new HashSet<Cell>();                 // holds all the intersection vertices      
		deadEnds = new HashSet<Cell>();                      // holds all of the dead end vertices
		theMaze = maze;
		mazeInfo = maze.getMazeData();
		
		createAdjacencyList();
		createHallways();
	}
	
	/**
	 * Goes through each cell to creates an adjacency list, mapping the cell to its neighbors. 
	 */
	private void createAdjacencyList() {
		for (int i = 0; i < mazeInfo.length; i++) {
			for (int k = 0; k < mazeInfo[i].length; k++) {
				graphDict.put(mazeInfo[i][k], mazeInfo[i][k].neighbors);
			}
		}
	}
	
	/**
	 * Use the graph adjacency list to make the hallways of the maze
	 */
	private void createHallways() {
		HashSet<Cell> takenRoutes; // holds cells that are adjacent to an intersection cell, so that no hallways are duplicated
		ArrayList<Cell> hallway;
		ArrayList<Cell> theNeighbors;
		ArrayList<Cell> routes;    // neighbors of intersection cells that go into different hallways
		Cell previous, current;
				
		for (Cell k : graphDict.keySet()) {                    // loop through every cell
			theNeighbors = graphDict.get(k);   				   // obtain the neighbors of the cell
			if (theNeighbors == null) {
				continue;
			}
			if (theNeighbors.size() >= 3) {                    // if there are more than 2 neighbors then the cell is an intersection point
				intersections.add(k);                               
			}
		}
		
		takenRoutes = new HashSet<Cell>();	
		// loop through each intersection cell
		for (Cell i : intersections) {
			// get the neighbors of the intersection to use as routes to make the hallways
			routes = graphDict.get(i);
			for (Cell startCell : routes) {
				// if that hallway has not already been counted then make a new hallway
				if (!takenRoutes.contains(startCell)) {
					hallway = new ArrayList<Cell>();
					/* The end points of a hallway are intersection or dead ends vertices,
					 * so add the intersection vertex as the first cell in the hallway and
					 * then add the start cell as the second cell in the hallway. */
					hallway.add(i);  
					hallway.add(startCell);
					
					// loop through all the cells in the hallway until a dead end or intersection is reached
					previous = i;
					current = startCell;
					while (!deadEnds.contains(current)) {
						if (current.neighbors.size() == 2) {
							/*If there is two openings then the vertex is a point within a line.
							 * get the neighbors associated with the openings, and compare the
							 * neighbor cells with the previous cell to ensure that it doesn't go backwards.*/
							if (current.neighbors.get(0) != previous) {
								previous = current;
								current = current.neighbors.get(0);
							} else {
								previous =current;
								current = current.neighbors.get(1);
							}
							hallway.add(current);                // add the current cell to the hallway
							
						} else if (current.isDeadEnd()) {
							deadEnds.add(current);       // if its a dead end the add it to deadEnds and exit the loop
						
						} else {
							/*It is another intersection point. Add the previous adjacent cell to the
							 * takenRoutes set, so that hallway is not duplicated. */ 
							takenRoutes.add(previous);
							break;    // exit the loop because we reached an end point of the hallway
						}
					}
					allHallways.add(hallway);      // add the hallway to the array list of all the hallways
				}
			}
		}
	}
	
	/**
	 * The complexity of a hallway is represented by the total traversal length of the hallway times
	 * the summation of one over the distance between corner vertices in the hallway
	 * @param hallway a hallway of cells 
	 * @return the complexity of the hallway
	 */
	private double hallwayComplexity(ArrayList<Cell> hallway) {
		ArrayList<Cell> h = hallway;
		double d = 0;               // summation of one over the distance between two adjacent corner vertex cells, or a corner and an end point (intersection or dead end)
		double distance;			// the distance between corner cells in units of cells
		double D = h.size() - 1;    // The total traversal length of the hallway is measured by the count of edges in the hallway
		double hComplexity = 0;   
		Cell prev, current; 
		
		// loop through each cell to summate (1 / distance) 
		current = h.get(0);
		for (Cell c: h) {
			if ((c.isCorner() || c.isDeadEnd()) && c != h.get(0)) {  
				prev = current;
				current = c;
				/* The unit of distance is a cell, so just figure out the difference 
				 * between the indexes of the corner cells to determine the distance. */
				distance = h.indexOf(current) - h.indexOf(prev);  
				d += (1 / distance);
			}
		}
		hComplexity = D * d;
		return hComplexity;
	}
	
	/**
	 * calculates the total complexity of the maze by summing up the complexities of all the hallways
	 * @return the complexity of the maze
	 */
	public double mazeComplexity() {
		double mComplexity = 0;
		
		for (ArrayList<Cell> h: allHallways) {
			mComplexity += hallwayComplexity(h);
		}
		return mComplexity;
	}
	
	/**
	 * @return the number of dead ends in the maze (note: currently counts the gates)
	 */
	public int numberOfDeadEnds() {
		return deadEnds.size();
	}
	
	/**
	 * @return the number of intersection vertices in the maze
	 */
	public int numberOfIntersections() {
		return intersections.size();
	}
	
	/**
	 * @return the traversal length of the maze (useless for entire maze, but will need this for solution path eventually)
	 */
	public int traversalLength() {
		int tLength = 0;
		for (ArrayList<Cell> h: allHallways) {
			tLength += (h.size()-1);   // sum up all the edges of the hallways
		}
		return tLength;
	}
	
	/**
	 * Resets the graph by clearing out all of the array lists and maps, and recreating the adjacency list and hallways
	 * @param newMaze the new maze to make a graph of
	 */
	public void resetGraph(Maze newMaze) {
		graphDict.clear();
		allHallways.clear();
		intersections.clear();
		deadEnds.clear();
		theMaze = newMaze;
		mazeInfo = newMaze.getMazeData();
		createAdjacencyList();
		createHallways();
	}
	
	private void drawPoint(Graphics g, Cell theCell, int radius) {
		int r = radius;
		int translateDot = r / 2; // centers the dot on the line with given radius r
		// the x and y coordinates of the center of the cell
		int xPos = (theCell.position.x * theMaze.cellSize) + (theMaze.cellSize / 2);
		int yPos = (theCell.position.y * theMaze.cellSize) + (theMaze.cellSize / 2);
		
		// if the cell is an intersection point then draw the dot green
		if (intersections.contains(theCell)){
			g.setColor(Color.green);
			g.fillOval(xPos - translateDot, yPos - translateDot, r, r);
			
		// draw it red if its a dead end
		}else if (deadEnds.contains(theCell)) { 
			g.setColor(Color.red);
			g.fillOval(xPos - translateDot, yPos - translateDot, r, r);
		}
	}
	
	public void draw(Graphics2D g, Dimension panelSize, BufferedImage mazePic) {
		int center = theMaze.cellSize / 2;  // x and y from the point of a cell are in the top left corner, so have to go half way down and over to get to the center
		g.setColor(Color.gray);
		g.fillRect(0, 0, panelSize.width, panelSize.height);
		graphImage = mazePic;
		Graphics2D g2 = (Graphics2D) graphImage.getGraphics();
		g2.setColor(Color.white);
		g2.fillRect(0,0, graphImage.getWidth(), graphImage.getHeight());  //erase the maze off of the image
		Cell c1;
		Cell c2; 
		int dotRadius = 10 * ((theMaze.cellSize / 100) + 1);  // cell sizes below 100 have dot radius defaulted to 10
		
		// loop through all of the hallways in the maze 
        for (ArrayList<Cell> h: allHallways) {
        	// loop through all of the cells in the hallway and draw a black line from c1 to c2
        	for (int k = 1; k < h.size(); k++) {
        		c1 = h.get(k - 1);
        		c2 = h.get(k);
        		g2.setColor(Color.black);
        		g2.drawLine(c1.position.x*theMaze.cellSize + center, c1.position.y*theMaze.cellSize + center, c2.position.x*theMaze.cellSize + center, c2.position.y*theMaze.cellSize + center);
        		
        		if (k == 1) {
        			drawPoint(g2, c1, dotRadius);
        		}
        		
        		if (intersections.contains(c2) || deadEnds.contains(c2)){
        			drawPoint(g2, c2, dotRadius);
        		}
        	}
        }
        // draw the image in the center of the screen - via Eron
        int drawX = panelSize.width/2-graphImage.getWidth()/2;
		int drawY = panelSize.height/2-graphImage.getHeight()/2;
		g.drawImage(graphImage, drawX, drawY, null);
	}
	

}
