package mchorse.bbs.network.core;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import mchorse.bbs.network.core.packet.Packet;
import mchorse.bbs.network.core.packet.PacketRegistry;
import mchorse.bbs.network.core.utils.ConnectionChannel;

public abstract class AbstractDispatcher {
    private PacketRegistry registry = new PacketRegistry();

    public AbstractDispatcher() {
        this.setup();
    }

    protected abstract void setup();

    public <T extends Packet> void register(Class<T> packet) {
        this.registry.register(packet);
    }

    public Packet create(String id) {
        return this.registry.create(id);
    }

    public static void send(Packet packet, ChannelHandlerContext context) {
        context.writeAndFlush(new ConnectionChannel.PacketType(packet.getClass().getSimpleName(), packet));
    }

    public static void send(Packet packet, ConnectionChannel channel) {
        channel.getChannel().writeAndFlush(new ConnectionChannel.PacketType(packet.getClass().getSimpleName(), packet));
    }

    public static void send(Packet packet, Channel channel) {
        channel.writeAndFlush(new ConnectionChannel.PacketType(packet.getClass().getSimpleName(), packet));
    }
}
