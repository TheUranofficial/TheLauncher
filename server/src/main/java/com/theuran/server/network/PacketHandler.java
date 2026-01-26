package com.theuran.server.network;

import io.netty.channel.ChannelHandlerContext;
import mchorse.bbs.network.AbstractDispatcher;
import mchorse.bbs.network.packet.Packet;
import mchorse.bbs.network.packet.PacketContext;
import mchorse.bbs.network.utils.ChannelHandler;

public class PacketHandler extends ChannelHandler {
    public PacketHandler(AbstractDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, Packet packet) {
        System.out.println("Received packet: " + packet.getClass().getSimpleName());

        this.dispatcher.handlePacket(packet, new PacketContext(this.dispatcher));
    }

    @Override
    public void channelActive(ChannelHandlerContext context) {
        System.out.println("Client connected: " + context.channel().remoteAddress());
    }

    @Override
    public void channelInactive(ChannelHandlerContext context) {
        System.out.println("Client disconnected");
    }
}
