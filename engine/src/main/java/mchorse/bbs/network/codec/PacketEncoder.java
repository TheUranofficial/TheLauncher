package mchorse.bbs.network.codec;

import mchorse.bbs.network.AbstractDispatcher;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import mchorse.bbs.network.utils.ByteSerialize;

public class PacketEncoder extends MessageToByteEncoder<AbstractDispatcher.PacketType> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, AbstractDispatcher.PacketType packet, ByteBuf byteBuf) throws Exception {
        ByteSerialize.writeString(byteBuf, packet.getId());
        packet.getPacket().toBytes(byteBuf);
    }
}
