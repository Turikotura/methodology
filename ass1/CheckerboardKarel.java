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
	
	public void run(){
		//special case when world is 1 column wide
		if(!frontIsClear()){
			turnLeft();
			fillRow();
		}
		//while loop checks if karel has to fill another row
		//since elevate ends in a situation where front is clear if there are more rows to fill 
		while(frontIsClear()){
			//we first fill the row normally
			fillRow();
			elevate();
			//check in case world's height is odd
			if(frontIsClear()){
				//if it's even fill the even row with offset
				fillRowOffset();
				elevate();
			}
		}
	}

	//pre: looking east
	//post: same position as start point, looking west
	public void fillRow(){
		putBeeper();
		while(frontIsClear()){
			move();
			//check in case world's width is even
			if(frontIsClear()){
				move();
				putBeeper();
			}
		}
		getBack();
	}
	
	//pre: looking east
	//post: same position as start point, looking west
	public void fillRowOffset(){
		while(frontIsClear()){
			move();
			putBeeper();
			//check in case world's width is even
			if(frontIsClear()){
				move();
			}
		}
		getBack();
	}
	
	//pre: looking east
	//post: first column of world, looking west
	private void getBack(){
		turnAround();
		while(frontIsClear()){
			move();
		}
	}

	//pre: looking west
	//post: if there are more rows to fill - next row, looking east
	//if not - same row, looking north, so it satisfies the while loop in run
	public void elevate(){
		turnRight();
		if(frontIsClear()){
			move();
			turnRight();
		}
	}
}
