/*
 * File: PythagoreanTheorem.java
 * Name: 
 * Section Leader: 
 * -----------------------------
 * This file is the starter file for the PythagoreanTheorem problem.
 */

import acm.program.*;

public class PythagoreanTheorem extends ConsoleProgram {
	public void run() {
		println("Enter values to compute Pythagorean theorem.");
		int a = readInt("a: ");
		int b = readInt("b: ");
		double c = calculateHypotenuse(a,b);
		println("c = " + c);
	}
	
	private double calculateHypotenuse(int a, int b){
		return Math.sqrt(a*a+b*b);
	}
}
