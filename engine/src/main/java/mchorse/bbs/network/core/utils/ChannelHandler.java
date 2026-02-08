package mchorse.bbs.network.core.utils;

import mchorse.bbs.network.core.AbstractDispatcher;
import mchorse.bbs.network.core.packet.Packet;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

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
