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
import java.util.LinkedList;
import java.util.Stack;

/**
 * A graph is a set of vertices and edges. An edge is just a pair of vertices in
 * the graph.
 * 
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
	private ArrayList<Cell> solutionTrail;
	private ArrayList<ArrayList<Cell>> solHallways;
	private ArrayList<ArrayList<ArrayList<Cell>>> branchPaths;
	private Cell gateStart;
	private Cell gateEnd;
	double hComplexityMax;
	double tComplexityMax;

	/**
	 * Constructor The graph of a maze treats each cell as a vertex in the graph. A
	 * dictionary is used to create an adjacency list to represent the edges, or
	 * connections between the cells. Cells with degrees of 1 are end points, cells
	 * with degrees of 3 or 4 are intersection points. The set of connected cells
	 * between intersection, or dead end points are hallways of the maze.
	 * 
	 * @param maze
	 *            the maze object that the graph is being constructed from
	 */
	public Graph(Maze maze) {
		graphDict = new HashMap<Cell, ArrayList<Cell>>(); // holds the mapping of cells to their neighbors
		allHallways = new ArrayList<ArrayList<Cell>>(); // holds all the hallways in the maze
		intersections = new HashSet<Cell>(); // holds all the intersection vertices
		deadEnds = new HashSet<Cell>(); // holds all of the dead end vertices
		theMaze = maze;
		mazeInfo = maze.getMazeData();
		solutionTrail = null; // no solution trail given
		hComplexityMax = 1;
		tComplexityMax = 1;
		createAdjacencyList();
		createHallways();
	}

	/**
	 * A maze can have a solution
	 * 
	 * @param maze
	 *            - the maze to make the graph of
	 * @param solution
	 *            - the solution path of the maze
	 */
	public Graph(Maze maze, ArrayList<Cell> solution) {
		graphDict = new HashMap<Cell, ArrayList<Cell>>(); // holds the mapping of cells to their neighbors
		allHallways = new ArrayList<ArrayList<Cell>>(); // holds all the hallways in the maze
		intersections = new HashSet<Cell>(); // holds all the intersection vertices
		deadEnds = new HashSet<Cell>(); // holds all of the dead end vertices
		theMaze = maze;
		mazeInfo = maze.getMazeData();
		solutionTrail = solution;
		solHallways = new ArrayList<ArrayList<Cell>>(); // holds all of the hallways in the solution path
		branchPaths = new ArrayList<ArrayList<ArrayList<Cell>>>(); // holds all of the different paths or trails (paths
																	// are array list containing adjacent hallways)
		hComplexityMax = 1;
		tComplexityMax = 1;
		createAdjacencyList();
		createHallways();
	}

	/**
	 * Goes through each cell to creates an adjacency list, mapping the cell to its
	 * neighbors.
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
		HashSet<Cell> takenRoutes; // holds cells that are adjacent to an intersection cell, so that no hallways
									// are duplicated

		ArrayList<Cell> hallway;
		ArrayList<Cell> theNeighbors;
		ArrayList<Cell> routes; // neighbors of intersection cells that go into different hallways
		Cell previous, current;

		for (Cell k : graphDict.keySet()) { // loop through every cell
			theNeighbors = graphDict.get(k); // obtain the neighbors of the cell
			if (theNeighbors == null) {
				continue;
			}
			if (theNeighbors.size() >= 3) { // if there are more than 2 neighbors then the cell is an intersection point
				intersections.add(k);
			}
		}
		// the begining to the maze is the top left corner and the end is the bottom
		// right corner
		gateStart = mazeInfo[0][0];
		gateEnd = mazeInfo[mazeInfo.length - 1][mazeInfo[0].length - 1];
		intersections.add(gateStart);
		intersections.add(gateEnd);

		takenRoutes = new HashSet<Cell>();
		// loop through each intersection cell
		for (Cell i : intersections) {
			// get the neighbors of the intersection to use as routes to make the hallways
			routes = graphDict.get(i);
			for (Cell startCell : routes) {
				// if that hallway has not already been counted then make a new hallway
				if (!takenRoutes.contains(startCell)) {
					hallway = new ArrayList<Cell>();
					/*
					 * The end points of a hallway are intersection or dead ends vertices, so add
					 * the intersection vertex as the first cell in the hallway and then add the
					 * start cell as the second cell in the hallway.
					 */
					hallway.add(i);
					hallway.add(startCell);

					// loop through all the cells in the hallway until a dead end or intersection is
					// reached
					previous = i;
					current = startCell;
					while (!deadEnds.contains(current)) {

						if (current.neighbors.size() == 2) {
							// make sure that it is not a gate cell
							if (current == gateStart || current == gateEnd) {
								takenRoutes.add(previous);
								break;
							}
							/*
							 * If there is two openings then the vertex is a point within a line. get the
							 * neighbors associated with the openings, and compare the neighbor cells with
							 * the previous cell to ensure that it doesn't go backwards.
							 */
							if (current.neighbors.get(0) != previous) {
								previous = current;
								current = current.neighbors.get(0);
							} else {
								previous = current;
								current = current.neighbors.get(1);
							}
							hallway.add(current); // add the current cell to the hallway

						} else if (current.isDeadEnd()) {
							deadEnds.add(current); // if its a dead end the add it to deadEnds and exit the loop
							// make sure it is not a gate cell
							if (current == gateStart || current == gateEnd) {
								takenRoutes.add(previous);
							}

						} else {
							/*
							 * It is another intersection point. Add the previous adjacent cell to the
							 * takenRoutes set, so that hallway is not duplicated.
							 */
							takenRoutes.add(previous);
							break; // exit the loop because we reached an end point of the hallway
						}
					}
					allHallways.add(hallway); // add the hallway to the array list of all the hallways
				}
			}
		}
		// If there is a solution use it to get the solution hallways and branch
		// hallways
		if (solutionTrail != null) {
			LinkedList<ArrayList<Cell>> branchHallways = new LinkedList<ArrayList<Cell>>();
			for (ArrayList<Cell> h : allHallways) {
				// determine if the hallway is in the solution trail or not
				boolean inSolution = true;
				for (Cell c : h) {
					if (!solutionTrail.contains(c)) {
						inSolution = false;
					}
				}
				if (inSolution) {
					solHallways.add(h);
				} else {
					branchHallways.add(h);
				}
			}
			// from the branch hallways create the branch paths (disconnected hallway
			// components of the graph from subtracting the solution path)
			while (!branchHallways.isEmpty()) {
				ArrayList<ArrayList<Cell>> branchPath = new ArrayList<ArrayList<Cell>>();
				ArrayList<Cell> branchRoute = branchHallways.pop();
				// pop a hallway and added it the current path
				Stack<ArrayList<Cell>> stk = new Stack<ArrayList<Cell>>();
				branchPath.add(branchRoute);
				// determine if it is adjacent hallways to the current hallway
				ArrayList<ArrayList<Cell>> adjacentBranches = getAdjacentHallways(branchHallways, branchRoute);
				if (adjacentBranches == null) {
					// if there is not then the path is just one hallway long
					branchPaths.add(branchPath); // add it too branch paths
					continue;
				} else {
					for (ArrayList<Cell> adjHall : adjacentBranches) {
						// if those adjacent hallways are not apart of the solution path then add them
						// to the stack
						if (!solHallways.contains(adjHall)) {
							stk.push(adjHall);
						}
					}
				} // loop until the stack is empty getting all of the hallways of a component of
					// the graph
				while (!stk.isEmpty()) {
					ArrayList<Cell> adjBranch = stk.pop(); // pop a hallway of the component
					branchPath.add(adjBranch); // add it to the branch path

					// remove that hallway from branch hallways so it is not processed twice
					branchHallways.remove(adjBranch);

					// get the current hallways adjacent hallways
					adjacentBranches = getAdjacentHallways(branchHallways, adjBranch);

					if (!(adjacentBranches == null)) {
						for (ArrayList<Cell> adjHall : adjacentBranches) {
							// again if it has adjacent hallways not in the solution path then add them to
							// the stack
							if (!solHallways.contains(adjHall)) {
								stk.push(adjHall);
							}
						}
					}
				}
				// when all of the branch hallways in the component have been processed add the
				// path to branch paths
				branchPaths.add(branchPath);
			}
		}
	}

	/**
	 * Get the adjacent hallways, adjacent hallways share an end point
	 * 
	 * @param hallwayCollection
	 *            the hallways to search for adjacent hallways in
	 * @param hallway
	 *            the hallway to look for adjacent hallways for
	 * @return an array list of hallways that are adjacent to the given hallway,
	 *         retun null if it has no adjacent hallways
	 */
	private ArrayList<ArrayList<Cell>> getAdjacentHallways(LinkedList<ArrayList<Cell>> hallwayCollection,
			ArrayList<Cell> hallway) {
		ArrayList<ArrayList<Cell>> adjHallways = new ArrayList<ArrayList<Cell>>(); // array list of hallways

		// get the endpoints of the hallway
		Cell front = hallway.get(0);
		Cell back = hallway.get(hallway.size() - 1);

		// Note: I think the below code is useless
		if (!intersections.contains(front) || !intersections.contains(back)) {
			return null;
		}

		// determine if there are adjacent hallways, if a hallway has the same endpoint
		// then it is an adjacent hallway
		for (ArrayList<Cell> hall : hallwayCollection) {
			if (hall.equals(hallway)) {
				continue; // skip itself
			}
			if (hall.contains(front) || hall.contains(back)) {
				adjHallways.add(hall);
			}
		}
		return adjHallways;

	}

	/**
	 * The complexity of a hallway is represented by the total traversal length of
	 * the hallway times the summation of one over the distance between corner
	 * vertices in the hallway
	 * 
	 * @param hallway
	 *            a hallway of cells
	 * @return the complexity of the hallway
	 */
	private double hallwayComplexity(ArrayList<Cell> hallway) {
		ArrayList<Cell> h = hallway;
		double d = 0; // summation of one over the distance between two adjacent corner vertex cells,
						// or a corner and an end point (intersection or dead end)
		double distance; // the distance between corner cells in units of cells
		double D = h.size() - 1; // The total traversal length of the hallway is measured by the count of edges
									// in the hallway
		double hComplexity = 0;
		Cell prev, current;

		// loop through each cell to summate (1 / distance)
		current = h.get(0);
		for (Cell c : h) {
			if ((c.isCorner() || c.isDeadEnd()) && c != h.get(0)) {
				prev = current;
				current = c;
				/*
				 * The unit of distance is a cell, so just figure out the difference between the
				 * indexes of the corner cells to determine the distance.
				 */
				distance = h.indexOf(current) - h.indexOf(prev);
				d += (1 / distance);
			}
		}
		hComplexity = D * d;
		if (hComplexity > hComplexityMax) {
			hComplexityMax = hComplexity;
		}
		return hComplexity;
	}

	/**
	 * calculates the total complexity of the maze by summing up the complexities of
	 * all the hallways
	 * 
	 * @return the complexity of the maze
	 */
	public double mazeComplexity() {
		double mComplexity = 0;

		for (ArrayList<Cell> h : allHallways) {
			mComplexity += hallwayComplexity(h);
		}
		return Math.log(mComplexity); // ln or natural log
	}

	/**
	 * @return the complexity of the solution path or trail, returns -1 if no
	 *         solution exists
	 */
	public double solutionComplexity() {
		if (solutionTrail == null) {
			System.out.println("Solution complexity unknown (no solution path find)");
			return -1.0;
		} else {
			double sComplexity = 0;

			for (ArrayList<Cell> hall : solHallways) { // move into separate function
				sComplexity += hallwayComplexity(hall);
			}
			sComplexity = Math.log(sComplexity);
			if (sComplexity > tComplexityMax) {
				tComplexityMax = sComplexity;
			}
			return sComplexity;
		}
	}

	private double branchComplexity(ArrayList<ArrayList<Cell>> branch) {

		double bComplexity = 0;

		for (ArrayList<Cell> hall : branch) { // move into separate function
			bComplexity += hallwayComplexity(hall);
		}
		bComplexity = Math.log(bComplexity);
		if (bComplexity > tComplexityMax) {
			tComplexityMax = bComplexity;
		}
		return bComplexity;

	}

	/**
	 * The difficulty of the maze is the product of the complexities of all the
	 * branch paths and the complexity of the solution path
	 * 
	 * @return the maze difficulty, returns -1 if no solution exists
	 */
	public double mazeDifficulty() {
		if (solutionTrail == null) {
			System.out.println("Difficulty unknown (no solution path found)");
			return -1.0;
		} else {
			double mDifficulty;
			double bDifficulty = 1, tDifficulty = 1; // dont need tDifficulty
			for (ArrayList<ArrayList<Cell>> path : branchPaths) {
				double pComplexity = 0;
				for (ArrayList<Cell> bHallways : path) {
					pComplexity += hallwayComplexity(bHallways);
				}
				bDifficulty *= (Math.log(pComplexity) + 1);
			}
			mDifficulty = Math.log(solutionComplexity() * bDifficulty);
			return mDifficulty;
		}
	}

	/**
	 * @return the number of dead ends in the maze
	 */
	public int numberOfDeadEnds() {
		int dEnds = deadEnds.size();
		// do not count gates as dead ends
		if (deadEnds.contains(gateStart)) {
			dEnds--;
		}
		if (deadEnds.contains(gateEnd)) {
			dEnds--;
		}
		return dEnds;
	}

	/**
	 * @return the number of intersection vertices in the maze
	 */
	public int numberOfIntersections() {

		int NUMBER_OF_GATES = 2; // don't count gates
		return intersections.size() - NUMBER_OF_GATES;

	}

	/**
	 * @return the traversal length of the solution trail, returns -1 if no solution
	 *         exists
	 */
	public int traversalLength() {

		if (solutionTrail == null) {
			return -1;
		} else {
			int tLength = 0;
			for (ArrayList<Cell> h : solHallways) {
				tLength += (h.size() - 1); // sum up all the edges of the hallways
			}
			return tLength;
		}
	}

	/**
	 * Resets the graph by clearing out all of the array lists and maps, finding a
	 * new solution path,and recreating the adjacency list and hallways
	 * 
	 * @param newMaze
	 *            the new maze to make a graph of
	 */
	public void resetGraph(Maze newMaze) {
		graphDict.clear();
		allHallways.clear();
		intersections.clear();
		deadEnds.clear();
		theMaze = newMaze;
		mazeInfo = newMaze.getMazeData();
		AstarMazeSolver sl = new AstarMazeSolver(newMaze);
		solutionTrail = sl.search();
		if (solutionTrail != null) {
			solHallways.clear();
			branchPaths.clear();
		}
		createAdjacencyList();
		createHallways();
	}
	
	/**
	 * draws a point representation of a cell on the graph
	 * @param g tool to draw with
	 * @param theCell either a gate, intersection, or dead end
	 * @param radius
	 */
	private void drawPoint(Graphics g, Cell theCell, int radius) {
		int r = radius;
		int translateDot = r / 2; // centers the dot on the line with given radius r
		// the x and y coordinates of the center of the cell
		int xPos = (theCell.position.x * theMaze.cellSize) + (theMaze.cellSize / 2);
		int yPos = (theCell.position.y * theMaze.cellSize) + (theMaze.cellSize / 2);

		if (theCell == gateStart || theCell == gateEnd) {
			g.setColor(Color.orange);
			g.fillOval(xPos - translateDot, yPos - translateDot, r, r);
		}

		// if the cell is an intersection point then draw the dot green
		else if (intersections.contains(theCell)) {
			g.setColor(Color.green);
			g.fillOval(xPos - translateDot, yPos - translateDot, r, r);

			// draw it red if its a dead end
		} else if (deadEnds.contains(theCell)) {
			g.setColor(Color.red);
			g.fillOval(xPos - translateDot, yPos - translateDot, r, r);
		}
	}

	/**
	 * Picks the yellow color of the hallway based on its hallway complexity. 
	 * @param hComplexity the hallway complexity
	 * @return super light yellow if complexity < 5% the max hallway complexity,
	 *         light yellow if complexity is 5% to <10% of max hallway complexity,
	 *         yellow if complexity is 10% to <20% of max hallway complexity
	 *         dark yellow if complexity is 20% to <50% of max hallway complexity,
	 *         super dark yellow if complexity > 50% of max hallway complexity
	 */
	private Color pickYellow(double hComplexity) {
		if (hComplexity < 0.05 * hComplexityMax) {
			// System.out.println("Hallway color - super light yellow");
			return new Color(255, 255, 204); // super light yellow
		} else if (hComplexity < 0.1 * hComplexityMax) {
			// System.out.println("Hallway color - light yellow");
			return new Color(255, 255, 102); // light yellow
		} else if (hComplexity < 0.2 * hComplexityMax) {
			// System.out.println("Hallway color - yellow");
			return new Color(255, 255, 0); // light yellow
		} else if (hComplexity < 0.5 * hComplexityMax) {
			// System.out.println("Hallway color - dark yellow");
			return new Color(204, 204, 0);
		} else {
			// System.out.println("Hallway color - super dark yellow");
			return new Color(133, 133, 0); // super dark yellow
		}
	}
	
	/**
	 * Picks the blue color of the hallway based on its hallway complexity. 
	 * @param hComplexity the hallway complexity
	 * @return super light blue if complexity < 5% the max hallway complexity,
	 *         light blue if complexity is 5% to <10% of max hallway complexity,
	 *         blue if complexity is 10% to <20% of max hallway complexity
	 *         dark blue if complexity is 20% to <50% of max hallway complexity,
	 *         super dark blue if complexity > 50% of max hallway complexity
	 */
	private Color pickBlue(double hComplexity) {
		if (hComplexity < 0.05 * hComplexityMax) {
			// System.out.println("Hallway color - super light blue");
			return new Color(135, 206, 250); // light sky blue
		} else if (hComplexity < 0.1 * hComplexityMax) {
			// System.out.println("Hallway color - light blue");
			return new Color(0, 191, 255); // deep sky blue
		} else if (hComplexity < 0.2 * hComplexityMax) {
			// System.out.println("Hallway color - blue");
			return new Color(30, 144, 255); // dodger blue
		} else if (hComplexity < 0.5 * hComplexityMax) {
			// System.out.println("Hallway color - dark blue");
			return new Color(0, 0, 255); // blue
		} else {
			// System.out.println("Hallway color - super dark blue");
			return new Color(0, 0, 139); // dark blue
		}
	}

	public void draw(Graphics2D g, Dimension panelSize, BufferedImage mazePic, boolean tile) {
		int center = theMaze.cellSize / 2; // x and y from the point of a cell are in the top left corner, so have to go
											// half way down and over to get to the center
		g.setColor(Color.gray);
		g.fillRect(0, 0, panelSize.width, panelSize.height);
		graphImage = mazePic;
		Graphics2D g2 = (Graphics2D) graphImage.getGraphics();
		g2.setColor(Color.white);
		g2.fillRect(0, 0, graphImage.getWidth(), graphImage.getHeight()); // erase the maze off of the image
		Cell c1;
		Cell c2;
		int dotRadius = 4 * ((theMaze.cellSize / 10) + 1); // cell sizes below 10 have dot radius defaulted to 10
		System.out.println(dotRadius);

		if (!tile) {
			// loop through all of the hallways in the maze
			for (ArrayList<Cell> h : allHallways) {
				// loop through all of the cells in the hallway and draw a black line from c1 to
				// c2

				for (int k = 1; k < h.size(); k++) {
					c1 = h.get(k - 1);
					c2 = h.get(k);
					if (solutionTrail != null) {
						if (solHallways.contains(h)) {
							g2.setColor(Color.orange); // if there is a solution then draw the solution path orange
						} else {
							g2.setColor(Color.blue); // draw the branches the color blue
						}
					} else {
						g2.setColor(Color.blue); // if no solution then draw everything blue
					}
					g2.drawLine(c1.position.x * theMaze.cellSize + center, c1.position.y * theMaze.cellSize + center,
							c2.position.x * theMaze.cellSize + center, c2.position.y * theMaze.cellSize + center);

					if (k == 1) {
						drawPoint(g2, c1, dotRadius);
					}

					if (intersections.contains(c2) || deadEnds.contains(c2)) {
						drawPoint(g2, c2, dotRadius);
					}
				}
			}
		} else{
			// Draw the tile graph
			// loop through all of the hallways in the maze
			for (ArrayList<Cell> h : allHallways) {

				// pick the color
				double hComplex = hallwayComplexity(h);
				if (solutionTrail != null) {
					if (solHallways.contains(h)) {
						Color yel = pickYellow(hComplex);
						g2.setColor(yel); // if there is a solution then draw the solution path orange
					} else {
						Color bleu = pickBlue(hComplex);
						g2.setColor(bleu); // draw the branches the color blue
					}
				} else {
					Color bleu = pickBlue(hComplex);
					g2.setColor(bleu); // if no solution then draw everything blue
				}
				Color hColor = g2.getColor();
				for (Cell c : h) {
					if (c.equals(gateStart) || c.equals(gateEnd)) {
						g2.setColor(Color.orange);
						g2.fillRect(c.position.x * theMaze.cellSize, c.position.y * theMaze.cellSize, theMaze.cellSize,
								theMaze.cellSize);
						g2.setColor(hColor);
					} /*
						 * draw intersections green else if (intersections.contains(c)) {
						 * g2.setColor(Color.green); g2.fillRect(c.position.x*theMaze.cellSize,
						 * c.position.y*theMaze.cellSize, theMaze.cellSize, theMaze.cellSize);
						 * g2.setColor(hColor); }
						 *//*
							 * draw dead ends red else if (deadEnds.contains(c)) { g2.setColor(Color.red);
							 * g2.fillRect(c.position.x*theMaze.cellSize, c.position.y*theMaze.cellSize,
							 * theMaze.cellSize, theMaze.cellSize); g2.setColor(hColor); }
							 */
					else if (solutionTrail != null && !solHallways.contains(h)) {
						if (intersections.contains(c) && solutionTrail.contains(c)) {
							continue;
						} else {
							g2.fillRect(c.position.x * theMaze.cellSize, c.position.y * theMaze.cellSize,
									theMaze.cellSize, theMaze.cellSize);
						}
					} else {
						g2.fillRect(c.position.x * theMaze.cellSize, c.position.y * theMaze.cellSize, theMaze.cellSize,
								theMaze.cellSize);
					}
				}
			}
		}
		// draw the image in the center of the screen - via Eron
		int drawX = panelSize.width / 2 - graphImage.getWidth() / 2;
		int drawY = panelSize.height / 2 - graphImage.getHeight() / 2;
		g.drawImage(graphImage, drawX, drawY, null);
	}

	public void draw(Graphics2D g, Dimension panelSize, BufferedImage mazePic) {
		this.draw(g, panelSize, mazePic, false);
	}
}
