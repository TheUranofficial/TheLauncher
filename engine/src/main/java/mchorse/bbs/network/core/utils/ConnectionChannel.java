package mchorse.bbs.network.core.utils;

import mchorse.bbs.network.core.packet.Packet;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;

public class ConnectionChannel {
    private Channel channel;

    public ConnectionChannel(Channel channel) {
        this.channel = channel;
    }

    public void send(Packet packet) {
        this.channel.writeAndFlush(new PacketType(packet.getClass().getSimpleName(), packet));
    }

    public Channel getChannel() {
        return this.channel;
    }

    public static class PacketType {
        private final String id;
        private final Packet packet;

        public PacketType(String id, Packet packet) {
            this.id = id;
            this.packet = packet;
        }

        public String getId() {
            return this.id;
        }

        public Packet getPacket() {
            return this.packet;
        }
    }
}