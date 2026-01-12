package mchorse.bbs.network.server;

import mchorse.bbs.network.packet.PacketContext;

public interface ServerPacketHandler {
    void handle(PacketContext context);
}
