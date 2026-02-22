package mchorse.bbs.network.core.utils;

import io.netty.buffer.ByteBuf;

public interface IBufferReceiver {
    void receiveBuffer(byte[] bytes, ByteBuf byteBuf);
}