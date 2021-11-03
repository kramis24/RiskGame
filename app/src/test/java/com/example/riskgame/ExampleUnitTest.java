package com.example.riskgame;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import com.example.riskgame.Risk.RiskLocalGame;
import com.example.riskgame.Risk.RiskMainActivity;
import com.example.riskgame.Risk.infoMessage.RiskGameState;
import com.example.riskgame.Risk.infoMessage.Territory;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */

public class ExampleUnitTest {
    public RiskLocalGame riskLG;
    @Before
    public void setup() {
        riskLG = new RiskLocalGame();
    }

    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void testCopyConstructorOfState_Empty() {
        RiskGameState riskState = new RiskGameState();
        RiskGameState copyState = new RiskGameState();
        assertTrue("Copy constructor did not produce equal states", riskState.equals(copyState));
    }

    @Test
    public void testCopyConstructorOfState_InProgress() {
        RiskGameState riskState = new RiskGameState();
        for(Territory t : riskState.getT()) {
            t.setTroops(5);
            t.setOwner(riskState.getCurrentTurn());
        }
        riskState.deploy(riskState.getT().get(0),3);
        assertTrue("The number of troops not correct",riskState.getT().get(0).getTroops() == 8);
    }
}