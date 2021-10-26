package com.example.riskgame.Risk;

import com.example.riskgame.GameFramework.LocalGame;
import com.example.riskgame.GameFramework.actionMessage.GameAction;
import com.example.riskgame.GameFramework.players.GamePlayer;
import com.example.riskgame.Risk.infoMessage.RiskGameState;
import com.example.riskgame.Risk.riskActionMessage.AttackAction;
import com.example.riskgame.Risk.riskActionMessage.DeployAction;
import com.example.riskgame.Risk.riskActionMessage.FortifyAction;

public class RiskLocalGame extends LocalGame {
    private RiskGameState riskGS;

    public RiskLocalGame() {
        riskGS = new RiskGameState();
    }

    @Override
    protected void sendUpdatedStateTo(GamePlayer p) {
        RiskGameState copy = new RiskGameState(riskGS);
        p.sendInfo(copy);
    }

    @Override
    protected boolean canMove(int playerIdx) {
        boolean canMove = false;
        if(riskGS.getCurrentTurn() == playerIdx) {
            canMove = true;
        }
        return canMove;
    }

    @Override
    protected boolean makeMove(GameAction action) {
        if(action instanceof DeployAction) {
            riskGS.deploy(((DeployAction) action).getDeployTo(), ((DeployAction) action).getNumDeploy());
            riskGS.nextTurn();
            return true;
        }
        if(action instanceof AttackAction) {
            riskGS.attack(((AttackAction) action).getAtk(), ((AttackAction) action).getDef());
            return true;
        }
        if(action instanceof FortifyAction) {
            riskGS.fortify(((FortifyAction) action).getDeployFrom(), ((FortifyAction) action).getDeployTo(), ((FortifyAction) action).getNumDeployed());
            riskGS.nextTurn();
            return true;
        }
        return false;
    }

    @Override
    protected String checkIfGameOver() {
        return null;
    }
}
