package com.example.riskgame.Risk;
/**
 * RiskMainActivity
 * Main activity for Risk Game. Pretty much a copy of the Tic Tac Toe example.
 *
 * @author Dylan Kramis
 * @version 11/7/2021
 */

import com.example.riskgame.GameFramework.GameMainActivity;
import com.example.riskgame.GameFramework.LocalGame;
import com.example.riskgame.GameFramework.gameConfiguration.GameConfig;
import com.example.riskgame.GameFramework.gameConfiguration.GamePlayerType;
import com.example.riskgame.GameFramework.infoMessage.GameState;
import com.example.riskgame.GameFramework.players.GamePlayer;
import com.example.riskgame.GameFramework.utilities.Logger;
import com.example.riskgame.GameFramework.utilities.Saving;
import com.example.riskgame.R;
import com.example.riskgame.Risk.infoMessage.RiskGameState;
import com.example.riskgame.Risk.players.RiskBasicComputerPlayer;
import com.example.riskgame.Risk.players.RiskHumanPlayer;

import java.util.ArrayList;

public class RiskMainActivity extends GameMainActivity {

    // instance variables
    private static final String TAG = "RiskMainActivity";
    public static final int PORT_NUMBER = 2830;

    /**
     * createDefaultConfig
     * Creates the default configuration for the game.
     *
     * @return default config
     */
    @Override
    public GameConfig createDefaultConfig() {

        // define allowed player
        ArrayList<GamePlayerType> playerTypes = new ArrayList<>();

        // human player
        playerTypes.add(new GamePlayerType("Local Human Player") {
            @Override
            public GamePlayer createPlayer(String name) {
                return new RiskHumanPlayer(name);
            }
        });

        // dumber computer player
        playerTypes.add(new GamePlayerType("Computer Player (dumber)") {
            @Override
            public GamePlayer createPlayer(String name) {
                return new RiskBasicComputerPlayer(name);
            }
        });

        GameConfig defaultConfig = new GameConfig(playerTypes, 2, 4,
                "Risk", PORT_NUMBER);

        // default players
        defaultConfig.addPlayer("Human", 0);
        defaultConfig.addPlayer("Computer", 1);

        // remote player
        defaultConfig.setRemoteData("Remote Player", "", 0);

        return defaultConfig;
    }

    /**
     * createLocalGame
     * Creates a local game.
     *
     * @param gameState
     *              The desired gameState to start at or null for new game
     * @return new local game
     */
    @Override
    public LocalGame createLocalGame(GameState gameState) {
        if (gameState == null)  {
            return new RiskLocalGame();
        }
        return new RiskLocalGame((RiskGameState) gameState);
    }

    /**
     * saveGame
     * Saves an ongoing game.
     *
     * @param gameName name of save state
     * @return saved game state
     */
    @Override
    public GameState saveGame(String gameName) {
        return super.saveGame(getGameString(gameName));
    }

    /**
     * loadGame
     * Loads a saved game state.
     *
     * @param gameName name of save state
     * @return loaded game state
     */
    @Override
    public GameState loadGame(String gameName) {
        String appName = getGameString(gameName);
        super.loadGame(appName);
        Logger.log(TAG, "Loading: " + gameName);
        return (GameState) new RiskGameState((RiskGameState)
                Saving.readFromFile(appName, this.getApplicationContext()));
    }
}