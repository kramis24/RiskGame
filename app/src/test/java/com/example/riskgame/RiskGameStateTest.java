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
     * calcTroops - Dylan Kramis
     * Tests the calcTroops method.
     *
     * @throws Exception
     */
    @Test
    public void calcTroops() throws Exception {

        // setup
        RiskGameState testState = new RiskGameState();
        int result;

        // test #1 -- no owned territories
        for (Territory t : testState.getTerritories()) {
            t.setOwner(1);
        }
        result = testState.calcTroops(0);
        assertEquals(3, result);

        // test #2 -- 6 owned territories
        for (int i = 0; i < 6; i++) {
            testState.getTerritories().get(i).setOwner(0);
        }
        result = testState.calcTroops(0);
        assertEquals(4, result);

        // test #3 == 6 owned + full ownership of africa and asia
        for (Territory t : testState.getTerritories()) {
            if ((t.getContinent() == Territory.Continent.AFRICA)
                || (t.getContinent() == Territory.Continent.ASIA)) {
                t.setOwner(0);
            }
        }
        result = testState.calcTroops(0);
        assertEquals(20, result);

    }

    /**
     * deploy - Dylan Kramis
     * Tests the deploy method.
     *
     * @throws Exception
     */
    @Test
    public void deploy() throws Exception {

        // setup
        RiskGameState testState = new RiskGameState();
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
     * rollDie - Dylan Kramis
     * Tests the rollDie method
     *
     * @throws Exception
     */
    @Test
    public void rollDie() throws Exception {

        // setup
        RiskGameState testState = new RiskGameState();
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
     * Phi Nguyen
     * Test the attack method
     */
    public void attack() {
        RiskGameState gameState = new RiskGameState();
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
     * Phi Nguyen
     * Test the exchange card method
     */
    public void exchangeCards() {
        RiskGameState gameState = new RiskGameState();
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
     * Phi Nguyen
     * Test the copy Constructor
     */
    public void copyConstructor(){
        RiskGameState gameState = new RiskGameState();
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
}