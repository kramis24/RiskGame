package com.example.riskgame.Risk.players;
/**
 * RiskAdvancedComputerPlayer
 * Less dumb (i.e. not random) computer player for Risk. Designed to make one move each time info
 * is received.
 * ---CURRENTLY UNUSED---
 *
 * @author Dylan Kramis
 * @version 11/22/2021
 */

import com.example.riskgame.GameFramework.infoMessage.GameInfo;
import com.example.riskgame.GameFramework.infoMessage.NotYourTurnInfo;
import com.example.riskgame.GameFramework.players.GameComputerPlayer;
import com.example.riskgame.Risk.infoMessage.RiskGameState;
import com.example.riskgame.Risk.infoMessage.Territory;
import com.example.riskgame.Risk.riskActionMessage.DeployAction;
import com.example.riskgame.Risk.riskActionMessage.NextTurnAction;

public class RiskAdvancedComputerPlayer extends GameComputerPlayer {

    // limits to keep the computer from getting stuck
    public static final int ATTACK_LIMIT = 20;
    public static final int FORTIFY_ATTEMPT_LIMIT = 5;

    // instance variables
    private RiskGameState gameState;

    // variables for attacking
    private int attackCount = 0;
    private Territory attackTarget; // focus maintainer
    private int territoriesCaptured = 0;

    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     */
    public RiskAdvancedComputerPlayer(String name) {
        super(name);
    }

    /**
     * receiveInfo
     * Processes received info and decides what to do with it.
     *
     * @param info info received
     */
    @Override
    protected void receiveInfo(GameInfo info) {

        // returns if wrong turn
        if (info instanceof NotYourTurnInfo) {
            return;
        }

        // proceeds to play the game if valid game state detected
        if (info instanceof RiskGameState) {
            gameState = (RiskGameState) info;

            // returns if wrong turn
            if (gameState.getCurrentTurn() != playerNum){
                return;
            }

            // calls action generator for current phase
            if (gameState.getCurrentPhase() == RiskGameState.Phase.DEPLOY) {
                generateDeploy();
            } else if (gameState.getCurrentPhase() == RiskGameState.Phase.ATTACK) {
                generateAttack();
            } else {
                generateFortify();
            }

            // 1 second pause
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * generateDeploy
     * Analyzes game state and determines where to deploy troops.
     */
    protected void generateDeploy() {

        // advances turn if no troops available
        if (gameState.getTotalTroops() == 0) {
            game.sendAction(new NextTurnAction(this));
        }

        // otherwise sends troops somewhere
        else {

            // TODO exchange cards

            // initializes variables
            Territory deployTo = null;
            Territory greatestThreat = null;
            double vulnerability = -1;

            // finds the most vulnerable territory
            for (Territory t : gameState.getTerritories()) {
                if ((t.getOwner() == playerNum) && t.hasEnemyAdjacent()) {
                    if ((t.getDisadvantage() < vulnerability) || (vulnerability < 0)) {
                        vulnerability = t.getDisadvantage();
                        greatestThreat = t.getGreatestThreat();
                        deployTo = t;
                    }
                }
            }

            // deploys to selected territory, troop count varies by situation
            // fail case if no enemies found, this should be a win
            if (vulnerability < 0 || deployTo == null || greatestThreat == null) {
                return;
            }
            // attempts to equalize if more enemy troops than troops in own territory
            else if (vulnerability < 1) {
                if ((greatestThreat.getTroops() - deployTo.getTroops())
                        <= gameState.getTotalTroops()) {
                    game.sendAction(new DeployAction(this, deployTo,
                            greatestThreat.getTroops() - deployTo.getTroops()));
                } else {
                    game.sendAction(new DeployAction(this, deployTo,
                            gameState.getTotalTroops()));
                }
            }
            // boost troop counts more conservatively if ratio is favorable
            else {
                if (gameState.getTotalTroops() > 3) {
                    game.sendAction(new DeployAction(this, deployTo, 3));
                } else {
                    game.sendAction(new DeployAction(this, deployTo,
                            gameState.getTotalTroops()));
                }
            }
        }
    }



    protected void generateAttack() {
        // TODO ask Charlie and Phi how logic should be implemented
    }

    protected void generateFortify() {
        // TODO ask Charlie and Phi how logic should be implemented
    }
}
