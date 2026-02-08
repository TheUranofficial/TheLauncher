package mchorse.bbs.network.core;

import mchorse.bbs.network.core.client.ClientPacketHandler;
import mchorse.bbs.network.core.packet.Packet;
import mchorse.bbs.network.core.packet.PacketRegistry;
import mchorse.bbs.network.core.server.ServerPacketHandler;
import mchorse.bbs.network.core.utils.ConnectionChannel;
import mchorse.bbs.network.core.utils.Side;

public abstract class AbstractDispatcher {
    private PacketRegistry registry = new PacketRegistry();

    public AbstractDispatcher() {
        this.setup();
    }

    protected abstract void setup();

    public <T extends Packet> void register(Class<T> packet) {
        this.registry.register(packet);
    }

    public void handlePacket(Packet packet, ConnectionChannel channel, Side side) {
        if (packet instanceof ClientPacketHandler && side.isClient()) {
            ((ClientPacketHandler) packet).handleClient(channel);
        }

        if (packet instanceof ServerPacketHandler && side.isServer()) {
            ((ServerPacketHandler) packet).handle(channel);
        }
    }

    public Packet create(String id) {
        return this.registry.create(id);
    }
}
