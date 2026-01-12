package mchorse.bbs.network;

import io.netty.channel.Channel;
import mchorse.bbs.network.client.ClientPacketHandler;
import mchorse.bbs.network.packet.Packet;
import mchorse.bbs.network.packet.PacketContext;
import mchorse.bbs.network.packet.PacketRegistry;
import mchorse.bbs.network.server.ServerPacketHandler;
import mchorse.bbs.network.utils.Side;

public abstract class AbstractDispatcher {
    private PacketRegistry registry = new PacketRegistry();
    private Channel channel;
    private Side side;

    public AbstractDispatcher(Side side) {
        this.side = side;
        this.setup();
    }

    protected abstract void setup();

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public <T extends Packet> void register(Class<T> packet) {
        this.registry.register(packet);
    }

    public void send(Packet packet) {
        this.channel.writeAndFlush(new PacketType(this.registry.getId(packet), packet));
    }

    public void handlePacket(Packet packet, PacketContext context) {
        if (packet instanceof ClientPacketHandler && this.side.isClient()) {
            ((ClientPacketHandler) packet).handleClient(context);
        }

        if (packet instanceof ServerPacketHandler && this.side.isServer()) {
            ((ServerPacketHandler) packet).handle(context);
        }
    }

    public Packet create(String id) {
        return this.registry.create(id);
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
