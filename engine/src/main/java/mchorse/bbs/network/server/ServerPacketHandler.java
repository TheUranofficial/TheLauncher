package mchorse.bbs.network.server;

import mchorse.bbs.network.packet.PacketContext;
import mchorse.bbs.network.utils.Side;
import mchorse.bbs.network.utils.SideOnly;

public interface ServerPacketHandler {
    @SideOnly(Side.SERVER)
    void handle(PacketContext context);
}
