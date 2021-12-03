package com.example.riskgame.Risk.players;
/**
 * RiskAdvancedComputerPlayer
 * Less dumb (i.e. not random) computer player for Risk. Designed to make one move each time info
 * is received.
 *
 * @author Dylan Kramis, designed based on suggestions from Charlie Benning
 * @version 11/30/2021
 */

import static com.example.riskgame.Risk.infoMessage.Territory.Continent.*;
import com.example.riskgame.GameFramework.infoMessage.GameInfo;
import com.example.riskgame.GameFramework.infoMessage.NotYourTurnInfo;
import com.example.riskgame.GameFramework.players.GameComputerPlayer;
import com.example.riskgame.Risk.infoMessage.RiskGameState;
import com.example.riskgame.Risk.infoMessage.Territory;
import com.example.riskgame.Risk.riskActionMessage.AttackAction;
import com.example.riskgame.Risk.riskActionMessage.DeployAction;
import com.example.riskgame.Risk.riskActionMessage.ExchangeCardAction;
import com.example.riskgame.Risk.riskActionMessage.FortifyAction;
import com.example.riskgame.Risk.riskActionMessage.NextTurnAction;

public class RiskAdvancedComputerPlayer extends GameComputerPlayer {

    // limits to keep the computer from getting stuck
    public static final int ATTACK_LIMIT = 20;
    public static final int FORTIFY_LIMIT = 5;

    // instance variables
    private RiskGameState gameState;

    // variables for attacking
    private int attackCount = 0;
    private int attackFailCount = 0;
    private Territory attackTarget = null; // focus maintainer
    private Territory attackFrom = null;
    private int territoryCount = 0;
    private int territoriesCaptured = 0;
    private int[] continentCounts = {0, 0, 0, 0, 0, 0};

    // variables for fortifying
    private int fortifyCount = 0;

    /**
     * constructor
     *
     * @param name the player's name (e.g., "John")
     */
    public RiskAdvancedComputerPlayer(String name) {
        super(name);
    }

