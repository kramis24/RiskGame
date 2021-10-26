package com.example.riskgame.Risk.riskActionMessage;

import com.example.riskgame.GameFramework.actionMessage.GameAction;
import com.example.riskgame.GameFramework.players.GamePlayer;
import com.example.riskgame.Risk.infoMessage.Territory;

public class DeployAction extends GameAction {

    // logging tag
    private static final String TAG = "DeployAction";
    // serializable support
    // TODO Serializable support

    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    private Territory deployTo;
    private int numDeploy;

    public DeployAction(GamePlayer player,Territory deployTo, int numDeploy) {
        super(player);
        this.deployTo = deployTo;
        this.numDeploy = numDeploy;
    }

    public int getNumDeploy() {
        return numDeploy;
    }

    public Territory getDeployTo() {
        return deployTo;
    }
}
