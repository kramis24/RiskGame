package com.example.riskgame.Risk.players;

import com.example.riskgame.GameFramework.infoMessage.GameInfo;
import com.example.riskgame.GameFramework.infoMessage.NotYourTurnInfo;
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
        if (info instanceof NotYourTurnInfo) return;
        //Logger.log("TTTComputer, "My Turn"");
        ///Deploy troops randomly t random territories
        //Hit next button when you can
        //randomly do action (either attack a random territoy or skip turn (hit next)
        //if attack is chosen -->randomly choose territory until chosen territory is owned by player
        //from chosen territory randomly choose territoy to attack
        //randomly choose nerihboring territory to attaxck
        //randomly choose to attack again (if nessicary) or not
        //iF WON A TERRITORY, RANDOMLY SELECTS A NUMBER OF TROOPS TO MOVE TO THAT LOCATION
        //go back and randomly select attack or skip again
        //CATCH CASE --> if there are less than two troops in all owned territories that have enemy territories ajacent --> automatically skip
        //When in fortify phase --> randomly slect to fortiy or not
        //If fortifing --> like attack randomly select two territores and aqmount of troops to move
        //then exit turn
    }
}
