package com.example.riskgame.Risk.players;

import com.example.riskgame.GameFramework.infoMessage.GameInfo;
import com.example.riskgame.GameFramework.players.GameComputerPlayer;

public class RiskBasicComputerPlayer extends GameComputerPlayer {
    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     */
    public RiskBasicComputerPlayer(String name) {
        super(name);
    }

    @Override
    protected void receiveInfo(GameInfo info) {

    }
}
