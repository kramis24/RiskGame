package com.example.riskgame.Risk.views;
/**
 * RiskMapView
 * Draws the Risk map.
 *
 * @author Dylan Kramis
 * @version 11/4/2021 WIP
 */

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;

import com.example.riskgame.GameFramework.utilities.FlashSurfaceView;
import com.example.riskgame.R;
import com.example.riskgame.Risk.infoMessage.RiskGameState;
import com.example.riskgame.Risk.infoMessage.Territory;

public class RiskMapView extends FlashSurfaceView {

    // member variables
    private Paint mapPaint;
    private Paint boxPaint;
    private Paint textPaint;
    private Paint namePaint;

    // game map, don't want to be loading this constantly
    final private Bitmap mapImage = BitmapFactory.decodeResource(getResources(), R.drawable.risk_board);

    // game state to render territory boxes
    private RiskGameState gameState;

    // player colors, more to come
    final static private int[] PLAYER_COLORS = {0xFFFF0000, // red
                                                0xFF0000FF, // blue
                                                0xFFFFBF00, // yellow-orange
                                                0xFF00DF00, // green
                                                0xFF9F00FF, // purple
                                                0xFF9C4300}; //brownish-orange

    // display variables
    private int left = getLeft();
    private int top = getTop();
    private int right = getRight();
    private int bottom = getBottom();
    private int shiftX = 0;
    private int shiftY = 0;

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
        mapPaint.setColor(Color.WHITE);

        boxPaint = new Paint(mapPaint);


        textPaint = new Paint();
        textPaint.setTextSize(40);
        textPaint.setTextAlign(Paint.Align.CENTER);

        namePaint = new Paint();
        namePaint.setColor(Color.BLACK);
        namePaint.setTextSize(30);
        namePaint.setTextAlign(Paint.Align.CENTER);

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

        // returns if no game state is given, this is required to avoid crashing
        if (gameState == null) {
            return;
        }

        // draws text boxes indicating troop counts and ownership
        for (Territory t : gameState.getTerritories()) {
            drawTextBox(t.getX() + left, t.getY() + top, t.getTroops(), t.getOwner(),
                    t.getName(), canvas);

        }

    }

    /**
     * drawTextBox
     * Draws a text box displaying the number of troops in a territory, along with the name
     * of the territory above it.
     *
     * @param x x coordinate
     * @param y y coordinate
     * @param count troop count
     * @param player owner of territory
     * @param name name of territory
     * @param canvas canvas used to draw
     */
    protected void drawTextBox(int x, int y, int count, int player, String name, Canvas canvas) {

        // draws a white square
        canvas.drawRect(x - 25, y - 25, x + 25, y + 25, boxPaint);

        // adds text in specified player color, this is a rough idea
        if (player < 0) {
            textPaint.setColor(Color.BLACK);
        } else {
            textPaint.setColor(PLAYER_COLORS[player]);
        }
        canvas.drawText(Integer.toString(count), x, y + 15, textPaint);
        canvas.drawText(name, x, y - 30, namePaint);
    }

    /**
     * updatePosition
     * Updates map position.
     *
     * @param changeX change in x coordinate
     * @param changeY change in y coordinate
     */
    public void updatePosition(float changeX, float changeY) {

        // updates position variables
            shiftX += changeX;
            shiftY += changeY;
            left += changeX;
            top += changeY;
            right = left + getWidth();
            bottom = top + getHeight();
            int leftBound = 0;
            int rightBound = 0;
            int topBound = 0;
            int bottomBound = 0;

        //checks if landscape or portrait and changes the bounds accordingly
        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            rightBound = 1050;
            bottomBound = -150;

        } else if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            rightBound = -600;
            bottomBound = 1400;
        }
        // checks if out of bounds, logic is rather counterintuitive

        if (left > leftBound) {
            left = topBound;
            right = getWidth();
            shiftX = 0;
        }
        if (top > topBound) {
            top = topBound;
            bottom = getHeight();
            shiftY = 0;
        }
        if (right < rightBound) { // getters didn't work here, so trial and error was used to determine
            right = rightBound;   // boundaries. use an actual tablet when testing
            left = right - getWidth();
            shiftX = left;
        }
        if (bottom < bottomBound) {
            bottom = bottomBound;
            top = bottom - getHeight();
            shiftY = top;
        }
        invalidate();

    }

    /**
     * setGameState
     * Sets game state.
     *
     * @param gs game state
     */
    public void setGameState(RiskGameState gs) {
        gameState = gs;
    }

    /**
     * getShiftX
     * Gets x coordinate shift.
     *
     * @return x coordinate shift
     */
    public float getShiftX() {
        return shiftX;
    }

    /**
     * getShiftY
     * Gets y coordinate shift.
     *
     * @return y coordinate shift
     */
    public float getShiftY() {
        return shiftY;
    }
}
