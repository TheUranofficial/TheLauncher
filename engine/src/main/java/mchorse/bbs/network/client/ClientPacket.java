package mchorse.bbs.network.client;

import mchorse.bbs.network.packet.Packet;
import mchorse.bbs.network.utils.Side;
import mchorse.bbs.network.utils.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class ClientPacket extends Packet implements ClientPacketHandler {
}
