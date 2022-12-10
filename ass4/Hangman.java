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
	HangmanLexicon lex;
	RandomGenerator rand;
	
    public void run() {
		println("Welcome to hangman!");
		runGameLoop();
	}

	private void runGameLoop() {
		lex = new HangmanLexicon();
		rand = RandomGenerator.getInstance();
		playOneGame();
	}
	
	public void init() { 
		canvas = new HangmanCanvas(APPLICATION_WIDTH/2,APPLICATION_HEIGHT);
		add(canvas);
	}
	
	private void playOneGame() {
		canvas.reset(APPLICATION_WIDTH/2,APPLICATION_HEIGHT);
		// generate word
		String word = lex.getWord(rand.nextInt(lex.getWordCount()));
		int guesses = 8;
		// bool map for already guessed characters
		boolean[] guessed = new boolean[26];
		canvas.displayWord(generateCoveredWord(word,guessed));

		while(guesses!=0){
			println("The word now looks like this " + generateCoveredWord(word,guessed));
			println("You have " + guesses + " guesses left");
			
			char guessedCharacter = readChar();
			if(!checkGuess(word,guessedCharacter,guessed)){
				guesses--;
			}
			guessed[guessedCharacter-'A']=true;
			canvas.displayWord(generateCoveredWord(word,guessed));
			// lose
			if(guesses==0){
				println("First time?");
				println("The word was: " + word);
				println("u ded");
				break;
			}
			// win
			if(generateCoveredWord(word,guessed).indexOf('-')==-1){
				println("You guessed the word: " + word);
				println("Well done, Houdini");
				break;
			}
		}
		playAgainPrompt();
		
		
	}

	private void playAgainPrompt() {
		while(true){
			String playAgain = readLine("Want to play again?: ").toLowerCase();
			if(playAgain.equals("yes")||playAgain.equals("ye")||playAgain.equals("y")||playAgain.equals("sure")){
			   	playOneGame();
			}else if(playAgain.equals("no")||playAgain.equals("nah")||playAgain.equals("n")){
				exit();
			}
		}
		
	}

	// checks if char is in word and updates canvas accordingly.
	// returns true if correct guess, false otherwise
	private boolean checkGuess(String word, char guessedCharacter, boolean[] guessed) {
		if(word.indexOf(guessedCharacter)==-1){
			println("There are no such words in the word");
			canvas.noteIncorrectGuess(guessedCharacter,guessed);
			return false;
		}
		println("That guess is correct");
		return true;
	}

	// custom method for reading single character between a-z, case insensitive
	private char readChar() {
		char res=0;
		while(true){
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
			break;
		}
		return res;
	}

	// generates covered word, with word and bool map
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
