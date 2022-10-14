/*
 * File: CollectNewspaperKarel.java
 * --------------------------------
 * At present, the CollectNewspaperKarel subclass does nothing.
 * Your job in the assignment is to add the necessary code to
 * instruct Karel to walk to the door of its house, pick up the
 * newspaper (represented by a beeper, of course), and then return
 * to its initial position in the upper left corner of the house.
 */

import stanford.karel.*;

public class CollectNewspaperKarel extends SuperKarel {

	public void run() {
		goToNewspaper();
		pickBeeper();
		goBack();
	}

	//pre: top-left corner of house, looking east
	//post: at newspaper, looking east
	private void goToNewspaper(){
		turnRight();
		move();
		turnLeft();
		for(int i = 0; i < 3; i++){
			move();
		}
	}
	
	//pre: at newspaper, looking east
	//post: top-left corner of house, looking east
	private void goBack(){
		turnAround();
		for(int i = 0; i < 3; i++){
			move();
		}
		turnRight();
		move();
		turnRight();
	}
}
