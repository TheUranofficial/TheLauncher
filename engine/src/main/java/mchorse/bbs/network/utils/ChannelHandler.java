package mchorse.bbs.network.utils;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import mchorse.bbs.network.AbstractDispatcher;
import mchorse.bbs.network.packet.Packet;

public abstract class ChannelHandler extends SimpleChannelInboundHandler<Packet> {
    protected AbstractDispatcher dispatcher;

    public ChannelHandler(AbstractDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        cause.printStackTrace();
        context.close();
    }
}
