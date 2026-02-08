package mchorse.bbs.network.core.client;

import mchorse.bbs.network.core.packet.Packet;
import mchorse.bbs.network.core.utils.Side;
import mchorse.bbs.network.core.utils.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class ClientPacket extends Packet implements ClientPacketHandler {
}
