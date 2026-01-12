package mchorse.bbs.network.codec;

import mchorse.bbs.network.AbstractDispatcher;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import mchorse.bbs.network.packet.Packet;
import mchorse.bbs.network.utils.ByteSerialize;

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
