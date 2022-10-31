/*
 * File: ProgramHierarchy.java
 * Name: 
 * Section Leader: 
 * ---------------------------
 * This file is the starter file for the ProgramHierarchy problem.
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;
import java.util.ArrayList;

public class ProgramHierarchy extends GraphicsProgram {
	
	private static final int RECT_HEIGHT = 60;
	private static final int RECT_WIDTH = 180;
	private static final int MARGIN_HOR = 20;
	private static final int MARGIN_VERT = 60;
	
	public void run() {
		drawHierarchy();
	}
	
	private void drawHierarchy(){
		drawProgram();
		drawSubPrograms();
		addLines();
	}

	private void drawProgram(){
		int posX = getWidth()/2 - RECT_WIDTH/2; // mid point including offset for rect size
		int posY = getHeight()/2-MARGIN_VERT/2-RECT_HEIGHT; // y pos so that midpoint between top and bottom boxes is center y
		drawBox("Program", posX, posY);
	}
	
	private void drawSubPrograms() {
		for(int i = -1; i <= 1; i++){
			int posX = getWidth()/2 - RECT_WIDTH/2 + i*(RECT_WIDTH+MARGIN_HOR); // accounting for margin between boxes
			int posY = getHeight()/2 + MARGIN_VERT/2;
			// I think arrays weren't yet explained so :D
			switch(i){
				case -1:
					drawBox("GraphicsProgram", posX, posY);
					break;
				case 0:
					drawBox("ConsoleProgram", posX, posY);
					break;
				case 1:
					drawBox("DialogProgram", posX, posY);
					break;
			}
		}
	}
	
	// function creating box with RECT_WIDTH and RECT_HEIGHT sizes and custom position and label text
	private void drawBox(String text, int posX, int posY){
		GRect rect = new GRect(RECT_WIDTH,RECT_HEIGHT);
		add(rect, posX, posY);
		GLabel label = new GLabel(text);
		int labelX = posX + RECT_WIDTH/2 - (int) label.getWidth()/2;
		int labelY = posY + RECT_HEIGHT/2 + (int) label.getHeight()/2; // addition because label's position center is on bottom
		add(label, labelX, labelY);
	}
	
	private void addLines(){
		for(int i = -1; i <=1; i++){
			int x0 = getWidth()/2;
			int y0 = getHeight()/2-MARGIN_VERT/2;
			int x1 = x0 + i*(MARGIN_HOR+RECT_WIDTH);
			int y1 = getHeight()/2+MARGIN_VERT/2;
			GLine l = new GLine(x0, y0, x1, y1);
			add(l);
		}
	}
}