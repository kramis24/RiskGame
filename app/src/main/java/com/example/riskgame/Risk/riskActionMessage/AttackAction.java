package com.example.riskgame.Risk.riskActionMessage;
/**
 * AttackAction
 * Action class for fortify move.
 *
 * @author Phi Nguyen
 * @version 11/7/2021
 */
import com.example.riskgame.GameFramework.actionMessage.GameAction;
import com.example.riskgame.GameFramework.players.GamePlayer;
import com.example.riskgame.Risk.infoMessage.Territory;

import java.io.Serializable;


public class AttackAction extends GameAction {

    // logging tag
    private static final String TAG = "AttackAction";
    // serializable support
    // TODO Serializable support

    //member variables
    private Territory atk;
    private Territory def;

    /**
     * Constructor for AttackAction.
     *
     * @param player player sending action
     * @param atk attacking territory
     * @param def defending territory
     */
    public AttackAction(GamePlayer player,Territory atk,Territory def) {
        super(player);
        this.atk = atk;
        this.def = def;
    }

    /**
     * Gets attacking territory.
     *
     * @return attacker
     */
    public Territory getAtk() {
        return this.atk;
    }

    /**
     * Gets defending territory.
     *
     * @return defender
     */
    public Territory getDef(){
        return this.def;
    }
}
