package com.example.riskgame.Risk.views;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceView;

import com.example.riskgame.R;

public class MapView extends SurfaceView {

    // member variables
    private Paint mapPaint;
    private Paint boxPaint;
    private Paint textPaint;

    // game map, don't want to be loading this constantly
    final private Bitmap mapImage = BitmapFactory.decodeResource(getResources(), R.drawable.risk_board);

    // player colors, more to come
    final private int PLAYER_1_COLOR = 0xFFFF3F3F; // light-ish red
    final private int PLAYER_2_COLOR = 0xFF3F3FFF; // light-ish blue

    /**
     * MapView
     * Constructor for MapView object, based off of SurfaceView class
     *
     * @param context
     * @param attrs
     */
    public MapView(Context context, AttributeSet attrs) {

        // call to SurfaceView constructor and indication that drawing will be done
        super(context, attrs);
        setWillNotDraw(false);

        //setting up paints and colors
        mapPaint = new Paint();
        mapPaint.setColor(Color.BLACK);

        boxPaint = new Paint(mapPaint);

        textPaint = new Paint();
        textPaint.setTextSize(40);
    }

    /**
     * onDraw
     * Draws the map and all overlaying objects
     * NOTE: Locations of drawn items may look off in current prototype state on emulator!
     *
     * @param canvas
     */
    @Override
    protected void onDraw(Canvas canvas) {

        // draws map, this is only hardcoded for this gui prototype
        canvas.drawBitmap(mapImage, -200, -500, mapPaint);

        // draws text boxes indicating troop counts and ownership, only hardcoded for prototype
        drawTextBox(250, 180, 1, PLAYER_1_COLOR, canvas);
        drawTextBox(500, 220, 4, PLAYER_1_COLOR, canvas);
        drawTextBox(250, 350, 1, PLAYER_1_COLOR, canvas);
        drawTextBox(500, 600, 2, PLAYER_1_COLOR, canvas);
        drawTextBox(700, 750, 3, PLAYER_1_COLOR, canvas);
        drawTextBox(1170, 700, 7, PLAYER_2_COLOR, canvas);
        drawTextBox(1400, 620, 3, PLAYER_2_COLOR, canvas);
        drawTextBox(1470, 750, 7, PLAYER_2_COLOR, canvas);
        drawTextBox(1050, 440, 3, PLAYER_2_COLOR, canvas);
        drawTextBox(1050, 150, 3, PLAYER_2_COLOR, canvas);
        drawTextBox(1250, 150, 2, PLAYER_2_COLOR, canvas);
        drawTextBox(1255, 320, 1, PLAYER_2_COLOR, canvas);
        drawTextBox(1500, 150, 1, PLAYER_2_COLOR, canvas);
        drawTextBox(1800, 200, 1, PLAYER_2_COLOR, canvas);
        drawTextBox(1600, 550, 2, PLAYER_2_COLOR, canvas);
    }

    protected void drawTextBox(int x, int y, int count, int player, Canvas canvas) {

        // draws a black square
        canvas.drawRect(x, y, x + 50, y + 50, boxPaint);

        // adds text in specified player color, this is a rough idea
        textPaint.setColor(player);
        canvas.drawText(Integer.toString(count), x + 5, y + 45, textPaint);
    }
}
