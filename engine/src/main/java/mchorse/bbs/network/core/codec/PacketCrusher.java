package mchorse.bbs.network.core.codec;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import mchorse.bbs.data.DataStorageUtils;
import mchorse.bbs.data.types.BaseType;
import mchorse.bbs.network.core.utils.ByteSerialize;
import mchorse.bbs.network.core.utils.ConnectionChannel;
import mchorse.bbs.network.core.utils.IBufferReceiver;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class PacketCrusher {
    public static final int BUFFER_SIZE = 30000;

    private Map<Integer, ByteArrayOutputStream> chunks = new HashMap<>();
    private int counter;

    public void reset() {
        this.chunks.clear();
        this.counter = 0;
    }

    public void receive(ByteBuf buf, IBufferReceiver receiver) {
        int id = ByteSerialize.readVarInt(buf);
        int index = ByteSerialize.readVarInt(buf);
        int total = ByteSerialize.readVarInt(buf);
        int size = ByteSerialize.readVarInt(buf);
        byte[] bytes = new byte[size];

        buf.readBytes(bytes);

        ByteArrayOutputStream map = this.chunks.computeIfAbsent(id, (k) -> new ByteArrayOutputStream(total * BUFFER_SIZE));

        try {
            map.write(bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (index == total - 1) {
            byte[] finalBytes = map.toByteArray();

            if (finalBytes.length == 1 && finalBytes[0] == 69) {
                finalBytes = null;
            }

            receiver.receiveBuffer(finalBytes, buf);
            this.chunks.remove(id);
        }
    }

    public void send(ConnectionChannel channel, String id, BaseType baseType, Consumer<ByteBuf> consumer) {
        this.send(Collections.singleton(channel), id, baseType, consumer);
    }

    public void send(ConnectionChannel channel, String id, byte[] bytes, Consumer<ByteBuf> consumer) {
        this.send(Collections.singleton(channel), id, bytes, consumer);
    }

    public void send(Collection<ConnectionChannel> channels, String id, BaseType baseType, Consumer<ByteBuf> consumer) {
        this.send(channels, id, DataStorageUtils.writeToBytes(baseType), consumer);
    }

    public void send(Collection<ConnectionChannel> channels, String id, byte[] bytes, Consumer<ByteBuf> consumer) {
        if (bytes.length == 0) {
            /* Ultra funny meme from McHorse */
            bytes = new byte[]{69};
        }

        int total = Math.max((int) Math.ceil(bytes.length / (float) BUFFER_SIZE), 1);
        int counter = this.counter;

        for (int index = 0; index < total; index++) {
            int offset = index * BUFFER_SIZE;

            ByteBuf buf = Unpooled.buffer();
            int size = Math.min(BUFFER_SIZE, bytes.length - offset);

            ByteSerialize.writeVarInt(buf, counter);
            ByteSerialize.writeVarInt(buf, index);
            ByteSerialize.writeVarInt(buf, total);
            ByteSerialize.writeVarInt(buf, size);
            buf.writeBytes(bytes, offset, size);

            if (consumer != null && index == total - 1) {
                consumer.accept(buf);
            }

            for (ConnectionChannel channel : channels) {
                channel.send(id, buf);
            }
        }

        this.counter += 1;
    }
}