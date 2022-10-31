/*
 * File: Hailstone.java
 * Name: 
 * Section Leader: 
 * --------------------
 * This file is the starter file for the Hailstone problem.
 */

import acm.program.*;

public class Hailstone extends ConsoleProgram {
	public void run() {
		int n = readInt("Enter a number: ");
		// check that number is natural
		while(n<=0){
			n = readInt("Number must be positive integer");
		}
		runLoop(n);
	}
	
	private void runLoop(int n){
		int steps = 0;
		while(n!=1){
			// update n with return value from function
			n = printAndReturn(n);
			// count steps
			steps++;
		}
		println("The process took " + steps + " steps to reach 1");
	}
	
	private int printAndReturn(int n){
		// using print so it doesn't go on new line
		print(n + " is ");
		int res;
		if(n%2==0){
			res = n/2;
			print("even, so I take half: "+ res);
		}else{
			res = 3*n+1;
			print("odd, so I make 3n+1: "+ res);
		}
		// println() for newline at end
		println();
		// return res to update n in runLoop()
		return res;
	}
}

