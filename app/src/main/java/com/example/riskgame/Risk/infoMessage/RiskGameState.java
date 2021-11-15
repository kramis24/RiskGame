package com.example.riskgame.Risk.infoMessage;
/**
 * RiskGameState
 * Game state variables and methods for Risk game.
 *
 * @author Phi Nguyen, Dylan Kramis, Charlie Benning
 * @version 11/14/2021
 */

import com.example.riskgame.GameFramework.infoMessage.GameState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class RiskGameState extends GameState {

    /**
     * Phase
     * Indicates turn phase.
     */
    public enum Phase {
        DEPLOY,
        ATTACK,
        FORTIFY
    }

    /**
     * Card
     * Indicates card types.
     *
     */
    public enum Card {
        INFANTRY,
        ARTILLERY,
        CAVALRY
    }

    // instance variables
    private int playerCount = 2;
    private int currentTurn = 0;
    private String playerNames[];
    private Phase currentPhase = Phase.DEPLOY;
    private int totalTroops = 100;
    private ArrayList<Territory> territories;
    private List<ArrayList<Card>> cards = new ArrayList<ArrayList<Card>>();

    /**
     * Default constructor for RiskGameState.
     */
    public RiskGameState() {

        // initialize territories array list
        territories = new ArrayList<Territory>();

        // initialize territories and add adjacents to each territory
        initTerritories();

        setTerritoryPlayers();
        setStartTroops();
        totalTroops = calcTroops(0);
        for(int i = 0; i < playerCount; i++) {
            cards.add(new ArrayList<Card>());
        }
    }

    public Phase getCurrentPhase() {
        return this.currentPhase;
    }

    /**
     * Copy constructor for RiskGameState
     *
     * @param other game state being copied
     */
    public RiskGameState(RiskGameState other) {

        // initialize territories array list
        territories = new ArrayList<Territory>();

        // copying variables
        this.currentTurn = other.currentTurn;
        this.playerCount = other.playerCount;
        this.currentPhase = other.currentPhase;
        this.totalTroops = other.totalTroops;
        for (Territory t : other.territories) {
            Territory newTerritory = new Territory(t);
            this.territories.add(newTerritory);
        }
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    /**
     * calcTroops
     * Calculates the number of troops given to each player at the start of their deploy phase
     * @param player player starting turn
     * @return the number of troops to deploy fo each player
     **/
    public int calcTroops(int player) {
        int territoryCount = 0; //set counter for player territories
        int[] territoryCounts = new int[6]; //set counter for territories for each Continent
        for (int i = 0; i < territories.size(); i++) {
            if (territories.get(i).getOwner() == player) { //if a territory is owned by a player add to territory count
                territoryCount++;
                territoryCounts[territories.get(i).getContinent().ordinal()]++; //count number of territories owned in each continent
            }
        }

        int troopCount = ((territoryCount)/3); //calculation for troops

        //check for continent bonuses (if a player has all territories in a continent)
        if (territoryCounts[Territory.Continent.ASIA.ordinal()] == 12) {
            troopCount = troopCount + 7;
        }
        if (territoryCounts[Territory.Continent.AFRICA.ordinal()] == 6) {
            troopCount = troopCount + 3;
        }
        if (territoryCounts[Territory.Continent.SOUTH_AMERICA.ordinal()] == 4) {
            troopCount = troopCount + 2;
        }
        if (territoryCounts[Territory.Continent.NORTH_AMERICA.ordinal()] == 9) {
            troopCount = troopCount + 5;
        }
        if (territoryCounts[Territory.Continent.EUROPE.ordinal()] == 7) {
            troopCount = troopCount + 5;
        }
        if (territoryCounts[Territory.Continent.OCEANIA.ordinal()] == 4) {
            troopCount = troopCount + 2;
        }

        if (troopCount < 3) {
            troopCount = 3;
        }
        return troopCount; //return number of troops given at the start of the round
    }

    /**
     * addTroops
     * adds a given number of troops to a given territory
     *
     * @param t territory selected
     * @param add troops being added
     */
    public void addTroop(Territory t, int add) {
        t.setTroops(t.getTroops() + add); //add a given number of troops to a territories
    }

    /**
     * setTerritoryPlayers
     * Sets up the map for risk gameplay by randomly assigning territories to each player
     */
    public void setTerritoryPlayers () {
            ArrayList<Integer> tempTerr = new ArrayList<>(); //create temp list for territory's indexes
            for (int i = 0; i < territories.size(); i++) {
                tempTerr.add(i); //set index number for each territory
            }
            Collections.shuffle(tempTerr); //shuffle the index numbers of territories
            for (int i = 0; i < tempTerr.size(); i++) {  //cycling through players till there are no territories are left
                territories.get(tempTerr.get(i)).setOwner(i%playerCount); //set a random territory to players
                territories.get(tempTerr.get(i)).setTroops(1); //set each newly territory to 1 troop (each territory must have a troop)
            }
        }

    /**
     * setStartTroops
     * Randomly deploys each player's starting troops to their territories
     * FIX: method causes program to freeze and crash, no error message printed
     */
    public void setStartTroops() {
        int[] troopsDeployed = new int[playerCount]; //create array to store each player's troop deployment number
        int startTroops = (50 - (5 *(playerCount))); //set the number of troops that each player gets to start (dependant on number of players)
        Random rnd = new Random();
        for (int i = 0; i < territories.size(); i++) { //for each territory owned by a player,
            //add 1 to their deployed troops count (as each initialized territory was given one troop automatically
            troopsDeployed[territories.get(i).getOwner()]++;
        }
        for (int i = 0; i < troopsDeployed.length; i++) {
            while (troopsDeployed[i] < startTroops) { //while each player has not yet deployed given number of troops
                int rNum = rnd.nextInt(territories.size());
                if (territories.get(rNum).getOwner() == i) { //deploy a troop to a random territory owned by the selected player
                    addTroop(territories.get(rNum), 1);
                    troopsDeployed[i]++; //increase deployed troop count for this player
                }
            }
        }
    }

    /**
     * attack
     * Attacks another territory from one owned by the player.
     * LOGIC NEEDS CLARIFICATION!
     *
     * @param atk attacking territory
     * @param def defending territory
     * @return true if legal move
     */
    public boolean attack(Territory atk, Territory def) {
        if (currentTurn == atk.getOwner() && currentTurn != def.getOwner()) {
            //checks that the player is not trying to attack themselves
            boolean adjacent = false;
            for(Territory t: atk.getAdjacents()) {
                if(def.equals(t)) {
                    adjacent = true;
                }
            }
            if (adjacent) { //checks if two territories are adjacent
                int numRollsAtk;
                int numRollsDef;

                //determines how many die the attacker has
                if (atk.getTroops() >= 4) {
                    numRollsAtk = 3;
                } else if (atk.getTroops() >= 3) {
                    numRollsAtk = 2;
                } else {
                    numRollsAtk = 1;
                }

                //determines how many die the defender has
                if (def.getTroops() >= 2) {
                    numRollsDef = 2;
                } else {
                    numRollsDef = 1;
                }

                //stores the die rolls into arraylist
                ArrayList<Integer> rollsAtk = rollDie(numRollsAtk);
                ArrayList<Integer> rollsDef = rollDie(numRollsDef);

                //sorts the die rolls from greatest to least
                Collections.sort(rollsAtk);
                Collections.sort(rollsDef);
                Collections.reverse(rollsAtk);
                Collections.reverse(rollsDef);
                //compares the die rolls of the two players to determine how many comparisons to do
                if (numRollsAtk == 1) {
                    numRollsDef = numRollsAtk;
                }

                //compares the highest rolls of the players to determine how many troops they lose
                for (int i = 0; i < numRollsDef; i++) {
                    if (rollsAtk.get(i) > rollsDef.get(i)) {
                        def.setTroops(def.getTroops() - 1);
                    } else if (rollsDef.get(i) >= rollsAtk.get(i)) {
                        atk.setTroops(atk.getTroops() - 1);
                    }
                }

                //if changes ownership of a territory if troops are 0
                if (def.getTroops() <= 0) {
                    def.setOwner(atk.getOwner());
                    addCard();
                    def.setTroops(1);
                    atk.setTroops(atk.getTroops() - 1);
                    def.highlightMoved = true;
                }
                atk.highlightMoved = true;
                return true;
            }
            return false;
        } else {
            return false;
        }
    }

    /**
     * deploy
     * Adds troops to territories.
     *
     * @param t territory getting troops
     * @param troops number of troops
     * @return true if move was legal
     **/
    //for occupy change total troops to terriories troop
    public boolean deploy(Territory t, int troops) {
        if(currentTurn == t.getOwner()) { //checks that the current territory is owned by the player
            //if owner matches
            if (troops >= 0 && troops <= totalTroops) {
                addTroop(t, troops);
                t.highlightMoved = true;
                return true;
            }
        }
        return false;
    }

    public boolean occupy(Territory t, Territory t2, int troops) {
        if (currentTurn == t.getOwner()) { //checks that the current territory is owned by the player
            if (troops > 0 && troops < t.getTroops()) {
                t2.setTroops(troops);
                t.setTroops(t.getTroops() - troops);
                return true;
            }
        }
        return false;
    }


    /**
     * fortify
     * ADD: add the ability to move through connected territories Probably Hardest part of fortify method
     * Moves troops from one territory to another.
     *
     * @param t1 origin territory
     * @param t2 destination territory
     * @param troops number of troops being moved
     * @return true if move was done successfully
     **/
    public boolean fortify(Territory t1, Territory t2, int troops) {
        if (currentTurn == t1.getOwner() && currentTurn == t2.getOwner()) { //checks if both territories are owned by player
            if(checkChain(t1,t2)) {
                if (troops > 0) {
                    if (t1.getTroops() - troops > 1) { //makes sure that you cannot send more troops than you have
                        t1.setTroops(t1.getTroops() - troops);
                        t2.setTroops(t2.getTroops() + troops);
                        nextTurn();
                        t1.highlightMoved = true;
                        t2.highlightMoved = false;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * checkChain
     * Checks if two territories are connected by a chain of territories that the player has
     * Parameters: Territory (starting chain), Territory (ending part of chain)
     * @return
     */
    public boolean checkChain(Territory t1, Territory t2) {
        if (t1.getOwner() == t2.getOwner()) { //check if the territories are owned by the same player, if not return false
            for (int i = 0; i < territories.size(); i++) { //set all territories to unchecked (haven't been looked at yet)
                territories.get(i).checked = false;
            }
            return checkHelper(t1, t2); //call helper method to search through adjacent arrays
        }
        return false;
    }

    /**
     * checkHelper
     * Helper method to check if two territories create a chain of adjacent territories that connect
     * @return
     */
    private boolean checkHelper(Territory t1, Territory t2) {
        boolean ans = false; ///assume both territories aren't connected
        for (int i = 0; i < t1.getAdjacents().size(); i++) {
            if (t1.getAdjacents().get(i).getOwner() == t1.getOwner()) {
                //if adjacent is owned by the same player as initial territory
                if (!t1.getAdjacents().get(i).checked) { //check to see if this territory has been scanned (for being in the chain) yet
                    t1.getAdjacents().get(i).checked = true; //this territory will now be scanned for being in the chain
                    if (t1.getAdjacents().get(i).equals(t2)) {
                        return true; //if adjacent territory is the territory that is the end of the chain, return true
                    } else {
                        if (checkHelper(t1.getAdjacents().get(i), t2)) { //recursively call this function and ask if it returns true
                            ans = true;
                        }
                    }
                }
            }
        }
        return ans; //return if territories are connected by a chain
    }

    //
    /* No GUI yet so these methods cannot be implemented
    public void viewStats() {
    }

    public void viewHelp() {
    }

    public void viewCards() {
    }
    */

    /**
     * nextTurn
     * Advances turn/phase.
     *
     * @return true if turn was advanced
    **/
    public boolean nextTurn() {

        // iteration through phases
        if (currentPhase == Phase.DEPLOY) {
            currentPhase = Phase.ATTACK;
        }
        else if (currentPhase == Phase.ATTACK) {
            currentPhase = Phase.FORTIFY;
        }
        else {
            currentPhase = Phase.DEPLOY;
            currentTurn++;// end of turn
            // iteration through players
            if(currentTurn % playerCount == 0) {
                currentTurn = 0;
            }
            totalTroops = calcTroops(currentTurn); //gives the player a determined amount of troops.
        }
        return true;
    }

    public void setTotalTroops(int set) {
        this.totalTroops = set;
    }

    /**
     * rollDie
     * Rolls dice.
     *
     * @param numRolls number of dice to be rolled
     * @return array with rolls in it
     **/
    public ArrayList<Integer> rollDie(int numRolls) {

        // initializes roll list
        ArrayList<Integer> rolls = new ArrayList<>();

        // loops to roll each die
        for (int i = 0; i < numRolls; i++) {
            Random die = new Random();
            int number = die.nextInt(6);
            rolls.add(number);
        }

        return rolls;
    }

    @Override
    /**
     * toString
     * Returns all the information about the current game state in a string.
     *
     * @return game state info
     */
    public String toString() {
        String info = "-----CURRENT GAME STATE----- \n";
        info = info + "____________________________ \n";
        info = info + "Current Phase: " + currentPhase + "\n" + "Current Turn: " + currentTurn + "\n";
        info = info + "____________________________ \n";
        for (Territory t : territories) {
            info = info + "Territory: " + t.getName() + "\n";
            info = info + "Continent: " + t.getContinent() + "\n";
            info = info + "Number of Troops: " + t.getTroops() + "\n";
            info = info + "Owner: Player " + t.getOwner() + "\n";
            info = info + "________________________________ \n";
        }

        return info;
    }

    /**
     * getT
     * Gets the list of territories.
     *
     * @return territories array list
     */
    public ArrayList<Territory> getTerritories() {
        return this.territories;
    }

    /**
     * getCurrentTurn
     * Gets the current turn.
     *
     * @return current turn
     */
    public int getCurrentTurn() { return currentTurn; }

    /**
     * getCurrentPhase
     * Gets the current phase.
     *
     * @return current phase
     */

    /**
     * getTotalTroops
     * Gets the total troops.
     *
     * @return total troops
     */
    public int getTotalTroops() { return totalTroops; }

    /**
     * initTerritories
     * LONG helper method for constructor, initializes each territory.
     */
    private void initTerritories() {

        // Tribelhorn Approved this message
        // initialize each territory then add it to the list
        Territory alaska = new Territory(Territory.Continent.NORTH_AMERICA, "Alaska", 230, 350);
        territories.add(alaska);

        Territory northWestTerritory = new Territory(Territory.Continent.NORTH_AMERICA,
                "North West Territory", 500, 350);
        territories.add(northWestTerritory);

        Territory greenland = new Territory(Territory.Continent.NORTH_AMERICA, "Greenland",
                1050, 250);
        territories.add(greenland);

        Territory alberta = new Territory(Territory.Continent.NORTH_AMERICA, "Alberta",
                470, 500);
        territories.add(alberta);

        Territory ontario = new Territory(Territory.Continent.NORTH_AMERICA, "Ontario",
                670, 550);
        territories.add(ontario);

        Territory quebec = new Territory(Territory.Continent.NORTH_AMERICA, "Quebec",
                870, 550);
        territories.add(quebec);

        Territory westernUnitedStates = new Territory(Territory.Continent.NORTH_AMERICA,
                "Western United States", 500, 700);
        territories.add(westernUnitedStates);

        Territory easternUnitedStates = new Territory(Territory.Continent.NORTH_AMERICA,
                "Eastern United States", 700, 750);
        territories.add(easternUnitedStates);

        Territory centralAmerica = new Territory(Territory.Continent.NORTH_AMERICA,
                "Central America", 500, 950);
        territories.add(centralAmerica);

        Territory venezuela = new Territory(Territory.Continent.SOUTH_AMERICA, "Venezuela",
                750, 1130);
        territories.add(venezuela);

        Territory peru = new Territory(Territory.Continent.SOUTH_AMERICA, "Peru",
                750, 1400);
        territories.add(peru);

        Territory brazil = new Territory(Territory.Continent.SOUTH_AMERICA, "Brazil",
                950, 1350);
        territories.add(brazil);

        Territory argentina = new Territory(Territory.Continent.SOUTH_AMERICA, "Argentina",
                800, 1600);
        territories.add(argentina);

        Territory iceland = new Territory(Territory.Continent.EUROPE, "Iceland",
                1270, 440);
        territories.add(iceland);

        Territory scandinavia = new Territory(Territory.Continent.EUROPE, "Scandinavia",
                1480, 470);
        territories.add(scandinavia);

        Territory ukraine = new Territory(Territory.Continent.EUROPE, "Ukraine",
                1750, 600);
        territories.add(ukraine);

        Territory greatBritain = new Territory(Territory.Continent.EUROPE, "Great Britain",
                1260, 700);
        territories.add(greatBritain);

        Territory northernEurope = new Territory(Territory.Continent.EUROPE, "Northern Europe",
                1500, 700);
        territories.add(northernEurope);

        Territory westernEurope = new Territory(Territory.Continent.EUROPE, "Western Europe",
                1300, 900);
        territories.add(westernEurope);

        Territory southernEurope = new Territory(Territory.Continent.EUROPE, "Southern Europe",
                1520, 870);
        territories.add(southernEurope);

        Territory northAfrica = new Territory(Territory.Continent.AFRICA, "North Africa",
                1400, 1250);
        territories.add(northAfrica);

        Territory congo = new Territory(Territory.Continent.AFRICA, "Congo", 1620, 1500);
        territories.add(congo);

        Territory southAfrica = new Territory(Territory.Continent.AFRICA, "South Africa",
                1650, 1770);
        territories.add(southAfrica);

        Territory madagascar = new Territory(Territory.Continent.AFRICA, "Madagascar",
                1900, 1770);
        territories.add(madagascar);

        Territory eastAfrica = new Territory(Territory.Continent.AFRICA, "East Africa",
                1720, 1370);
        territories.add(eastAfrica);

        Territory egypt = new Territory(Territory.Continent.AFRICA, "Egypt",
                1620, 1170);
        territories.add(egypt);

        Territory middleEast = new Territory(Territory.Continent.ASIA, "Middle East",
                1850, 1020);
        territories.add(middleEast);

        Territory afghanistan = new Territory(Territory.Continent.ASIA, "Afghanistan",
                2020, 770);
        territories.add(afghanistan);

        Territory ural = new Territory(Territory.Continent.ASIA, "Ural", 2060, 500);
        territories.add(ural);

        Territory siberia = new Territory(Territory.Continent.ASIA, "Siberia", 2220, 400);
        territories.add(siberia);

        Territory yakutsk = new Territory(Territory.Continent.ASIA, "Yakutsk", 2450, 300);
        territories.add(yakutsk);

        Territory kamchatka = new Territory(Territory.Continent.ASIA, "Kamchatka",
                2680, 320);
        territories.add(kamchatka);

        Territory irkutsk = new Territory(Territory.Continent.ASIA, "Irkutsk", 2400, 520);
        territories.add(irkutsk);

        Territory japan = new Territory(Territory.Continent.ASIA, "Japan", 2720, 770);
        territories.add(japan);

        Territory mongolia = new Territory(Territory.Continent.ASIA, "Mongolia", 2440, 720);
        territories.add(mongolia);

        Territory china = new Territory(Territory.Continent.ASIA, "China", 2350, 920);
        territories.add(china);

        Territory india = new Territory(Territory.Continent.ASIA, "India", 2180, 1050);
        territories.add(india);

        Territory siam = new Territory(Territory.Continent.ASIA, "Siam", 2400, 1150);
        territories.add(siam);

        Territory indonesia = new Territory(Territory.Continent.OCEANIA, "Indonesia",
                2460, 1470);
        territories.add(indonesia);

        Territory newGuinea = new Territory(Territory.Continent.OCEANIA, "New Guinea",
                2720, 1400);
        territories.add(newGuinea);

        Territory easternAustralia = new Territory(Territory.Continent.OCEANIA,
                "Eastern Australia", 2600, 1750);
        territories.add(easternAustralia);

        Territory westernAustralia = new Territory(Territory.Continent.OCEANIA,
                "Western Australia", 2800, 1700);
        territories.add(westernAustralia);

        // adding adjacents for each territory
        westernAustralia.addAdjacent(indonesia);
        westernAustralia.addAdjacent(easternAustralia);
        westernAustralia.addAdjacent(newGuinea);

        easternAustralia.addAdjacent(westernAustralia);
        easternAustralia.addAdjacent(newGuinea);

        newGuinea.addAdjacent(easternAustralia);
        newGuinea.addAdjacent(westernAustralia);
        newGuinea.addAdjacent(indonesia);

        indonesia.addAdjacent(newGuinea);
        indonesia.addAdjacent(westernAustralia);
        indonesia.addAdjacent(siam);

        siam.addAdjacent(indonesia);
        siam.addAdjacent(india);
        siam.addAdjacent(china);

        india.addAdjacent(siam);
        india.addAdjacent(china);
        india.addAdjacent(afghanistan);
        india.addAdjacent(middleEast);

        china.addAdjacent(siam);
        china.addAdjacent(india);
        china.addAdjacent(afghanistan);
        china.addAdjacent(ural);
        china.addAdjacent(siberia);
        china.addAdjacent(mongolia);

        mongolia.addAdjacent(china);
        mongolia.addAdjacent(siberia);
        mongolia.addAdjacent(irkutsk);
        mongolia.addAdjacent(kamchatka);
        mongolia.addAdjacent(japan);

        japan.addAdjacent(mongolia);
        japan.addAdjacent(kamchatka);

        irkutsk.addAdjacent(siberia);
        irkutsk.addAdjacent(yakutsk);
        irkutsk.addAdjacent(kamchatka);
        irkutsk.addAdjacent(mongolia);

        kamchatka.addAdjacent(yakutsk);
        kamchatka.addAdjacent(irkutsk);
        kamchatka.addAdjacent(mongolia);
        kamchatka.addAdjacent(japan);
        kamchatka.addAdjacent(alaska);

        yakutsk.addAdjacent(kamchatka);
        yakutsk.addAdjacent(irkutsk);
        yakutsk.addAdjacent(siberia);

        siberia.addAdjacent(yakutsk);
        siberia.addAdjacent(irkutsk);
        siberia.addAdjacent(mongolia);
        siberia.addAdjacent(china);
        siberia.addAdjacent(ural);

        ural.addAdjacent(siberia);
        ural.addAdjacent(china);
        ural.addAdjacent(afghanistan);
        ural.addAdjacent(ukraine);

        afghanistan.addAdjacent(ural);
        afghanistan.addAdjacent(china);
        afghanistan.addAdjacent(india);
        afghanistan.addAdjacent(middleEast);
        afghanistan.addAdjacent(ukraine);

        middleEast.addAdjacent(afghanistan);
        middleEast.addAdjacent(india);
        middleEast.addAdjacent(eastAfrica);
        middleEast.addAdjacent(egypt);
        middleEast.addAdjacent(southernEurope);
        middleEast.addAdjacent(ukraine);

        egypt.addAdjacent(middleEast);
        egypt.addAdjacent(eastAfrica);
        egypt.addAdjacent(northAfrica);
        egypt.addAdjacent(southernEurope);

        eastAfrica.addAdjacent(middleEast);
        eastAfrica.addAdjacent(egypt);
        eastAfrica.addAdjacent(northAfrica);
        eastAfrica.addAdjacent(congo);
        eastAfrica.addAdjacent(southAfrica);
        eastAfrica.addAdjacent(madagascar);

        madagascar.addAdjacent(eastAfrica);
        madagascar.addAdjacent(southAfrica);

        southAfrica.addAdjacent(congo);
        southAfrica.addAdjacent(madagascar);
        southAfrica.addAdjacent(eastAfrica);

        congo.addAdjacent(northAfrica);
        congo.addAdjacent(southAfrica);
        congo.addAdjacent(eastAfrica);

        northAfrica.addAdjacent(westernEurope);
        northAfrica.addAdjacent(southernEurope);
        northAfrica.addAdjacent(egypt);
        northAfrica.addAdjacent(eastAfrica);
        northAfrica.addAdjacent(congo);
        northAfrica.addAdjacent(brazil);

        westernEurope.addAdjacent(northAfrica);
        westernEurope.addAdjacent(southernEurope);
        westernEurope.addAdjacent(northernEurope);
        westernEurope.addAdjacent(greatBritain);

        southernEurope.addAdjacent(middleEast);
        southernEurope.addAdjacent(egypt);
        southernEurope.addAdjacent(northAfrica);
        southernEurope.addAdjacent(westernEurope);
        southernEurope.addAdjacent(northernEurope);
        southernEurope.addAdjacent(ukraine);

        ukraine.addAdjacent(ural);
        ukraine.addAdjacent(afghanistan);
        ukraine.addAdjacent(middleEast);
        ukraine.addAdjacent(southernEurope);
        ukraine.addAdjacent(northernEurope);
        ukraine.addAdjacent(scandinavia);

        scandinavia.addAdjacent(ukraine);
        scandinavia.addAdjacent(northernEurope);
        scandinavia.addAdjacent(greatBritain);
        scandinavia.addAdjacent(iceland);

        northernEurope.addAdjacent(scandinavia);
        northernEurope.addAdjacent(ukraine);
        northernEurope.addAdjacent(southernEurope);
        northernEurope.addAdjacent(westernEurope);
        northernEurope.addAdjacent(greatBritain);

        greatBritain.addAdjacent(westernEurope);
        greatBritain.addAdjacent(northernEurope);
        greatBritain.addAdjacent(scandinavia);
        greatBritain.addAdjacent(iceland);

        iceland.addAdjacent(scandinavia);
        iceland.addAdjacent(greatBritain);
        iceland.addAdjacent(greenland);

        greenland.addAdjacent(iceland);
        greenland.addAdjacent(northWestTerritory);
        greenland.addAdjacent(ontario);
        greenland.addAdjacent(quebec);

        northWestTerritory.addAdjacent(greenland);
        northWestTerritory.addAdjacent(alaska);
        northWestTerritory.addAdjacent(alberta);
        northWestTerritory.addAdjacent(ontario);

        alaska.addAdjacent(kamchatka);
        alaska.addAdjacent(northWestTerritory);
        alaska.addAdjacent(alberta);

        alberta.addAdjacent(alaska);
        alberta.addAdjacent(northWestTerritory);
        alberta.addAdjacent(westernUnitedStates);
        alberta.addAdjacent(ontario);

        ontario.addAdjacent(westernUnitedStates);
        ontario.addAdjacent(alberta);
        ontario.addAdjacent(northWestTerritory);
        ontario.addAdjacent(greenland);
        ontario.addAdjacent(quebec);
        ontario.addAdjacent(easternUnitedStates);

        quebec.addAdjacent(greenland);
        quebec.addAdjacent(ontario);
        quebec.addAdjacent(easternUnitedStates);

        easternUnitedStates.addAdjacent(quebec);
        easternUnitedStates.addAdjacent(ontario);
        easternUnitedStates.addAdjacent(westernUnitedStates);
        easternUnitedStates.addAdjacent(centralAmerica);

        westernUnitedStates.addAdjacent(alberta);
        westernUnitedStates.addAdjacent(ontario);
        westernUnitedStates.addAdjacent(easternUnitedStates);
        westernUnitedStates.addAdjacent(centralAmerica);

        centralAmerica.addAdjacent(westernUnitedStates);
        centralAmerica.addAdjacent(easternUnitedStates);
        centralAmerica.addAdjacent(venezuela);

        venezuela.addAdjacent(centralAmerica);
        venezuela.addAdjacent(brazil);
        venezuela.addAdjacent(peru);

        peru.addAdjacent(venezuela);
        peru.addAdjacent(brazil);
        peru.addAdjacent(argentina);

        argentina.addAdjacent(peru);
        argentina.addAdjacent(brazil);

        brazil.addAdjacent(venezuela);
        brazil.addAdjacent(peru);
        brazil.addAdjacent(argentina);
        brazil.addAdjacent(northAfrica);
    }
    /**
     * addCard
     * adds a card to the current players hand
     * this method is called in attack method
     */
    private void addCard() {
        if(cards.get(currentTurn).size() >= 5) {
            return;
        }
        List<Card> ListOfCards =(Arrays.asList(Card.values()));//stores the enums into an array
        int size = ListOfCards.size(); //size of the enums array
        Random rnd = new Random();
        cards.get(currentTurn).add(ListOfCards.get(rnd.nextInt(size)));//adds card for the current player
    }

    /**
     * exchangeCard
     * exchanges cards for bonus troops
     * @return true if cards were able to be exchanged
     */
    public boolean exchangeCards() {
        if(currentPhase != Phase.DEPLOY) {
            return false;
        }
        int countArtillery = 0;
        int countCavalry = 0;
        int countInfantry = 0;

        //checks how many of each card the player has
        for(int i = 0; i < cards.get(currentTurn).size(); i++) {
            if(cards.size() <= 0 ) {
                return false;
            }
            if(cards.get(currentTurn).get(i) == Card.ARTILLERY) {
                countArtillery++;
            }
            if(cards.get(currentTurn).get(i) == Card.CAVALRY) {
                countCavalry++;
            }
            if(cards.get(currentTurn).get(i) == Card.INFANTRY) {
                countInfantry++;
            }
        }

        //removes the cards from the players hand and gives bonus troops
        if(countArtillery >= 1 && countCavalry >= 1 && countInfantry >= 1) {
            cards.get(currentTurn).remove(Card.ARTILLERY);
            cards.get(currentTurn).remove(Card.CAVALRY);
            cards.get(currentTurn).remove(Card.INFANTRY);
            totalTroops += 10;
        } else if(countArtillery >= 3) {
            cards.get(currentTurn).remove(Card.ARTILLERY);
            cards.get(currentTurn).remove(Card.ARTILLERY);
            cards.get(currentTurn).remove(Card.ARTILLERY);
            totalTroops += 8;
        } else if(countCavalry >= 3) {
            cards.get(currentTurn).remove(Card.CAVALRY);
            cards.get(currentTurn).remove(Card.CAVALRY);
            cards.get(currentTurn).remove(Card.CAVALRY);
            totalTroops += 6;
        } else if(countInfantry >= 3) {
            cards.get(currentTurn).remove(Card.INFANTRY);
            cards.get(currentTurn).remove(Card.INFANTRY);
            cards.get(currentTurn).remove(Card.INFANTRY);
            totalTroops += 4;
        } else {
            return false;
        }
        countArtillery = 0;
        countCavalry = 0;
        countInfantry = 0;
        return true;
    }

    public List<ArrayList<Card>> getCards() {
        return cards;
    }
}