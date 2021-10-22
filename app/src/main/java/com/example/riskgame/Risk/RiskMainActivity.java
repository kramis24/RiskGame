package com.example.riskgame.Risk;

import com.example.riskgame.GameFramework.GameMainActivity;
import com.example.riskgame.GameFramework.LocalGame;
import com.example.riskgame.GameFramework.gameConfiguration.GameConfig;
import com.example.riskgame.GameFramework.infoMessage.GameState;

public class RiskMainActivity extends GameMainActivity {

    private static final String TAG = "RiskMainActivity";
    // TODO port number

    @Override
    public GameConfig createDefaultConfig() {
        return null;
    }

    @Override
    public LocalGame createLocalGame(GameState gameState) {
        return null;
    }

    @Override
    public GameState saveGame(String gameName) {
        return super.saveGame(getGameString(gameName));
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