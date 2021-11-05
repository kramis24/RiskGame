package com.example.riskgame.Risk.riskActionMessage;

import com.example.riskgame.GameFramework.actionMessage.GameAction;
import com.example.riskgame.GameFramework.players.GamePlayer;
import com.example.riskgame.Risk.infoMessage.Territory;

public class DeployAction extends GameAction {

    // logging tag
    private static final String TAG = "DeployAction";
    // serializable support
    // TODO Serializable support
    private Territory territory;
    private int troop;
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public DeployAction(GamePlayer player, Territory terr, int troopCnt) {
        super(player);
        this.territory = terr;
        this.troop = troopCnt;
    }

    public Territory getTerritory() {
        return territory;
    }

    public int getTroop() {
        return troop;
    }
}
