package com.theuran.server.network;

import io.netty.channel.ChannelHandlerContext;
import mchorse.bbs.network.core.packet.Packet;
import mchorse.bbs.network.core.utils.ChannelHandler;
import mchorse.bbs.network.core.utils.ConnectionChannel;
import mchorse.bbs.network.core.utils.Side;

public class PacketHandler extends ChannelHandler {
    @Override
    protected void channelRead0(ChannelHandlerContext context, Packet packet) {
        this.handlePacket(packet, new ConnectionChannel(context.channel()), Side.SERVER);
    }

    @Override
    public void channelActive(ChannelHandlerContext context) {
        System.out.println("Client connected: " + context.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext context) {
        System.out.println("Client disconnected: " + context.channel().remoteAddress());
    }
}
