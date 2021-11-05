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

    private Territory territory, territory2;

    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public AttackAction(GamePlayer player, Territory terr, Territory terr2) {
        super(player);
        this.territory = terr;
        this.territory2 = terr2;

    }

    public Territory getTerritory() {
        return territory;
    }

    public  Territory getTerritory2() {
        return territory2;
    }
}
