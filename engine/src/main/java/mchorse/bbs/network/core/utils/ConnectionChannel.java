package mchorse.bbs.network.core.utils;

import io.netty.buffer.ByteBuf;
import mchorse.bbs.network.core.packet.Packet;
import io.netty.channel.Channel;
import java.util.function.Consumer;

public class ConnectionChannel {
    private Channel channel;

    public ConnectionChannel(Channel channel) {
        this.channel = channel;
    }

    public void send(Packet packet) {
        this.channel.writeAndFlush(new PacketType(packet.getClass().getSimpleName(), packet::toBytes));
    }

    public void send(String id, ByteBuf buf) {
        this.channel.writeAndFlush(new PacketType(id, (buffer) -> buffer.writeBytes(buf)));
    }

    public Channel getChannel() {
        return this.channel;
    }

    public static class PacketType {
        private final String id;
        private final Consumer<ByteBuf> byteBuf;

        public PacketType(String id, Consumer<ByteBuf> byteBuf) {
            this.id = id;
            this.byteBuf = byteBuf;
        }

        public String getId() {
            return this.id;
        }

        public Consumer<ByteBuf> getByteBuf() {
            return this.byteBuf;
        }
    }
}