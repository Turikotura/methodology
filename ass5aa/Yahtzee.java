/*
 * File: Yahtzee.java
 * ------------------
 * This program will eventually play the Yahtzee game.
 */

import acm.io.*;
import acm.program.*;
import acm.util.*;

public class Yahtzee extends GraphicsProgram implements YahtzeeConstants {
	
	public static void main(String[] args) {
		new Yahtzee().start(args);
	}
	
	public void run() {
		IODialog dialog = getDialog();
		nPlayers=-1;
		while(nPlayers>4 || nPlayers < 0){
			nPlayers = dialog.readInt("Enter number of players");
		}
		// initialize arrays
		totalScores = new int[nPlayers][3]; // 0 -> UPPER_SCORE, 1 -> LOWER_SCORE, 2 -> UPPER_BONUS
		selected = new boolean[nPlayers][16]; // map to check if certain row has been selected. 16 because last rows cant be selected anyways.
		playerNames = new String[nPlayers];
		for (int i = 1; i <= nPlayers; i++) {
			playerNames[i - 1] = dialog.readLine("Enter name for player " + i);
		}
		display = new YahtzeeDisplay(getGCanvas(), playerNames);
		playGame();
		endGame();
	}

	private void playGame() {
		for(int i = 0; i < 13; i++) runOneTurn();
	}
	
	private void runOneTurn() {
		for(int player = 0; player < nPlayers; player++){
			playerTurn(player);
		}
		
	}

	private void endGame() {
		int winner = -1;
		int winnerScore = 0;
		for(int p = 0; p < nPlayers; p++){
			int total = totalScores[p][0] + totalScores[p][1] + totalScores[p][2];
			display.updateScorecard(TOTAL, p+1, total);
			if(total>winnerScore){
				winnerScore = total;
				winner = p;
			}
		}
		display.printMessage("Congrats, " + playerNames[winner] + ", you are the winner with a total score of " + winnerScore + " !");
	}

	private void playerTurn(int p) {
		display.printMessage(playerNames[p]+"'s turn. Press \"Roll Dice\" to play again.");
		display.waitForPlayerToClickRoll(p+1);
		
		int[] diceSet = generateRandomDiceArray();
		display.displayDice(diceSet);
		// 2 rerolls
		changeDice(diceSet);
		changeDice(diceSet);
		
		display.printMessage("Select a category for this roll.");
		int cat = display.waitForPlayerToSelectCategory();
		// check if category has been selected already
		while(selected[p][cat]==true){
			display.printMessage("Please, choose empty category.");
			cat = display.waitForPlayerToSelectCategory();
		}
		int score = calculateScore(cat, diceSet);
		updateScores(p,cat,score);
	}
	
	// updates scorecard and keeps total sums in totalScores array
	private void updateScores(int p, int cat, int score){
		selected[p][cat]=true;
		display.updateScorecard(cat, p+1, score);
		if(cat < UPPER_SCORE){
			totalScores[p][0]+=score;
			display.updateScorecard(UPPER_SCORE, p+1, totalScores[p][0]);
		}else{
			totalScores[p][1]+=score;
			display.updateScorecard(LOWER_SCORE, p+1, totalScores[p][1]);
		}
		checkBonus(p);
		display.updateScorecard(TOTAL, p+1, totalScores[p][0]+totalScores[p][1]+totalScores[p][2]);
	}
	
	// checks if bonus is applicable and applies it if so
	private void checkBonus(int p){
		if(totalScores[p][0] >= 63){
			totalScores[p][2] = 35;
			display.updateScorecard(UPPER_BONUS, p+1, 35);
		}
	}
	
	// reroll logic
	private void changeDice(int[] diceSet){
		display.printMessage("Select the dice you want to re-roll and click \"Roll Again\".");
		display.waitForPlayerToSelectDice();
		updateDiceReroll(diceSet); //
		display.displayDice(diceSet);
	}
	
