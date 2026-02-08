package mchorse.bbs.network.core.client;

import mchorse.bbs.network.core.utils.ConnectionChannel;
import mchorse.bbs.network.core.utils.Side;
import mchorse.bbs.network.core.utils.SideOnly;

public interface ClientPacketHandler {
    @SideOnly(Side.CLIENT)
    void handleClient(ConnectionChannel context);
}