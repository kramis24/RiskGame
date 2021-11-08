package com.example.riskgame.Risk;

import com.example.riskgame.GameFramework.LocalGame;
import com.example.riskgame.GameFramework.actionMessage.GameAction;
import com.example.riskgame.GameFramework.players.GamePlayer;
import com.example.riskgame.Risk.infoMessage.RiskGameState;
import com.example.riskgame.Risk.riskActionMessage.AttackAction;
import com.example.riskgame.Risk.riskActionMessage.DeployAction;
import com.example.riskgame.Risk.riskActionMessage.FortifyAction;
import com.example.riskgame.Risk.riskActionMessage.NextTurnAction;

public class RiskLocalGame extends LocalGame {
    RiskGameState riskGs;

    public RiskLocalGame() {
        riskGs = new RiskGameState();
    }
    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {
        RiskGameState copy = new RiskGameState(riskGs);
        p.sendInfo(copy);
    }

    @Override
    protected boolean canMove(int playerIdx) {
        boolean canMove = false;
        if(playerIdx == riskGs.getCurrentTurn()) {
            canMove = true;
        }
        return canMove;
    }

    @Override
    protected boolean makeMove(GameAction action) {
        if(action instanceof DeployAction){
            riskGs.deploy(((DeployAction) action).getTerritory(), ((DeployAction) action).getTroop());
            return true;
        }
        if(action instanceof AttackAction) {
            riskGs.attack(((AttackAction) action).getTerritory(), ((AttackAction) action).getTerritory2());
            return true;
        }
        if(action instanceof FortifyAction) {
            riskGs.fortify(((FortifyAction) action).getTerritory(), ((FortifyAction) action).getTerritory2(), ((FortifyAction) action).getTroop());
            return true;
        }
        if(action instanceof NextTurnAction) {
            riskGs.nextTurn();
            return true;
        }
        return false;
    }

    @Override
    protected String checkIfGameOver() {
        return null;
    }
}
