package mchorse.bbs.gamemode;

import mchorse.bbs.data.types.MapType;
import mchorse.bbs.utils.manager.BaseManager;
import mchorse.bbs.utils.manager.storage.JSONLikeStorage;

import java.io.File;

public class GameModeManager extends BaseManager<GameMode> {
    public GameModeManager(File folder) {
        super(folder);

        this.storage = new JSONLikeStorage().json();
    }

    @Override
    protected GameMode createData(String id, MapType mapType) {
        GameMode gameMode = new GameMode();

        if (mapType != null) {
            gameMode.fromData(mapType);
        }

        return gameMode;
    }
}
