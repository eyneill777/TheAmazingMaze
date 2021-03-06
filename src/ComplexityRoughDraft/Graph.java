/**
 * 
 */
package ComplexityRoughDraft;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import javax.swing.JComponent;
import javax.swing.JFrame;


/**
 * A graph is a set of vertices and edges.  An edge is just a pair of vertices in the graph. 
 * @author Steven Lawrence
 */
public class Graph {
	// a graph representation of a maze
	private HashMap<Cell, ArrayList<Cell>> graphDict;
	private HashSet<Cell> intersections;
	private HashSet<Cell> deadEnds;
	private ArrayList<ArrayList<Cell>> allHallways;
	private Cell[][] graph; 
	private static final int FRAME_SIZE = 2000;
	
	

	/**
	 * Constructor
	 * The graph of a maze treats each cell as a vertex in the graph.  A dictionary is used to create
	 * an adjacency list to represent the edges, or connections between the cells.  Cells with degrees
	 * of 1 are end points, cells with degrees of 3 or 4 are intersection points. The set of connected cells
	 * between intersection, or dead end points are hallways of the maze.   
	 * @param mazeGrid the Grid object of the maze
	 */
	public Graph(Grid mazeGrid) { 
		graphDict = new HashMap<Cell, ArrayList<Cell>>();    // holds the mapping of cells to their neighbors
		graph = mazeGrid.getGrid();                          
		allHallways = new ArrayList<ArrayList<Cell>>();      // holds all the hallways in the maze
		intersections = new HashSet<Cell>();                 // holds all the intersection vertices      
		deadEnds = new HashSet<Cell>();                      // holds all of the dead end vertices
		
		createAdjacencyList();  // create the adjacency list of the graph
	}

	/**
	 * Finds the neighbors of the cells
	 * @param c the cell of interest we want to find neighbors of
	 * @return an array list containing all of the cell's neighboring cells
	 */
	public ArrayList<Cell> findNeighbors(Cell c) {
		ArrayList<Cell> neighbors = new ArrayList<Cell>();
		
		// loop through all of the cell's neighbors looking for openings
		for (Wall neighbor : c.getWALLS()) {                  
			if (neighbor.isBustedWall()) {
				neighbors.add(c.getNeighbor(graph, neighbor)); 
			}
		}
		return neighbors;
	}
	
	/**
	 * Goes through each cell to creates an adjacency list. If a cell's neighbor has an opening in between
	 *  them then that cell is added.
	 */
	private void createAdjacencyList() {
		// for each cell in the grid map itself to its neighbors
		for (int i = 0; i < graph.length; i++) {
			for (int j = 0; j < graph[i].length; j++) {
				graph[i][j].detectWallBreaks(graph);
				graphDict.put(graph[i][j], findNeighbors(graph[i][j]));
			}
		}
	}

