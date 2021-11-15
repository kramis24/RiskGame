package com.example.riskgame.Risk.infoMessage;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;

public class RiskGameStateTest {

    @Test
    public void getCurrentPhase() {
    }

    @Test
    public void setPlayerCount() {
    }

    @Test
    public void calcTroops() {
    }

    @Test
    public void addTroop() {
    }

    @Test
    public void setTerritoryPlayers() {
    }

    @Test
    public void setStartTroops() {
    }

    @Test
    public void attack() {
    }

    @Test
    public void deploy() {
    }

    @Test
    public void occupy() {
    }

    /**
     * Test for fortify method making sure it fits withing th parameters/rules of the game
     *
     * @author: Charlie Benning
     **/
    @Test
    public void fortify() {
        RiskGameState RiskTest = new RiskGameState();
        ArrayList<Territory> territoriesTest = RiskTest.getTerritories();
        Territory alaska = territoriesTest.get(0); //alaska
        Territory NWterr = territoriesTest.get(1); //Northwest territory
        Territory greenland = territoriesTest.get(2); //greenland
        alaska.setOwner(0);
        NWterr.setOwner(1);
        greenland.setOwner(0);
        alaska.setTroops(5);
        NWterr.setTroops(3);
        greenland.setTroops(1);
        assertFalse(RiskTest.fortify(alaska,NWterr,1)); //cant fortify to non owned terr
        assertFalse(RiskTest.fortify(alaska,greenland,1)); //terr not connected
        assertFalse(RiskTest.fortify(NWterr,greenland,1)); //cant fortify from enemy terr
        NWterr.setOwner(0); //change owner to match other terr
        assertFalse(RiskTest.fortify(greenland,NWterr,1)); //cant move a troop from terr with only 1 troop
        assertFalse(RiskTest.fortify(alaska,NWterr,0)); //cant move zero troops
        assertFalse(RiskTest.fortify(alaska,NWterr,10)); //cant move more troops that are in territory
        assertFalse(RiskTest.fortify(alaska,NWterr,-4)); //cant move negative troops
        assertFalse(RiskTest.fortify(alaska,NWterr,5)); //cant move all troops from a terr
        assertTrue(RiskTest.fortify(alaska,NWterr,2)); //can move to adjacent
        //assertTrue(RiskTest.fortify(alaska,greenland,2)); //can move through chain ERROR --> can't fortify twice ????
        //NOT due to checked
    }

    /**
     * Test for checkchain method making sure it fits withing th parameters/rules of the game
     *
     * @author: Charlie Benning
     **/
    @Test
    public void checkChain() {
        RiskGameState RiskTest = new RiskGameState();
        ArrayList<Territory> territoriesTest = RiskTest.getTerritories();
        for (int i = 0; i < territoriesTest.size(); i++) {
            territoriesTest.get(i).setOwner(0); //set all territories to the same player
        }
        assertTrue(RiskTest.checkChain(territoriesTest.get(0),territoriesTest.get(41))); //all territories should be connected
        territoriesTest.get(37).setOwner(1); //make siam owned by another player (block chain)
        assertFalse(RiskTest.checkChain(territoriesTest.get(0),territoriesTest.get(41))); //territories aren't connected
        assertFalse(RiskTest.checkChain(territoriesTest.get(37),territoriesTest.get(41))); //territories aren't owned by the same player
    }

    /**
     * Test for nextTurn method making sure it fits withing th parameters/rules of the game
     *
     * @author: Charlie Benning
     **/
    @Test
    public void nextTurn() {
        RiskGameState RiskTest = new RiskGameState();
        ArrayList<Territory> territoriesTest = RiskTest.getTerritories();
        assertTrue(RiskTest.getCurrentPhase() == RiskGameState.Phase.DEPLOY); //make sure first phase is Deploy
        assertEquals(0,RiskTest.getCurrentTurn()); //make sure it starts with player 0
        RiskTest.nextTurn();
        assertTrue(RiskTest.getCurrentPhase() == RiskGameState.Phase.ATTACK); //switch to attack phase (after fortify)
        RiskTest.nextTurn();
        assertTrue(RiskTest.getCurrentPhase() == RiskGameState.Phase.FORTIFY); //switch to fortify phase (after attack)
        RiskTest.nextTurn();
        assertTrue(RiskTest.getCurrentPhase() == RiskGameState.Phase.DEPLOY); //switch to net player and deploy phase (after fortify)
        assertEquals(1,RiskTest.getCurrentTurn()); //check that players switched
    }

    @Test
    public void setTotalTroops() {
    }

    @Test
    public void rollDie() {
    }

    @Test
    public void testToString() {
    }

    @Test
    public void getTerritories() {
    }

    @Test
    public void getCurrentTurn() {
    }

    @Test
    public void getTotalTroops() {
    }

    @Test
    public void exchangeCards() {
    }

    @Test
    public void getCards() {
    }
}