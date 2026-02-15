package com.theuran.launcher.utils.repos;

import com.theuran.launcher.TheLauncherEngine;
import mchorse.bbs.data.types.BaseType;
import mchorse.bbs.data.types.MapType;
import mchorse.bbs.gamemode.GameMode;
import mchorse.bbs.network.Dispatcher;
import mchorse.bbs.network.packets.ManagerDataPacket;
import mchorse.bbs.utils.repos.IRepository;
import mchorse.bbs.utils.repos.RepositoryOperation;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
        this.sendPacketLoad(id, (data) -> {
            if (data.isMap()) {
                callback.accept(this.create(id, data.asMap()));
            }
        });
    }

    @Override
    public void save(String id, MapType data) {
        MapType map = new MapType();

        map.putString("id", id);
        map.put("data", data);

        this.sendPacket(RepositoryOperation.SAVE, map, -1);
    }

    @Override
    public void rename(String from, String to) {
        MapType map = new MapType();

        map.putString("from", from);
        map.putString("to", to);

        this.sendPacket(RepositoryOperation.RENAME, map, -1);
    }

    @Override
    public void delete(String id) {
        MapType map = new MapType();

        map.putString("id", id);

        this.sendPacket(RepositoryOperation.DELETE, map, -1);
    }

    @Override
    public void requestKeys(Consumer<Collection<String>> callback) {
        MapType map = new MapType();

        this.sendPacket(RepositoryOperation.KEYS, map, (data) -> {
            if (data.isList()) {
                List<String> list = new ArrayList<>();

                for (BaseType type : data.asList()) {
                    list.add(type.asString());
                }

                callback.accept(list);
            }
        });
    }

    @Override
    public File getFolder() {
        return null;
    }

    @Override
    public void addFolder(String path, Consumer<Boolean> callback) {
        MapType map = new MapType();

        map.putString("folder", path);

        this.sendPacket(RepositoryOperation.ADD_FOLDER, map, (data) -> {
            if (data.isNumeric()) {
                callback.accept(data.asNumeric().boolValue());
            }
        });
    }

    @Override
    public void renameFolder(String from, String to, Consumer<Boolean> callback) {
        MapType map = new MapType();

        map.putString("from", from);
        map.putString("to", to);

        this.sendPacket(RepositoryOperation.RENAME_FOLDER, map, (data) -> {
            if (data.isNumeric()) {
                callback.accept(data.asNumeric().boolValue());
            }
        });
    }

    @Override
    public void deleteFolder(String path, Consumer<Boolean> callback) {
        MapType map = new MapType();

        map.putString("folder", path);

        this.sendPacket(RepositoryOperation.DELETE_FOLDER, map, (data) -> {
            if (data.isNumeric()) {
                callback.accept(data.asNumeric().boolValue());
            }
        });
    }

    private void sendPacket(RepositoryOperation operation, MapType data, int callbackId) {
        Dispatcher.send(new ManagerDataPacket(operation, data, callbackId), TheLauncherEngine.getChannel());
    }

    private void sendPacket(RepositoryOperation operation, MapType data, Consumer<BaseType> callback) {
        Dispatcher.callbacks.put(Dispatcher.ids, callback);
        this.sendPacket(operation, data, Dispatcher.ids);
        Dispatcher.ids++;
    }

    private void sendPacketLoad(String id, Consumer<BaseType> callback) {
        MapType data = new MapType();

        data.putString("id", id);

        this.sendPacket(RepositoryOperation.LOAD, data, callback);
    }
}
