package com.junyangwang.monkeyrescue;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Home {
	private GameView gameView;
    private Bitmap bmp;
    private int width;
    private int height;
    private int x;
    private int y;

    public Home (GameView gameView, Bitmap bmp, int x, int y) {
          this.width = bmp.getWidth();
          this.height = bmp.getHeight();
          this.gameView = gameView;
          this.bmp = bmp;
          
          this.x = x;
          this.y = y;
    }

    public void onDraw(Canvas canvas) {
          canvas.drawBitmap(bmp, x, y, null);
    }
    
    public boolean isCollision(int x2, int y2) {
        return x2 > x && x2 < x + width && y2 > y && y2 < y + height;
    }
    
}
