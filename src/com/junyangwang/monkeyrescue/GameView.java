package com.junyangwang.monkeyrescue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.junyangwang.monkeyrescue.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;

public class GameView extends SurfaceView {
    private GameLoopThread gameLoopThread;
    private List<Snake> snakes = new ArrayList<Snake>();
    private List<Trap> traps = new ArrayList<Trap>();
    private List<Reward> rewards = new ArrayList<Reward>();
    private List<Banana> bananas = new ArrayList<Banana>();
    private List<SuperCrocodile> superCs = new ArrayList<SuperCrocodile>();
    private List<SuperLion> superLs = new ArrayList<SuperLion>();
    private Bait bait;
    private Home home;
    private long lastClick;
    private Bitmap bmpBackground;
    private Bitmap bmpBait;
    private Bitmap bmpSnake;
    private Bitmap bmpTrap1;
    private Bitmap bmpTrap2;
    private Bitmap bmpTrap3;
    private Bitmap bmpTrap4;
    private Bitmap bmpReward;
    private Bitmap bmpReward2;
    private Bitmap bmpReward3;
    private Bitmap bmpReward4;
    private Bitmap bmpHome;
    private Bitmap bmpSuperCrocodile;
    private Bitmap bmpSuperCrocodile2;
    private Bitmap bmpSuperCrocodile3;
    private Bitmap bmpSuperCrocodile4;
    private Bitmap bmpSuperLion;
    private int baitX;
    private int baitY;
    private int speedLevel = 0;
    private int level = 0;
    
    private boolean traped = false;
    private int score = 0;
    private int banana = 0;
    
    private Activity activity;
    private Context context;
    
    private int seconds = 100; //only 100 seconds for each game
    private int startTime;
    private int leftTime;

    public GameView(Context context, final int level) {
          super(context);
          
          activity = (Activity) context;
          this.context = context;
          this.level = level;
          
          gameLoopThread = new GameLoopThread(this);
          getHolder().addCallback(new SurfaceHolder.Callback() {

                 public void surfaceDestroyed(SurfaceHolder holder) {
                        boolean retry = true;
                        gameLoopThread.setRunning(false);
                        while (retry) {
                               try {
                                     gameLoopThread.join();
                                     retry = false;
                               } catch (InterruptedException e) {}
                        }
                 }

                 public void surfaceCreated(SurfaceHolder holder) {
                	    //set up Theme before we start the level
                	 	setUpTheme(level);
                	 	
                        createSnakes(level);
                        createTraps(level);
                        createRewards(level);
                        setUpHome(level);
                        setUpBait();
                        setStartTime();
                        
                        gameLoopThread.setRunning(true);
                        gameLoopThread.start();
                 }

				public void surfaceChanged(SurfaceHolder holder, int format,
                               int width, int height) {
                 }
          });
    }
    
    private void setUpTheme(int level) {
		// TODO Auto-generated method stub
		if (level < 5){
			bmpBackground = BitmapFactory.decodeResource(getResources(), R.drawable.bg);
			bmpTrap1 = BitmapFactory.decodeResource(getResources(), R.drawable.crocodile);
        } else if (level < 10){
        	bmpBackground = BitmapFactory.decodeResource(getResources(), R.drawable.bg2);
        	bmpTrap1 = BitmapFactory.decodeResource(getResources(), R.drawable.lion);
        } else if (level < 15){
        	bmpBackground = BitmapFactory.decodeResource(getResources(), R.drawable.bg3);
        	bmpTrap1 = BitmapFactory.decodeResource(getResources(), R.drawable.shark);
        } else if (level < 20){
        	bmpBackground = BitmapFactory.decodeResource(getResources(), R.drawable.bg4);
        } else {
        	bmpBackground = BitmapFactory.decodeResource(getResources(), R.drawable.bg4);
        	bmpTrap1 = BitmapFactory.decodeResource(getResources(), R.drawable.shark);
        }
		
		bmpBait = BitmapFactory.decodeResource(getResources(), R.drawable.marker);
        bmpSnake = BitmapFactory.decodeResource(getResources(), R.drawable.monkey1);
         
        bmpReward = BitmapFactory.decodeResource(getResources(), R.drawable.banana);
        bmpReward2 = BitmapFactory.decodeResource(getResources(), R.drawable.banana2);
        bmpReward3 = BitmapFactory.decodeResource(getResources(), R.drawable.banana3);
        bmpReward4 = BitmapFactory.decodeResource(getResources(), R.drawable.banana4);
        
        bmpHome = BitmapFactory.decodeResource(getResources(), R.drawable.home);
          
        bmpSuperCrocodile = BitmapFactory.decodeResource(getResources(), R.drawable.crocodile1);  
        bmpSuperCrocodile2 = BitmapFactory.decodeResource(getResources(), R.drawable.crocodile2);
        bmpSuperCrocodile3 = BitmapFactory.decodeResource(getResources(), R.drawable.crocodile3); 
        bmpSuperCrocodile4 = BitmapFactory.decodeResource(getResources(), R.drawable.crocodile4);
        
        bmpSuperLion = BitmapFactory.decodeResource(getResources(), R.drawable.superlion);
	}
    
    private void setStartTime() {
		// TODO Auto-generated method stub
		startTime = (int) (System.currentTimeMillis()/1000);
	}
    
    private void setUpBait() {
		// TODO Auto-generated method stub
    	bait = new Bait(this, bmpBait);
        baitX = 200;
        baitY = this.getHeight()/2;
	}
    
    private void setUpHome(int level){
    	home = new Home(this, bmpHome, this.getWidth() - bmpHome.getWidth(), this.getHeight()/2 - bmpHome.getHeight()/2);
    }

    private void createSnakes(int level) {
    	if (level < 5){
    		speedLevel = 0;
    	} else if (level < 10){
    		speedLevel = 1;
    	} else if (level < 15){
    		speedLevel = 2;
    	} else if (level < 20){
    		speedLevel = 3;
    	} 
    	snakes.add(new Snake(this, bmpSnake, bmpSnake.getWidth()/2, this.getHeight()/2, speedLevel));
    	snakes.add(new Snake(this, bmpSnake, bmpSnake.getWidth()/2 - bmpSnake.getWidth(), this.getHeight()/2, speedLevel));
    	snakes.add(new Snake(this, bmpSnake, bmpSnake.getWidth()/2 - bmpSnake.getWidth()*2, this.getHeight()/2, speedLevel));
    	snakes.add(new Snake(this, bmpSnake, bmpSnake.getWidth()/2 - bmpSnake.getWidth()*3, this.getHeight()/2, speedLevel));
    	snakes.add(new Snake(this, bmpSnake, bmpSnake.getWidth()/2 - bmpSnake.getWidth()*4, this.getHeight()/2, speedLevel));
    	snakes.add(new Snake(this, bmpSnake, bmpSnake.getWidth()/2 - bmpSnake.getWidth()*5, this.getHeight()/2, speedLevel));
    }
    
