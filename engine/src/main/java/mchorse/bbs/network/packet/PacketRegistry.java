package mchorse.bbs.network.packet;

import java.util.HashMap;
import java.util.Map;

public class PacketRegistry {
    private Map<String, Class<? extends Packet>> packets = new HashMap<>();

    public <T extends Packet> void register(Class<T> packet) {
        this.packets.put(packet.getSimpleName(), packet);
    }

    public String getId(Packet packet) {
        return packet.getClass().getSimpleName();
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