package com.example.riskgame.Risk.infoMessage;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.ArrayList;

public class RiskGameStateTest {

    @Test
    public void calcTroops() throws Exception {
    }

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

        //tests
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

    @Test
    public void nextTurn() throws Exception {
    }
}