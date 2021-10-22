package com.example.riskgame.Risk.players;

import android.view.MotionEvent;
import android.view.View;

import com.example.riskgame.GameFramework.GameMainActivity;
import com.example.riskgame.GameFramework.infoMessage.GameInfo;
import com.example.riskgame.GameFramework.players.GameHumanPlayer;

public class RiskHumanPlayer extends GameHumanPlayer implements View.OnClickListener,
        View.OnTouchListener {
    /**
     * constructor
     *
     * @param name the name of the player
     */
    public RiskHumanPlayer(String name) {
        super(name);
    }

    @Override
    public View getTopView() {
        return null;
    }

    @Override
    public void receiveInfo(GameInfo info) {

    }

    @Override
    public void setAsGui(GameMainActivity activity) {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        return false;
    }
}
