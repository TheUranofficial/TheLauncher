package mchorse.bbs.network.packets;

import mchorse.bbs.BBSData;
import mchorse.bbs.data.DataStorageUtils;
import mchorse.bbs.data.types.BaseType;
import mchorse.bbs.data.types.ByteType;
import mchorse.bbs.data.types.ListType;
import mchorse.bbs.data.types.MapType;
import mchorse.bbs.gamemode.GameModeManager;
import mchorse.bbs.network.Dispatcher;
import mchorse.bbs.network.core.packet.CommonPacket;
import mchorse.bbs.network.core.utils.ConnectionChannel;
import mchorse.bbs.network.core.utils.Side;
import mchorse.bbs.network.core.utils.SideOnly;
import mchorse.bbs.settings.values.ValueEnum;
import mchorse.bbs.settings.values.ValueGroup;
import mchorse.bbs.settings.values.ValueInt;
import mchorse.bbs.settings.values.ValueType;
import mchorse.bbs.utils.repos.RepositoryOperation;

import java.util.function.Consumer;

public class ManagerDataPacket extends CommonPacket {
    private final ValueInt callbackId = new ValueInt("callbackId", 0);
    private final ValueEnum<RepositoryOperation> operation = new ValueEnum<>("operation", null);
    private final ValueType data = new ValueType("data", null);

    public ManagerDataPacket() {
        super();
        this.add(this.callbackId, this.operation, this.data);
    }

    public ManagerDataPacket(RepositoryOperation operation, BaseType data, int callbackId) {
        this();
        this.operation.set(operation);
        this.data.set(data);
        this.callbackId.set(callbackId);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void handleClient(ConnectionChannel context) {
        Consumer<BaseType> callback = Dispatcher.callbacks.remove(this.callbackId.get());

        if (callback != null) {
            callback.accept(this.data.get());
        }
    }

    @SideOnly(Side.SERVER)
    @Override
    public void handle(ConnectionChannel channel) {
        GameModeManager manager = BBSData.getGameModes();
        MapType data = this.data.get().asMap();

        switch (this.operation.get()) {
            case LOAD:
                String id = data.getString("id");
                ValueGroup group = manager.load(id);

                Dispatcher.send(new ManagerDataPacket(this.operation.get(), group.toData(), this.callbackId.get()), channel);
                break;
            case SAVE:
                manager.save(data.getString("id"), data.getMap("data"));
                break;
            case RENAME:
                manager.rename(data.getString("from"), data.getString("to"));
                break;
            case DELETE:
                manager.delete(data.getString("id"));
                break;
            case KEYS:
                ListType list = DataStorageUtils.stringListToData(manager.getKeys());

                Dispatcher.send(new ManagerDataPacket(this.operation.get(), list, this.callbackId.get()), channel);
                break;
            case ADD_FOLDER:
                Dispatcher.send(new ManagerDataPacket(this.operation.get(), new ByteType(manager.addFolder(data.getString("folder"))), this.callbackId.get()), channel);
                break;
            case RENAME_FOLDER:
                Dispatcher.send(new ManagerDataPacket(this.operation.get(), new ByteType(manager.renameFolder(data.getString("from"), data.getString("to"))), this.callbackId.get()), channel);
                break;
            case DELETE_FOLDER:
                Dispatcher.send(new ManagerDataPacket(this.operation.get(), new ByteType(manager.deleteFolder(data.getString("folder"))), this.callbackId.get()), channel);
                break;
        }
    }
}
