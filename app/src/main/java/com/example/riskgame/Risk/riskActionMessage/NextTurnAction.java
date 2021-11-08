package com.example.riskgame.Risk.riskActionMessage;
/**
 * NextTurnAction
 * Action class for advancing turns.
 *
 * @author Charlie Benning
 * @version 11/7/2021
 */
import com.example.riskgame.GameFramework.actionMessage.GameAction;
import com.example.riskgame.GameFramework.players.GamePlayer;

public class NextTurnAction extends GameAction {

    /**
     * constructor for GameAction
     *
     * @param player the player who created the action
     */
    public NextTurnAction(GamePlayer player) {
        super(player);
    }
}
