package com.example.riskgame.Risk.riskActionMessage;

import com.example.riskgame.GameFramework.actionMessage.GameAction;
import com.example.riskgame.GameFramework.players.GamePlayer;

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
    public FortifyAction(GamePlayer player) {
        super(player);
    }
}
