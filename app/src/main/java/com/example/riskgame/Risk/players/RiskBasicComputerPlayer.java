package com.example.riskgame.Risk.players;

import com.example.riskgame.GameFramework.infoMessage.GameInfo;
import com.example.riskgame.GameFramework.infoMessage.NotYourTurnInfo;
import com.example.riskgame.GameFramework.players.GameComputerPlayer;
import com.example.riskgame.Risk.infoMessage.RiskGameState;
import com.example.riskgame.Risk.riskActionMessage.AttackAction;
import com.example.riskgame.Risk.riskActionMessage.DeployAction;
import com.example.riskgame.Risk.riskActionMessage.FortifyAction;
import com.example.riskgame.Risk.riskActionMessage.NextTurnAction;

import java.util.Random;

public class RiskBasicComputerPlayer extends GameComputerPlayer {
    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     */
    public RiskBasicComputerPlayer(String name) {
        super(name);
    }

    private boolean fortify = false;
    private boolean attack = false;
    private boolean deploy = false;

    @Override
    protected void receiveInfo(GameInfo info) {


        if (info instanceof NotYourTurnInfo) return;

        if (info instanceof RiskGameState) {


            RiskGameState Risk = (RiskGameState) info;
            Random rnd = new Random();
            int terr = rnd.nextInt(42);
            int skip = rnd.nextInt(2);
            //Catch case (in case player cannot move, all of this player owned territories have a troop set to 1)
            boolean canMove = true;
            for (int i = 0; i < Risk.getTerritories().size(); i ++) {
                if (Risk.getTerritories().get(i).getOwner() == this.playerNum) {
                    if (Risk.getTerritories().get(i).getTroops() == 1) {
                        canMove = false;
                    }
                }
            }


            if (!fortify) {
                if (!attack) {
                    if (!deploy) {
                        int deployTroopTotal = Risk.calcTroops(this.playerNum); //edit how many troop remain
                        while (Risk.getTotalTroops != 0) { //create method to ge the total troops
                            ///Deploy each troop to random territories //if statement?
                            if (Risk.deploy(Risk.getTerritories().get(terr), 1)) {
                                game.sendAction(new DeployAction(this,Risk.getTerritories().get(terr),1));
                                return;//hopefully successfully deployed 1 troop, will deploy more next when method is called
                            }
                        }
                        if (deployTroopTotal == 0 && Risk.getCurrentPhase() == RiskGameState.Phase.DEPLOY) { //need to write current phase method
                            //do we even need to check the phase?
                            game.sendAction(new NextTurnAction(this));
                            //change boolean to true?
                            deploy = true;
                            return;
                        }
                    } // end deploy phase

                    if (skip == 0 && canMove) { //keep attacking until one succesful attack --> once sucess randomly choose too attack again or skip?
                        //do i need some sort of recursive call?
                        //

                        while (!Risk.attack(Risk.getTerritories().get(terr), Risk.getTerritories().get(terr))) { //will this keep trying to attack until sucessful?
                            game.sendAction(new AttackAction(this,Risk.getTerritories().get(terr),Risk.getTerritories().get(terr)));
                            return;
                        } //CATCH CASE --> if there are less than two troops in all owned territories that have enemy territories ajacent --> automatically skip
                        //DONT NEED SKIP OR ATTACK METHOD BECASUE WE RETURN AFTER EACH ACTION AND THEN CALL
                        //skipOrAttack(rnd.nextInt(2),Risk);
                        //now recursivly call it and randomly decide to attack again or skip turn
                    }
                    game.sendAction(new NextTurnAction(this));//Hit next
                    //change boolean attack to true
                    attack = true;
                    return;
                } //end attack phase
                if (skip == 0 && canMove) {
                    //Keep trying to fortifey until sucess
                    //generate nadom int --> set it to a variable use it for fortifey
                    while (!Risk.fortify(Risk.getTerritories().get(terr), Risk.getTerritories().get(terr), Risk.getTerritories().get(terr).getTroops() - 1)) { //this moves all troops from one territory to antoher
                        game.sendAction(new FortifyAction(this,Risk.getTerritories().get(terr),Risk.getTerritories().get(terr),Risk.getTerritories().get(terr).getTroops() - 1)); //change troop count to random at some point
                        //change boolean fortify to true
                        fortify = true;
                        return;
                    } //CATCH CASE --> if there are less than two troops in all owned territories that are connected to any other owned territories --> automatically skip
                }
                //change boolean fortify to true
                fortify = true;
                //Reset all boolean for when it is the com's turn again?
                return;
            }// end fortify phase

            deploy = false;
            attack = false;
            fortify = false;
            game.sendAction(new NextTurnAction(this));
            return;

        }
    }

    //is this method no longer needed?
    public void skipOrAttack(int skip, RiskGameState Risk) {
        Random rnd = new Random();
        int terr = rnd.nextInt(42);
        if (skip == 0) { //keep attacking until one succesful attack --> once sucess randomly choose too attack again or skip?
            //do i need some sort of recursive call?
            while (!Risk.attack(Risk.getTerritories().get(terr), Risk.getTerritories().get(terr))) { //will this keep trying to attack until sucessful?
                game.sendAction(new AttackAction(this,Risk.getTerritories().get(terr),Risk.getTerritories().get(terr)));
                return;
            } //CATCH CASE --> if there are less than two troops in all owned territories that have enemy territories ajacent --> automatically skip

            skipOrAttack(rnd.nextInt(2),Risk);
            //now recursivly call it and randomly decide to attack again or skip turn
        }
        Risk.nextTurn();//Hit next
    }



    //Logger.log("TTTComputer, "My Turn"");
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
