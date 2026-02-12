package mchorse.bbs.network.core.utils;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.DecoderException;

import java.nio.charset.StandardCharsets;

public class ByteSerialize {
    public static void writeVarInt(ByteBuf buf, int value) {
        while ((value & ~0x7F) != 0) {
            buf.writeByte((value & 0x7F) | 0x80);
            value >>>= 7;
        }
        buf.writeByte(value);
    }

    public static int readVarInt(ByteBuf buf) {
        int numRead = 0;
        int result = 0;
        byte read;

        do {
            read = buf.readByte();

            int value = (read & 0x7F);

            result |= (value << (7 * numRead));

            if (++numRead > 5)
                System.err.println("VarInt size so much");
        } while ((read & 0x80) != 0);

        return result;
    }

    public static void writeString(ByteBuf buf, String string) {
        byte[] bytes = string.getBytes(StandardCharsets.UTF_8);

        writeVarInt(buf, bytes.length);

        buf.writeBytes(bytes);
    }

    public static String readString(ByteBuf buf) {
        byte[] bytes = new byte[readVarInt(buf)];

        buf.readBytes(bytes);

        return new String(bytes, StandardCharsets.UTF_8);
    }

    public static byte[] readByteArray(ByteBuf buf) {
        return readByteArray(buf, buf.readableBytes());
    }

    public static ByteBuf writeByteArray(ByteBuf buf, byte[] array) {
        writeVarInt(buf, array.length);
        buf.writeBytes(array);

        return buf;
    }

    public static byte[] readByteArray(ByteBuf buf, int maxSize) {
        int i = readVarInt(buf);

        if (i > maxSize) {
            throw new DecoderException("ByteArray with size " + i + " is bigger than allowed " + maxSize);
        } else {
            byte[] bs = new byte[i];

            buf.readBytes(bs);

            return bs;
        }
    }
}
