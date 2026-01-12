package mchorse.bbs.network.utils;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToMessageCodec;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class Encryption extends MessageToMessageCodec<ByteBuf, ByteBuf> {
    private Cipher encryptCipher;
    private Cipher decryptCipher;

    public Encryption(String secretKey) {
        try {
            byte[] keyBytes = new byte[16];
            byte[] passwordBytes = secretKey.getBytes(StandardCharsets.UTF_8);

            System.arraycopy(passwordBytes, 0, keyBytes, 0, Math.min(passwordBytes.length, keyBytes.length));

            SecretKeySpec keySpec = new SecretKeySpec(keyBytes, "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(keyBytes);

            this.encryptCipher = Cipher.getInstance("AES/CFB8/NoPadding");
            this.encryptCipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

            this.decryptCipher = Cipher.getInstance("AES/CFB8/NoPadding");
            this.decryptCipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void encode(ChannelHandlerContext context, ByteBuf buf, List<Object> list) throws Exception {
        byte[] plain = new byte[buf.readableBytes()];

        buf.readBytes(plain);

        byte[] encrypted = this.encryptCipher.update(plain);
        ByteBuf buffer = context.alloc().buffer(encrypted.length);

        buffer.writeBytes(encrypted);
        list.add(buffer);
    }

    @Override
    protected void decode(ChannelHandlerContext context, ByteBuf buf, List<Object> list) {
        byte[] encrypted = new byte[buf.readableBytes()];

        buf.readBytes(encrypted);

        byte[] decrypted = this.decryptCipher.update(encrypted);
        ByteBuf buffer = context.alloc().buffer(decrypted.length);

        buffer.writeBytes(decrypted);
        list.add(buffer);
    }
}
