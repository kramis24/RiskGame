package com.example.riskgame.Risk;

import com.example.riskgame.GameFramework.LocalGame;
import com.example.riskgame.GameFramework.actionMessage.GameAction;
import com.example.riskgame.GameFramework.players.GamePlayer;
import com.example.riskgame.Risk.infoMessage.RiskGameState;
import com.example.riskgame.Risk.infoMessage.Territory;
import com.example.riskgame.Risk.riskActionMessage.AttackAction;
import com.example.riskgame.Risk.riskActionMessage.DeployAction;
import com.example.riskgame.Risk.riskActionMessage.FortifyAction;
import com.example.riskgame.Risk.riskActionMessage.NextTurnAction;

public class RiskLocalGame extends LocalGame {
    private RiskGameState riskGS;

    public RiskLocalGame() {
        state = new RiskGameState();
        riskGS = (RiskGameState) state;
    }

    public RiskLocalGame(RiskGameState gameState) {
        state = gameState;
        riskGS = (RiskGameState) state;
    }

    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {
        RiskGameState copy = new RiskGameState(riskGS);
        p.sendInfo(copy);
    }

    @Override
    protected boolean canMove(int playerIdx) {
        boolean canMove = false;
        if(playerIdx == riskGS.getCurrentTurn()) {
            canMove = true;
        }
        return canMove;
    }

    @Override
    protected boolean makeMove(GameAction action) {
        if(action instanceof DeployAction){
            riskGS.deploy(((DeployAction) action).getDeployTo(), ((DeployAction) action).getNumDeploy());
            return true;
        }
        if(action instanceof AttackAction) {
            riskGS.attack(((AttackAction) action).getAtk(), ((AttackAction) action).getDef());
            return true;
        }
        if(action instanceof FortifyAction) {
            riskGS.fortify(((FortifyAction) action).getDeployFrom(), ((FortifyAction) action).getDeployTo(), ((FortifyAction) action).getNumDeployed());
            return true;
        }
        if(action instanceof NextTurnAction) {
            riskGS.nextTurn();
            return true;
        }
        return false;
    }

    @Override
    protected String checkIfGameOver() {
        int winner = riskGS.getTerritories().get(0).getOwner();
        for(Territory t: riskGS.getTerritories()) {
            if(t.getOwner() != winner) {
                return null;
            }
        }
        return playerNames[winner] + " has won";
    }
}
