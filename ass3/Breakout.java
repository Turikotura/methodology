/*
 * File: Breakout.java
 * -------------------
 * Name:
 * Section Leader:
 * 
 * This file will eventually implement the game of Breakout.
 */

import acm.graphics.*;
import acm.program.*;
import acm.util.*;
import java.lang.Object.*;
import java.applet.*;
import java.awt.*;
import java.awt.event.*;

public class Breakout extends GraphicsProgram {

/** Dimensions of the paddle */
	private static final int PADDLE_WIDTH = 60;
	private static final int PADDLE_HEIGHT = 10;

/** Offset of the paddle up from the bottom */
	private static final int PADDLE_Y_OFFSET = 30;

/** Number of bricks per row */
	private static final int NBRICKS_PER_ROW = 10;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 10;

/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick */
	private static final int BRICK_WIDTH = 80;

  /** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH =  BRICK_WIDTH * NBRICKS_PER_ROW + (NBRICKS_PER_ROW + 1) * BRICK_SEP - 8;
	public static final int APPLICATION_HEIGHT = 600;

/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;
  
/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 70;

/** Number of turns */
	private static final int NTURNS = 3;
	
	private static final Color[] colors = {Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN, Color.CYAN};
	
	private RandomGenerator rgen = RandomGenerator.getInstance();
	
	AudioClip bounceClip = MediaTools.loadAudioClip("bounce.mp3");
	int totalBricks = NBRICKS_PER_ROW * NBRICK_ROWS;
	GRect paddle;
	GOval ball;
	double vxInit;
	double vyInit;
	double vx;
	double vy;
	int points;
	GLabel text;
	GLabel tryAgain;
	GLabel pointsLabel;
	boolean gameOver = false;
	boolean startUp = true;

/* Method: run() */
/** Runs the Breakout program. */
	public void run() {
		addMouseListeners();
		initializeGame();
		runGameLoop();
	}

	private void runGameLoop() {
		while(true){
			moveBall();
			pause(30);
		}
	}

	private void moveBall() {
		GObject obj = checkCollision();
		if(obj != paddle && obj != null){
			totalBricks--;
			for(int i = 0; i < colors.length; i++){
				if(colors[i])
				points += ArrayUtils.indexOf(colors,obj.getColor());
			}
			
			remove(obj);
			double coefficient = 1.5 / (NBRICKS_PER_ROW * NBRICK_ROWS);
			vx+=vxInit*coefficient;
			vy+=vyInit*coefficient;
			if(totalBricks==0){
				winGame();
			}
		}
		ball.setLocation(ball.getX()+vx, ball.getY()+vy);
	}

	private void winGame() {
		gameOver = true;
		remove(ball);
		text = new GLabel("You Won");
		text.setFont("*-*-20");
		tryAgain = new GLabel("Left click to play again");
		tryAgain.setFont("*-*-16");
		add(text,getWidth()/2-text.getWidth()/2,getHeight()/2-20);
		add(tryAgain,getWidth()/2-tryAgain.getWidth()/2,getHeight()/2+20);
	}

	private GObject checkCollision() {
		double x = ball.getX();
		double y = ball.getY();
		GObject westObj = getElementAt(x - 1, y + BALL_RADIUS/2);
		GObject eastObj = getElementAt(x + 1 + BALL_RADIUS, y + BALL_RADIUS/2);
		GObject northObj = getElementAt(x + BALL_RADIUS/2, y-1);
		boolean northCheck = (northObj != null && northObj!=paddle) || y < 0;
		boolean southCheck = getElementAt(x + BALL_RADIUS/2, y + BALL_RADIUS + 1)!=null;
		boolean westCheck = (westObj != null && westObj!=paddle) || x < 0;
		boolean eastCheck = (eastObj != null && eastObj!=paddle)|| x + BALL_RADIUS > getWidth();
		boolean loseCheck = y + BALL_RADIUS > getHeight();
		if(northCheck){
			bounceClip.play();
			vy = -vy;
			return getElementAt(x + BALL_RADIUS/2, y-1);
		}
		if(southCheck){
			bounceClip.play();
			vy = -vy;
			return getElementAt(x + BALL_RADIUS/2, y +1 + BALL_RADIUS);
		}
		if(westCheck){
			bounceClip.play();
			vx = -vx;
			return getElementAt(x-1, y + BALL_RADIUS/2);
		}
		if(eastCheck){
			bounceClip.play();
			vx = -vx;
			return getElementAt(x+1 + BALL_RADIUS, y + BALL_RADIUS/2);
		}
		if(loseCheck){
			endGame();
			ball.setLocation(0,0);
			vx = 0;
			vy = 0;
		}
		return null;
	}

