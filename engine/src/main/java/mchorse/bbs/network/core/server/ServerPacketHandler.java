package mchorse.bbs.network.core.server;

import mchorse.bbs.network.core.utils.ConnectionChannel;
import mchorse.bbs.network.core.utils.Side;
import mchorse.bbs.network.core.utils.SideOnly;

public interface ServerPacketHandler {
    @SideOnly(Side.SERVER)
    void handle(ConnectionChannel channel);
}
