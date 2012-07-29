package com.junyangwang.monkeyrescue;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class SuperLion {
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
    private int level;
    private int type;
    private int life;
    
    private boolean xAvoid = false;
    private boolean yAvoid = false;
    
    private int avoidBanana = -1;
    private int detectDistance = 50; //can change with higher level
    private int avoidSpeed = 10; //can change with higher level
    
    private int speedPerFrame = 5; //4 is fast enough, 3 is easier
    
    public SuperLion(GameView gameView, Bitmap bmp, int sX, int sY, int level, int type) {
          this.gameView = gameView;
          this.bmp = bmp;
  		  this.width = bmp.getWidth();
  		  this.height = bmp.getHeight();
  		  
          this.level = level;
          this.type = type;
          
          this.x = sX;
          this.y = sY;    
          
          if (type == 0){
        	  life = 3;
          }
          
          if (level == 15){
        	  speedPerFrame = 5;
          }
           
    }

	private void update(int fX, int fY, int[] x2, int[] y2) {
		
		dx = fX - x;
  	  	dy = fY - y;
  	  
  	  	double ratio = speedPerFrame/Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
  	  	
  	  	if (this.isAvoidDone()){
	  	  	xSpeed = (int) (dx*ratio);
	  	  	ySpeed = (int) (dy*ratio);
  	  	}
  	  	
  	  	if (x2 != null || y2 != null){
  	  		int[] dx2 = new int[x2.length];
  	  	 	int[] dy2 = new int[x2.length];
  	  		
  	  	 	//always update the new bananas x and y
  	  		for (int i = 0; i < x2.length; i++){
		        dx2[i] = x2[i] - x;
		        dy2[i] = y2[i] - y;
  	  		}
  	  		
	  	  	if (this.isAvoidDone()){ //only detect the first one, and will detect again until the first banana has been avoided. so it will NOT go to detect the next banana until the first avoid is done
	  	  		for (int i = 0; i < x2.length; i++){
		  			if (Math.abs(dx2[i]) < detectDistance && Math.abs(dy2[i]) < detectDistance){
		  				avoidBanana = i;
		  				break; //only detect the first one within the detectDistance
		  			}
	  	  		}
	  	  	} 
  	  		
	  	  	if (avoidBanana >= 0){
	  	  		if (Math.abs(dx2[avoidBanana]) < detectDistance && Math.abs(dy2[avoidBanana]) < detectDistance){
		  	  		if (Math.abs(dx2[avoidBanana]) < Math.abs(dy2[avoidBanana]) && !yAvoid){
			        	if (dx2[avoidBanana] < 0){
			      	  		xSpeed = avoidSpeed;
			      	  	} else {
			      	  		xSpeed = -avoidSpeed;
			      	  	}
			        	xAvoid = true;
			      	} else if (!xAvoid){
				      	if (dy2[avoidBanana] < 0){
				  	  		ySpeed = avoidSpeed;
				  	  	} else {
				  	  		ySpeed = -avoidSpeed;
				  	  	}
				      	  	yAvoid = true;
			      	}
		  	  	} else {
		  	  		avoidBanana = -1;
			        xAvoid = false;
			        yAvoid = false;
			    }  
	  	  	}
	  	  	
  	  	} else {
  	  		avoidBanana = -1;
        	xAvoid = false;
        	yAvoid = false;
        }  
	        
  	  	
        x = x + xSpeed;
        y = y + ySpeed;
        
    }

    public void onDraw(Canvas canvas, int fX, int fY, int[] x2, int[] y2) {
          update(fX, fY, x2, y2);
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
    
    public boolean isAvoidDone(){
    	return !xAvoid && !yAvoid;
    }
    
    public void resetAvoidBananaAndAvoidDone(){
    	avoidBanana = -1;
        xAvoid = false;
        yAvoid = false;
    }
    
    public int getAvoidBanana(){
    	return avoidBanana;
    }
    
}
