package com.example.riskgame.Risk.players;

import com.example.riskgame.GameFramework.infoMessage.GameInfo;
import com.example.riskgame.GameFramework.infoMessage.NotYourTurnInfo;
import com.example.riskgame.GameFramework.players.GameComputerPlayer;
import com.example.riskgame.Risk.infoMessage.RiskGameState;

/**
 * RiskAdvancedComputerPlayer
 * Less dumb (i.e. not random) computer player for Risk. Designed to make one move each time info
 * is received.
 * ---CURRENTLY UNUSED---
 *
 * @author Dylan Kramis
 * @version 11/15/2021 created
 */

public class RiskAdvancedComputerPlayer extends GameComputerPlayer {

    // instance variables
    RiskGameState gameState;

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
        }

    }

    protected void generateDeploy() {
        // TODO ask Charlie and Phi how logic should be implemented
    }

    protected void generateAttack() {
        // TODO ask Charlie and Phi how logic should be implemented
    }

    protected void generateFortify() {
        // TODO ask Charlie and Phi how logic should be implemented
    }
}
