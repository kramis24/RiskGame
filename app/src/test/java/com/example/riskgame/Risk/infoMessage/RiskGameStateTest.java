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
        RiskGameState riskTest = new RiskGameState();
        assertTrue(riskTest.getCurrentPhase() == RiskGameState.Phase.DEPLOY);
        riskTest.nextTurn();
        assertTrue(riskTest.getCurrentPhase() == RiskGameState.Phase.ATTACK);
        riskTest.nextTurn();
        assertTrue(riskTest.getCurrentPhase() == RiskGameState.Phase.FORTIFY);
        riskTest.nextTurn();
        assertTrue(riskTest.getCurrentPhase() == RiskGameState.Phase.DEPLOY);
    }

    /**
     * Test for setPlayerCount method making sure it fits withing th parameters/rules of the game
     *
     * @author: Charlie Benning
     **/
    @Test
    public void setPlayerCount() {
        RiskGameState riskTest = new RiskGameState();
        riskTest.setPlayerCount(0);
        //assertEquals(0,riskTest.);
        riskTest.setPlayerCount(0);
        //assertEquals(6,riskTest.);
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
        RiskGameState riskTest = new RiskGameState();
        ArrayList<Territory> territoriesTest = riskTest.getTerritories();
        int tempTroop = 10;
        territoriesTest.get(0).setTroops(0);
        riskTest.addTroop(territoriesTest.get(0),10);
        assertEquals(10,territoriesTest.get(0).getTroops());
        riskTest.addTroop(territoriesTest.get(0),5);
        assertEquals(15,territoriesTest.get(0).getTroops());
        riskTest.addTroop(territoriesTest.get(0),-3);
        assertEquals(15,territoriesTest.get(0).getTroops());
    }

    /**
     * Test for setTerritoryPlayers method making sure it fits withing th parameters/rules of the game
     *
     * @author: Charlie Benning
     **/
    @Test
    public void setTerritoryPlayers() {
        RiskGameState riskTest = new RiskGameState();
        riskTest.setTerritoryPlayers();
        int terrCntPlyr0 = 0;
        int terrCntPlyr1 = 0;
        for (int i = 0; i < riskTest.getTerritories().size(); i++) {
            if (riskTest.getTerritories().get(i).getOwner() == 0) {
                terrCntPlyr0++;
            }
            else {
                terrCntPlyr1++;
            }
        }
        assertEquals(21,terrCntPlyr0);
        assertEquals(21,terrCntPlyr1);
        RiskGameState riskTest2 = new RiskGameState();
        terrCntPlyr0 = 0;
        terrCntPlyr1 = 0;
        int terrCntPlyr2 = 0;
        riskTest2.setPlayerCount(3);
        riskTest2.setTerritoryPlayers();
        for (int i = 0; i < riskTest2.getTerritories().size(); i++) {
            if (riskTest2.getTerritories().get(i).getOwner() == 0) {
                terrCntPlyr0++;
            }
            else if (riskTest2.getTerritories().get(i).getOwner() == 1) {
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
        RiskGameState riskTest = new RiskGameState();
        ArrayList<Territory> territoriesTest = riskTest.getTerritories();
        int troopCnt = 0;
        riskTest.setTerritoryPlayers();
        riskTest.setStartTroops();
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
        RiskGameState riskTest = new RiskGameState();
        ArrayList<Territory> territoriesTest = riskTest.getTerritories();
        riskTest.nextTurn();
        riskTest.nextTurn();
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
        assertFalse(riskTest.fortify(alaska,NWterr,1)); //cant fortify to non owned terr
        assertFalse(riskTest.fortify(alaska,greenland,1)); //terr not connected
        assertFalse(riskTest.fortify(NWterr,greenland,1)); //cant fortify from enemy terr
        NWterr.setOwner(0); //change owner to match other terr
        assertFalse(riskTest.fortify(greenland,NWterr,1)); //cant move a troop from terr with only 1 troop
        assertFalse(riskTest.fortify(alaska,NWterr,0)); //cant move zero troops
        assertFalse(riskTest.fortify(alaska,NWterr,10)); //cant move more troops that are in territory
        assertFalse(riskTest.fortify(alaska,NWterr,-4)); //cant move negative troops
        assertFalse(riskTest.fortify(alaska,NWterr,5)); //cant move all troops from a terr
        assertTrue(riskTest.fortify(alaska,NWterr,2)); //can move to adjacent
        riskTest.nextTurn();
        riskTest.nextTurn();
        riskTest.nextTurn();
        assertTrue(alaska.getTroops() > 2);
        assertTrue(alaska.getOwner() == greenland.getOwner());
        assertTrue(riskTest.getCurrentTurn() == 0);
        assertTrue(riskTest.fortify(alaska,greenland,2)); //can move through chain ERROR --> can't fortify twice ????
        //NOT due to checked
    }

    /**
     * Test for checkchain method making sure it fits withing th parameters/rules of the game
     *
     * @author: Charlie Benning
     **/
    @Test
    public void checkChain() {
        RiskGameState riskTest = new RiskGameState();
        ArrayList<Territory> territoriesTest = riskTest.getTerritories();
        for (int i = 0; i < territoriesTest.size(); i++) {
            territoriesTest.get(i).setOwner(0); //set all territories to the same player
        }
        assertTrue(riskTest.checkChain(territoriesTest.get(0),territoriesTest.get(41))); //all territories should be connected
        territoriesTest.get(37).setOwner(1); //make siam owned by another player (block chain)
        assertFalse(riskTest.checkChain(territoriesTest.get(0),territoriesTest.get(41))); //territories aren't connected
        assertFalse(riskTest.checkChain(territoriesTest.get(37),territoriesTest.get(41))); //territories aren't owned by the same player
    }

    /**
     * Test for nextTurn method making sure it fits withing th parameters/rules of the game
     *
     * @author: Charlie Benning
     **/
    @Test
    public void nextTurn() {
        RiskGameState riskTest = new RiskGameState();
        ArrayList<Territory> territoriesTest = riskTest.getTerritories();
        assertTrue(riskTest.getCurrentPhase() == RiskGameState.Phase.DEPLOY); //make sure first phase is Deploy
        assertEquals(0,riskTest.getCurrentTurn()); //make sure it starts with player 0
        riskTest.nextTurn();
        assertTrue(riskTest.getCurrentPhase() == RiskGameState.Phase.ATTACK); //switch to attack phase (after fortify)
        riskTest.nextTurn();
        assertTrue(riskTest.getCurrentPhase() == RiskGameState.Phase.FORTIFY); //switch to fortify phase (after attack)
        riskTest.nextTurn();
        assertTrue(riskTest.getCurrentPhase() == RiskGameState.Phase.DEPLOY); //switch to net player and deploy phase (after fortify)
        assertEquals(1,riskTest.getCurrentTurn()); //check that players switched
    }

    /**
     * Test for setTotalTroops method making sure it fits withing th parameters/rules of the game
     *
     * @author: Charlie Benning
     **/
    @Test
    public void setTotalTroops() {
        RiskGameState riskTest = new RiskGameState();
        riskTest.setTotalTroops(5);
        assertEquals(5, riskTest.getTotalTroops());
        riskTest.setTotalTroops(0);
        assertEquals(0, riskTest.getTotalTroops());
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
        RiskGameState riskTest = new RiskGameState();
        ArrayList<Territory> territoriesTest = riskTest.getTerritories();
        assertEquals(riskTest.getTerritories().get(0),territoriesTest.get(0));
        assertEquals(riskTest.getTerritories().get(27),territoriesTest.get(27));
        assertFalse(riskTest.getTerritories().get(0) ==territoriesTest.get(33));
    }

    /**
     * Test for getCurrentTurn method making sure it fits withing th parameters/rules of the game
     *
     * @author: Charlie Benning
     **/
    @Test
    public void getCurrentTurn() {
        RiskGameState riskTest = new RiskGameState();
        assertEquals(0, riskTest.getCurrentTurn());
        riskTest.nextTurn();
        riskTest.nextTurn();
        riskTest.nextTurn();
        assertEquals(1, riskTest.getCurrentTurn());
    }

    /**
     * Test for getTotalTroops method making sure it fits withing th parameters/rules of the game
     *
     * @author: Charlie Benning
     **/
    @Test
    public void getTotalTroops() {
        RiskGameState riskTest = new RiskGameState();
        riskTest.setTotalTroops(10);
        assertEquals(10,riskTest.getTotalTroops());
        riskTest.setTotalTroops(77);
        assertEquals(77,riskTest.getTotalTroops());
        riskTest.setTotalTroops(-3);
        assertEquals(-3,riskTest.getTotalTroops());
    }

    //Phi
    @Test
    public void exchangeCards() {
    }

    @Test
    public void getCards() {
    }
}