package com.example.riskgame.Risk.riskActionMessage;

import com.example.riskgame.GameFramework.actionMessage.GameAction;
import com.example.riskgame.GameFramework.players.GamePlayer;
import com.example.riskgame.Risk.infoMessage.Territory;

public class FortifyAction extends GameAction {

    // logging tag
    private static final String TAG = "FortifyAction";
    // serializable support
    // TODO Serializable support

    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    private Territory deployFrom;
    private Territory deployTo;
    private int numDeployed;

    public FortifyAction(GamePlayer player,Territory deployFrom,Territory deployTo, int numDeployed) {
        super(player);
        this.deployFrom = deployFrom;
        this.deployTo = deployTo;
        this.numDeployed = numDeployed;
    }

    public Territory getDeployTo() {
        return deployTo;
    }

    public int getNumDeployed() {
        return numDeployed;
    }

    public Territory getDeployFrom() {
        return deployFrom;
    }


}
