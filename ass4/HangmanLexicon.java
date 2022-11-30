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

import acm.util.*;


public class HangmanLexicon {
	BufferedReader br;
	
	public int getWordCount() {
		try {
			br = new BufferedReader(new FileReader("HangmanLexicon.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		try {
			int c = 0;
			while(br.readLine()!=null){
				c++;
			}
			return c;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
	}

/** Returns the word at the specified index. */
	public String getWord(int index) {
		try {
			br = new BufferedReader(new FileReader("HangmanLexicon.txt"));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		String s="";
		for(int i = 0; i < index; i++){
			try {
				s = br.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return s;
	};
}
