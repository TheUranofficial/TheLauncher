package mchorse.bbs.network.core.utils;

import mchorse.bbs.network.core.client.ClientPacketHandler;
import mchorse.bbs.network.core.packet.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import mchorse.bbs.network.core.server.ServerPacketHandler;

public abstract class ChannelHandler extends SimpleChannelInboundHandler<Packet> {
    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        cause.printStackTrace();
        context.close();
    }

    protected void handlePacket(Packet packet, ConnectionChannel channel, Side side) {
        if (packet instanceof ClientPacketHandler && side.isClient()) {
            ((ClientPacketHandler) packet).handleClient(channel);
        }

        if (packet instanceof ServerPacketHandler && side.isServer()) {
            ((ServerPacketHandler) packet).handle(channel);
        }
    }
}
