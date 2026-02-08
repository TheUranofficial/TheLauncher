package mchorse.bbs.network.core.server;

import mchorse.bbs.network.core.packet.Packet;
import mchorse.bbs.network.core.utils.Side;
import mchorse.bbs.network.core.utils.SideOnly;

@SideOnly(Side.SERVER)
public abstract class ServerPacket extends Packet implements ServerPacketHandler {
}
