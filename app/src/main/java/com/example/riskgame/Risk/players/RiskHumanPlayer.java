package com.example.riskgame.Risk.players;
/**
 * RiskHumanPlayer
 * Display, info, and input handling for human players.
 *
 * @author Dylan Kramis
 * @version 11/4/2021 WIP
 */

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.riskgame.GameFramework.GameMainActivity;
import com.example.riskgame.GameFramework.actionMessage.EndTurnAction;
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
     * Constructor for RiskHumanPlayer
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

        // returns if invalid game state
        if (!(info instanceof RiskGameState)) {
            return;
        }
        // typecasts game state
        gameState = (RiskGameState) info;

        // updates mapView with new game state
        mapView.setGameState(gameState);
        mapView.invalidate();

        // updates textViews
        playerTextView.setText(name);
        if (gameState.getCurrentPhase() == RiskGameState.Phase.DEPLOY) {
            turnPhaseTextView.setText("Deploy");
        } else if (gameState.getCurrentPhase() == RiskGameState.Phase.ATTACK) {
            turnPhaseTextView.setText("Attack");
        } else {
            turnPhaseTextView.setText("Fortify");
        }
        troopCountTextView.setText("Troops: -");
    }

    /**
     * setAsGui
     * Sets GUI elements.
     *
     * @param activity
     */
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

    /**
     * onClick
     * Handles button inputs, WIP
     *
     * @param view button clicked
     */
    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.helpButton:
                // TODO user manual popup display
            case R.id.exitButton:
                // TODO ExitAction
            case R.id.cardButton:
                // TODO card popup display
            case R.id.nextButton:
                // next turn action
        }

    }

    /**
     * onTouch
     * Handles touch inputs.
     *
     * @param view view touched
     * @param motionEvent touch event
     * @return true if touch processed successfully
     */
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
            if ((Math.abs(motionEvent.getX() - touchX) > 3.00)
                || (Math.abs(motionEvent.getY() - touchY) > 3.00)) {
                screenDragged = true;
                mapView.updatePosition(motionEvent.getX() - touchX,
                        motionEvent.getY() - touchY);
                touchX = motionEvent.getX();
                touchY = motionEvent.getY();
                return true;
            }

        } else if (motionEvent.getActionMasked() == MotionEvent.ACTION_UP) {

            // processes touch to create action
            if (!screenDragged) {
                for (Territory t : gameState.getTerritories()) {
                    if ((Math.abs(touchX - (t.getX() + mapView.getShiftX() + 50)) < (float) (t.getWidth() / 2))
                            && (Math.abs(touchY - (t.getY() + mapView.getShiftY() + 50)) < (float) (t.getHeight() / 2))) {
                        askTroops();
                        if (selectedT1 != null
                                && gameState.getCurrentPhase() != RiskGameState.Phase.DEPLOY) {
                            selectedT2 = t;
                        } else {
                            selectedT1 = t;
                        }
                                                //generateAction(t);
                        break;
                    }
                }
            }
            return true;
        }

        return false;
    }

    private void generateAction(Territory t, int numTroops) {

        if (gameState.getCurrentPhase() == RiskGameState.Phase.DEPLOY) {
            game.sendAction(new DeployAction(this, t, numTroops));
        } else if (gameState.getCurrentPhase() == RiskGameState.Phase.ATTACK) {
            if (selectedT2 != null) {
                game.sendAction(new AttackAction(this, selectedT2, selectedT1));// clear this up with phi
            }
        } else {
            if (selectedT1 != null) {
                game.sendAction(new FortifyAction(this, selectedT1, selectedT2, numTroops));
            }
        }

        selectedT1 = null;
        selectedT2 = null;

    }

    private void askTroops() {

        // initialize popup
        LayoutInflater inflater = (LayoutInflater)
                myActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popup = inflater.inflate(R.layout.ask_troops_popup, null);
        Button confirmButton = (Button)   popup.findViewById(R.id.confirmButton);
        EditText inputText   = (EditText) popup.findViewById(R.id.inputText);

        // create popup
        PopupWindow popupWindow = new PopupWindow(popup, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, true);

        // displays popup window
        popupWindow.showAtLocation(mapView, Gravity.NO_GRAVITY, (int) touchX,
                (int) touchY);

        // listener for confirmButton
        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // collects string input and tries to parse to int, flashes if failed
                String input = inputText.getText().toString();
                try {
                    generateNumber(Integer.parseInt(input));
                    popupWindow.dismiss();
                } catch (NumberFormatException nfe) {
                    mapView.flash(Color.RED, 50);
                }
            }
        });

    }

    private void generateNumber(int numTroops) {
        generateAction(selectedT1, numTroops);
    }
}
