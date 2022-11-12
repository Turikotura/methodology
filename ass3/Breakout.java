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
	private static final int NBRICK_ROWS = 5;

/** Separation between bricks */
	private static final int BRICK_SEP = 4;

/** Width of a brick */
	private static final int BRICK_WIDTH = 40;

  /** Width and height of application window in pixels */
	public static final int APPLICATION_WIDTH = -8 + BRICK_WIDTH * NBRICKS_PER_ROW + (NBRICKS_PER_ROW + 1) * BRICK_SEP;
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
	
	AudioClip bounceClip = MediaTools.loadAudioClip("bounce.au");
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

	// to change brick quantity change NBRICK_ROWS, NBRICKS_PER_ROW
	// application width is set by combination of NBRICK_ROWS, NBRICKS_PER_ROW and BRICK_WIDTH
	
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
		GObject obj = checkCollision(); // gets collided object if there is one
		println(vx + " " + vy);
		if(obj!=null && obj.getHeight()==8){ // checks if obj is brick by it's height
			totalBricks--;
			// handles adding points based on brick color
			for(int i = 0; i < colors.length; i++){
				if(colors[i]==obj.getColor()){
					points += (5-i) * 10;
					pointsLabel.setLabel("Points: " + points);
					break;
				}
			}
			
			remove(obj);
			// handles speeding up the ball
			// at the end of game speed will be 2x the original if no hps are lost
			double coefficient = 1.0 / (NBRICKS_PER_ROW * NBRICK_ROWS);
			// dir will be 1 or -1 depending on direction
			double vxDir = vx/Math.abs(vx);
			double vyDir = vy/Math.abs(vy);
			
			vx=vx + Math.abs(vxInit*coefficient)*vxDir;
			vy=vy + Math.abs(vyInit*coefficient)*vyDir;
			// win condition
			if(totalBricks==0){
				winGame();
			}
		}
		ball.setLocation(ball.getX()+vx, ball.getY()+vy);
	}

	// collision system, returns collided object or null if there is none
	private GObject checkCollision() {
		double x = ball.getX();
		double y = ball.getY();
		// checks are based of 1 pixel off from side object checks
		GObject topObject = getElementAt(x+BALL_RADIUS,y-1);
		GObject botObject = getElementAt(x+BALL_RADIUS,y+2*BALL_RADIUS+1);
		GObject leftObject = getElementAt(x-1,y+BALL_RADIUS);
		GObject rightObject = getElementAt(x+2*BALL_RADIUS+1,y+BALL_RADIUS);
		boolean loseCheck = y + BALL_RADIUS > getHeight();
		
		// ball getting stuck in paddle was fixed by only changing direction
		// if ball move direction and colliding object were opposing directions
		if(topObject != null){
			bounceClip.play();
			vy = Math.abs(vy);
			return topObject;
		}
		if(botObject != null){
			// logic for paddle and ball interaction
			// if direction doesn't match side of paddle x direction changes too
			if(botObject == paddle){
				bounceClip.play();
				double paddleCenter = paddle.getX() + PADDLE_WIDTH/2;
				double ballTouchPoint = x+BALL_RADIUS;
				boolean rightHit = ballTouchPoint>paddleCenter;
				boolean rightDir = vx>0;
				if(rightHit^rightDir){
					vx = -vx;
				}
				vy = -Math.abs(vy);
				return paddle;
			}
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
			hideBall();
			loseHealth();
		}
		return null;
	}
	
	private void winGame() {
		// gameOver check for mouseClicked event
		gameOver = true;
		hideBall();
		
		text = new GLabel("You Won");
		text.setFont("*-*-20");
		tryAgain = new GLabel("Left click to play again");
		tryAgain.setFont("*-*-16");
		add(text,WIDTH/2-text.getWidth()/2,getHeight()/2-20);
		add(tryAgain,WIDTH/2-tryAgain.getWidth()/2,getHeight()/2+20);
	}
	
	private void loseHealth() {
		lives--;
		remove(lifeBlocks[lives]);
		if(lives>0){
			pause(1000);
			buildBall();
			resetSpeed();
		}else{
			loseGame();
		}
	}

	private void loseGame() {
		gameOver = true;
		hideBall();
		
		text = new GLabel("You Lost");
		text.setFont("*-*-20");
		tryAgain = new GLabel("Left click to play again");
		tryAgain.setFont("*-*-16");
		add(text,WIDTH/2-text.getWidth()/2,getHeight()/2-20);
		add(tryAgain,WIDTH/2-tryAgain.getWidth()/2,getHeight()/2+20);
	}
	
	// initializing objects
	private void initializeGame(){
		// only runs during startup
		if(startUp){
			pointsLabel = new GLabel("Points: " + points);
			add(pointsLabel,5,WALL_WIDTH/2+pointsLabel.getHeight()/3);
			buildWalls();
			buildPaddle();
			startUp = false;
		}
		points = 0;
		pointsLabel.setLabel("Points: " + points);
		lives = 3;
		addLifeBlocks();
		totalBricks = NBRICKS_PER_ROW * NBRICK_ROWS;
		buildBricks();
		buildBall();
		resetSpeed();
	}	
	
	// only works during lose or win game screens
	public void mouseClicked(MouseEvent e){
		if(gameOver){
			remove(text);
			remove(tryAgain);
			initializeGame();
			gameOver = false;
		}
	}

	// paddle controller
	public void mouseMoved(MouseEvent e){
		if(e.getX()+PADDLE_WIDTH/2>WIDTH+8){
			paddle.setLocation(WIDTH+8-PADDLE_WIDTH, paddle.getY());
		}else if(e.getX()-PADDLE_WIDTH/2<0){
			paddle.setLocation(0, paddle.getY());
		}else{
			paddle.setLocation(e.getX()-PADDLE_WIDTH/2, paddle.getY());
		}
	}
	
	private void addLifeBlocks(){
		for(int i = 0; i < 3; i++){
			lifeBlocks[i] = new GRect(10,10);
			setRectangleColor(lifeBlocks[i],Color.GREEN);
			add(lifeBlocks[i],WIDTH-15-i*20,WALL_WIDTH/2-5);
		}
	}
	
	private void buildBall() {
		ball = new GOval(2*BALL_RADIUS, 2*BALL_RADIUS);
		ball.setFilled(true);
		ball.setFillColor(Color.BLUE);
		println(WIDTH/2-BALL_RADIUS/2);
		add(ball,WIDTH/2-BALL_RADIUS/2, HEIGHT/2-BALL_RADIUS/2);
	}
	
	private void buildPaddle() {
		paddle = new GRect(PADDLE_WIDTH, PADDLE_HEIGHT);
		paddle.setFilled(true);
		paddle.setFillColor(Color.BLUE);
		add(paddle, WIDTH/2-PADDLE_WIDTH/2, getHeight()-PADDLE_Y_OFFSET-PADDLE_HEIGHT);
	}
	
	// game borders
	private void buildWalls(){
		GRect leftWall = new GRect(WALL_WIDTH, HEIGHT);
		GRect rightWall = new GRect(WALL_WIDTH, HEIGHT);
		GRect topWall = new GRect(WIDTH+8, WALL_WIDTH);
		add(leftWall,-WALL_WIDTH,0);
		add(rightWall,WIDTH+8,0);
		add(topWall,0,0);
	}
	
	private void buildBricks(){
		for(int i = 0; i < NBRICK_ROWS; i++){
			buildRow(i);
		}
	}

	private void buildRow(int i) {
		Color col = colors[calculateColorIndex(i)];
		for(int j = 0; j < NBRICKS_PER_ROW; j++){
			GRect brick = new GRect(BRICK_WIDTH, BRICK_HEIGHT);
			setRectangleColor(brick,col);
			int posX = (BRICK_WIDTH+BRICK_SEP)*j+BRICK_SEP;
			int posY = i*(BRICK_HEIGHT+BRICK_SEP) + BRICK_Y_OFFSET;
			if(getElementAt(posX,posY)==null){
				add(brick, posX, posY);
			}
		}
	}
	
	private void resetSpeed(){
		vxInit = rgen.nextDouble(1.0,3.0);
		if(rgen.nextBoolean()==true) vxInit = -vxInit;
		vx = vxInit;
		vyInit = 5;
		vy = vyInit;
	}
	
	private void hideBall(){
		ball.setLocation(0,0);
		vx = 0;
		vy = 0;
		remove(ball);
	}
	
	private void setRectangleColor(GRect rect, Color col){
		rect.setFilled(true);
		rect.setColor(col);
		rect.setFillColor(col);
	}
	
	// hard to understand, read at your own risk ;D
	private int calculateColorIndex(int i){
		int floorFiveMultiple = (NBRICK_ROWS/5)*5;
		int howManyFullDivisions = NBRICK_ROWS-floorFiveMultiple;
		int divisor = NBRICK_ROWS/5 + 1;
		int count = 0;
		int result = 0;
		for(int j = 0; j <= i; j++){
			if(howManyFullDivisions>0){
				if(count == divisor){
					result++;
					howManyFullDivisions--;
					count = 0;
				}
			}else{
				if(count == divisor-1){
					result++;
					count = 0;
				}
			}
			count++;
		}
		return result;
	}
}
