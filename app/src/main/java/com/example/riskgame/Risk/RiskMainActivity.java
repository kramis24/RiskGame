package com.example.riskgame.Risk;

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

    private static final String TAG = "RiskMainActivity";
    public static final int PORT_NUMBER = 2830;

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

    @Override
    public LocalGame createLocalGame(GameState gameState) {
        if (gameState == null)  {
            return new RiskLocalGame();
        }
        return new RiskLocalGame((RiskGameState) gameState);
    }

    @Override
    public GameState saveGame(String gameName) {
        return super.saveGame(getGameString(gameName));
    }

    @Override
    public GameState loadGame(String gameName) {
        String appName = getGameString(gameName);
        super.loadGame(appName);
        Logger.log(TAG, "Loading: "+gameName);
        return (GameState) new RiskGameState((RiskGameState)
                Saving.readFromFile(appName, this.getApplicationContext()));
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        // initializes mapView object
//        com.example.riskgame.Risk.views.MapView mapView = (com.example.riskgame.Risk.views.MapView) findViewById(R.id.mapView);
//    }

}