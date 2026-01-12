package mchorse.bbs.network.client;

import mchorse.bbs.network.packet.PacketContext;

public interface ClientPacketHandler {
    void handleClient(PacketContext context);
}