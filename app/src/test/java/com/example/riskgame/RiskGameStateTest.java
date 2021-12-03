package com.example.riskgame;
/**
 * RiskGameStateTest
 * Unit tests for RiskGameState.
 *
 * @author Dylan Kramis, Phi Nguyen
 */

import static org.junit.Assert.*;

import com.example.riskgame.Risk.infoMessage.RiskGameState;
import com.example.riskgame.Risk.infoMessage.Territory;

import org.junit.Test;

import java.util.ArrayList;

public class RiskGameStateTest {

    /**
     * calcTroops
     * Tests the calcTroops method.
     *
     * @author Dylan Kramis
     * @throws Exception
     */
    @Test
    public void calcTroops() throws Exception {

        // setup
        RiskGameState testState = new RiskGameState();
        testState.init(2);
        int result;

        // test #1 -- no owned territories
        for (Territory t : testState.getTerritories()) {
            t.setOwner(1);
        }
        result = testState.calcTroops(0);
        assertEquals(3, result);

        // test #2 -- 12 owned territories
        for (int i = 0; i < 6; i++) {
            testState.getTerritories().get(i).setOwner(0);
        }
        for (int i = 22; i < 28; i++) {
            testState.getTerritories().get(i).setOwner(0);
        }
        result = testState.calcTroops(0);
        assertEquals(4, result);

        // test #3 == 6 n north america + full ownership of africa and asia
        for (Territory t : testState.getTerritories()) {
            if ((t.getContinent() == Territory.Continent.AFRICA)
                || (t.getContinent() == Territory.Continent.ASIA)) {
                t.setOwner(0);
            }
        }
        result = testState.calcTroops(0);
        assertEquals(18, result);

    }

    /**
     * deploy
     * Tests the deploy method.
     *
     * @author Dylan Kramis
     * @throws Exception
     */
    @Test
    public void deploy() throws Exception {

        // setup
        RiskGameState testState = new RiskGameState();
        testState.init(2);
        ArrayList<Territory> testTerritories = testState.getTerritories();
        testState.setTotalTroops(6);
        testTerritories.get(0).setOwner(0);
        testTerritories.get(0).setTroops(1);
        testTerritories.get(1).setOwner(0);
        testTerritories.get(1).setTroops(1);
        testTerritories.get(2).setOwner(0);
        testTerritories.get(2).setTroops(1);
        testTerritories.get(3).setOwner(1);
        testTerritories.get(3).setTroops(1);

        // tests trying to deploy to an enemy territory
        testState.deploy(testTerritories.get(3), 1);
        assertEquals(1, testTerritories.get(3).getTroops());
        assertEquals(6, testState.getTotalTroops());

        // tests sequentially draining totalTroops
        testState.deploy(testTerritories.get(0), 1);
        assertEquals(2, testTerritories.get(0).getTroops());
        assertEquals(5, testState.getTotalTroops());

        testState.deploy(testTerritories.get(1), 2);
        assertEquals(3, testTerritories.get(1).getTroops());
        assertEquals(3, testState.getTotalTroops());

        testState.deploy(testTerritories.get(2), 3);
        assertEquals(4, testTerritories.get(2).getTroops());
        assertEquals(0, testState.getTotalTroops());

    }

    /**
     * rollDie
     * Tests the rollDie method
     *
     * @author Dylan Kramis
     * @throws Exception
     */
    @Test
    public void rollDie() throws Exception {
        // setup
        RiskGameState testState = new RiskGameState();
        testState.init(2);
        ArrayList<Integer> rolls;

        // test #1 -- 1 die
        rolls = testState.rollDie(1);
        assertEquals(1, rolls.size());
        assertTrue((rolls.get(0) >= 1) || (rolls.get(0) <= 6));

        // test #2 -- 2 dice
        rolls = testState.rollDie(2);
        assertEquals(2, rolls.size());
        assertTrue((rolls.get(0) >= 1) || (rolls.get(0) <= 6));
        assertTrue((rolls.get(1) >= 1) || (rolls.get(1) <= 6));

        // test #3 -- 3 dice
        rolls = testState.rollDie(3);
        assertEquals(3, rolls.size());
        assertTrue((rolls.get(0) >= 1) || (rolls.get(0) <= 6));
        assertTrue((rolls.get(1) >= 1) || (rolls.get(1) <= 6));
        assertTrue((rolls.get(2) >= 1) || (rolls.get(2) <= 6));

    }

    @Test
    /**
     * Test the attack method
     * @author Phi Nguyen
     */
    public void attack() {
        RiskGameState gameState = new RiskGameState();
        gameState.init(2);
        //checks that the attack method returns false when both territories have the same owner
        gameState.getTerritories().get(0).setOwner(0);
        gameState.getTerritories().get(1).setOwner(0);
        assertFalse(gameState.attack(gameState.getTerritories().get(0),gameState.getTerritories().get(1)));
        //checks that the attack method returns true when territories are owned by different players
        gameState.getTerritories().get(1).setOwner(1);
        gameState.getTerritories().get(0).setTroops(10);
        assertTrue(gameState.attack(gameState.getTerritories().get(0),gameState.getTerritories().get(1)));
        assertFalse(gameState.attack(gameState.getTerritories().get(1),gameState.getTerritories().get(0)));
        //checks that the attack method returns false when both territories are owned by the enemy
        gameState.getTerritories().get(0).setOwner(1);
        assertFalse((gameState.attack(gameState.getTerritories().get(0),gameState.getTerritories().get(1))));

    }

