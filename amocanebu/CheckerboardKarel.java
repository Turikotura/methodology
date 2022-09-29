/*
 * File: CheckerboardKarel.java
 * ----------------------------
 * When you finish writing it, the CheckerboardKarel class should draw
 * a checkerboard using beepers, as described in Assignment 1.  You
 * should make sure that your program works for all of the sample
 * worlds supplied in the starter folder.
 */

import stanford.karel.*;

public class CheckerboardKarel extends SuperKarel {

	// You fill in this part
	public void run(){
		while(frontIsClear()){
			fillRowOffset();
			elevate();
			if(frontIsClear()){
				fillRow();
			}
		}
	}

	public void fillRow(){
		while(frontIsClear()){
			move();
			putBeeper();
			if(frontIsClear()){
				move();
			}
		}
		turnAround();
		while(frontIsClear()){
			move();
		}
	}

	public void fillRowOffset(){
		putBeeper();
		while(frontIsClear()){
			move();
			if(frontIsClear()){
				move();
				putBeeper();
			}
		}
		turnAround();
		while(frontIsClear()){
			move();
		}
	}

	public void elevate(){
		turnRight();
		if(frontIsClear()){
			move();
			turnRight();
		}
	}

	private void turnRight(){
		for(int i = 0; i < 3; i++){
			turnLeft();
		}
	}

	private void turnAround(){
		for(int i = 0; i < 2; i++){
			turnLeft();
		}
	}
}
