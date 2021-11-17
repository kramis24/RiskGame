package com.example.riskgame.Risk.infoMessage;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.*;

public class RiskGameStateTest {

    /**
     * Test for getCurrentPhase method making sure it fits withing th parameters/rules of the game
     *
     * @author: Charlie Benning
     **/
    @Test
    public void getCurrentPhase() {
        RiskGameState RiskTest = new RiskGameState();
        assertTrue(RiskTest.getCurrentPhase() == RiskGameState.Phase.DEPLOY);
        RiskTest.nextTurn();
        assertTrue(RiskTest.getCurrentPhase() == RiskGameState.Phase.ATTACK);
        RiskTest.nextTurn();
        assertTrue(RiskTest.getCurrentPhase() == RiskGameState.Phase.FORTIFY);
        RiskTest.nextTurn();
        assertTrue(RiskTest.getCurrentPhase() == RiskGameState.Phase.DEPLOY);
    }

    /**
     * Test for setPlayerCount method making sure it fits withing th parameters/rules of the game
     *
     * @author: Charlie Benning
     **/
    @Test
    public void setPlayerCount() {
        RiskGameState RiskTest = new RiskGameState();
        RiskTest.setPlayerCount(0);
        //assertEquals(0,RiskTest.);
        RiskTest.setPlayerCount(0);
        //assertEquals(6,RiskTest.);
    }

    //Dylan
    @Test
    public void calcTroops() {
    }

    /**
     * Test for addTroop method making sure it fits withing th parameters/rules of the game
     *
     * @author: Charlie Benning
     **/
    @Test
    public void addTroop() {
        RiskGameState RiskTest = new RiskGameState();
        ArrayList<Territory> territoriesTest = RiskTest.getTerritories();
        int tempTroop = 10;
        territoriesTest.get(0).setTroops(0);
        RiskTest.addTroop(territoriesTest.get(0),10);
        assertEquals(10,territoriesTest.get(0).getTroops());
        RiskTest.addTroop(territoriesTest.get(0),5);
        assertEquals(15,territoriesTest.get(0).getTroops());
        RiskTest.addTroop(territoriesTest.get(0),-3);
        assertEquals(15,territoriesTest.get(0).getTroops());
    }

    /**
     * Test for setTerritoryPlayers method making sure it fits withing th parameters/rules of the game
     *
     * @author: Charlie Benning
     **/
    @Test
    public void setTerritoryPlayers() {
        RiskGameState RiskTest = new RiskGameState();
        RiskTest.setTerritoryPlayers();
        int terrCntPlyr0 = 0;
        int terrCntPlyr1 = 0;
        for (int i = 0; i < RiskTest.getTerritories().size(); i++) {
            if (RiskTest.getTerritories().get(i).getOwner() == 0) {
                terrCntPlyr0++;
            }
            else {
                terrCntPlyr1++;
            }
        }
        assertEquals(21,terrCntPlyr0);
        assertEquals(21,terrCntPlyr1);
        RiskGameState RiskTest2 = new RiskGameState();
        terrCntPlyr0 = 0;
        terrCntPlyr1 = 0;
        int terrCntPlyr2 = 0;
        RiskTest2.setPlayerCount(3);
        RiskTest2.setTerritoryPlayers();
        for (int i = 0; i < RiskTest2.getTerritories().size(); i++) {
            if (RiskTest2.getTerritories().get(i).getOwner() == 0) {
                terrCntPlyr0++;
            }
            else if (RiskTest2.getTerritories().get(i).getOwner() == 1) {
                terrCntPlyr1++;
            }
            else {
                terrCntPlyr2++; //doubles than expaects????
            }
        }
        assertEquals(14,terrCntPlyr0);
        assertEquals(14,terrCntPlyr1);
        assertEquals(14,terrCntPlyr2);
    }

    /**
     * Test for setStartTroops method making sure it fits withing th parameters/rules of the game
     *
     * @author: Charlie Benning
     **/
    @Test
    public void setStartTroops() {
        RiskGameState RiskTest = new RiskGameState();
        ArrayList<Territory> territoriesTest = RiskTest.getTerritories();
        int troopCnt = 0;
        RiskTest.setTerritoryPlayers();
        RiskTest.setStartTroops();
        for (int i = 0; i < territoriesTest.size(); i++) {
            if (territoriesTest.get(i).getOwner() == 0) {
                troopCnt += territoriesTest.get(i).getTroops();
            }
        }
        assertEquals(40,troopCnt); //2 instead
    }

    //Phi
    @Test
    public void attack() {
    }

    //dylan
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
        RiskTest.nextTurn();
        RiskTest.nextTurn();
        Territory alaska = territoriesTest.get(0); //alaska
        Territory NWterr = territoriesTest.get(1); //Northwest territory
        Territory greenland = territoriesTest.get(2); //greenland
        for (int i = 0; i < territoriesTest.size(); i++) {
            territoriesTest.get(i).setOwner(1);
        }
        alaska.setOwner(0);
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
        RiskTest.nextTurn();
        RiskTest.nextTurn();
        RiskTest.nextTurn();
        assertTrue(alaska.getTroops() > 2);
        assertTrue(alaska.getOwner() == greenland.getOwner());
        assertTrue(RiskTest.getCurrentTurn() == 0);
        assertTrue(RiskTest.fortify(alaska,greenland,2)); //can move through chain ERROR --> can't fortify twice ????
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

    /**
     * Test for setTotalTroops method making sure it fits withing th parameters/rules of the game
     *
     * @author: Charlie Benning
     **/
    @Test
    public void setTotalTroops() {
        RiskGameState RiskTest = new RiskGameState();
        RiskTest.setTotalTroops(5);
        assertEquals(5, RiskTest.getTotalTroops());
        RiskTest.setTotalTroops(0);
        assertEquals(0, RiskTest.getTotalTroops());
    }

    //Dylan
    @Test
    public void rollDie() {
    }

    @Test
    public void testToString() {
    }

    @Test
    public void getTerritories() {
        RiskGameState RiskTest = new RiskGameState();
        ArrayList<Territory> territoriesTest = RiskTest.getTerritories();
        assertEquals(RiskTest.getTerritories().get(0),territoriesTest.get(0));
        assertEquals(RiskTest.getTerritories().get(27),territoriesTest.get(27));
        assertFalse(RiskTest.getTerritories().get(0) ==territoriesTest.get(33));
    }

    /**
     * Test for getCurrentTurn method making sure it fits withing th parameters/rules of the game
     *
     * @author: Charlie Benning
     **/
    @Test
    public void getCurrentTurn() {
        RiskGameState RiskTest = new RiskGameState();
        assertEquals(0, RiskTest.getCurrentTurn());
        RiskTest.nextTurn();
        RiskTest.nextTurn();
        RiskTest.nextTurn();
        assertEquals(1, RiskTest.getCurrentTurn());
    }

    /**
     * Test for getTotalTroops method making sure it fits withing th parameters/rules of the game
     *
     * @author: Charlie Benning
     **/
    @Test
    public void getTotalTroops() {
        RiskGameState RiskTest = new RiskGameState();
        RiskTest.setTotalTroops(10);
        assertEquals(10,RiskTest.getTotalTroops());
        RiskTest.setTotalTroops(77);
        assertEquals(77,RiskTest.getTotalTroops());
        RiskTest.setTotalTroops(-3);
        assertEquals(-3,RiskTest.getTotalTroops());
    }

    //Phi
    @Test
    public void exchangeCards() {
    }

    @Test
    public void getCards() {
    }
}