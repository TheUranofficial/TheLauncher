package mchorse.bbs.network.core.client;

import mchorse.bbs.network.core.AbstractDispatcher;
import mchorse.bbs.network.core.codec.PacketDecoder;
import mchorse.bbs.network.core.codec.PacketEncoder;
import mchorse.bbs.network.core.utils.ChannelHandler;
import mchorse.bbs.network.core.utils.Encryption;
import mchorse.bbs.network.core.utils.Side;
import mchorse.bbs.network.core.utils.SideOnly;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldPrepender;

@SideOnly(Side.CLIENT)
public class ClientChannel extends ChannelInitializer<SocketChannel> {
    private AbstractDispatcher dispatcher;
    private String encryptionKey;
    private Class<? extends ChannelHandler> handler;

    public ClientChannel(AbstractDispatcher dispatcher, Class<? extends ChannelHandler> handler, String encryptionKey) {
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

        pipeline.addLast("prepender", new LengthFieldPrepender(4));

        pipeline.addLast("decoder", new PacketDecoder(this.dispatcher));
        pipeline.addLast("encoder", new PacketEncoder());
        pipeline.addLast("handler", this.handler.newInstance());
    }
}
