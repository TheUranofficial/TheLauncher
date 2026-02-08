package com.theuran.launcher.network;

import io.netty.channel.ChannelHandlerContext;
import mchorse.bbs.network.core.AbstractDispatcher;
import mchorse.bbs.network.core.packet.Packet;
import mchorse.bbs.network.core.utils.ChannelHandler;
import mchorse.bbs.network.core.utils.ConnectionChannel;
import mchorse.bbs.network.core.utils.Side;

public class PacketHandler extends ChannelHandler {
    public PacketHandler(AbstractDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, Packet packet) {
        this.dispatcher.handlePacket(packet, new ConnectionChannel(context.channel()), Side.CLIENT);
    }

    @Override
    public void channelActive(ChannelHandlerContext context) {
        System.out.println("Connected to server");
    }

    @Override
    public void channelInactive(ChannelHandlerContext context) {
        System.out.println("Disconnected from server");
    }
}
