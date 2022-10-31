/*
 * File: MidpointFindingKarel.java
 * -------------------------------
 * When you finish writing it, the MidpointFindingKarel class should
 * leave a beeper on the corner closest to the center of 1st Street
 * (or either of the two central corners if 1st Street has an even
 * number of corners).  Karel can put down additional beepers as it
 * looks for the midpoint, but must pick them up again before it
 * stops.  The world may be of any size, but you are allowed to
 * assume that it is at least as tall as it is wide.
 */

import stanford.karel.*;

public class MidpointFindingKarel extends SuperKarel {
	//algorithm is go up twice and go right once
	//this only works because the world is a square as said in problem
	public void run(){
		turnLeft();
		while(frontIsClear()){
			goUpTwice();
			goRight();
		}
		goDown();
		putBeeper();
	}
	
	//pre: looking north
	//post: looking south, on the first row
	private void goDown() {
		turnAround();
		while(frontIsClear()){
			move();
		}
	}
	
	//pre: looking north
	//post: looking east, up two rows
	//		may also go up one or zero rows if it's end of the world
	private void goUpTwice(){
		checkWallMove();
		checkWallMove();
		turnRight();
	}
	
	//pre: looking east
	//post: looking north, right by one column
	private void goRight(){
		checkWallMove();
		turnLeft();
	}
	
	//checks if there is wall ahead and moves only if there is not
	private void checkWallMove(){
		if(frontIsClear()){
			move();
		}
	}
}
