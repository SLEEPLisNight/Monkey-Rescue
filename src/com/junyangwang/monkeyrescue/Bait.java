package com.junyangwang.monkeyrescue;

import android.graphics.Bitmap;
import android.graphics.Canvas;

public class Bait {
	
	private GameView gameView;
    private Bitmap bmp;
    private int width;
    private int height;

    public Bait(GameView gameView, Bitmap bmp) {
          this.width = bmp.getWidth();
          this.height = bmp.getHeight();
          this.gameView = gameView;
          this.bmp = bmp;
    }

    public void onDraw(Canvas canvas, int x, int y) {
          canvas.drawBitmap(bmp, x - width/2, y - height/2, null);
    }

}
