package com.example.riskgame.Risk.players;
/**
 * RiskHumanPlayer
 * Display, info, and input handling for human players.
 *
 * @author Dylan Kramis
 * @version 10/29/2021 WIP
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.riskgame.GameFramework.GameMainActivity;
import com.example.riskgame.GameFramework.infoMessage.GameInfo;
import com.example.riskgame.GameFramework.players.GameHumanPlayer;
import com.example.riskgame.R;
import com.example.riskgame.Risk.infoMessage.RiskGameState;
import com.example.riskgame.Risk.infoMessage.Territory;
import com.example.riskgame.Risk.riskActionMessage.AttackAction;
import com.example.riskgame.Risk.riskActionMessage.DeployAction;
import com.example.riskgame.Risk.riskActionMessage.FortifyAction;
import com.example.riskgame.Risk.views.RiskMapView;

public class RiskHumanPlayer extends GameHumanPlayer implements View.OnClickListener,
        View.OnTouchListener {

    // instance variables
    private Button helpButton;
    private Button exitButton;
    private Button cardButton;
    private Button nextButton;
    private TextView playerTextView;
    private TextView turnPhaseTextView;
    private TextView troopCountTextView;
    private RiskMapView mapView;
    private float touchX;
    private float touchY;
    private boolean screenDragged;
    private Territory selectedT1;
    private Territory selectedT2;
    private RiskGameState gameState;


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

        if (!(info instanceof RiskGameState)) {
            return;
        }

        gameState = (RiskGameState) info;
    }

    @Override
    public void setAsGui(GameMainActivity activity) {

        // set activity
        myActivity = activity;

        // load layout
        activity.setContentView(R.layout.risk_player);

        // initialize layout elements
        helpButton = (Button)activity.findViewById(R.id.helpButton);
        exitButton = (Button)activity.findViewById(R.id.exitButton);
        cardButton = (Button)activity.findViewById(R.id.cardButton);
        nextButton = (Button)activity.findViewById(R.id.nextButton);
        playerTextView     = (TextView)activity.findViewById(R.id.playerTextView);
        turnPhaseTextView  = (TextView)activity.findViewById(R.id.turnPhaseTextView);
        troopCountTextView = (TextView)activity.findViewById(R.id.troopCountTextView);
        mapView = (RiskMapView)activity.findViewById(R.id.mapView);

        // set listeners
        helpButton.setOnClickListener(this);
        exitButton.setOnClickListener(this);
        cardButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        mapView.setOnTouchListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.helpButton:
                // TODO HelpAction or popup display
            case R.id.exitButton:
                // TODO ExitAction
            case R.id.cardButton:
                // TODO ViewCardAction or popup display
            case R.id.nextButton:
                // TODO NextPhaseAction
        }

    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        if (motionEvent.getActionMasked() == MotionEvent.ACTION_DOWN) {
            // retrieves position of touch and sets screenDragged to false
            touchX = motionEvent.getX();
            touchY = motionEvent.getY();
            screenDragged = false;
            return true;

        } else if (motionEvent.getActionMasked() == MotionEvent.ACTION_MOVE) {

            // sets screenDragged to true, updates position, and refreshes touch location
            screenDragged = true;
            mapView.updatePosition(motionEvent.getX() - touchX,
                                   motionEvent.getY() - touchY);
            touchX = motionEvent.getX();
            touchY = motionEvent.getY();
            return true;

        } else if (motionEvent.getActionMasked() == MotionEvent.ACTION_UP) {

            // processes touch to create action
            for (Territory t : gameState.getTerritories()) {
                if ((Math.abs(touchX - t.centerX) < (float) (t.boxWidth / 2))
                    && (Math.abs(touchY - t.centerY) < (float) (t.boxHeight / 2))) {
                    //generateAction(t);
                    break;
                }
            }
            return true;
        }

        return false;
    }

    private void generateAction(Territory t) {

        // return if it's not the human player's turn
        if (gameState.getCurrentPlayer() != playerNum) return;

        //
        if (gameState.getCurrentPhase() == RiskGameState.Phase.DEPLOY) {
            int numTroops = askTroops();
            game.sendAction(new DeployAction(this, t, numTroops));
        }

        else if (gameState.getCurrentPhase() == RiskGameState.Phase.ATTACK) {
            if (selectedT1 == null) {
                selectedT1 = t;
            } else if (selectedT2 == null) {
                selectedT2 = t;
            } else {
                game.sendAction(new AttackAction(this, selectedT1, selectedT2));
            }
        } else if (gameState.getCurrentPhase() == RiskGameState.Phase.FORTIFY) {
            if (selectedT1 == null) {
                selectedT1 = t;
            } else if (selectedT2 == null) {
                selectedT2 = t;
            } else {
                game.sendAction(new FortifyAction(this, selectedT1, selectedT2, 1));
            }
        }

    }

    private int askTroops() {

        // initialize popup
        LayoutInflater inflater = (LayoutInflater)
                myActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popup = inflater.inflate(R.layout.ask_troops_popup, null);


        return 1;

    }
}
