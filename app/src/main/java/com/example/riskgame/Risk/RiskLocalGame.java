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

    /**
     * Constructor for the Local Game
     */
    public RiskLocalGame() {
        state = new RiskGameState();
        riskGS = (RiskGameState) state;
    }

    /**
     * Copy Constructor for the gamestate
     * @param gameState
     */
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

            //updates the updates the gamestate
            for(Territory t:riskGS.getTerritories()) {
                if(((DeployAction) action).getDeployTo().equals(t)) {
                    int index = riskGS.getTerritories().indexOf(t);
                    riskGS.getTerritories().set(index, ((DeployAction) action).getDeployTo());
                }
            }
            riskGS.setTotalTroops(riskGS.getTotalTroops() - ((DeployAction) action).getNumDeploy());
            if(riskGS.getTotalTroops() <= 0) {
                riskGS.nextTurn();
            }

            return true;
        }
        if(action instanceof AttackAction) {
            riskGS.attack(((AttackAction) action).getAtk(), ((AttackAction) action).getDef());
            //updates the gamestate
            for(Territory t:riskGS.getTerritories()) {
                if(((AttackAction) action).getAtk().equals(t)) {
                    int index = riskGS.getTerritories().indexOf(t);
                    riskGS.getTerritories().set(index, ((AttackAction)action).getAtk());
                }
            }

            //updates the gamestate
            for(Territory t:riskGS.getTerritories()) {
                if(((AttackAction) action).getDef().equals(t)) {
                    int index = riskGS.getTerritories().indexOf(t);
                    riskGS.getTerritories().set(index, ((AttackAction)action).getDef());
                }
            }
            return true;
        }
        if(action instanceof FortifyAction) {
            riskGS.fortify(((FortifyAction) action).getDeployFrom(), ((FortifyAction) action).getDeployTo(), ((FortifyAction) action).getNumDeployed());
            //updates the gamestate
            for(Territory t:riskGS.getTerritories()) {
                if(((FortifyAction) action).getDeployFrom().equals(t)) {
                    int index = riskGS.getTerritories().indexOf(t);
                    riskGS.getTerritories().set(index, ((FortifyAction) action).getDeployFrom());
                }
            }
            for(Territory t:riskGS.getTerritories()) {
                //updates the gamestate
                if(((FortifyAction) action).getDeployTo().equals(t)) {
                    int index = riskGS.getTerritories().indexOf(t);
                    riskGS.getTerritories().set(index, ((FortifyAction) action).getDeployTo());
                }
            }
            return true;
        }
        if(action instanceof NextTurnAction) {
           /* if(riskGS.getTotalTroops() > 0) {
                return false;
            }*/
            riskGS.nextTurn();
            return true;
        }
        return false;
    }

    @Override
    /**
     * Checks if the someone has won the game
     */
    protected String checkIfGameOver() {
        int winner = riskGS.getTerritories().get(0).getOwner();
        //checks if all territories have the same owner
        for(Territory t: riskGS.getTerritories()) {
            if(t.getOwner() != winner) {
                return null;
            }
        }
        return playerNames[winner] + " has won";
    }
}
