/*
 * File: StoneMasonKarel.java
 * --------------------------
 * The StoneMasonKarel subclass as it appears here does nothing.
 * When you finish writing it, it should solve the "repair the quad"
 * problem from Assignment 1.  In addition to editing the program,
 * you should be sure to edit this comment so that it no longer
 * indicates that the program does nothing.
 */

import stanford.karel.*;

public class StoneMasonKarel extends SuperKarel {
	
	public void run(){
		buildColumn();
		while(frontIsClear()){
			moveToNextColumn();
			buildColumn();
		}
	}
	
	//pre: on first row, 4k+1 column, looking east
	//post: same as pre, but column built
	private void buildColumn(){
		turnLeft();
		goUpAndPlaceBeepers();
		goDown();
	}

	//pre: on first row, 4k+1 column, looking north
	//post: below a wall on same column, looking north, column built
	private void goUpAndPlaceBeepers() {
		checkAndPutBeeper();
		while(frontIsClear()){
			move();
			checkAndPutBeeper();
		}
	}

	//same as putBeeper, but checks if beeper is already present
	private void checkAndPutBeeper() {
		if(!beepersPresent()){
			putBeeper();
		}
	}

	//pre: looking north
	//post: on first row, looking east
	private void goDown() {
		turnAround();
		while(frontIsClear()){
			move();
		}
		turnLeft();
	}

	//pre: looking east
	//post: 4 columns ahead, looking east
	private void moveToNextColumn(){
		for(int i = 0; i < 4; i++){
			move();
		}
	}
}
