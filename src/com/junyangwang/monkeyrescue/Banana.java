package com.junyangwang.monkeyrescue;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

public class Banana {
	private GameView gameView;
    private Bitmap bmp;
    private Bitmap bmp1;
    private Bitmap bmp2;
    private Bitmap bmp3;
    private Bitmap bmp4;
    private int x = 0;
    private int y = 0;
    private int fX = 0;
    private int fY = 0;
    private int dx = 0;
    private int dy = 0;
    private int xSpeed;
    private int ySpeed;
    private int width;
    private int height;
    
    private int speedPerFrame = 15; // less than 15 is too slow.
    
    public Banana(GameView gameView, Bitmap bmp, int sX, int sY, int fX, int fY, Bitmap bmp2, Bitmap bmp3, Bitmap bmp4) {
          this.width = bmp.getWidth();
          this.height = bmp.getHeight();
          this.gameView = gameView;
          this.bmp = bmp;
          this.bmp1 = bmp;
          this.bmp2 = bmp2;
          this.bmp3 = bmp3;
          this.bmp4 = bmp4;
          
          this.x = sX;
          this.y = sY;
          this.fX = fX;
          this.fY = fY;
          
          setUpSpeed();
    }

    private void setUpSpeed() {
		// TODO Auto-generated method stub
    	dx = fX - x;
  	  	dy = fY - y;
  	  
  	  	double ratio = speedPerFrame/Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
  	  
  	  	xSpeed = (int) (dx*ratio);
  	  	ySpeed = (int) (dy*ratio);
	}

	private void update() {
		
    	  //check if banana go out of boundry
    	  if (x + xSpeed > gameView.getWidth() - width/2 || x + xSpeed < width/2) {
              	xSpeed = -xSpeed;
    	  }
    	  if (y + ySpeed > gameView.getHeight() - height/2 || y + ySpeed < height/2) {
              	ySpeed = -ySpeed;
    	  }
    	  
    	  
          x = x + xSpeed;
          y = y + ySpeed;
          
          //change bmp
          if (bmp == bmp1){
        	  bmp = bmp2;
          } else if (bmp == bmp2){
        	  bmp = bmp3;
          } else if (bmp == bmp3){
        	  bmp = bmp4;
          } else if (bmp == bmp4){
        	  bmp = bmp1;
          }
    }

    public void onDraw(Canvas canvas) {
          update();
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
    
}
