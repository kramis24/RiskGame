package com.example.riskgame;


import static org.junit.Assert.*;

import com.example.riskgame.Risk.infoMessage.RiskGameState;
import com.example.riskgame.Risk.infoMessage.Territory;

import org.junit.Test;

import java.util.ArrayList;

public class RiskGameStateTest {

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
}