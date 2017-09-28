/**
 * 
 */
package ComplexityRoughDraft;

import java.awt.Graphics2D;

/**
 * @author Steven Lawrence
 *
 */
public class SampleMaze {
	int width,height;  // made useless here because I am defaulting the sample maze to 5x5
	Grid grid;
	Cell[][] gridSheet;
	Graph graph;
	private double mazeComplexity;
	private int travelLength, deadEndCount, iCount;
	
	/**
	 * Constructor
	 * This would be where the height and width of the maze are inputed, but I am defaulting the sample
	 * maze's height and width to 5.
	 */
	public SampleMaze() {
		width = 5;
		height = 5;
		grid = new Grid(width,height);
		gridSheet = grid.getGrid();
		
		constructMaze();  // construct the maze
		
		// construct the graph of the maze and get the complexity, traversal length, 
		// intersections, and dead ends of the maze.
		graph = new Graph(grid);
		mazeComplexity = graph.mazeComplexity();
		travelLength = graph.traversalLength();
		deadEndCount = graph.numberOfDeadEnds();
		iCount = graph.numberOfIntersections();
	}
	
	/**
	 * This is where the maze generating algorithms go. For sample maze a pre-determined maze is made.
	 */
	private void constructMaze() {
		
		// manual code to construct a sample maze going cell by cell
		gridSheet[0][0].breakSouthWall();
		gridSheet[1][0].breakEastWall();
		gridSheet[1][1].breakSouthWall();
		gridSheet[2][1].breakWestWall();
		gridSheet[2][0].breakSouthWall();
		gridSheet[2][1].breakEastWall();
		gridSheet[2][2].breakSouthWall();
		gridSheet[3][2].breakSouthWall();
		gridSheet[4][2].breakWestWall();
		gridSheet[4][1].breakNorthWall();
		gridSheet[4][1].breakWestWall();
		gridSheet[2][2].breakEastWall();
		gridSheet[2][3].breakNorthWall();
		gridSheet[1][3].breakWestWall();
		gridSheet[1][3].breakNorthWall();
		gridSheet[0][3].breakWestWall();
		gridSheet[0][2].breakWestWall();
		gridSheet[0][3].breakEastWall();
		gridSheet[0][4].breakSouthWall();
		gridSheet[1][4].breakSouthWall();
		gridSheet[2][4].breakSouthWall();
		gridSheet[3][4].breakWestWall();
		gridSheet[3][4].breakSouthWall();
		gridSheet[4][4].breakWestWall();
		
	}
	
	/**
	 * display the maze
	 * @param g2 tool to draw with
	 */
	public void display(Graphics2D g2) {
		grid.display(g2);
	}
	
	/**
	 * display the graph of the maze
	 */
	public void displayGraph() {
		graph.display();
	}
	
	/**
	 * @return the mazeComplexity
	 */
	public double getMazeComplexity() {
		return mazeComplexity;
	}
	
	/**
	 * @return the travelLength
	 */
	public int getTravelLength() {
		return travelLength;
	}
	
	/**
	 * @return the deadEndCount
	 */
	public int getDeadEndCount() {
		return deadEndCount;
	}
	
	/**
	 * @return the iCount
	 */
	public int getiCount() {
		return iCount;
	}
	
	/**
	 * Resets the maze over by reseting the grid, re-making the maze, and then re-making its graph
	 */
	public void resetMaze() {
		grid.resetGrid();
		gridSheet = grid.getGrid();
		constructMaze();
		graph.resetGraph(gridSheet);
		mazeComplexity = graph.mazeComplexity();
		travelLength = graph.traversalLength();
		deadEndCount = graph.numberOfDeadEnds();
		iCount = graph.numberOfIntersections();
	}
}


