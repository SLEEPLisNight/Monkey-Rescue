package com.junyangwang.monkeyrescue;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class SuperCrocodile {
	private GameView gameView;
    private Bitmap bmp;
    private Bitmap bmp1;
    private Bitmap bmp2;
    private Bitmap bmp3;
    private Bitmap bmp4;
    private int x = 0;
    private int y = 0;
    private int dx = 0;
    private int dy = 0;
    private int xSpeed;
    private int ySpeed;
    private int width;
    private int height;
    private int level;
    private int type;
    private int life;
    
    private int speedPerFrame = 3; //4 is fast enough, 3 is easier
    
    public SuperCrocodile(GameView gameView, Bitmap bmp, int sX, int sY, int level, int type, Bitmap bmp2, Bitmap bmp3, Bitmap bmp4) {
          this.gameView = gameView;
          this.bmp1 = bmp;
          this.bmp2 = bmp2;
          this.bmp3 = bmp3;
          this.bmp4 = bmp4;
          
          this.level = level;
          this.type = type;
          
          this.x = sX;
          this.y = sY;    
          
          if (type == 0){
        	  life = 2;
          }
          
          if (level == 15){
        	  speedPerFrame = 5;
          }
           
    }

	private void update(int fX, int fY) {
		
		dx = fX - x;
  	  	dy = fY - y;
  	  	
  	  	if (Math.abs(dx) > Math.abs(dy)){
  	  		if (dx < 0){
  	  			bmp = bmp1;
  	  			width = bmp.getWidth();
  	  			height = bmp.getHeight();
  	  		} else {
  	  			bmp = bmp4;
  	  			width = bmp.getWidth();
  	  			height = bmp.getHeight();
  	  		}
  	  	} else {
  	  		if (dy < 0){
  	  			bmp = bmp3;
  	  			width = bmp.getWidth();
  	  			height = bmp.getHeight();
  	  		} else {
  	  			bmp = bmp2;
  	  			width = bmp.getWidth();
  	  			height = bmp.getHeight();
  	  		}
  	  	}
  	  
  	  	double ratio = speedPerFrame/Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
  	  
  	  	xSpeed = (int) (dx*ratio);
  	  	ySpeed = (int) (dy*ratio);
    	  
        x = x + xSpeed;
        y = y + ySpeed;
    }

    public void onDraw(Canvas canvas, int fX, int fY) {
          update(fX, fY);
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
    
    public int getLife(){
    	return life;
    }
    
    public void destroyLife(int dl){
    	life -= dl;
    }
    
    
}