	// mutates argument array
	private void updateDiceReroll(int[] diceSet) {
		boolean[] selected = checkSelectedDice();
		for(int i = 0; i < 5; i++) diceSet[i] = selected[i]?rgen.nextInt(1,6):diceSet[i]; // if selected reroll, else keep original value
	}

	// creates boolean array: true if die is selected, false if not
	private boolean[] checkSelectedDice() {
		boolean[] result = new boolean[5];
		for(int i = 0; i < 5; i++) result[i] = display.isDieSelected(i);
		return result;
	}

	// random set of 5 dice generator
	private int[] generateRandomDiceArray(){
		int[] result = new int[5];
		for(int i = 0; i < 5; i++) result[i] = rgen.nextInt(1,6);
		return result;
	}
	
	// returns score based on category and set of dice
	private int calculateScore(int category, int[] dice){
		switch(category){
			// same logic for first 6, returns sum of dice if it matches category
			case ONES:
			case TWOS:
			case THREES:
			case FOURS:
			case FIVES:
			case SIXES:
				return calculateSum(category, dice);
			// if most occurence of same value die is >= 3 or 4 returns sum of all dice
			case THREE_OF_A_KIND:
				return countMostOccurences(dice) >= 3 ? calculateSum(0,dice) : 0;
			case FOUR_OF_A_KIND:
				return countMostOccurences(dice) >= 4 ? calculateSum(0,dice) : 0;
			// map is list of size 6, value indicating number of dice of index in set
			// if throughout map we have three and two items, it must be full house
			case FULL_HOUSE:
				int[] map = makeDiceCountMap(dice);
				boolean threeIdentical = false;
				boolean twoIdentical = false;
				for(int i: map){
					threeIdentical = i==3 || threeIdentical; // keeps the value if it's true at least once in loop
					twoIdentical = i==2 || twoIdentical;
				}
				return threeIdentical && twoIdentical ? 25 : 0;
			case SMALL_STRAIGHT:
				return checkStraight(4,dice) ? 30 : 0;
			case LARGE_STRAIGHT:
				return checkStraight(5,dice) ? 40 : 0;
			case YAHTZEE:
				return countMostOccurences(dice)==5?50:0;
			case CHANCE:
				return calculateSum(0,dice);
		}
		return 0;
	}
	
	// checks if there is substring of size 'length' in count map without any zeroes
	private boolean checkStraight(int length, int[] dice){
		int[] map = makeDiceCountMap(dice);
		for(int i:map)print(i);
		for(int i = 0; i <= 6-length; i++){
			boolean isStraight = true;
			for(int j = 0+i; j < length+i; j++){
				if(map[j]==0){
					isStraight = false;
					break;
				}
			}
			if(isStraight==true) return true;
		}
		return false;
	}
	
	// finds most occurring item in dice set and returns quantity of it
	private int countMostOccurences(int[] dice) {
		int[] map = makeDiceCountMap(dice);
		int max = 0;
		for(int i: map) max = max>i?max:i;
		return max;
	}
	
	// makes a map: if we have dice set 4, 3, 2, 1, 6, 6 map is 1,1,1,1,0,2
	private int[] makeDiceCountMap(int[] dice) {
		int[] map = new int[6];
		for(int i: dice) map[i-1]++;
		return map;
	}

	// calculates sum of dice
	// if num is nonzero, it calculates sum of all dice with num value
	// if num is zero, calculates sum of all dice
	private int calculateSum(int num, int[] dice) {
		int result = 0;
		for(int i: dice) result += (i==num || num==0)? i : 0;
		return result;
	}
	
/* Private instance variables */
	private int nPlayers;
	Leaderboard leaderboard;
	private String[] playerNames;
	private YahtzeeDisplay display;
	private RandomGenerator rgen = new RandomGenerator();
	private int[][] totalScores;
	private boolean[][] selected;

}
