package com.example.riskgame.Risk.riskActionMessage;

import com.example.riskgame.GameFramework.actionMessage.GameAction;
import com.example.riskgame.GameFramework.players.GamePlayer;

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
    public DeployAction(GamePlayer player) {
        super(player);
    }
}
