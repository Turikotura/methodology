/*
 * File: Target.java
 * Name: 
 * Section Leader: 
 * -----------------
 * This file is the starter file for the Target problem.
 */

import acm.graphics.*;
import acm.program.*;
import java.awt.*;

public class Target extends GraphicsProgram {
	
	private static final int BIG_RADIUS = 72;
	
	public void run() {
		makeTarget();
		
		
	}
	
	private void makeTarget(){
		boolean isRed = true;
		for(int rad = BIG_RADIUS; rad>0; rad-=BIG_RADIUS/3){ // values were pretty close to multiples of 24
			GOval circle = new GOval(2*rad,2*rad);
			circle.setFilled(true);
			if(isRed){
				setColorToCircle(circle, Color.RED);
				isRed=false;
			}else{
				setColorToCircle(circle, Color.WHITE);
				isRed=true;
			}
			add(circle,getWidth()/2-rad,getHeight()/2-rad); // offset since position is left top point
		}
	}
	
	// sets color of desired circle to desired color
	private void setColorToCircle(GOval circle, Color col){
		circle.setFillColor(col);
		circle.setColor(col);
	}
}
