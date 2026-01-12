package mchorse.bbs.network.packet;

import mchorse.bbs.network.AbstractDispatcher;

public class PacketContext {
    public AbstractDispatcher dispatcher;

    public PacketContext(AbstractDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    public void send(Packet packet) {
        this.dispatcher.send(packet);
    }
}
