package mchorse.bbs.network.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import mchorse.bbs.network.AbstractDispatcher;
import mchorse.bbs.network.codec.PacketDecoder;
import mchorse.bbs.network.codec.PacketEncoder;
import mchorse.bbs.network.utils.ChannelHandler;
import mchorse.bbs.network.utils.Encryption;
import mchorse.bbs.network.utils.Side;
import mchorse.bbs.network.utils.SideOnly;

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

        pipeline.addLast("frameDecoder", new LengthFieldBasedFrameDecoder(
            1048576,
            0,
            4,
            0,
            4
        ));

        if (this.encryptionKey != null && !this.encryptionKey.isEmpty()) {
            pipeline.addLast("encryption", new Encryption(this.encryptionKey));
        }

        pipeline.addLast("decoder", new PacketDecoder(this.dispatcher));
        pipeline.addLast("encoder", new PacketEncoder());
        pipeline.addLast("handler", this.handler.getConstructor(AbstractDispatcher.class).newInstance(this.dispatcher));

        this.dispatcher.setChannel(channel);
    }
}
