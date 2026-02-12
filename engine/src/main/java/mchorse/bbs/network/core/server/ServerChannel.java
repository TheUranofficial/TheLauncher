package mchorse.bbs.network.core.server;

import mchorse.bbs.core.Engine;
import mchorse.bbs.network.core.AbstractDispatcher;
import mchorse.bbs.network.core.codec.PacketDecoder;
import mchorse.bbs.network.core.codec.PacketEncoder;
import mchorse.bbs.network.core.utils.ChannelHandler;
import mchorse.bbs.network.core.utils.Encryption;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import mchorse.bbs.network.core.utils.Side;
import mchorse.bbs.network.core.utils.SideOnly;

@SideOnly(Side.SERVER)
public class ServerChannel extends ChannelInitializer<SocketChannel> {
    private AbstractDispatcher dispatcher;
    private String encryptionKey;
    private Class<? extends ChannelHandler> handler;

    public ServerChannel(AbstractDispatcher dispatcher, Class<? extends ChannelHandler> handler, String encryptionKey) {
        this.dispatcher = dispatcher;
        this.encryptionKey = encryptionKey;
        this.handler = handler;
    }

    @Override
    protected void initChannel(SocketChannel channel) throws Exception {
        ChannelPipeline pipeline = channel.pipeline();

        if (this.encryptionKey != null && !this.encryptionKey.isEmpty()) {
            pipeline.addLast("encryption", new Encryption(this.encryptionKey));
        }

        pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(
            1048576,
            0,
            4,
            0,
            4
        ));

        pipeline.addLast("decoder", new PacketDecoder(this.dispatcher));
        pipeline.addLast("encoder", new PacketEncoder());
        pipeline.addLast("handler", this.handler.newInstance());
    }
}
