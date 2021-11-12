package com.example.riskgame.Risk.infoMessage;

import static org.junit.Assert.*;

import org.junit.Test;

public class RiskGameStateTest {

    @Test
    public void attack() {
        RiskGameState gameState = new RiskGameState();
        gameState.getTerritories().get(0).setOwner(0);
        gameState.getTerritories().get(1).setOwner(0);
        assertFalse(gameState.attack(gameState.getTerritories().get(0),gameState.getTerritories().get(1)));
        gameState.getTerritories().get(1).setOwner(1);
        gameState.getTerritories().get(0).setTroops(10);
        assertTrue(gameState.attack(gameState.getTerritories().get(0),gameState.getTerritories().get(1)));

    }

    @Test
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
}