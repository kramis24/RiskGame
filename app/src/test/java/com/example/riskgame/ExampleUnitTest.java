package com.example.riskgame;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    public boolean equals(Object object) {
    //if object instn an intanceof Riskgamestate -->false
    //RiskState riskState = (RiskState) object;
    //go through all data and make sure it is a match
    return true;
    }

    //test-equals()
    //Gamestate state = new GS()
    //assertTrue()State.equals(state);
    //GS second = new GS();
    //second.sexX(z);
    //assertFalse(State.equals(second));
}