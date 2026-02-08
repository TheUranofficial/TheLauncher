package com.theuran.launcher.utils.repos;

import com.theuran.launcher.gamemode.GameMode;
import mchorse.bbs.data.types.MapType;
import mchorse.bbs.utils.repos.IRepository;

import java.io.File;
import java.util.Collection;
import java.util.function.Consumer;

public class GameModeRepository implements IRepository<GameMode> {
    @Override
    public GameMode create(String id, MapType data) {
        GameMode gameMode = new GameMode();

        gameMode.setId(id);

        if (data != null) {
            gameMode.fromData(data);
        }

        return gameMode;
    }

    @Override
    public void load(String id, Consumer<GameMode> callback) {

    }

    @Override
    public void save(String id, MapType data) {

    }

    @Override
    public void rename(String id, String name) {

    }

    @Override
    public void delete(String id) {

    }

    @Override
    public void requestKeys(Consumer<Collection<String>> callback) {

    }

    @Override
    public File getFolder() {
        return null;
    }

    @Override
    public void addFolder(String path, Consumer<Boolean> callback) {

    }

    @Override
    public void renameFolder(String path, String name, Consumer<Boolean> callback) {

    }

    @Override
    public void deleteFolder(String path, Consumer<Boolean> callback) {

    }
}
