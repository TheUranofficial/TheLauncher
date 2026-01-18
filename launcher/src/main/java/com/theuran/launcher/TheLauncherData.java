package com.theuran.launcher;

import com.theuran.launcher.gamemode.GameModeManager;
import mchorse.bbs.bridge.IBridge;

import java.io.File;

public class TheLauncherData {
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
