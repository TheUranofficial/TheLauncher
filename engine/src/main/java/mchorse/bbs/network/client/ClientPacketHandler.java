package mchorse.bbs.network.client;

import mchorse.bbs.network.packet.PacketContext;
import mchorse.bbs.network.utils.Side;
import mchorse.bbs.network.utils.SideOnly;

public interface ClientPacketHandler {
    @SideOnly(Side.CLIENT)
    void handleClient(PacketContext context);
}