package com.example.riskgame.Risk;

import com.example.riskgame.GameFramework.LocalGame;
import com.example.riskgame.GameFramework.actionMessage.GameAction;
import com.example.riskgame.GameFramework.players.GamePlayer;

public class RiskLocalGame extends LocalGame {
    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {

    }

    @Override
    protected boolean canMove(int playerIdx) {
        return false;
    }

    @Override
    protected boolean makeMove(GameAction action) {
        return false;
    }

    @Override
    protected String checkIfGameOver() {
        return null;
    }
}
