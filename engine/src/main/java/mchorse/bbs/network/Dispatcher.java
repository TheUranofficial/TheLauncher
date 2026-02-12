package mchorse.bbs.network;

import mchorse.bbs.data.types.BaseType;
import mchorse.bbs.network.core.AbstractDispatcher;
import mchorse.bbs.network.core.utils.Side;
import mchorse.bbs.network.core.utils.SideOnly;
import mchorse.bbs.network.packets.ManagerDataPacket;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class Dispatcher extends AbstractDispatcher {
    @SideOnly(Side.CLIENT)
    public static Map<Integer, Consumer<BaseType>> callbacks = new HashMap<>();

    @Override
    protected void setup() {
        this.register(ManagerDataPacket.class);
    }
}