    /**
     * receiveInfo
     * Processes received info and decides what to do with it.
     *
     * @param info info received
     */
    @Override
    protected void receiveInfo(GameInfo info) {

        // returns if wrong turn
        if (info instanceof NotYourTurnInfo) {
            return;
        }

        // proceeds to play the game if valid game state detected
        if (info instanceof RiskGameState) {
            gameState = (RiskGameState) info;

            // returns if wrong turn
            if (gameState.getCurrentTurn() != playerNum){
                return;
            }

            // calls action generator for current phase,
            // resets attempt counters if necessary
            if (gameState.getCurrentPhase() == RiskGameState.Phase.DEPLOY) {
                generateDeploy();
                attackCount = 0;
                fortifyCount = 0;
            } else if (gameState.getCurrentPhase() == RiskGameState.Phase.ATTACK) {
                generateAttack();
            } else {
                generateFortify();
            }

            // 1 second pause, ideally
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * generateDeploy
     * Analyzes game state and determines where to deploy troops.
     */
    protected void generateDeploy() {

        // advances turn if no troops available
        if (gameState.getTotalTroops() == 0) {
            game.sendAction(new NextTurnAction(this));
        }

        // otherwise sends troops somewhere
        else {

            // exchanges cards if right combination obtained or cards maxed out,
            // returns to wait for updated information
            if ((gameState.getCards().get(playerNum).size() == 5)
                || (gameState.getCards().get(playerNum).contains(RiskGameState.Card.INFANTRY)
                    && gameState.getCards().get(playerNum).contains(RiskGameState.Card.ARTILLERY)
                    && gameState.getCards().get(playerNum).contains(RiskGameState.Card.CAVALRY))) {
                game.sendAction(new ExchangeCardAction(this));
                return;
            }

            // initializes variables
            Territory deployTo = null;
            Territory greatestThreat = null;
            double vulnerability = -1;

            // finds the most vulnerable territory
            for (Territory t : gameState.getTerritories()) {
                if ((t.getOwner() == playerNum) && t.hasEnemyAdjacent()) {
                    if ((t.getDisadvantage() < vulnerability) || (vulnerability < 0)) {
                        vulnerability = t.getDisadvantage();
                        greatestThreat = t.getGreatestThreat();
                        deployTo = t;
                    }
                }
            }

            // deploys to selected territory, troop count varies by situation
            // fail case if no enemies found, this should be a win
            if (vulnerability < 0 || deployTo == null || greatestThreat == null) {
                return;
            }
            // attempts to equalize if more enemy troops than troops in own territory
            else if (vulnerability < 1) {
                if ((greatestThreat.getTroops() - deployTo.getTroops())
                        <= gameState.getTotalTroops()) {
                    game.sendAction(new DeployAction(this, deployTo,
                            greatestThreat.getTroops() - deployTo.getTroops()));
                } else {
                    game.sendAction(new DeployAction(this, deployTo,
                            gameState.getTotalTroops()));
                }
            }
            // boost troop counts more conservatively if ratio is favorable
            else {
                if (gameState.getTotalTroops() > 3) {
                    game.sendAction(new DeployAction(this, deployTo, 3));
                } else {
                    game.sendAction(new DeployAction(this, deployTo,
                            gameState.getTotalTroops()));
                }
            }
        }
    }

    /**
     * generateAttack
     * Analyzes game and determines where to attack
     */
    protected void generateAttack() {

        // move on if attack limit reached
        if (attackCount >= ATTACK_LIMIT) {
            game.sendAction(new NextTurnAction(this));
            return;
        }

        // reset fail count if start of phase
        if (attackCount == 0) {
            attackFailCount = 0;
        }

        // counts up territories similar to calcTroops in game state
        int newTerritoryCount = 0;
        for (Territory t : gameState.getTerritories()) {
            if (t.getOwner() == playerNum) {
                territoryCount++;
                continentCounts[t.getContinent().ordinal()]++;
            }
        }

        // updates relevant info
        if (territoryCount != 0) {
            territoriesCaptured = newTerritoryCount - territoryCount;
        }
        territoryCount = newTerritoryCount;

        // sets target and attacking territory if start of attack phase or when territory captured
        // will prioritize continents if ables to
        if (attackTarget == null || attackTarget.getOwner() == playerNum || attackCount == 0) {
            attackTarget = null;
            if (continentCounts[ASIA.ordinal()] >= 6) {
                setAttackTarget(ASIA);
            } else if (continentCounts[AFRICA.ordinal()] >= 3) {
                setAttackTarget(AFRICA);
            } else if (continentCounts[SOUTH_AMERICA.ordinal()] >= 2) {
                setAttackTarget(SOUTH_AMERICA);
            } else if (continentCounts[NORTH_AMERICA.ordinal()] >= 5) {
                setAttackTarget(NORTH_AMERICA);
            } else if (continentCounts[EUROPE.ordinal()] >= 4) {
                setAttackTarget(EUROPE);
            } else if (continentCounts[OCEANIA.ordinal()] >= 2) {
                setAttackTarget(OCEANIA);
            }

            // default if no continents can be prioritized, code is that from
            // setAttackTarget without the continent check by first finding an owned
            // territory capable of attacking, then finding what to attack
            else {
                for (Territory t : gameState.getTerritories()) {
                    if (t.getOwner() == playerNum && t.getTroops() >= 2) {
                        for (Territory a : t.getAdjacents()) {
                            if (a.getOwner() != playerNum) {
                                if (attackTarget == null) {
                                    attackTarget = a;
                                    attackFrom   = t;
                                } else if ((a.getTroops() / t.getTroops())
                                        < (attackTarget.getTroops() / attackFrom.getTroops())) {
                                    attackTarget = a;
                                    attackFrom   = t;
                                }
                            }
                        }
                    }
                }
            }
        }

        // sends attack action if all requirements have been met
        if (attackTarget == null || attackFrom == null) {
            game.sendAction(new NextTurnAction(this));
        } else {
            game.sendAction(new AttackAction(this, attackFrom, attackTarget, false));
        }

        // advances attack counter
        attackCount++;

    }

    /**
     * setAttackTarget
     * Sets an attack target within a specified continent.
     *
     * @param c continent
     */
    protected void setAttackTarget(Territory.Continent c) {

        // searches for territories that can attack territories still needed in
        // the desired continent
        for (Territory t : gameState.getTerritories()) {
            if (t.getOwner() == playerNum && t.getTroops() >= 2) {
                for (Territory a : t.getAdjacents()) {
                    if (a.getOwner() != playerNum && a.getContinent() == c) {
                        if (attackTarget == null) {
                            attackTarget = a;
                            attackFrom   = t;
                        } else if ((a.getTroops() / t.getTroops())
                                < (attackTarget.getTroops() / attackFrom.getTroops())) {
                            attackTarget = a;
                            attackFrom   = t;
                        }
                    }
                }
            }
        }
    }

    /**
     * generateFortify
     * Analyzes game state and determines where to fortify.
     */
    protected void generateFortify() {

        // move on if fortify limit reached
        if (fortifyCount >= FORTIFY_LIMIT) {
            game.sendAction(new NextTurnAction(this));
        }

        // method variables
        Territory moveFrom = null;
        Territory moveTo = null;
        boolean wellProtected;

        // looks for an owned territory entirely surrounded by other owned territories
        for (Territory t : gameState.getTerritories()) {
            if (t.getOwner() == playerNum) {
                wellProtected = true;
                for (Territory a : t.getAdjacents()) {
                    if (a.getOwner() != playerNum) {
                        wellProtected = false;
                    }
                }
                if (wellProtected) {
                    moveFrom = t;
                    break;
                }
            }
        }

        // abort if no well protected territory found
        if (moveFrom == null) {
            game.sendAction(new NextTurnAction(this));
            return;
        }

        // determines where to move troops to
        for (Territory a : moveFrom.getAdjacents()) {
            if (moveTo == null) {
                moveTo = a;
            } else if (moveTo.getTroops() < a.getTroops()) {
                moveTo = a;
            }
        }

        // sends action, deploys all but one troop
        game.sendAction(new FortifyAction(this, moveFrom, moveTo,
                moveFrom.getTroops() - 1));

        // increments attempt counter
        fortifyCount++;

    }
}
