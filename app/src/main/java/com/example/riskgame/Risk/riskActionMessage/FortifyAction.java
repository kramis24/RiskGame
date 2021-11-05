package com.example.riskgame.Risk.riskActionMessage;

import com.example.riskgame.GameFramework.actionMessage.GameAction;
import com.example.riskgame.GameFramework.players.GamePlayer;
import com.example.riskgame.Risk.infoMessage.Territory;

public class FortifyAction extends GameAction {

    // logging tag
    private static final String TAG = "FortifyAction";
    // serializable support
    // TODO Serializable support

    private Territory territory, territory2;
    private int troop;

    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public FortifyAction(GamePlayer player, Territory terr, Territory terr2, int troopCnt) {
        super(player);
        this.territory = terr;
        this.territory2 = terr2;
        this.troop = troopCnt;
    }

    public Territory getTerritory() {
        return territory;
    }

    public Territory getTerritory2() {
        return territory2;
    }

    public int getTroop() {
        return troop;
    }

}
