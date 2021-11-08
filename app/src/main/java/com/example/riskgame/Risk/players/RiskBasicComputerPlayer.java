package com.example.riskgame.Risk.players;

import com.example.riskgame.GameFramework.infoMessage.GameInfo;
import com.example.riskgame.GameFramework.infoMessage.NotYourTurnInfo;
import com.example.riskgame.GameFramework.players.GameComputerPlayer;
import com.example.riskgame.Risk.infoMessage.RiskGameState;
import com.example.riskgame.Risk.infoMessage.Territory;
import com.example.riskgame.Risk.riskActionMessage.AttackAction;
import com.example.riskgame.Risk.riskActionMessage.DeployAction;
import com.example.riskgame.Risk.riskActionMessage.FortifyAction;
import com.example.riskgame.Risk.riskActionMessage.NextTurnAction;

import java.util.ArrayList;
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

    //Basic varibles to keep track of what stages player has been through
    //
    private boolean fortify = false;
    private boolean attack = false;
    private boolean deploy = false;
    @Override
    protected void receiveInfo(GameInfo info) {



        if (info instanceof NotYourTurnInfo) return;

        if (info instanceof RiskGameState) {
            if(this.playerNum !=  ((RiskGameState) info).getCurrentTurn() ) {
                return;
            }
            RiskGameState Risk = (RiskGameState) info;
            //Catch case (in case player cannot move, all of this player owned territories have a troop set to 1)
            boolean canAttack = false;
            boolean canFortify = false;
            ArrayList<Territory> ownedTerr = new ArrayList<>(); //array of all of the player's owned territories
            for (int i = 0; i < Risk.getTerritories().size(); i ++) {
                if (Risk.getTerritories().get(i).getOwner() == this.playerNum) {
                    ownedTerr.add(Risk.getTerritories().get(i)); //if the territory is owned by the player, add it to the owned territory array
                    if (Risk.getTerritories().get(i).getTroops() > 1) { // if an owned territory has more than one troop in it could maybe make moves
                        for (int j = 0; j < Risk.getTerritories().get(i).getAdjacents().size(); j++) {
                            if (Risk.getTerritories().get(i).getAdjacents().get(j).getOwner() != this.playerNum) {
                                canAttack = true; //if any of the adjacent territories are enemies, player can attack
                            }
                            if (Risk.getTerritories().get(i).getAdjacents().get(j).getOwner() == this.playerNum) {
                                canFortify = true; //if any of the adjacent territories are owned by the player, the player can fortify
                            }
                        }
                    }
                }
            }

            Random rnd = new Random();
            int skip = rnd.nextInt(2);
            int loopCntr = 0;
            int ownTerr = rnd.nextInt(ownedTerr.size()); //random int for a terriroty owned by the player
            int terr = rnd.nextInt(ownedTerr.get(ownTerr).getAdjacents().size());
            int terr2 = rnd.nextInt(ownedTerr.size());

            //if (!fortify) { //if player hasn't fortified yet //not needed as once done fortifying turn ends
            if (!attack) { //if player hasn't finished attacking yet
                if (!deploy) { //if player hasn't finished deploying yet
                    while (Risk.getTotalTroops() != 0) { //while the troops aren't equal to zero
                        //if (Risk.deploy(Risk.getTerritories().get(terr), 1)) { //_>alternate way of choosing territories
                        if (Risk.deploy(ownedTerr.get(ownTerr), 1)) {
                            sleep(1);
                            game.sendAction(new DeployAction(this,ownedTerr.get(ownTerr),1));
                            return;//hopefully successfully deployed 1 troop, will deploy more next when method is called
                        }
                    }
                    if (Risk.getTotalTroops() == 0) { //if the total troops to deploy is now at zero
                        sleep(1);
                        game.sendAction(new NextTurnAction(this)); //go to next phase of the turn (attack)
                        deploy = true; //note that player has finished deploy phase
                        return;
                    }
                } // end deploy phase
                if (skip == 0 && canAttack) { //if chosen to attack and player can attack, try to attack
                    while (!Risk.attack(ownedTerr.get(ownTerr), ownedTerr.get(ownTerr).getAdjacents().get(terr))) { //keep selecting new territory matches until one works
                        if (loopCntr > 100) { //catch case in order to avoid endlessly looking for attack match --> skip attack
                            sleep(1);
                            game.sendAction(new NextTurnAction(this));//Go to next phase (Fortify)
                            attack = true; //change boolean attack to indicate player has finished attacking
                            return;
                        }
                        //generate new random territories to try attack method again
                        ownTerr = rnd.nextInt(ownedTerr.size());
                        terr = rnd.nextInt(ownedTerr.get(ownTerr).getAdjacents().size());
                        loopCntr++; //count number of attempts to try and attack
                    }
                    sleep(1);
                    game.sendAction(new AttackAction(this,ownedTerr.get(ownTerr),ownedTerr.get(ownTerr).getAdjacents().get(terr)));
                    return;
                }
                sleep(1);
                game.sendAction(new NextTurnAction(this));//Go to next phase (Fortify)
                attack = true; //change boolean attack to indicate player has finished attacking
                return;
            } //end attack phase
            if (skip == 0 && canFortify) { //If player has chosen not to skip and can fortify -->attempt to fortify
                int fortRnd = rnd.nextInt(ownedTerr.get(ownTerr).getTroops() - 2); //generate random num of troops to move (more than 0 and less than the total number of troops)
                while (!Risk.fortify(ownedTerr.get(ownTerr), ownedTerr.get(terr2), fortRnd + 1)) { //this moves all troops from one territory to antoher
                    if (loopCntr > 100) {
                        fortify = true; //change boolean fortify to true to indicate player is done with the fortify phase
                        return;
                    }
                    ownTerr = rnd.nextInt(ownedTerr.size());
                    terr2 = rnd.nextInt(ownedTerr.size());
                    fortRnd = rnd.nextInt(ownedTerr.get(ownTerr).getTroops() - 2); //generate randome num of troops to move (more than 0 and less than the total number of troops)
                    loopCntr++;
                }
                sleep(1);
                //return all marker booleans to original state
                deploy = false;
                attack = false;
                game.sendAction(new FortifyAction(this,ownedTerr.get(ownTerr),ownedTerr.get(terr2),fortRnd + 1)); // end fortify phase and turn
                return;
            }
            //return all marker booleans to original state
            deploy = false;
            attack = false;
            sleep(1);
            game.sendAction(new NextTurnAction(this)); //End player's turn
            return;
        }
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