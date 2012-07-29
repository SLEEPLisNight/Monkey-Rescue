package com.junyangwang.monkeyrescue;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Reward {
	private GameView gameView;
    private Bitmap bmp;
    private int width;
    private int height;
    private int x;
    private int y;
    private int xSpeed;
    private int ySpeed;
    private int level;

    public Reward(GameView gameView, Bitmap bmp, int x, int y, int level) {
          this.width = bmp.getWidth();
          this.height = bmp.getHeight();
          this.gameView = gameView;
          this.bmp = bmp;
          
          this.x = x;
          this.y = y;
          
          this.level = level;
    }
    
    private void update() {
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