    private void createTraps(int level) {
    	
    	if (level == 0){
	    	traps.add(new Trap(this, bmpTrap1, 150*this.getWidth()/800, 0, level));
	    	traps.add(new Trap(this, bmpTrap1, 150*this.getWidth()/800, bmpTrap1.getHeight(), level));
	    	traps.add(new Trap(this, bmpTrap1, 150*this.getWidth()/800, bmpTrap1.getHeight()*2, level));
	    	traps.add(new Trap(this, bmpTrap1, 150*this.getWidth()/800, bmpTrap1.getHeight()*3, level));
	    	
	    	traps.add(new Trap(this, bmpTrap1, 300*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*6, level));
	    	traps.add(new Trap(this, bmpTrap1, 300*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*5, level));
	    	traps.add(new Trap(this, bmpTrap1, 300*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 300*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*3, level));
	    	traps.add(new Trap(this, bmpTrap1, 300*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*2, level));
	    	traps.add(new Trap(this, bmpTrap1, 300*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight(), level));
	    	
	    	traps.add(new Trap(this, bmpTrap1, 500*this.getWidth()/800, 0, level));
	    	traps.add(new Trap(this, bmpTrap1, 500*this.getWidth()/800, bmpTrap1.getHeight(), level));
	    	traps.add(new Trap(this, bmpTrap1, 500*this.getWidth()/800, bmpTrap1.getHeight()*2, level));
	    	traps.add(new Trap(this, bmpTrap1, 500*this.getWidth()/800, bmpTrap1.getHeight()*3, level));
	    	traps.add(new Trap(this, bmpTrap1, 500*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	
	    	traps.add(new Trap(this, bmpTrap1, 600*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*7, level));
	    	traps.add(new Trap(this, bmpTrap1, 600*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*6, level));
	    	traps.add(new Trap(this, bmpTrap1, 600*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*5, level));
	    	traps.add(new Trap(this, bmpTrap1, 600*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 600*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*3, level));
	    	traps.add(new Trap(this, bmpTrap1, 600*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*2, level));
	    	traps.add(new Trap(this, bmpTrap1, 600*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight(), level));
    	} else if (level == 1){
    		traps.add(new Trap(this, bmpTrap1, 150*this.getWidth()/800, 0, level));
	    	traps.add(new Trap(this, bmpTrap1, 150*this.getWidth()/800, bmpTrap1.getHeight(), level));
	    	traps.add(new Trap(this, bmpTrap1, 150*this.getWidth()/800, bmpTrap1.getHeight()*2, level));
	    	traps.add(new Trap(this, bmpTrap1, 150*this.getWidth()/800, bmpTrap1.getHeight()*3, level));
	    	traps.add(new Trap(this, bmpTrap1, 150*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 150*this.getWidth()/800, bmpTrap1.getHeight()*5, level));
	    	traps.add(new Trap(this, bmpTrap1, 150*this.getWidth()/800, bmpTrap1.getHeight()*6, level));
	    	
	    	traps.add(new Trap(this, bmpTrap1, 350*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*8, level));
	    	traps.add(new Trap(this, bmpTrap1, 300*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*8, level));
	    	traps.add(new Trap(this, bmpTrap1, 300*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*7, level));
	    	traps.add(new Trap(this, bmpTrap1, 300*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*6, level));
	    	traps.add(new Trap(this, bmpTrap1, 300*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*5, level));
	    	traps.add(new Trap(this, bmpTrap1, 300*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 300*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*3, level));
	    	traps.add(new Trap(this, bmpTrap1, 300*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*2, level));
	    	traps.add(new Trap(this, bmpTrap1, 300*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight(), level));
	    	
	    	traps.add(new Trap(this, bmpTrap1, 500*this.getWidth()/800, 0, level));
	    	traps.add(new Trap(this, bmpTrap1, 500*this.getWidth()/800, bmpTrap1.getHeight(), level));
	    	traps.add(new Trap(this, bmpTrap1, 500*this.getWidth()/800, bmpTrap1.getHeight()*2, level));
	    	traps.add(new Trap(this, bmpTrap1, 500*this.getWidth()/800, bmpTrap1.getHeight()*3, level));
	    	traps.add(new Trap(this, bmpTrap1, 550*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 550*this.getWidth()/800, bmpTrap1.getHeight()*5, level));
	    	traps.add(new Trap(this, bmpTrap1, 550*this.getWidth()/800, bmpTrap1.getHeight()*6, level));
	    	
	    	traps.add(new Trap(this, bmpTrap1, 650*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*7, level));
	    	traps.add(new Trap(this, bmpTrap1, 650*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*6, level));
	    	traps.add(new Trap(this, bmpTrap1, 650*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*5, level));
	    	traps.add(new Trap(this, bmpTrap1, 650*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 650*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*3, level));
	    	traps.add(new Trap(this, bmpTrap1, 650*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*2, level));
	    	traps.add(new Trap(this, bmpTrap1, 650*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight(), level));
    	} else if (level == 2){
    		traps.add(new Trap(this, bmpTrap1, 150*this.getWidth()/800, 0, level));
	    	traps.add(new Trap(this, bmpTrap1, 150*this.getWidth()/800, bmpTrap1.getHeight(), level));
	    	traps.add(new Trap(this, bmpTrap1, 150*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 150*this.getWidth()/800, bmpTrap1.getHeight()*5, level));
	    	traps.add(new Trap(this, bmpTrap1, 150*this.getWidth()/800, bmpTrap1.getHeight()*6, level));
	    	
	    	traps.add(new Trap(this, bmpTrap1, 300*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*8, level));
	    	traps.add(new Trap(this, bmpTrap1, 300*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*7, level));
	    	traps.add(new Trap(this, bmpTrap1, 300*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*6, level));
	    	traps.add(new Trap(this, bmpTrap1, 300*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 300*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*3, level));
	    	traps.add(new Trap(this, bmpTrap1, 300*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*2, level));
	    	traps.add(new Trap(this, bmpTrap1, 300*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight(), level));
	    	
	    	traps.add(new Trap(this, bmpTrap1, 450*this.getWidth()/800, 0, level));
	    	traps.add(new Trap(this, bmpTrap1, 450*this.getWidth()/800, bmpTrap1.getHeight(), level));
	    	traps.add(new Trap(this, bmpTrap1, 500*this.getWidth()/800, bmpTrap1.getHeight(), level));
	    	traps.add(new Trap(this, bmpTrap1, 450*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 450*this.getWidth()/800, bmpTrap1.getHeight()*5, level));
	    	traps.add(new Trap(this, bmpTrap1, 450*this.getWidth()/800, bmpTrap1.getHeight()*6, level));
	    	
	    	traps.add(new Trap(this, bmpTrap1, 600*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*8, level));
	    	traps.add(new Trap(this, bmpTrap1, 600*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*7, level));
	    	traps.add(new Trap(this, bmpTrap1, 600*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*6, level));
	    	traps.add(new Trap(this, bmpTrap1, 600*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 600*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*3, level));
	    	traps.add(new Trap(this, bmpTrap1, 600*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*2, level));
	    	traps.add(new Trap(this, bmpTrap1, 600*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight(), level));
    	} else if (level == 3){
    		traps.add(new Trap(this, bmpTrap1, 150*this.getWidth()/800, 0, level));
	    	traps.add(new Trap(this, bmpTrap1, 150*this.getWidth()/800, bmpTrap1.getHeight(), level));
	    	traps.add(new Trap(this, bmpTrap1, 150*this.getWidth()/800, bmpTrap1.getHeight()*2, level));
	    	traps.add(new Trap(this, bmpTrap1, 150*this.getWidth()/800, bmpTrap1.getHeight()*3, level));
	    	traps.add(new Trap(this, bmpTrap1, 150*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 150*this.getWidth()/800, bmpTrap1.getHeight()*5, level));
	    	traps.add(new Trap(this, bmpTrap1, 150*this.getWidth()/800, bmpTrap1.getHeight()*6, level));
	    	traps.add(new Trap(this, bmpTrap1, 150*this.getWidth()/800, bmpTrap1.getHeight()*7, level));
	    	
	    	traps.add(new Trap(this, bmpTrap1, 300*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*8, level));
	    	traps.add(new Trap(this, bmpTrap1, 300*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*7, level));
	    	traps.add(new Trap(this, bmpTrap1, 300*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*6, level));
	    	traps.add(new Trap(this, bmpTrap1, 300*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*5, level));
	    	traps.add(new Trap(this, bmpTrap1, 300*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 300*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*3, level));
	    	traps.add(new Trap(this, bmpTrap1, 300*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*2, level));
	    	traps.add(new Trap(this, bmpTrap1, 300*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight(), level));
	    	
	    	traps.add(new Trap(this, bmpTrap1, 450*this.getWidth()/800, 0, level));
	    	traps.add(new Trap(this, bmpTrap1, 450*this.getWidth()/800, bmpTrap1.getHeight(), level));
	    	traps.add(new Trap(this, bmpTrap1, 450*this.getWidth()/800, bmpTrap1.getHeight()*2, level));
	    	traps.add(new Trap(this, bmpTrap1, 450*this.getWidth()/800, bmpTrap1.getHeight()*3, level));
	    	traps.add(new Trap(this, bmpTrap1, 450*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 450*this.getWidth()/800, bmpTrap1.getHeight()*5, level));
	    	traps.add(new Trap(this, bmpTrap1, 450*this.getWidth()/800, bmpTrap1.getHeight()*6, level));
	    	traps.add(new Trap(this, bmpTrap1, 450*this.getWidth()/800, bmpTrap1.getHeight()*7, level));
	    	traps.add(new Trap(this, bmpTrap1, 500*this.getWidth()/800, bmpTrap1.getHeight()*7, level));
	    	
	    	traps.add(new Trap(this, bmpTrap1, 600*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*8, level));
	    	traps.add(new Trap(this, bmpTrap1, 600*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*7, level));
	    	traps.add(new Trap(this, bmpTrap1, 600*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*6, level));
	    	traps.add(new Trap(this, bmpTrap1, 600*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*5, level));
	    	traps.add(new Trap(this, bmpTrap1, 650*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 700*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*3, level));
	    	traps.add(new Trap(this, bmpTrap1, 750*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight()*2, level));
	    	traps.add(new Trap(this, bmpTrap1, 750*this.getWidth()/800, this.getHeight() - bmpTrap1.getHeight(), level));
    	} else if (level == 4){
    		traps.add(new Trap(this, bmpTrap1, 200*this.getWidth()/800, bmpTrap1.getHeight(), level));
	    	traps.add(new Trap(this, bmpTrap1, 200*this.getWidth()/800, bmpTrap1.getHeight()*2, level));
	    	traps.add(new Trap(this, bmpTrap1, 200*this.getWidth()/800, bmpTrap1.getHeight()*3, level));
	    	traps.add(new Trap(this, bmpTrap1, 100*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 150*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 200*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 200*this.getWidth()/800, bmpTrap1.getHeight()*5, level));
	    	traps.add(new Trap(this, bmpTrap1, 200*this.getWidth()/800, bmpTrap1.getHeight()*6, level));
	    	traps.add(new Trap(this, bmpTrap1, 200*this.getWidth()/800, bmpTrap1.getHeight()*7, level));
	    	traps.add(new Trap(this, bmpTrap1, 200*this.getWidth()/800, bmpTrap1.getHeight()*8, level));
	    	
	    	traps.add(new Trap(this, bmpTrap1, 350*this.getWidth()/800, bmpTrap1.getHeight(), level));
	    	traps.add(new Trap(this, bmpTrap1, 350*this.getWidth()/800, bmpTrap1.getHeight()*2, level));
	    	traps.add(new Trap(this, bmpTrap1, 400*this.getWidth()/800, bmpTrap1.getHeight()*2, level));
	    	traps.add(new Trap(this, bmpTrap1, 450*this.getWidth()/800, bmpTrap1.getHeight()*2, level));
	    	traps.add(new Trap(this, bmpTrap1, 350*this.getWidth()/800, bmpTrap1.getHeight()*3, level));
	    	traps.add(new Trap(this, bmpTrap1, 450*this.getWidth()/800, bmpTrap1.getHeight()*3, level));
	    	traps.add(new Trap(this, bmpTrap1, 300*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 350*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 400*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 450*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 500*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	
	    	traps.add(new Trap(this, bmpTrap1, 350*this.getWidth()/800, bmpTrap1.getHeight()*6, level));
	    	traps.add(new Trap(this, bmpTrap1, 400*this.getWidth()/800, bmpTrap1.getHeight()*6, level));
	    	traps.add(new Trap(this, bmpTrap1, 450*this.getWidth()/800, bmpTrap1.getHeight()*6, level));
	    	traps.add(new Trap(this, bmpTrap1, 500*this.getWidth()/800, bmpTrap1.getHeight()*6, level));
	    	traps.add(new Trap(this, bmpTrap1, 350*this.getWidth()/800, bmpTrap1.getHeight()*7, level));
	    	traps.add(new Trap(this, bmpTrap1, 450*this.getWidth()/800, bmpTrap1.getHeight()*7, level));
	    	traps.add(new Trap(this, bmpTrap1, 350*this.getWidth()/800, bmpTrap1.getHeight()*8, level));
	    	traps.add(new Trap(this, bmpTrap1, 400*this.getWidth()/800, bmpTrap1.getHeight()*8, level));
	    	traps.add(new Trap(this, bmpTrap1, 450*this.getWidth()/800, bmpTrap1.getHeight()*8, level));
	    	
	    	traps.add(new Trap(this, bmpTrap1, 600*this.getWidth()/800, 0, level));
	    	traps.add(new Trap(this, bmpTrap1, 600*this.getWidth()/800, bmpTrap1.getHeight(), level));
	    	traps.add(new Trap(this, bmpTrap1, 600*this.getWidth()/800, bmpTrap1.getHeight()*2, level));
	    	traps.add(new Trap(this, bmpTrap1, 600*this.getWidth()/800, bmpTrap1.getHeight()*3, level));
	    	traps.add(new Trap(this, bmpTrap1, 600*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 600*this.getWidth()/800, bmpTrap1.getHeight()*5, level));
	    	traps.add(new Trap(this, bmpTrap1, 600*this.getWidth()/800, bmpTrap1.getHeight()*6, level));
	    	traps.add(new Trap(this, bmpTrap1, 600*this.getWidth()/800, bmpTrap1.getHeight()*7, level));
	    	traps.add(new Trap(this, bmpTrap1, 700*this.getWidth()/800, bmpTrap1.getHeight()*3, level));
	    	traps.add(new Trap(this, bmpTrap1, 650*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 650*this.getWidth()/800, bmpTrap1.getHeight()*5, level));
	    	traps.add(new Trap(this, bmpTrap1, 650*this.getWidth()/800, bmpTrap1.getHeight()*6, level));
	    	traps.add(new Trap(this, bmpTrap1, 650*this.getWidth()/800, bmpTrap1.getHeight()*7, level));
    	} else if (level == 5){
	    	traps.add(new Trap(this, bmpTrap1, 100*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 200*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 300*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 400*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 500*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 600*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 700*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
    	} else if (level == 6){
	    	traps.add(new Trap(this, bmpTrap1, 100*this.getWidth()/800, bmpTrap1.getHeight()*3, level));
	    	traps.add(new Trap(this, bmpTrap1, 150*this.getWidth()/800, bmpTrap1.getHeight()*1, level));
	    	traps.add(new Trap(this, bmpTrap1, 200*this.getWidth()/800, bmpTrap1.getHeight()*2, level));
	    	traps.add(new Trap(this, bmpTrap1, 300*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 400*this.getWidth()/800, bmpTrap1.getHeight()*7, level));
	    	traps.add(new Trap(this, bmpTrap1, 500*this.getWidth()/800, bmpTrap1.getHeight()*2, level));
	    	traps.add(new Trap(this, bmpTrap1, 600*this.getWidth()/800, bmpTrap1.getHeight()*6, level));
	    	traps.add(new Trap(this, bmpTrap1, 650*this.getWidth()/800, bmpTrap1.getHeight()*1, level));
	    	traps.add(new Trap(this, bmpTrap1, 700*this.getWidth()/800, bmpTrap1.getHeight()*5, level));
    	} else if (level == 7){
    		traps.add(new Trap(this, bmpTrap1, 100*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 200*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 300*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 400*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 500*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 600*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 700*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
    	} else if (level == 8){
	    	traps.add(new Trap(this, bmpTrap1, 100*this.getWidth()/800, bmpTrap1.getHeight()*3, level));
	    	traps.add(new Trap(this, bmpTrap1, 150*this.getWidth()/800, bmpTrap1.getHeight()*1, level));
	    	traps.add(new Trap(this, bmpTrap1, 200*this.getWidth()/800, bmpTrap1.getHeight()*2, level));
	    	traps.add(new Trap(this, bmpTrap1, 300*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 400*this.getWidth()/800, bmpTrap1.getHeight()*7, level));
	    	traps.add(new Trap(this, bmpTrap1, 500*this.getWidth()/800, bmpTrap1.getHeight()*2, level));
	    	traps.add(new Trap(this, bmpTrap1, 600*this.getWidth()/800, bmpTrap1.getHeight()*6, level));
	    	traps.add(new Trap(this, bmpTrap1, 650*this.getWidth()/800, bmpTrap1.getHeight()*1, level));
	    	traps.add(new Trap(this, bmpTrap1, 700*this.getWidth()/800, bmpTrap1.getHeight()*5, level));
    	} else if (level == 9){
	    	traps.add(new Trap(this, bmpTrap1, 200*this.getWidth()/800, bmpTrap1.getHeight()*7, level));
	    	traps.add(new Trap(this, bmpTrap1, 250*this.getWidth()/800, bmpTrap1.getHeight()*6, level));
	    	traps.add(new Trap(this, bmpTrap1, 300*this.getWidth()/800, bmpTrap1.getHeight()*5, level));
	    	traps.add(new Trap(this, bmpTrap1, 350*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 400*this.getWidth()/800, bmpTrap1.getHeight()*3, level));
	    	traps.add(new Trap(this, bmpTrap1, 450*this.getWidth()/800, bmpTrap1.getHeight()*2, level));
	    	traps.add(new Trap(this, bmpTrap1, 500*this.getWidth()/800, bmpTrap1.getHeight()*1, level));
	    	traps.add(new Trap(this, bmpTrap1, 500*this.getWidth()/800, bmpTrap1.getHeight()*7, level));
	    	traps.add(new Trap(this, bmpTrap1, 550*this.getWidth()/800, bmpTrap1.getHeight()*6, level));
	    	traps.add(new Trap(this, bmpTrap1, 600*this.getWidth()/800, bmpTrap1.getHeight()*5, level));
	    	traps.add(new Trap(this, bmpTrap1, 650*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 700*this.getWidth()/800, bmpTrap1.getHeight()*3, level));
	    	traps.add(new Trap(this, bmpTrap1, 750*this.getWidth()/800, bmpTrap1.getHeight()*2, level));
	    	traps.add(new Trap(this, bmpTrap1, 800*this.getWidth()/800, bmpTrap1.getHeight()*1, level));
    	} else if (level == 10){
    		superCs.add(new SuperCrocodile(this, bmpSuperCrocodile, this.getWidth() - bmpSuperCrocodile.getWidth(), this.getHeight()/2 - bmpSuperCrocodile.getHeight()/2, level, 0, bmpSuperCrocodile2, bmpSuperCrocodile3, bmpSuperCrocodile4));
    	} else if (level == 11){
    		traps.add(new Trap(this, bmpTrap1, 100*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 200*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 300*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 400*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 500*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 600*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 700*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	superCs.add(new SuperCrocodile(this, bmpSuperCrocodile, this.getWidth() - bmpSuperCrocodile.getWidth(), this.getHeight()/2 - bmpSuperCrocodile.getHeight()/2, level, 0, bmpSuperCrocodile2, bmpSuperCrocodile3, bmpSuperCrocodile4));
    	} else if (level == 12){
    		traps.add(new Trap(this, bmpTrap1, 100*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 200*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 300*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 400*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 500*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 600*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 700*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	superCs.add(new SuperCrocodile(this, bmpSuperCrocodile, this.getWidth() - bmpSuperCrocodile.getWidth(), this.getHeight()/2 - bmpSuperCrocodile.getHeight()/2, level, 0, bmpSuperCrocodile2, bmpSuperCrocodile3, bmpSuperCrocodile4));
    	} else if (level == 13){
    		traps.add(new Trap(this, bmpTrap1, 100*this.getWidth()/800, bmpTrap1.getHeight()*3, level));
	    	traps.add(new Trap(this, bmpTrap1, 150*this.getWidth()/800, bmpTrap1.getHeight()*1, level));
	    	traps.add(new Trap(this, bmpTrap1, 200*this.getWidth()/800, bmpTrap1.getHeight()*2, level));
	    	traps.add(new Trap(this, bmpTrap1, 300*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 400*this.getWidth()/800, bmpTrap1.getHeight()*7, level));
	    	traps.add(new Trap(this, bmpTrap1, 500*this.getWidth()/800, bmpTrap1.getHeight()*2, level));
	    	traps.add(new Trap(this, bmpTrap1, 600*this.getWidth()/800, bmpTrap1.getHeight()*6, level));
	    	traps.add(new Trap(this, bmpTrap1, 650*this.getWidth()/800, bmpTrap1.getHeight()*1, level));
	    	traps.add(new Trap(this, bmpTrap1, 700*this.getWidth()/800, bmpTrap1.getHeight()*5, level));
	    	superCs.add(new SuperCrocodile(this, bmpSuperCrocodile, this.getWidth() - bmpSuperCrocodile.getWidth(), this.getHeight()/2 - bmpSuperCrocodile.getHeight()/2 - 100, level, 0, bmpSuperCrocodile2, bmpSuperCrocodile3, bmpSuperCrocodile4));
	    	superCs.add(new SuperCrocodile(this, bmpSuperCrocodile, this.getWidth() - bmpSuperCrocodile.getWidth(), this.getHeight()/2 - bmpSuperCrocodile.getHeight()/2 + 100, level, 0, bmpSuperCrocodile2, bmpSuperCrocodile3, bmpSuperCrocodile4));
    	} else if (level == 14){
    		traps.add(new Trap(this, bmpTrap1, 100*this.getWidth()/800, bmpTrap1.getHeight()*3, level));
	    	traps.add(new Trap(this, bmpTrap1, 150*this.getWidth()/800, bmpTrap1.getHeight()*1, level));
	    	traps.add(new Trap(this, bmpTrap1, 200*this.getWidth()/800, bmpTrap1.getHeight()*2, level));
	    	traps.add(new Trap(this, bmpTrap1, 300*this.getWidth()/800, bmpTrap1.getHeight()*4, level));
	    	traps.add(new Trap(this, bmpTrap1, 400*this.getWidth()/800, bmpTrap1.getHeight()*7, level));
	    	traps.add(new Trap(this, bmpTrap1, 500*this.getWidth()/800, bmpTrap1.getHeight()*2, level));
	    	traps.add(new Trap(this, bmpTrap1, 600*this.getWidth()/800, bmpTrap1.getHeight()*6, level));
	    	traps.add(new Trap(this, bmpTrap1, 650*this.getWidth()/800, bmpTrap1.getHeight()*1, level));
	    	traps.add(new Trap(this, bmpTrap1, 700*this.getWidth()/800, bmpTrap1.getHeight()*5, level));
	    	superCs.add(new SuperCrocodile(this, bmpSuperCrocodile, this.getWidth() - bmpSuperCrocodile.getWidth(), this.getHeight()/2 - bmpSuperCrocodile.getHeight()/2 - 100, level, 0, bmpSuperCrocodile2, bmpSuperCrocodile3, bmpSuperCrocodile4));
	    	superCs.add(new SuperCrocodile(this, bmpSuperCrocodile, this.getWidth() - bmpSuperCrocodile.getWidth(), this.getHeight()/2 - bmpSuperCrocodile.getHeight()/2, level, 0, bmpSuperCrocodile2, bmpSuperCrocodile3, bmpSuperCrocodile4));
	    	superCs.add(new SuperCrocodile(this, bmpSuperCrocodile, this.getWidth() - bmpSuperCrocodile.getWidth(), this.getHeight()/2 - bmpSuperCrocodile.getHeight()/2 + 100, level, 0, bmpSuperCrocodile2, bmpSuperCrocodile3, bmpSuperCrocodile4));
    	} else if (level == 15){
    		superLs.add(new SuperLion(this, bmpSuperLion, this.getWidth() - bmpSuperLion.getWidth(), this.getHeight()/2 - bmpSuperLion.getHeight()/2, level, 0));
    	} else if (level == 16){
    		superCs.add(new SuperCrocodile(this, bmpSuperCrocodile, this.getWidth() - bmpSuperCrocodile.getWidth(), this.getHeight()/2 - bmpSuperCrocodile.getHeight()/2 - 100, level, 0, bmpSuperCrocodile2, bmpSuperCrocodile3, bmpSuperCrocodile4));
    		superLs.add(new SuperLion(this, bmpSuperLion, this.getWidth() - bmpSuperLion.getWidth(), this.getHeight()/2 - bmpSuperLion.getHeight()/2 + 100, level, 0));
    	} else if (level == 17){
    		superCs.add(new SuperCrocodile(this, bmpSuperCrocodile, this.getWidth() - bmpSuperCrocodile.getWidth(), this.getHeight()/2 - bmpSuperCrocodile.getHeight()/2 - 100, level, 0, bmpSuperCrocodile2, bmpSuperCrocodile3, bmpSuperCrocodile4));
    		superLs.add(new SuperLion(this, bmpSuperLion, this.getWidth() - bmpSuperLion.getWidth(), this.getHeight()/2 - bmpSuperLion.getHeight()/2, level, 0));
    		superCs.add(new SuperCrocodile(this, bmpSuperCrocodile, this.getWidth() - bmpSuperCrocodile.getWidth(), this.getHeight()/2 - bmpSuperCrocodile.getHeight()/2 + 100, level, 0, bmpSuperCrocodile2, bmpSuperCrocodile3, bmpSuperCrocodile4));
    	} else if (level == 18){
    		superCs.add(new SuperCrocodile(this, bmpSuperCrocodile, this.getWidth() - bmpSuperCrocodile.getWidth(), this.getHeight()/2 - bmpSuperCrocodile.getHeight()/2 - 150, level, 0, bmpSuperCrocodile2, bmpSuperCrocodile3, bmpSuperCrocodile4));
    		superLs.add(new SuperLion(this, bmpSuperLion, this.getWidth() - bmpSuperLion.getWidth(), this.getHeight()/2 - bmpSuperLion.getHeight()/2 - 100, level, 0));
    		superLs.add(new SuperLion(this, bmpSuperLion, this.getWidth() - bmpSuperLion.getWidth(), this.getHeight()/2 - bmpSuperLion.getHeight()/2 + 100, level, 0));
    		superCs.add(new SuperCrocodile(this, bmpSuperCrocodile, this.getWidth() - bmpSuperCrocodile.getWidth(), this.getHeight()/2 - bmpSuperCrocodile.getHeight()/2 + 150, level, 0, bmpSuperCrocodile2, bmpSuperCrocodile3, bmpSuperCrocodile4));
    	} else if (level == 19){
    		superCs.add(new SuperCrocodile(this, bmpSuperCrocodile, this.getWidth() - bmpSuperCrocodile.getWidth(), this.getHeight()/2 - bmpSuperCrocodile.getHeight()/2 - 150, level, 0, bmpSuperCrocodile2, bmpSuperCrocodile3, bmpSuperCrocodile4));
    		superLs.add(new SuperLion(this, bmpSuperLion, this.getWidth() - bmpSuperLion.getWidth(), this.getHeight()/2 - bmpSuperLion.getHeight()/2 - 100, level, 0));
    		superLs.add(new SuperLion(this, bmpSuperLion, this.getWidth() - bmpSuperLion.getWidth(), this.getHeight()/2 - bmpSuperLion.getHeight()/2, level, 0));
    		superLs.add(new SuperLion(this, bmpSuperLion, this.getWidth() - bmpSuperLion.getWidth(), this.getHeight()/2 - bmpSuperLion.getHeight()/2 + 100, level, 0));
    		superCs.add(new SuperCrocodile(this, bmpSuperCrocodile, this.getWidth() - bmpSuperCrocodile.getWidth(), this.getHeight()/2 - bmpSuperCrocodile.getHeight()/2 + 150, level, 0, bmpSuperCrocodile2, bmpSuperCrocodile3, bmpSuperCrocodile4));
    	} 
    }
    
    private void createRewards(int level) {
    	Random rnd = new Random();
        
    	if (level < 5) {
    		rewards.add(new Reward(this, bmpReward, rnd.nextInt(this.getWidth() - bmpReward.getWidth()), rnd.nextInt(this.getHeight() - bmpReward.getHeight()), level));
        	rewards.add(new Reward(this, bmpReward, rnd.nextInt(this.getWidth() - bmpReward.getWidth()), rnd.nextInt(this.getHeight() - bmpReward.getHeight()), level));
    	} else if (level < 10){
    		rewards.add(new Reward(this, bmpReward, rnd.nextInt(this.getWidth() - bmpReward.getWidth()), rnd.nextInt(this.getHeight() - bmpReward.getHeight()), level));
        	rewards.add(new Reward(this, bmpReward, rnd.nextInt(this.getWidth() - bmpReward.getWidth()), rnd.nextInt(this.getHeight() - bmpReward.getHeight()), level));
        	rewards.add(new Reward(this, bmpReward, rnd.nextInt(this.getWidth() - bmpReward.getWidth()), rnd.nextInt(this.getHeight() - bmpReward.getHeight()), level));
        	rewards.add(new Reward(this, bmpReward, rnd.nextInt(this.getWidth() - bmpReward.getWidth()), rnd.nextInt(this.getHeight() - bmpReward.getHeight()), level));
        	rewards.add(new Reward(this, bmpReward, rnd.nextInt(this.getWidth() - bmpReward.getWidth()), rnd.nextInt(this.getHeight() - bmpReward.getHeight()), level));
    	} else if (level < 15){
    		rewards.add(new Reward(this, bmpReward, rnd.nextInt(this.getWidth() - bmpReward.getWidth()), rnd.nextInt(this.getHeight() - bmpReward.getHeight()), level));
        	rewards.add(new Reward(this, bmpReward, rnd.nextInt(this.getWidth() - bmpReward.getWidth()), rnd.nextInt(this.getHeight() - bmpReward.getHeight()), level));
        	rewards.add(new Reward(this, bmpReward, rnd.nextInt(this.getWidth() - bmpReward.getWidth()), rnd.nextInt(this.getHeight() - bmpReward.getHeight()), level));
        	rewards.add(new Reward(this, bmpReward, rnd.nextInt(this.getWidth() - bmpReward.getWidth()), rnd.nextInt(this.getHeight() - bmpReward.getHeight()), level));
        	rewards.add(new Reward(this, bmpReward, rnd.nextInt(this.getWidth() - bmpReward.getWidth()), rnd.nextInt(this.getHeight() - bmpReward.getHeight()), level));
        	rewards.add(new Reward(this, bmpReward, rnd.nextInt(this.getWidth() - bmpReward.getWidth()), rnd.nextInt(this.getHeight() - bmpReward.getHeight()), level));
        	rewards.add(new Reward(this, bmpReward, rnd.nextInt(this.getWidth() - bmpReward.getWidth()), rnd.nextInt(this.getHeight() - bmpReward.getHeight()), level));
        	rewards.add(new Reward(this, bmpReward, rnd.nextInt(this.getWidth() - bmpReward.getWidth()), rnd.nextInt(this.getHeight() - bmpReward.getHeight()), level));
        	rewards.add(new Reward(this, bmpReward, rnd.nextInt(this.getWidth() - bmpReward.getWidth()), rnd.nextInt(this.getHeight() - bmpReward.getHeight()), level));
        	rewards.add(new Reward(this, bmpReward, rnd.nextInt(this.getWidth() - bmpReward.getWidth()), rnd.nextInt(this.getHeight() - bmpReward.getHeight()), level));
    	} else if (level < 20){
    		rewards.add(new Reward(this, bmpReward, rnd.nextInt(this.getWidth() - bmpReward.getWidth()), rnd.nextInt(this.getHeight() - bmpReward.getHeight()), level));
        	rewards.add(new Reward(this, bmpReward, rnd.nextInt(this.getWidth() - bmpReward.getWidth()), rnd.nextInt(this.getHeight() - bmpReward.getHeight()), level));
        	rewards.add(new Reward(this, bmpReward, rnd.nextInt(this.getWidth() - bmpReward.getWidth()), rnd.nextInt(this.getHeight() - bmpReward.getHeight()), level));
        	rewards.add(new Reward(this, bmpReward, rnd.nextInt(this.getWidth() - bmpReward.getWidth()), rnd.nextInt(this.getHeight() - bmpReward.getHeight()), level));
        	rewards.add(new Reward(this, bmpReward, rnd.nextInt(this.getWidth() - bmpReward.getWidth()), rnd.nextInt(this.getHeight() - bmpReward.getHeight()), level));
        	rewards.add(new Reward(this, bmpReward, rnd.nextInt(this.getWidth() - bmpReward.getWidth()), rnd.nextInt(this.getHeight() - bmpReward.getHeight()), level));
        	rewards.add(new Reward(this, bmpReward, rnd.nextInt(this.getWidth() - bmpReward.getWidth()), rnd.nextInt(this.getHeight() - bmpReward.getHeight()), level));
        	rewards.add(new Reward(this, bmpReward, rnd.nextInt(this.getWidth() - bmpReward.getWidth()), rnd.nextInt(this.getHeight() - bmpReward.getHeight()), level));
        	rewards.add(new Reward(this, bmpReward, rnd.nextInt(this.getWidth() - bmpReward.getWidth()), rnd.nextInt(this.getHeight() - bmpReward.getHeight()), level));
        	rewards.add(new Reward(this, bmpReward, rnd.nextInt(this.getWidth() - bmpReward.getWidth()), rnd.nextInt(this.getHeight() - bmpReward.getHeight()), level));
        	rewards.add(new Reward(this, bmpReward, rnd.nextInt(this.getWidth() - bmpReward.getWidth()), rnd.nextInt(this.getHeight() - bmpReward.getHeight()), level));
        	rewards.add(new Reward(this, bmpReward, rnd.nextInt(this.getWidth() - bmpReward.getWidth()), rnd.nextInt(this.getHeight() - bmpReward.getHeight()), level));
        	rewards.add(new Reward(this, bmpReward, rnd.nextInt(this.getWidth() - bmpReward.getWidth()), rnd.nextInt(this.getHeight() - bmpReward.getHeight()), level));
        	rewards.add(new Reward(this, bmpReward, rnd.nextInt(this.getWidth() - bmpReward.getWidth()), rnd.nextInt(this.getHeight() - bmpReward.getHeight()), level));
        	rewards.add(new Reward(this, bmpReward, rnd.nextInt(this.getWidth() - bmpReward.getWidth()), rnd.nextInt(this.getHeight() - bmpReward.getHeight()), level));
        	rewards.add(new Reward(this, bmpReward, rnd.nextInt(this.getWidth() - bmpReward.getWidth()), rnd.nextInt(this.getHeight() - bmpReward.getHeight()), level));
    	} 	
    }

	@Override
    protected void onDraw(Canvas canvas) {
    	
    	if (snakes.size() <= 0){
    		//add some scores for the leftTime from last level game
    		score += leftTime*20;
    		
    		//save the game: level, score
    		saveLevel(level, score);
    		
    		//start a new level and a new score
    		level++;
    		score = 0;
    		banana = 0;
    		
    		snakes = new ArrayList<Snake>();
    		traps = new ArrayList<Trap>();
    		rewards = new ArrayList<Reward>();
    		bananas = new ArrayList<Banana>();
    		superCs = new ArrayList<SuperCrocodile>();
    		superLs = new ArrayList<SuperLion>();
    		
    		setUpTheme(level);
    		createSnakes(level);
            createTraps(level);
            createRewards(level);
            setUpHome(level);
            setUpBait();
            setStartTime();
    	}
    	
    	//draw background
    	canvas.drawBitmap(bmpBackground, 0, 0, null);
    	
    	//draw level and score
    	Paint text = new Paint();
    	text.setTextSize(30);
    	text.setColor(Color.DKGRAY);
    	text.setFakeBoldText(true);
    	canvas.drawText("level: " + level, 5, 30, text);
    	canvas.drawText("score: " + score, 5, 60, text);
    	canvas.drawText("banana: " + banana, 5, 90, text);
    	
    	//draw seconds left
    	int finishTime = (int) (System.currentTimeMillis()/1000);
    	leftTime = seconds - (finishTime - startTime);
    	canvas.drawText("time left: " + leftTime + "s", 5, this.getHeight() - 5, text);
    	
    	//check if there is no time left
    	if (leftTime <= 0){
    		gameLoopThread.setRunning(false);
			
			//show the alertDialog options
			activity.runOnUiThread(new Runnable(){
				public void run(){
					gameLoopThread.setRunning(false);
					showAlertDialog(1);
				}
			});
    	}
        
    	//draw Home
    	home.onDraw(canvas);
    	
    	//draw bait
    	bait.onDraw(canvas, baitX, baitY);
         
    	//draw the first snake
    	snakes.get(0).onDraw(canvas, baitX, baitY);
          
    	//draw the rest snakes
    	for (int i = 1; i <= snakes.size() - 1; i++) {
    		Snake snakeBefore = snakes.get(i - 1);
    		Snake snake = snakes.get(i);
    		snake.onDrawBefore(canvas, snakeBefore.getX(), snakeBefore.getY());
    	}
    	
    	//draw rewards
    	for (int i = 0; i <= rewards.size() - 1; i++) {
    		Reward reward = rewards.get(i);
    		reward.onDraw(canvas);
    		
    		//check if snakes hit the reward
    		boolean rewardRemoved = false;
    		for (int j = 0; j <= snakes.size() - 1; j++) {
    			if (reward.isCollision(snakes.get(j).getX(), snakes.get(j).getY())){
    				rewards.remove(reward);
    				rewardRemoved = true;
    				banana++;
        			score += 10;
        			break;
    			}
    		}
    		
    		//break the rewards loop
    		if(rewardRemoved){
    			break;
    		}
    		
    	}
    	
    	//draw traps
    	for (int i = 0; i <= traps.size() - 1; i++) {
    		Trap trap = traps.get(i);
    		trap.onDraw(canvas);
    		
    		//check if snakes hit the trap
    		for (int j = 0; j <= snakes.size() - 1; j++) {
    			if (trap.isCollision(snakes.get(j).getX(), snakes.get(j).getY())){
    				gameLoopThread.setRunning(false);
    				
    				//show the alertDialog options
    				activity.runOnUiThread(new Runnable(){
    					public void run(){
    						gameLoopThread.setRunning(false);
    						showAlertDialog(0);
    					}
    				});
    			}
    		}
    		
    	}
    	
    	//draw SuperCrocodiles
    	for (int i = 0; i <= superCs.size() - 1; i++){
    		SuperCrocodile superC = superCs.get(i);
    		superC.onDraw(canvas, snakes.get(0).getX(), snakes.get(0).getY());
    		
    		//check if snakes hit the superC
    		for (int j = 0; j <= snakes.size() - 1; j++) {
    			if (superC.isCollision(snakes.get(j).getX(), snakes.get(j).getY())){
    				gameLoopThread.setRunning(false);
    				
    				//show the alertDialog options
    				activity.runOnUiThread(new Runnable(){
    					public void run(){
    						gameLoopThread.setRunning(false);
    						showAlertDialog(2);
    					}
    				});
    			}
    		}
    		
    	}
    	
    	//draw SuperLions
    	for (int i = 0; i <= superLs.size() - 1; i++){
    		SuperLion superL = superLs.get(i);
    		
    		//need to check every banana in order to avoid them
    		if (bananas.size() > 0){
    			int[] bananaXs = new int[bananas.size()], bananaYs = new int[bananas.size()];
    			
    			//put all the x and y of bananas into superL update method, then it will pick the first pair within detectDistance
	    		for (int j = 0; j <= bananas.size() - 1; j++){
	    			bananaXs[j] = bananas.get(j).getX();
	    			bananaYs[j] = bananas.get(j).getY();
	    		}
	    		
	    		superL.onDraw(canvas, snakes.get(0).getX(), snakes.get(0).getY(), bananaXs, bananaYs);
    		} else {
    			superL.onDraw(canvas, snakes.get(0).getX(), snakes.get(0).getY(), null, null);
    		}
    		
    		//check if snakes hit the superL
    		for (int j = 0; j <= snakes.size() - 1; j++) {
    			if (superL.isCollision(snakes.get(j).getX(), snakes.get(j).getY())){
    				gameLoopThread.setRunning(false);
    				
    				//show the alertDialog options
    				activity.runOnUiThread(new Runnable(){
    					public void run(){
    						gameLoopThread.setRunning(false);
    						showAlertDialog(3);
    					}
    				});
    			}
    		}
    		
    	}
    	
    	//draw bananas
    	for (int i = 0; i <= bananas.size() - 1; i++){
    		bananas.get(i).onDraw(canvas);
    		
    		boolean bananaRemoved = false;
    		//destroy traps
    		for (int j = 0; j <= traps.size() - 1; j++) {
        		Trap trap = traps.get(j);
    			if (trap.isCollision(bananas.get(i).getX(), bananas.get(i).getY())){
    				traps.remove(trap);
    				bananas.remove(bananas.get(i));
    				bananaRemoved = true;
    				
    				//avoid banana removed, but other superL is still waiting for this banana to be test for detectDistance
    				for (int k = 0; k <= superLs.size() - 1; k++){
        				if (superLs.get(k).getAvoidBanana() == i){
        					superLs.get(k).resetAvoidBananaAndAvoidDone();
        				}
    				}
    				
    				break;
    			}
    		}
    		
    		//destroy SuperCrocodiles
    		if (!bananaRemoved){
    			for (int j = 0; j <= superCs.size() - 1; j++){
    				SuperCrocodile superC = superCs.get(j);
          			if (superC.isCollision(bananas.get(i).getX(), bananas.get(i).getY())){
          			
          				//destroy superC's life
          				superC.destroyLife(1);
          			
	          			//after life destroyed, check if its life is less/equal to 0, if it is, remove it from superCs List.
	          			if (superC.getLife() <= 0){
	          				superCs.remove(superC);
	          			}
	          			
	          			//remove banana
	    				bananas.remove(bananas.get(i));
	    				bananaRemoved = true;
	    				
	    				//avoid banana removed, but other superL is still waiting for this banana to be test for detectDistance
	    				for (int k = 0; k <= superLs.size() - 1; k++){
	        				if (superLs.get(k).getAvoidBanana() == i){
	        					superLs.get(k).resetAvoidBananaAndAvoidDone();
	        				}
	    				}
	    				
	    				break;
	    			}
    			}
    		}
    		
    		//destroy SuperLions
    		if (!bananaRemoved){
    			for (int j = 0; j <= superLs.size() - 1; j++){
    				SuperLion superL = superLs.get(j);
          			if (superL.isCollision(bananas.get(i).getX(), bananas.get(i).getY())){
          			
          				//destroy superC's life
          				superL.destroyLife(1);
          			
	          			//after life destroyed, check if its life is less/equal to 0, if it is, remove it from superCs List.
	          			if (superL.getLife() <= 0){
	          				superLs.remove(superL);
	          			}
	          			
	          			//remove banana
	    				bananas.remove(bananas.get(i));
	    				bananaRemoved = true;
	    				
	    				//avoid banana removed, while other superL is still waiting for this banana to be test for detectDistance
	    				for (int k = 0; k <= superLs.size() - 1; k++){
	        				if (superLs.get(k).getAvoidBanana() == i){
	        					superLs.get(k).resetAvoidBananaAndAvoidDone();
	        				}
	    				}
	    				
	    				break;
	    			}
    			}
    		}
    		
    		//break the bananas loop
    		if (bananaRemoved){
    			break;
    		}
    		
    	}
    	
    	//if the snake went home, snake will be removed from the list. ---> always put it at the end of onDraw()
    	for (int i = 0; i <= snakes.size() - 1; i++) {
    		Snake snake = snakes.get(i);
    		if (home.isCollision(snake.getX(), snake.getY())){
    			snakes.remove(snake);
    			break;
    		}
    	}
    	
	}
    

    private void showAlertDialog(int type) {
		// TODO Auto-generated method stub
    	AlertDialog alert = new AlertDialog.Builder(context).create();
		alert.setTitle("Game Over");
		if (type == 0){
			if (level < 5){
				alert.setMessage("Monkeys ran into some crocodiles.");
			} else if (level < 10){
				alert.setMessage("Monkeys ran into some lions.");
			} else if (level < 15){
				alert.setMessage("Monkeys ran into some sharks.");
			} else {
				alert.setMessage("Monkeys ran into some sharks.");
			}
		} else if (type == 1){
			alert.setMessage("No time left to rescue monkeys.");
		} else if (type == 2){
			alert.setMessage("Monkeys ran into SUPER crocodiles.");
		} else if (type == 3){
			alert.setMessage("Monkeys ran into SUPER lions.");
		}
		alert.setButton("Play Again", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				startGameAgain();
			}
		});
		alert.setButton2("Go Back", new DialogInterface.OnClickListener() {
			
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				activity.finish(); //finish this current activity first
				Intent i = new Intent("android.intent.action.GAME");
				activity.startActivity(i); //start another new activity (the same intent though)
			}
		});
		alert.show();
	}
    
    public void startGameAgain() {
		// TODO Auto-generated method stub
		score = 0;
		banana = 0;
		
		snakes = new ArrayList<Snake>();
		traps = new ArrayList<Trap>();
		rewards = new ArrayList<Reward>();
		bananas = new ArrayList<Banana>();
		superCs = new ArrayList<SuperCrocodile>();
		superLs = new ArrayList<SuperLion>();
		
		setUpTheme(level);
		createSnakes(level);
        createTraps(level);
        createRewards(level);
        setUpHome(level);
        setUpBait();
        setStartTime();
        
        gameLoopThread = new GameLoopThread(this);
        gameLoopThread.setRunning(true);
        gameLoopThread.start();
	}

	private void saveLevel(int level2, int score2) {
		// TODO Auto-generated method stub
    	try {
			Database entry = new Database(context);
			entry.open();
			entry.createEntry(level2, score2);
			entry.close();
		} catch (Exception e){
			
		}
	}

	@Override
    public boolean onTouchEvent(MotionEvent event) {
          if (System.currentTimeMillis() - lastClick > 300) {
                 lastClick = System.currentTimeMillis();
                 int x = (int) event.getX();
                 int y = (int) event.getY();
                 synchronized (getHolder()) {
                	 
                	 boolean touchTrap = false;
                	 
                	 //shoot bananas at traps
                	 for (int i = 0; i <= traps.size() - 1; i++) {
                 		Trap trap = traps.get(i);
                 		if (trap.isCollision(x, y)){
                 			if (banana > 0){
                 				bananas.add(new Banana(this, bmpReward, snakes.get(0).getX(), snakes.get(0).getY(), x, y, bmpReward2, bmpReward3, bmpReward4));
                 				banana--;
                 			}
                 			touchTrap = true;
                 		}
                	 }
                	 
                	 //shoot bananas at SuperCrocodiles
                	 for (int i = 0; i <= superCs.size() - 1; i++){
                 		SuperCrocodile superC = superCs.get(i);
                 		if (superC.isCollision(x, y)){
                 			if (banana > 0){
                 				bananas.add(new Banana(this, bmpReward, snakes.get(0).getX(), snakes.get(0).getY(), x, y, bmpReward2, bmpReward3, bmpReward4));
                 				banana--;
                 			}
                 			touchTrap = true;
                 		}
                	 }
                	 
                	//shoot bananas at SuperLions
                	 for (int i = 0; i <= superLs.size() - 1; i++){
                 		SuperLion superL = superLs.get(i);
                 		if (superL.isCollision(x, y)){
                 			if (banana > 0){
                 				bananas.add(new Banana(this, bmpReward, snakes.get(0).getX(), snakes.get(0).getY(), x, y, bmpReward2, bmpReward3, bmpReward4));
                 				banana--;
                 			}
                 			touchTrap = true;
                 		}
                	 }
                	 
                	 //only setup bait when traps/SuperCs are NOT touched
                	 if (!touchTrap){
                	 	baitX = x;
                	 	baitY = y;
                	 	snakes.get(0).setNoStop(false);
                	 }
                	 
                 }
          }
          return true;
    }

    
}