	/**
	 * Use the graph adjacency list to make the hallways of the maze
	 */
	private void createHallways() {
		HashSet<Cell> takenRoutes; // holds cells that are adjacent to an intersection cell, so that no hallways are duplicated
		ArrayList<Cell> hallway;
		ArrayList<Cell> routes;    // neighbors of intersection cells that go into different hallways
		Cell previous, current;
				
		for (Cell k : graphDict.keySet()) {                    // loop through every cell
			ArrayList<Cell> theNeighbors = graphDict.get(k);   // obtain the neighbors of the cell
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
						if (current.getOpenings().length == 2) {
							/*If there is two openings then the vertex is a point within a line.
							 * get the neighbors associated with the openings, and compare the
							 * neighbor cells with the previous cell to ensure that it doesn't go backwards.*/
							if (current.getNeighbor(graph, current.getOpenings()[0]) != previous) {
								previous = current;
								current = current.getNeighbor(graph, current.getOpenings()[0]);
							} else {
								previous = current;
								current = current.getNeighbor(graph, current.getOpenings()[1]);
							}
							hallway.add(current);        // add the current cell to the hallway
							
						} else if (current.isDeadEnd()) {
							deadEnds.add(current);       // if its a dead end the add it to deadEnds and exit the loop
							
						} else { 
							/*It is another intersection point. Add the previous adjacent cell to the
							 * takenRoutes set, so that hallway is not duplicated. */ 
							takenRoutes.add(previous);
							break;    // exit the loop because we reached an end point of the hallway
						}
					}
					allHallways.add(hallway);   // add the hallway to the array list of all the hallways
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
		double d = 0;             // summation of one over the distance between two adjacent corner vertex cells, or a corner and an end point (intersection or dead end)
		double distance;			  // the distance between corner cells in units of cells
		double D = h.size() - 1;  // The total traversal length of the hallway is measured by the count of edges in the hallway
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
		
		createHallways();    // make the hallways
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
	 * Resets the graph by clearing out all of the array lists and maps, and recreating the adjacency list
	 * @param newGrid the new 2D array grid of cells to be used to make the graph
	 */
	public void resetGraph(Cell[][] newGrid) {
		graphDict.clear();
		allHallways.clear();
		intersections.clear();
		deadEnds.clear();
		graph = newGrid;
		createAdjacencyList();
	}
	
	/**
	 * Draw the graph of the maze in its own frame
	 */
	public void display() {
		JFrame f = new JFrame();
		f.setTitle("Graph of the Maze");
		f.setSize(FRAME_SIZE,FRAME_SIZE);
		graphCanvas graphDrawing = new graphCanvas();
		f.add(graphDrawing);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setVisible(true);
	}
	
	/**
	 * a class to draw/paint the graph of the maze
	 */
	public class graphCanvas extends JComponent {
		private int iTotal, DETotal;
		private static final long serialVersionUID = 1L;
		
		// these are constants for the this sample maze, but they can be made into variables that 
		// would use the height and width of the maze to determine their values
		private static final int DOT_RADIUS = 30;              // radius of the drawn points
		private static final int LEGEND_X = 1400;              // x position of the legend
		private static final int LEGEND_Y = 900;               // y position of the legend
		private static final int NEXT_LINE = LEGEND_Y + 30;    // next line of the legend
		private static final int FONT_SIZE = 30;
		
		/**
		 * Constructor
		 * Makes the painting the graph of the maze is painted on
		 */
		public graphCanvas() {
			iTotal = intersections.size();
			DETotal = deadEnds.size();
		}
		
		/**
		 * draws a vertex point representing an intersection or dead end on the graph
		 * @param g the tool to draw with
		 * @param theCell the vertex cell on the graph
		 * @param radious the radius of the dot
		 */
		private void drawPoint(Graphics g, Cell theCell, int radius) {
			int r = radius;
			int translateDot = r / 2; // centers the dot on the line with given radius r
			
			// if the cell is an intersection point then draw the dot green
			if (intersections.contains(theCell)){
    			g.setColor(Color.green);
    			g.fillOval(theCell.getX() - translateDot, theCell.getY() - translateDot, r, r);
    			
    		// draw it red if its a dead end
    		}else if (deadEnds.contains(theCell)) { 
    			g.setColor(Color.red);
    			g.fillOval(theCell.getX() - translateDot, theCell.getY() - translateDot, r, r);
    		}
		}
		
		/**
		 * override JComponent paint method to paint the graph
		 *  @param g tool to draw with
		 */
		public void paint(Graphics g) {
			Cell c1;
			Cell c2; 
			
			// loop through all of the hallways in the maze 
            for (ArrayList<Cell> h: allHallways) {
            	// loop through all of the cells in the hallway and draw a black line from c1 to c2
            	for (int k = 1; k < h.size(); k++) {
            		c1 = h.get(k - 1);
            		c2 = h.get(k);
            		g.setColor(Color.black);
            		g.drawLine(c1.getX(), c1.getY(), c2.getX(), c2.getY());
            		
            		if (k == 1) {
            			drawPoint(g, c1, DOT_RADIUS);
            		}
            		
            		if (intersections.contains(c2) || deadEnds.contains(c2)){
            			drawPoint(g, c2, DOT_RADIUS);
            		}
            	}
            }		
            // legend on the side
            g.setColor(Color.black);
            g.setFont(new Font("TimesRoman", Font.PLAIN, FONT_SIZE)); 
            g.drawString("Intersection (green) - " + iTotal, LEGEND_X, LEGEND_Y);
            g.drawString("dead ends (red) - " + DETotal, LEGEND_X, NEXT_LINE);
            
		}
		
	}	
}
