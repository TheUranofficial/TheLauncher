package mchorse.bbs;

import mchorse.bbs.bridge.IBridge;
import mchorse.bbs.gamemode.GameModeManager;

import java.io.File;

public class BBSData {
    private static GameModeManager gameModes;

    public static GameModeManager getGameModes() {
        return gameModes;
    }

    public static void load(File folder, IBridge bridge) {
        gameModes = new GameModeManager(new File(folder, "gameModes"));
    }

    public static void delete() {
        gameModes = null;
    }
}
