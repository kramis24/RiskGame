package com.example.riskgame.Risk.riskActionMessage;
/**
 * DeployAction
 * Action class for deploy move.
 *
 * @author Phi Nguyen
 * @version 11/7/2021
 */
import com.example.riskgame.GameFramework.actionMessage.GameAction;
import com.example.riskgame.GameFramework.players.GamePlayer;
import com.example.riskgame.Risk.infoMessage.Territory;

public class DeployAction extends GameAction {

    // logging tag
    private static final String TAG = "DeployAction";
    // serializable support
    // TODO Serializable support
    // member variables
    private Territory deployTo;
    private int numDeploy;

    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public DeployAction(GamePlayer player,Territory deployTo, int numDeploy) {
        super(player);
        this.deployTo = deployTo;
        this.numDeploy = numDeploy;
    }

    /**
     * Gets the number of troops being deployed
     *
     * @return number of troops deployed
     */
    public int getNumDeploy() {
        return numDeploy;
    }

    /**
     * Gets territory being deployed to.
     *
     * @return destination territory
     */
    public Territory getDeployTo() {
        return deployTo;
    }
}
