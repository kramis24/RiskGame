package com.example.riskgame.Risk.infoMessage;

import static org.junit.Assert.*;

import com.example.riskgame.R;

import org.junit.Test;

import java.util.ArrayList;

public class RiskGameStateTest {

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