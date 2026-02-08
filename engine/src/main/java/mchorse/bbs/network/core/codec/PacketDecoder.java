package mchorse.bbs.network.core.codec;

import mchorse.bbs.network.core.AbstractDispatcher;
import mchorse.bbs.network.core.packet.Packet;
import mchorse.bbs.network.core.utils.ByteSerialize;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class PacketDecoder extends ByteToMessageDecoder {
    private AbstractDispatcher dispatcher;

    public PacketDecoder(AbstractDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf byteBuf, List<Object> out) {
        if (!byteBuf.isReadable()) {
            return;
        }

        String packetId = ByteSerialize.readString(byteBuf);
        Packet packet = this.dispatcher.create(packetId);

        packet.fromBytes(byteBuf);
        out.add(packet);
    }
}
