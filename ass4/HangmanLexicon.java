/*
 * File: HangmanLexicon.java
 * -------------------------
 * This file contains a stub implementation of the HangmanLexicon
 * class that you will reimplement for Part III of the assignment.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.ArrayList;

import acm.util.*;


public class HangmanLexicon {
	BufferedReader br;
	ArrayList<String> dictionary = new ArrayList<String>();
	
	public HangmanLexicon(){
		try {
			br = new BufferedReader(new FileReader("HangmanLexicon.txt"));
			for(String word = br.readLine(); word!=null; word = br.readLine()){
				dictionary.add(word);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getWordCount() {
		return dictionary.size();
	}

/** Returns the word at the specified index. */
	public String getWord(int index) {
		return dictionary.get(index);
	};
}
