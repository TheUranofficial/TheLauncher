package com.theuran.launcher.network;

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
        this.dispatcher.handlePacket(packet, new PacketContext(this.dispatcher));
    }
}
