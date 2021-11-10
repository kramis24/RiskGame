package com.example.riskgame.Risk.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.SurfaceView;

import com.example.riskgame.GameFramework.players.GamePlayer;
import com.example.riskgame.GameFramework.utilities.FlashSurfaceView;
import com.example.riskgame.Risk.infoMessage.RiskGameState;

import java.util.ArrayList;
import java.util.List;

public class CardView extends FlashSurfaceView {

    private RiskGameState riskGameState;
    private Paint testPaint;

    public CardView(Context context) {
        super(context);
        setWillNotDraw(false);
        testPaint.setColor(Color.RED);
    }

    public void setRiskGameState(RiskGameState riskGameState) {
        this.riskGameState = riskGameState;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(riskGameState == null) {
            return;
        }
        List<ArrayList<RiskGameState.Card>> cards = riskGameState.getCards();
        for(int i = 0; i < cards.size(); i++) {
            if(i == riskGameState.getCurrentTurn()) {
                for(int j = 0; j < cards.get(i).size(); j++) {
                    canvas.drawRect(j*10,10,j*20,50,testPaint);
                }
            }
        }


    }
}
