/*
 * File: HangmanCanvas.java
 * ------------------------
 * This file keeps track of the Hangman display.
 */

import acm.graphics.*;

public class HangmanCanvas extends GCanvas {
	int step = 0;
	int width;
	int height;
	GLabel wordLabel;
	GLabel wrongGuessesLabel;
	String wrongGuesses;
	int y = 0;
	int x = 0;
	public HangmanCanvas(int w, int h){
		reset(w,h);
	}
	
/** Resets the display so that only the scaffold appears */
	public void reset(int w, int h) {
		removeAll();
		
		step = 0;
		width = w;
		height = h;
		// variables that make it easier to position hangman
		x = width/2-BEAM_LENGTH;
		y = height/2-SCAFFOLD_HEIGHT/2;
		// scaffold
		add(new GLine(x,y,x,y+SCAFFOLD_HEIGHT));
		add(new GLine(x,y,width/2,y));
		add(new GLine(width/2,y,width/2,y+ROPE_LENGTH));
		
		y+=ROPE_LENGTH;
		x=width/2;
		// add empty word label
		wordLabel = new GLabel("");
		wordLabel.setFont("*-*-30");
		add(wordLabel,width/6,height*5/6); // positions are random numbers that seemed nice
		// add empty wrong guesses label
		wrongGuesses = "";
		wrongGuessesLabel = new GLabel("");
		wrongGuessesLabel.setFont("*-*-20");
		add(wrongGuessesLabel,width/6,height*8/9); // here too
	}

/**
 * Updates the word on the screen to correspond to the current
 * state of the game.  The argument string shows what letters have
 * been guessed so far; unguessed letters are indicated by hyphens.
 */
	public void displayWord(String word) {
		wordLabel.setLabel(word);
	}

/**
 * Updates the display to correspond to an incorrect guess by the
 * user.  Calling this method causes the next body part to appear
 * on the scaffold and adds the letter to the list of incorrect
 * guesses that appears at the bottom of the window.
 */
	public void noteIncorrectGuess(char letter, boolean[] guessed) {
		// check if letter is already guessed
		if(guessed[letter-'A']==false){
			wrongGuesses += letter;
			wrongGuessesLabel.setLabel(wrongGuesses);
		}
		switch(step){
			case 0:
				step0();
				break;
			case 1:
				step1();
				break;
			case 2:
				step2();
				break;
			case 3:
				step3();
				break;
			case 4:
				step4();
				break;
			case 5:
				step5();
				break;
			case 6:
				step6();
				break;
			case 7:
				step7();
				break;
		}
		step++;
	}
	
	//head
	//pre-condition: y is pointed at rope's lower end, x is middle of canvas
	public void step0(){
		GOval head = new GOval(HEAD_RADIUS*2,HEAD_RADIUS*2);
		add(head,x-HEAD_RADIUS,y);
		y+=HEAD_RADIUS*2;
	}
	//body
	//pre-condition: y is pointed at head's lower end, x is middle of canvas
	public void step1(){
		add(new GLine(x,y,x,y+BODY_LENGTH));
	}
	//left arm
	//pre-condition: y is pointed at head's lower end, x is middle of canvas
	public void step2(){
		int yArm = y+ARM_OFFSET_FROM_HEAD;
		int xArmEnd = x-UPPER_ARM_LENGTH;
		add(new GLine(x,yArm,xArmEnd,yArm));
		add(new GLine(xArmEnd,yArm,xArmEnd,yArm+LOWER_ARM_LENGTH));
	}
	//right arm
	//pre-condition: y is pointed at head's lower end, x is middle of canvas
	public void step3(){
		int yArm = y+ARM_OFFSET_FROM_HEAD;
		int xArmEnd = x+UPPER_ARM_LENGTH;
		add(new GLine(x,yArm,xArmEnd,yArm));
		add(new GLine(xArmEnd,yArm,xArmEnd,yArm+LOWER_ARM_LENGTH));
	}
	//left leg
	//pre-condition: y is pointed at head's lower end, x is middle of canvas
	public void step4(){
		y+=BODY_LENGTH;
		int xHipEnd = x-HIP_WIDTH;
		add(new GLine(x,y,xHipEnd,y));
		add(new GLine(xHipEnd,y,xHipEnd,y+LEG_LENGTH));
	}
	//right leg
	//pre-condition: y is pointed at body's lower end, x is middle of canvas
	public void step5(){
		int xHipEnd = x+HIP_WIDTH;
		add(new GLine(x,y,xHipEnd,y));
		add(new GLine(xHipEnd,y,xHipEnd,y+LEG_LENGTH));
	}
	//left foot
	//pre-condition: y is pointed at body's lower end, x is middle of canvas
	public void step6(){
		y+=LEG_LENGTH;
		int xFootStart = x-HIP_WIDTH;
		add(new GLine(xFootStart,y,xFootStart-FOOT_LENGTH,y));
	}
	//right foot
	//pre-condition: y is pointed at foot's height, x is middle of canvas
	public void step7(){
		int xFootStart = x+HIP_WIDTH;
		add(new GLine(xFootStart,y,xFootStart+FOOT_LENGTH,y));
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
