package com.example.riskgame.Risk.riskActionMessage;

import com.example.riskgame.GameFramework.actionMessage.GameAction;
import com.example.riskgame.GameFramework.players.GamePlayer;
import com.example.riskgame.Risk.infoMessage.Territory;

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
    private Territory atk;
    private Territory def;
    public AttackAction(GamePlayer player,Territory atk,Territory def) {
        super(player);
        this.atk = atk;
        this.def = def;
    }

    public Territory getAtk() {
        return this.atk;
    }

    public Territory getDef(){
        return this.def;
    }
}
