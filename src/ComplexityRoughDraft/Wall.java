/**
 * 
 */
package ComplexityRoughDraft;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * A wall object that is described by its two end points.  The wall itself may be broken or not.
 * 
 * @author Steven Lawrence
 *
 */
public class Wall {
	// a wall object that has a beginning and an end point.
private int x1;
private int y1;
private int x2;
private int y2;
private boolean bustedWall;
private static final int TRANSLATION = 500;   // shifts the coordinates over by 500 units


/**
 * Constructor 
 * The points are translated over and down by 500 since java.swing sets the origin 
 * in the top left corner. Walls are defaulted to not broken. 
 * @param x1 the x coordinate of the starting point
 * @param y1 the y coordinate of the starting point
 * @param x2 the x coordinate of the ending point
 * @param y2 the y coordinate of the ending point
 */
public Wall(int x1, int y1,int x2,int y2) {
	this.x1 = x1 + TRANSLATION;
	this.x2 = x2 + TRANSLATION;
	this.y1 = y1 + TRANSLATION;
	this.y2 = y2 + TRANSLATION;
	bustedWall = false;        
}

/**
 * Draws the wall black if the wall is not busted. Otherwise the wall  is drawn white 
 * to represent a wall break.
 * @param g the tool to draw the wall with
 */
public void draw(Graphics2D g2) {
	if (!bustedWall) {
		g2.setColor(new Color(0, 0, 0)); 
		System.out.println("black");  // for testing purposes
	}else {
		g2.setColor(new Color(255,255,255)); 
		System.out.println("white");  // for testing purposes
	}
	g2.drawLine(x1, y1, x2, y2); 
	// print the untranslated coordinates for testing purposes
	System.out.println(String.format("%d,%d,%d,%d",x1 - TRANSLATION ,y1 - TRANSLATION,x2 - TRANSLATION,y2 - TRANSLATION));
}

/**
 * sets the wall as broken
 */
public void breakWall() {
	bustedWall = true;
}

/**
 * @return if the wall is broken or not
 */
public boolean isBustedWall() {
	return bustedWall;
}

/**
 * @return the translation
 */
public static int getTranslation() {
	return TRANSLATION;
}

/**
 * resets the status of the wall to not being broken
 */
public void resetWall() {
	bustedWall = false;
}

}
