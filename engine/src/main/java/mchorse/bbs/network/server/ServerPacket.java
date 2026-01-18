package mchorse.bbs.network.server;

import mchorse.bbs.network.packet.Packet;
import mchorse.bbs.network.utils.Side;
import mchorse.bbs.network.utils.SideOnly;

@SideOnly(Side.SERVER)
public abstract class ServerPacket extends Packet implements ServerPacketHandler {
}
