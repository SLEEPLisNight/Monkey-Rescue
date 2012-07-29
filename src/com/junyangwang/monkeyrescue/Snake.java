package com.junyangwang.monkeyrescue;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Snake {
	private GameView gameView;
    private Bitmap bmp;
    private int x = 0;
    private int y = 0;
    private int dx = 0;
    private int dy = 0;
    private int xSpeed;
    private int ySpeed;
    private int width;
    private int height;
    
    private int speedPerFrame = 5;
    private boolean noStop = false;

    public Snake(GameView gameView, Bitmap bmp, int x, int y, int speedLevel) {
          this.width = bmp.getWidth();
          this.height = bmp.getHeight();
          this.gameView = gameView;
          this.bmp = bmp;

          this.x = x;
          this.y = y;
          
          speedPerFrame = speedPerFrame + speedLevel;
    }

    private void update(int x2, int y2) {
         
    	  dx = x2 - x;
    	  dy = y2 - y;
    	  
    	  double ratio = (speedPerFrame/Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)));
    	  
    	  if (ratio > 1){
    	  		//xSpeed = dx;
    	  		//ySpeed = dy;
    		  	noStop = true;
    	  } else if (!noStop){
    	  		xSpeed = (int) (dx*ratio);
    	  		ySpeed = (int) (dy*ratio);
    	  }
    	  
    	  //check if Snake(0) go out of boundry
    	  if (x + xSpeed > gameView.getWidth() - width/2 || x + xSpeed < width/2) {
              	xSpeed = -xSpeed;
    	  }
    	  if (y + ySpeed > gameView.getHeight() - height/2 || y + ySpeed < height/2) {
              	ySpeed = -ySpeed;
    	  }
    	  
    	  
          x = x + xSpeed;
          y = y + ySpeed;
    }

    public void onDraw(Canvas canvas, int x2, int y2) {
          update(x2, y2);
          canvas.drawBitmap(bmp, x - width/2, y - height/2, null);
    }
    
    private void updateBefore(int x2, int y2) {
        
  	  	dx = x2 - x;
  	  	dy = y2 - y;
  	  
  	  	double ratioDistance = (width/Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)));
  	  	double ratio = (speedPerFrame/Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2)));
  	  	
  	  	if (ratioDistance > 1){
  	  		xSpeed = (int) (-dx*(1 - 1/ratioDistance));
  	  		ySpeed = (int) (-dy*(1 - 1/ratioDistance));
  	  	} else {
  	  		xSpeed = (int) (dx*ratio);
  	  		ySpeed = (int) (dy*ratio);
  	  	}
  	  
        x = x + xSpeed;
        y = y + ySpeed;
    }
    
    public void onDrawBefore(Canvas canvas, int x2, int y2) {
        updateBefore(x2, y2);
        canvas.drawBitmap(bmp, x - width/2, y - height/2, null);
    }

    public boolean isCollision(int x2, int y2) {
        return x2 > x - width/2 && x2 < x + width/2 && y2 > y - height/2 && y2 < y + height/2;
  }
    
    public int getX(){
    	return x;
    }
    
    public int getY(){
    	return y;
    }
    
    public void setNoStop(boolean noStop){
    	this.noStop = noStop;
    }
    
}
