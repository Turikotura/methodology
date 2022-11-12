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
	private static final int NBRICKS_PER_ROW = 6;

/** Number of rows of bricks */
	private static final int NBRICK_ROWS = 8;

/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick */
	private static final int BRICK_WIDTH = 50;

  /** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH =  BRICK_WIDTH * NBRICKS_PER_ROW + (NBRICKS_PER_ROW + 1) * BRICK_SEP;
	public static final int APPLICATION_HEIGHT = 620;

/** Dimensions of game board (usually the same) */
	private static final int WIDTH = APPLICATION_WIDTH;
	private static final int HEIGHT = APPLICATION_HEIGHT;
  
/** Height of a brick */
	private static final int BRICK_HEIGHT = 8;

/** Radius of the ball in pixels */
	private static final int BALL_RADIUS = 10;

/** Offset of the top brick row from the top */
	private static final int BRICK_Y_OFFSET = 90;

/** Number of turns */
	private static final int NTURNS = 3;
	
	private static final int WALL_WIDTH = 20;
	
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
	GRect[] lifeBlocks = new GRect[3];
	int lives;

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
		
		if(obj!=null && obj.getHeight()==8){
			totalBricks--;
			for(int i = 0; i < colors.length; i++){
				if(colors[i]==obj.getColor()){
					points += (5-i) * 10;
					pointsLabel.setLabel("Points: " + points);
					break;
				}
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
		GObject topObject = getElementAt(x+BALL_RADIUS,y-1);
		GObject botObject = getElementAt(x+BALL_RADIUS,y+2*BALL_RADIUS+1);
		GObject leftObject = getElementAt(x-1,y+BALL_RADIUS);
		GObject rightObject = getElementAt(x+2*BALL_RADIUS+1,y+BALL_RADIUS);
		boolean loseCheck = y + BALL_RADIUS > getHeight();
		if(topObject != null){
			bounceClip.play();
			vy = Math.abs(vy);
			return topObject;
		}
		if(botObject != null){
			bounceClip.play();
			vy = -Math.abs(vy);
			return botObject;
		}
		if(leftObject != null){
			bounceClip.play();
			vx = Math.abs(vx);
			return leftObject;
		}
		if(rightObject != null){
			bounceClip.play();
			vx = -Math.abs(vx);
			return rightObject;
		}
		if(loseCheck){
			ball.setLocation(0,0);
			vx = 0;
			vy = 0;
			loseHealth();
		}
		return null;
	}

	private void loseHealth() {
		lives--;
		remove(ball);
		if(lives==2){
			remove(lifeBlocks[0]);
		}else if(lives==1){
			remove(lifeBlocks[1]);
		}else{
			remove(lifeBlocks[2]);
		}
		if(lives>0){
			pause(1000);
			buildBall();
			vxInit = rgen.nextDouble(1.0,3.0);
			if(rgen.nextBoolean()==true) vxInit = -vxInit;
			vx = vxInit;
			vyInit = 5;
			vy = vyInit;
		}else{
			endGame();
		}
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
		if(startUp){
			pointsLabel = new GLabel("Points: " + points);
			add(pointsLabel,5,WALL_WIDTH/2+pointsLabel.getHeight()/3);
			buildWalls();
			buildPaddle();
			
			startUp = false;
		}
		points = 0;
		lives = 3;
		addLifeBlocks();
		totalBricks = NBRICKS_PER_ROW * NBRICK_ROWS;
		buildBricks();
		buildBall();
		vxInit = rgen.nextDouble(1.0,3.0);
		if(rgen.nextBoolean()==true) vxInit = -vxInit;
		vx = vxInit;
		vyInit = 5;
		vy = vyInit;
	}
	
	private void addLifeBlocks(){
		for(int i = 0; i < 3; i++){
			println(lifeBlocks[i]);
			lifeBlocks[i] = new GRect(10,10);
			println(lifeBlocks[i]);
			lifeBlocks[i].setFilled(true);
			lifeBlocks[i].setFillColor(Color.GREEN);
			add(lifeBlocks[i],WIDTH-15-i*20,WALL_WIDTH/2-5);
		}
	}
	
	private void addLifeBlock(GRect block, int index){
		
	}
	
	private void buildBall() {
		ball = new GOval(2*BALL_RADIUS, 2*BALL_RADIUS);
		ball.setFilled(true);
		ball.setFillColor(Color.BLACK);
		println(WIDTH/2-BALL_RADIUS/2);
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
			initializeGame();
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
	
	private void buildWalls(){
		GRect leftWall = new GRect(WALL_WIDTH, HEIGHT);
		GRect rightWall = new GRect(WALL_WIDTH, HEIGHT);
		GRect topWall = new GRect(WIDTH, WALL_WIDTH);
		add(leftWall,-WALL_WIDTH,0);
		add(rightWall,WIDTH,0);
		add(topWall,0,0);
		println(getHeight());
	}
	
	private void buildBricks(){
		for(int i = 0; i < NBRICK_ROWS; i++){
			buildRow(i);
		}
	}

	private void buildRow(int i) {
		println(NBRICK_ROWS/5);
		Color col = colors[i/(NBRICK_ROWS/5)];
		for(int j = 0; j < NBRICKS_PER_ROW; j++){
			GRect brick = new GRect(BRICK_WIDTH, BRICK_HEIGHT);
			brick.setFilled(true);
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
