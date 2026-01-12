package mchorse.bbs.network.packet;

import io.netty.buffer.ByteBuf;

public abstract class Packet {
    public abstract void fromBytes(ByteBuf buf);

    public abstract void toBytes(ByteBuf buf);
}
