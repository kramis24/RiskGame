package com.example.riskgame.Risk.riskActionMessage;

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
