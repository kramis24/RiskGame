package com.example.riskgame.Risk.riskActionMessage;
/**
 * ExchangeCardAction
 * Action for exchanging cards.
 *
 * @author Phi Nguyen
 * @version 11/16/2021
 */

import com.example.riskgame.GameFramework.actionMessage.GameAction;
import com.example.riskgame.GameFramework.players.GamePlayer;

public class ExchangeCardAction extends GameAction {
    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public ExchangeCardAction(GamePlayer player) {
        super(player);
    }
}
