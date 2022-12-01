/*
 * File: Hangman.java
 * ------------------
 * This program will eventually play the Hangman game from
 * Assignment #4.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;

import java.awt.*;

public class Hangman extends ConsoleProgram {
	
	public static final int APPLICATION_WIDTH=1200;
	public static final int APPLICATION_HEIGHT=800;
	
	private HangmanCanvas canvas;
	
    public void run() {
		println("Welcome to hangman!");
		runGameLoop();
	}

	private void runGameLoop() {
		HangmanLexicon lex = new HangmanLexicon();
		RandomGenerator rand = RandomGenerator.getInstance();
		playOneGame(lex,rand);
		
	}
	public void init() { 
		canvas = new HangmanCanvas(APPLICATION_WIDTH/2,APPLICATION_HEIGHT);
		add(canvas);
	} 
	private void playOneGame(HangmanLexicon lex, RandomGenerator rand) {
		String word = lex.getWord(rand.nextInt(lex.getWordCount()));
		int guesses = 8;
		boolean[] guessed = new boolean[26];
		canvas.displayWord(generateCoveredWord(word,guessed));
		while(guesses!=0){
			println("The word now looks like this " + generateCoveredWord(word,guessed));
			println("You have " + guesses + " guesses");
			char c = readChar();
			if(word.indexOf(c)==-1){
				println("There are no such words in the word");
				canvas.incorrectGuess(c);
				guesses--;
			}else{
				println("That guess is correct");
			}
			guessed[c-'A']=true;
			
			canvas.displayWord(generateCoveredWord(word,guessed));
			
			if(guesses==0){
				println("you a ded nigger");
				break;
			}
			if(generateCoveredWord(word,guessed).indexOf('-')==-1){
				println("well done white guy");
				break;
			}
		}
		
		
	}

	private char readChar() {
		char res=0;
		boolean badInput = true;
		while(badInput){
			String s = readLine("Your guess: ");
			s = s.trim();
			if(s.length()!=1){
				println("Input must be a single character");
				continue;
			}
			if(s.toUpperCase().charAt(0)>'Z' || s.toUpperCase().charAt(0) < 'A'){
				println("Input must be a letter");
				continue;
			}
			res = s.toUpperCase().charAt(0);
			badInput = false;
		}
		return res;
	}

	private String generateCoveredWord(String word, boolean[] guessed) {
		String res = "";
		for(int i = 0; i < word.length(); i++){
			if(!guessed[(word.charAt(i))-'A']){
				res=res+"-";
			}else{
				res=res+word.charAt(i);
			}
		}
		return res;
	}

}
