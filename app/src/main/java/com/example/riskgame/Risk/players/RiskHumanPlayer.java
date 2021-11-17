package com.example.riskgame.Risk.players;
/**
 * RiskHumanPlayer
 * Display, info, and input handling for human players.
 *
 * @author Dylan Kramis, Phi Nguyen
 * @version 11/16/2021 Beta
 */

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_NUMBER_VARIATION_NORMAL;
import static android.text.InputType.TYPE_NUMBER_VARIATION_PASSWORD;

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
import android.widget.Switch;
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
import com.example.riskgame.Risk.riskActionMessage.ExchangeCardAction;
import com.example.riskgame.Risk.riskActionMessage.FortifyAction;
import com.example.riskgame.Risk.riskActionMessage.NextTurnAction;
import com.example.riskgame.Risk.views.CardView;
import com.example.riskgame.Risk.views.RiskMapView;

public class RiskHumanPlayer extends GameHumanPlayer implements View.OnClickListener,
        View.OnTouchListener {

    // instance variables
    private final int TOUCH_WINDOW = 35;
    private Button helpButton;
    private Button exitButton;
    private Button cardButton;
    private Button nextButton;
    private CardView cardView;
    private TextView playerTextView;
    private TextView turnPhaseTextView;
    private TextView troopCountTextView;
    private TextView cardTextView;
    private TextView currentCards;
    private CardView cards;
    private RiskMapView mapView;
    private float touchX;
    private float touchY;
    private boolean screenDragged;
    private Territory selectedT1;
    private Territory selectedT2;
    private RiskGameState gameState;
    int countArtillery = 0;
    int countCavalry = 0;
    int countInfantry = 0;


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

    /**
     * receiveInfo
     * receives game state and other info
     *
     * @param info
     */
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
         int[] PLAYER_COLORS = {0xFFFF0000, // red
                0xFF0000FF, // blue
                0xFFFFBF00, // yellow-orange
                0xFF00DF00};// green
        playerTextView.setTextColor(PLAYER_COLORS[gameState.getCurrentTurn()]);
        playerTextView.setText(allPlayerNames[gameState.getCurrentTurn()]);

        if (gameState.getCurrentPhase() == RiskGameState.Phase.DEPLOY) {
            turnPhaseTextView.setText("Deploy");
        } else if (gameState.getCurrentPhase() == RiskGameState.Phase.ATTACK) {
            turnPhaseTextView.setText("Attack");
        } else {
            turnPhaseTextView.setText("Fortify");
        }
        troopCountTextView.setText("Troops: " + ((RiskGameState) info).getTotalTroops());
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

        cardTextView = activity.findViewById(R.id.gainCardText);


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

        // switch case checking buttons
        switch (view.getId()) {
            case R.id.helpButton:
                // calls help popup then breaks out of switch case
                helpPopup();
                break;

            case R.id.exitButton:
                // exits game, no break needed
                System.exit(0);

            case R.id.cardButton:
                // TODO show players what cards they have
                ExchangeCardAction exchange = new ExchangeCardAction(this);
                LayoutInflater inflater = (LayoutInflater)
                        myActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View popup = inflater.inflate(R.layout.card_popup, null);
                Button exchangeButton = (Button) popup.findViewById(R.id.exchange_cards);
                currentCards = popup.findViewById(R.id.currentCards);
                cardTextView = popup.findViewById(R.id.gainCardText);
                countArtillery = 0;
                countCavalry = 0;
                countInfantry = 0;
                if(gameState != null && currentCards != null) {
                    for(int i = 0; i < gameState.getCards().get(gameState.getCurrentTurn()).size(); i++) {
                        if(gameState.getCards().size() <= 0 ) {
                            return;
                        }
                        if(gameState.getCards().get(this.playerNum).get(i) == RiskGameState.Card.ARTILLERY) {
                            countArtillery++;
                        }
                        if(gameState.getCards().get(playerNum).get(i) == RiskGameState.Card.CAVALRY) {
                            countCavalry++;
                        }
                        if(gameState.getCards().get(playerNum).get(i) == RiskGameState.Card.INFANTRY) {
                            countInfantry++;
                        }
                    }


                    currentCards.setBackgroundColor(Color.WHITE);
                    currentCards.setTextColor(Color.BLACK);
                    currentCards.setText("Artillery: " + countArtillery + "\n");
                    currentCards.append("Cavalry: " + countCavalry + "\n");
                    currentCards.append("Infantry: " + countInfantry + "\n");
                    currentCards.append("\n Exchange Bonuses: \n");
                    currentCards.append(" 3 Infantry = 4 troops\n 3 Cavalry = 6 troops\n 3 Artillery = 8 troops\n 1 of each = 10 troops\n");
                    currentCards.append("pressing exchange cards automatically gives you the highest number of troops");
                    currentCards.invalidate();
                }
                // creates popup
                PopupWindow popupWindow = new PopupWindow(popup, LinearLayout.LayoutParams.WRAP_CONTENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT, true);
                // displays popup window
                popupWindow.showAtLocation(mapView, Gravity.CENTER, 0, 0);
                exchangeButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        if(gameState.getCurrentPhase() == RiskGameState.Phase.DEPLOY) {
                            game.sendAction(exchange);
                            troopCountTextView.invalidate();
                            if(countArtillery >= 1 && countCavalry >= 1 && countInfantry >= 1) {
                                cardTextView.setText("Gained 10 troops");
                                countArtillery--;
                                countCavalry--;
                                countInfantry--;
                            } else if(countArtillery >= 3) {
                                cardTextView.setText("Gained 8 troops");
                                countArtillery=-3;
                            } else if(countCavalry >= 3) {
                                cardTextView.setText("Gained 6 troops");
                                countCavalry =-3;
                            } else if(countInfantry >= 3) {
                                cardTextView.setText("Gained 4 troops");
                                countInfantry =-3;
                            }
                            currentCards.setText("Artillery: " + countArtillery + "\n");
                            currentCards.append("Cavalry: " + countCavalry + "\n");
                            currentCards.append("Infantry: " + countInfantry + "\n");
                            currentCards.append("\n Exchange Bonuses: \n");
                            currentCards.append(" 3 Infantry = 4 troops\n 3 Cavalry = 6 troops\n 3 Artillery = 8 troops\n 1 of each = 10 troops\n");
                            currentCards.append("pressing exchange cards automatically gives you the highest number of troops");
                        }
                    }
                });
                break;

            case R.id.nextButton:
                // clears selections, sends action to advance turn, then breaks
                selectedT2 = null;
                selectedT1 = null;
                game.sendAction(new NextTurnAction(this));
                break;

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
            if ((Math.abs(motionEvent.getX() - touchX) > 5.00)
                || (Math.abs(motionEvent.getY() - touchY) > 5.00)) {
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
                    if ((Math.abs(touchX - (t.getX() + mapView.getShiftX() + TOUCH_WINDOW))
                            < (float) (t.getWidth() / 2))
                            && (Math.abs(touchY - (t.getY() + mapView.getShiftY() + TOUCH_WINDOW))
                            < (float) (t.getHeight() / 2))) {

                        if ((gameState.getCurrentPhase() == RiskGameState.Phase.DEPLOY)
                            || (gameState.getCurrentPhase() == RiskGameState.Phase.FORTIFY
                            && selectedT1 != null)) {
                            askTroops();
                        } else {
                            confirm();
                        }

                        if (selectedT1 != null
                                && gameState.getCurrentPhase() != RiskGameState.Phase.DEPLOY) {
                            selectedT2 = t;
                        } else {
                            selectedT1 = t;
                            if(selectedT1.getOwner() != this.playerNum) {
                                selectedT1 = null;
                                flash(Color.RED,1);
                            }
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

    /**
     * generateAction
     * Sends actions to the local game.
     *
     * @param t territory for deploy
     * @param numTroops number of troops for deploy and fortify
     */
    private void generateAction(Territory t, int numTroops) {
        if (gameState.getCurrentPhase() == RiskGameState.Phase.DEPLOY) {
            game.sendAction(new DeployAction(this, t, numTroops));
            selectedT1 = null;
            selectedT2 = null;
        } else if (gameState.getCurrentPhase() == RiskGameState.Phase.ATTACK) {
            if (selectedT2 != null) {
                game.sendAction(new AttackAction(this, selectedT1, selectedT2));
                selectedT1 = null;
                selectedT2 = null;
            }
        } else {
            if (selectedT1 != null && selectedT2 != null) {
                game.sendAction(new FortifyAction(this, selectedT1, selectedT2, numTroops));
                selectedT1 = null;
                selectedT2 = null;
            }
        }



    }

    /**
     * askTroops
     * Creates a popup asking how many troops to use.
     */
    private void askTroops() {
        // initialize popup
        LayoutInflater inflater = (LayoutInflater)
                myActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popup = inflater.inflate(R.layout.ask_troops_popup, null);
        Button confirmButton = (Button) popup.findViewById(R.id.confirmButton);
        EditText inputText = (EditText) popup.findViewById(R.id.inputText);
        inputText.setInputType(TYPE_CLASS_NUMBER | TYPE_NUMBER_VARIATION_NORMAL);

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

    /**
     * confirm
     * Popup asking for confirmation upo selecting a territory
     */
    private void confirm() {

        //initialize popup
        LayoutInflater inflater = (LayoutInflater)
                myActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popup = inflater.inflate(R.layout.confirm_popup, null);
        Button confirmButton = (Button) popup.findViewById(R.id.confirmButton);

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

                generateNumber(0);
                popupWindow.dismiss();
            }
        });

    }

    /**
     * generateNumber
     * Calls generateAction when popup clicked.
     *
     * @param numTroops
     */
    private void generateNumber(int numTroops) {
        generateAction(selectedT1, numTroops);
    }

    @Override
    protected void initAfterReady() {
        if(gameState == null) {
            return;
        }
        gameState.setPlayerCount(playerNum);
        cardView.setRiskGameState(gameState);
    }

    /**
     * helpPopup
     * Displays the user manual.
     */
    private void helpPopup() {

        // initialize popup
        LayoutInflater inflater = (LayoutInflater)
                myActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View popup = inflater.inflate(R.layout.help_popup, null);
        Button dismissButton = (Button) popup.findViewById(R.id.dismissButton);

        // creates and displays popup
        PopupWindow popupWindow = new PopupWindow(popup, LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT, false);
        popupWindow.showAtLocation(mapView, Gravity.CENTER, 0,
                0);

        // dismissButton listener
        dismissButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.dismiss();
            }
        });

    }
}
