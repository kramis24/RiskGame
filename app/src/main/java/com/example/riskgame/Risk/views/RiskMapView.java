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

public class RiskMapView extends SurfaceView {

    // member variables
    private Paint mapPaint;
    private Paint boxPaint;
    private Paint textPaint;

    // game map, don't want to be loading this constantly
    final private Bitmap mapImage = BitmapFactory.decodeResource(getResources(), R.drawable.risk_board);

    // player colors, more to come
    final private int PLAYER_1_COLOR = 0xFFFF3F3F; // light-ish red
    final private int PLAYER_2_COLOR = 0xFF3F3FFF; // light-ish blue

    // display variables
    private int left = 0;
    private int top = 0;
    private int right = getWidth();
    private int bottom = getHeight();


    /**
     * MapView
     * Constructor for MapView object, based off of SurfaceView class
     *
     * @param context
     * @param attrs
     */
    public RiskMapView(Context context, AttributeSet attrs) {

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
        canvas.drawBitmap(mapImage, left, top, mapPaint);

        // draws text boxes indicating troop counts and ownership

    }

    protected void drawTextBox(int x, int y, int count, int player, Canvas canvas) {

        // draws a black square
        canvas.drawRect(x, y, x + 50, y + 50, boxPaint);

        // adds text in specified player color, this is a rough idea
        textPaint.setColor(player);
        canvas.drawText(Integer.toString(count), x + 5, y + 45, textPaint);
    }

    public void updatePosition(float changeX, float changeY) {

        // updates position variables
        left += changeX;
        top += changeY;
        right = left + getWidth();
        bottom = top + getHeight();

        // checks if out of bounds
        if (left > 0) {
            left = 0;
            right = getWidth();
        }
        if (top > 0) {
            top = 0;
            bottom = getHeight();
        }
        /*if (right < mapImage.getWidth()) {
            right = mapImage.getWidth();
            left = right - getWidth();
        }
        if (bottom < mapImage.getHeight()) {
            bottom = mapImage.getHeight);
            top = bottom - getHeight();
        }*/
        invalidate();
    }
}
