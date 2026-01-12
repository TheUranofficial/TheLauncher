package mchorse.bbs.network.utils;

import io.netty.buffer.ByteBuf;

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
        writeVarInt(buf, string.length());
        buf.writeCharSequence(string, StandardCharsets.UTF_8);
    }

    public static String readString(ByteBuf buf) {
        return buf.readCharSequence(readVarInt(buf), StandardCharsets.UTF_8).toString();
    }
}
