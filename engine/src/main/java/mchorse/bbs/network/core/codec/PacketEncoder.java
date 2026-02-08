package mchorse.bbs.network.core.codec;

import mchorse.bbs.network.core.utils.ByteSerialize;
import mchorse.bbs.network.core.utils.ConnectionChannel;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class PacketEncoder extends MessageToByteEncoder<ConnectionChannel.PacketType> {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, ConnectionChannel.PacketType packet, ByteBuf byteBuf) {
        ByteSerialize.writeString(byteBuf, packet.getId());
        packet.getPacket().toBytes(byteBuf);
    }
}