    @Test
    /**
     * Test the exchange card method
     * @author Phi Nguyen
     */
    public void exchangeCards() {
        RiskGameState gameState = new RiskGameState();
        gameState.init(2);
        gameState.getCards().get(0).add(RiskGameState.Card.INFANTRY);
        gameState.getCards().get(0).add(RiskGameState.Card.CAVALRY);
        gameState.getCards().get(0).add(RiskGameState.Card.ARTILLERY);
        gameState.setTotalTroops(0);
        gameState.exchangeCards();
        assertTrue(gameState.getTotalTroops() == 10);
        gameState.getCards().get(0).add(RiskGameState.Card.ARTILLERY);
        gameState.getCards().get(0).add(RiskGameState.Card.ARTILLERY);
        gameState.getCards().get(0).add(RiskGameState.Card.ARTILLERY);
        gameState.setTotalTroops(0);
        gameState.exchangeCards();
        assertTrue(gameState.getTotalTroops() == 8);
        gameState.getCards().get(0).add(RiskGameState.Card.CAVALRY);
        gameState.getCards().get(0).add(RiskGameState.Card.CAVALRY);
        gameState.getCards().get(0).add(RiskGameState.Card.CAVALRY);
        gameState.setTotalTroops(0);
        gameState.exchangeCards();
        assertTrue(gameState.getTotalTroops() == 6);
        gameState.getCards().get(0).add(RiskGameState.Card.INFANTRY);
        gameState.getCards().get(0).add(RiskGameState.Card.INFANTRY);
        gameState.getCards().get(0).add(RiskGameState.Card.INFANTRY);
        gameState.setTotalTroops(0);
        gameState.exchangeCards();
        assertTrue(gameState.getTotalTroops() == 4);
        assertTrue(gameState.getCards().get(0).size() == 0);
    }

    @Test
    /**
     * Test the copy Constructor
     * @author Phi Nguyen
     */
    public void copyConstructor(){
        RiskGameState gameState = new RiskGameState();
        gameState.init(2);
        gameState.nextTurn();
        gameState.setTotalTroops(100);
        gameState.addCard();

        gameState.addCard();
        gameState.addCard();
        gameState.getTerritories().get(25).setOwner(1);
        gameState.getTerritories().get(23).setTroops(62);
        RiskGameState copy = new RiskGameState(gameState);
        assertTrue(gameState.getCurrentPhase() == copy.getCurrentPhase());
        assertTrue(gameState.getCurrentTurn() == copy.getCurrentTurn());
        assertTrue(gameState.getTotalTroops() == copy.getTotalTroops());

        for(int i = 0; i < 41; i++) {
            assertTrue(gameState.getTerritories().get(i).getName() == copy.getTerritories().get(i).getName());
            assertTrue(gameState.getTerritories().get(i).getTroops() == copy.getTerritories().get(i).getTroops());
            assertTrue(gameState.getTerritories().get(i).getOwner() == copy.getTerritories().get(i).getOwner());
            assertTrue(gameState.getTerritories().get(i).getContinent() == copy.getTerritories().get(i).getContinent());
        }


        for(int i = 0; i < gameState.getCards().size(); i++) {
            for(int j = 0; j < gameState.getCards().get(i).size(); j++) {
                assertTrue(gameState.getCards().get(i).get(j) == copy.getCards().get(i).get(j) );
            }
        }
    }

    @Test
    /**
     * Phi Nguyen
     * Test winning the game
     */
    public void win() throws Exception {
        boolean win = false;
        RiskGameState gameState = new RiskGameState();
        gameState.init(2);
        for(int i = 0; i < 41; i++) {
            gameState.getTerritories().get(i).setOwner(0);
        }
        gameState.getTerritories().get(41).setOwner(0);
        int winner = gameState.getTerritories().get(0).getOwner();
        //checks if all territories have the same owner
        for(Territory t: gameState.getTerritories()) {
            if(t.getOwner() != winner) {
                assertFalse(win);
            }
        }
        win = true;
        assertTrue(win);

    }

    /**
     * Test for fortify method making sure it fits withing th parameters/rules of the game
     *
     * @author Charlie Benning
     **/
    @Test
    public void fortify() {
        RiskGameState riskTest = new RiskGameState();
        riskTest.init(2);
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
    }

    /**
     * Test for checkchain method making sure it fits withing th parameters/rules of the game
     *
     * @author Charlie Benning
     **/
    @Test
    public void checkChain() {
        RiskGameState riskTest = new RiskGameState();
        riskTest.init(2);
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
     * @author Charlie Benning
     **/
    @Test
    public void nextTurn() {
        RiskGameState riskTest = new RiskGameState();
        riskTest.init(2);
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

    /**
     * Test for addTroop method making sure it fits withing th parameters/rules of the game
     *
     * @author: Charlie Benning
     **/
    @Test
    public void addTroop() {
        RiskGameState riskTest = new RiskGameState();
        riskTest.init(2);
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
        riskTest.init(2);
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
        riskTest2.init(3);
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
        riskTest.init(2);
        ArrayList<Territory> territoriesTest = riskTest.getTerritories();
        int troopCnt = 0;
        riskTest.setTerritoryPlayers();
        riskTest.setStartTroops();
        for (int i = 0; i < territoriesTest.size(); i++) {
            if (territoriesTest.get(i).getOwner() == 0) {
                troopCnt += territoriesTest.get(i).getTroops();
            }
        }
        assertEquals(40,troopCnt);
    }
}