package com.junyangwang.monkeyrescue;

import java.util.Random;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Trap {
	private GameView gameView;
    private Bitmap bmp;
    private int width;
    private int height;
    private int x;
    private int y;
    private int xSpeed = 0;
    private int ySpeed = 0;
    private int level;
    private int MAX_SPEED = 5;

    public Trap(GameView gameView, Bitmap bmp, int x, int y, int level) {
          this.width = bmp.getWidth();
          this.height = bmp.getHeight();
          this.gameView = gameView;
          this.bmp = bmp;
          
          this.x = x;
          this.y = y;
          
          this.level = level;
          
          Random rnd = new Random();
          if (level == 5){
        	  ySpeed = rnd.nextInt(MAX_SPEED*2) - MAX_SPEED;
          } else if (level == 6){
        	  MAX_SPEED += 1;
        	  ySpeed = rnd.nextInt(MAX_SPEED*2) - MAX_SPEED;
          } else if (level == 7){
        	  ySpeed = rnd.nextInt(MAX_SPEED*2) - MAX_SPEED;
        	  xSpeed = rnd.nextInt(MAX_SPEED*2) - MAX_SPEED;
          } else if (level == 8){
        	  MAX_SPEED += 1;
        	  ySpeed = rnd.nextInt(MAX_SPEED*2) - MAX_SPEED;
        	  xSpeed = rnd.nextInt(MAX_SPEED*2) - MAX_SPEED;
          } else if (level == 9){
        	  MAX_SPEED -= 1;
        	  ySpeed = - MAX_SPEED;
        	  xSpeed = - MAX_SPEED;
          } else if (level == 11){
        	  ySpeed = rnd.nextInt(MAX_SPEED*2) - MAX_SPEED;
          } else if (level == 12){
        	  ySpeed = rnd.nextInt(MAX_SPEED*2) - MAX_SPEED;
        	  xSpeed = rnd.nextInt(MAX_SPEED*2) - MAX_SPEED;
          } else if (level == 13){
        	  ySpeed = rnd.nextInt(MAX_SPEED*2) - MAX_SPEED;
        	  xSpeed = rnd.nextInt(MAX_SPEED*2) - MAX_SPEED;
          } else if (level == 14){      	  
        	  ySpeed = rnd.nextInt(MAX_SPEED*2) - MAX_SPEED;
        	  xSpeed = rnd.nextInt(MAX_SPEED*2) - MAX_SPEED;
          } 
    }
    
    private void update() {
    	
    	if (y + ySpeed >= gameView.getHeight() - height || y + ySpeed <= 0){
    		ySpeed = -ySpeed;
    	}
    	
    	if (x + xSpeed >= gameView.getWidth() - width || x + xSpeed <= 0){
    		xSpeed = -xSpeed;
    	}
    	
        x = x + xSpeed;
        y = y + ySpeed;
        
    }
    
    public void onDraw(Canvas canvas) {
    	update();
    	canvas.drawBitmap(bmp, x, y, null);
    }
    
    public boolean isCollision(float x2, float y2) {
        return x2 >= x && x2 <= x + width && y2 >= y && y2 <= y + height;
    }
    
}
