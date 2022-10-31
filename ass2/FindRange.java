/*
 * File: FindRange.java
 * Name: 
 * Section Leader: 
 * --------------------
 * This file is the starter file for the FindRange problem.
 */

import acm.program.*;

public class FindRange extends ConsoleProgram {
	
	private static final int END_CONDITION = 0;
	
	public void run() {
		println("This program finds the largest and smallest numbers.");
		runLoop();
	}
	
	private void runLoop(){
		// max and min values of ints to not allow for bigger/smaller number
		int minSoFar = Integer.MAX_VALUE;
		int maxSoFar = Integer.MIN_VALUE;
		int n = readInt("? ");
		
		// check if first number is END_CONDITION and prompts to input again if so
		while(n == END_CONDITION){
			println("You must input at least 1 number");
			n = readInt("? ");
		}
		
		// updates minSoFar and maxSoFar if n is smaller/bigger than last kept minimal/maximal value
		while(n!=END_CONDITION){
			minSoFar = min(minSoFar, n);
			maxSoFar = max(maxSoFar, n);
			n = readInt("? ");
		}
		println("smallest: " + minSoFar);
		println("largest: " + maxSoFar);
	}
	
	// returns smaller of two integers
	private int min(int a, int b){
		if(a < b){
			return a;
		}
		return b;
	}
	
	// returns bigger of two integers
	private int max(int a, int b){
		if(a > b){
			return a;
		}
		return b;
	}
}

