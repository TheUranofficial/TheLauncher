package mchorse.bbs.network.core.packet;

import java.util.HashMap;
import java.util.Map;

public class PacketRegistry {
    private Map<String, Class<? extends Packet>> packets = new HashMap<>();

    public <T extends Packet> void register(Class<T> packet) {
        this.packets.put(packet.getSimpleName(), packet);
    }

    public Packet create(String id) {
        try {
            return this.packets.get(id).newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}