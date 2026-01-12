package mchorse.bbs.network.packet;

import mchorse.bbs.network.client.ClientPacketHandler;
import mchorse.bbs.network.server.ServerPacketHandler;

public abstract class CommonPacket extends Packet implements ClientPacketHandler, ServerPacketHandler {
}
