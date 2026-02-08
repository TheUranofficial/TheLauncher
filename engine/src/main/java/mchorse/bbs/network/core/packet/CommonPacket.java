package mchorse.bbs.network.core.packet;

import mchorse.bbs.network.core.client.ClientPacketHandler;
import mchorse.bbs.network.core.server.ServerPacketHandler;

public abstract class CommonPacket extends Packet implements ClientPacketHandler, ServerPacketHandler {
}
