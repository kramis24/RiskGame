package com.example.riskgame.Risk.riskActionMessage;

import com.example.riskgame.GameFramework.actionMessage.GameAction;
import com.example.riskgame.GameFramework.players.GamePlayer;

import java.io.Serializable;


public class AttackAction extends GameAction {

    // logging tag
    private static final String TAG = "AttackAction";
    // serializable support
    // TODO Serializable support

    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public AttackAction(GamePlayer player) {
        super(player);
    }
}
