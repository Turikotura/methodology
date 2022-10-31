/*
 * File: Pyramid.java
 * Name: 
 * Section Leader: 
 * ------------------
 * This file is the starter file for the Pyramid problem.
 * It includes definitions of the constants that match the
 * sample run in the assignment, but you should make sure
 * that changing these values causes the generated display
 * to change accordingly.
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class Pyramid extends GraphicsProgram {

/** Width of each brick in pixels */
	private static final int BRICK_WIDTH = 30;

/** Width of each brick in pixels */
	private static final int BRICK_HEIGHT = 12;

/** Number of bricks in the base of the pyramid */
	private static final int BRICKS_IN_BASE = 14;
	
	public void run() {
		buildPyramid();
	}
	
	private void buildPyramid(){
		for(int i = 0; i < BRICKS_IN_BASE; i++){
			int rowLength = BRICKS_IN_BASE-i;
			for(int j = 0; j < rowLength; j++){
				GRect rect = new GRect(BRICK_WIDTH,BRICK_HEIGHT);
				int middleX = getWidth()/2; // x coordinate of mid point
				int offsetX = rowLength*BRICK_WIDTH/2; // offset to left increasing by half brick width for each row going down
				int posX = middleX-offsetX+j*BRICK_WIDTH; // will increase by BRICK_WIDTH on each iteration
				int posY = getHeight()-BRICK_HEIGHT*(i+1); // starting from below. i+1 since y coordinate is top of rect
				add(rect,posX,posY);
			}
		}
	}
}

