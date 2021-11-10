package com.example.riskgame.Risk.riskActionMessage;
/**
 * FortifyAction
 * Action class for fortify move.
 *
 * @author Phi Nguyen
 * @version 11/7/2021
 */

import com.example.riskgame.GameFramework.actionMessage.GameAction;
import com.example.riskgame.GameFramework.players.GamePlayer;
import com.example.riskgame.Risk.infoMessage.Territory;

public class FortifyAction extends GameAction {

    // logging tag
    private static final String TAG = "FortifyAction";
    // serializable support
    // TODO Serializable support

    // member variables
    private Territory deployFrom;
    private Territory deployTo;
    private int numDeployed;

    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public FortifyAction(GamePlayer player,Territory deployFrom,Territory deployTo, int numDeployed) {
        super(player);
        this.deployFrom = deployFrom;
        this.deployTo = deployTo;
        this.numDeployed = numDeployed;
    }

    /**
     * Gets the territory being deployed to.
     *
     * @return destination territory
     */
    public Territory getDeployTo() {
        return deployTo;
    }

    /**
     * Gets the number of troops being transferred
     *
     * @return moving troops
     */
    public int getNumDeployed() {
        return numDeployed;
    }

    /**
     * Gets the territory being deployed from.
     *
     * @return origin territory.
     */
    public Territory getDeployFrom() {
        return deployFrom;
    }


}
