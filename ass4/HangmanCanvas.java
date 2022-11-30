/*
 * File: HangmanCanvas.java
 * ------------------------
 * This file keeps track of the Hangman display.
 */

import acm.graphics.*;

public class HangmanCanvas extends GCanvas {

	public static final int APPLICATION_HEIGHT = 620;
	int step = 0;
	
	public HangmanCanvas(){
		double x = getWidth()/2-BEAM_LENGTH;
		add(new GLine(x,getHeight()/2-SCAFFOLD_HEIGHT/2,x,getHeight()/2+SCAFFOLD_HEIGHT/2));
		add(new GLine(x,getHeight()/2-SCAFFOLD_HEIGHT/2,getWidth()/2,getHeight()/2-SCAFFOLD_HEIGHT/2));
		add(new GLine(getWidth()/2,getHeight()/2-SCAFFOLD_HEIGHT/2,getWidth()/2,getHeight()/2-SCAFFOLD_HEIGHT/2+ROPE_LENGTH));
	}
	
/** Resets the display so that only the scaffold appears */
	public void reset() {
		/* You fill this in */
	}

/**
 * Updates the word on the screen to correspond to the current
 * state of the game.  The argument string shows what letters have
 * been guessed so far; unguessed letters are indicated by hyphens.
 */
	public void displayWord(String word) {
		
	}

/**
 * Updates the display to correspond to an incorrect guess by the
 * user.  Calling this method causes the next body part to appear
 * on the scaffold and adds the letter to the list of incorrect
 * guesses that appears at the bottom of the window.
 */
	public void noteIncorrectGuess(char letter) {
		switch(step){
			case 0:
				step0();
				break;
			case 1:
				step1();
				break;
		}
		step++;
		
	}

	public void step0(){
		
	}
	
	public void step1(){
		
	}
/* Constants for the simple version of the picture (in pixels) */
	private static final int SCAFFOLD_HEIGHT = 360;
	private static final int BEAM_LENGTH = 144;
	private static final int ROPE_LENGTH = 18;
	private static final int HEAD_RADIUS = 36;
	private static final int BODY_LENGTH = 144;
	private static final int ARM_OFFSET_FROM_HEAD = 28;
	private static final int UPPER_ARM_LENGTH = 72;
	private static final int LOWER_ARM_LENGTH = 44;
	private static final int HIP_WIDTH = 36;
	private static final int LEG_LENGTH = 108;
	private static final int FOOT_LENGTH = 28;

}