	private void endGame() {
		gameOver = true;
		remove(ball);
		text = new GLabel("You Lost");
		text.setFont("*-*-20");
		tryAgain = new GLabel("Left click to play again");
		tryAgain.setFont("*-*-16");
		add(text,getWidth()/2-text.getWidth()/2,getHeight()/2-20);
		add(tryAgain,getWidth()/2-tryAgain.getWidth()/2,getHeight()/2+20);
	}

	private void initializeGame(){
		buildBricks();
		buildPaddle();
		buildBall();
		if(startUp){
			pointsLabel = new GLabel("Points: " + points);
			add(pointsLabel,0,40);
			startUp = false;
		}
		vxInit = rgen.nextDouble(1.0,3.0);
		if(rgen.nextBoolean()==true) vxInit = -vxInit;
		vx = vxInit;
		vyInit = 5;
		vy = vyInit;
	}
	
	private void buildBall() {
		ball = new GOval(BALL_RADIUS, BALL_RADIUS);
		ball.setFilled(true);
		ball.setFillColor(Color.BLACK);
		add(ball,WIDTH/2-BALL_RADIUS/2, HEIGHT/2-BALL_RADIUS/2);
	}

	private void buildPaddle() {
		paddle = new GRect(PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFilled(true);
		paddle.setFillColor(Color.BLACK);
		paddle.addMouseMotionListener(new MouseAdapter(){
			
		});
		add(paddle, getWidth()/2-PADDLE_WIDTH/2, getHeight()-PADDLE_Y_OFFSET-PADDLE_HEIGHT);
	}

	public void mouseClicked(MouseEvent e){
		if(gameOver){
			remove(text);
			remove(tryAgain);
			remove(paddle);
			initializeGame();
			totalBricks = NBRICKS_PER_ROW * NBRICK_ROWS;
			gameOver = false;
		}
	}

	public void mouseMoved(MouseEvent e){
		if(e.getX()+PADDLE_WIDTH/2>getWidth()){
			paddle.setLocation(getWidth()-PADDLE_WIDTH, paddle.getY());
		}else if(e.getX()-PADDLE_WIDTH/2<0){
			paddle.setLocation(0, paddle.getY());
		}else{
			paddle.setLocation(e.getX()-PADDLE_WIDTH/2, paddle.getY());
		}
	}
	
	private void buildBricks(){
		for(int i = 0; i < NBRICK_ROWS; i++){
			buildRow(i);
		}
	}

	private void buildRow(int i) {
		Color col = colors[i/2];
		for(int j = 0; j < NBRICKS_PER_ROW; j++){
			GRect brick = new GRect(BRICK_WIDTH, BRICK_HEIGHT);
			brick.setFilled(true);
			println(getWidth());
			brick.setColor(col);
			brick.setFillColor(col);
			int posX = (BRICK_WIDTH+BRICK_SEP)*j+BRICK_SEP;
			int posY = i*(BRICK_HEIGHT+BRICK_SEP) + BRICK_Y_OFFSET;
			if(getElementAt(posX,posY)==null){
				add(brick, posX, posY);
			}	
		}
	}
}
