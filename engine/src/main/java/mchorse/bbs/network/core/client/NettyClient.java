package mchorse.bbs.network.core.client;

import mchorse.bbs.network.core.AbstractDispatcher;
import mchorse.bbs.network.core.utils.ChannelHandler;
import mchorse.bbs.network.core.utils.Side;
import mchorse.bbs.network.core.utils.SideOnly;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.nio.NioSocketChannel;

@SideOnly(Side.CLIENT)
public class NettyClient {
    public AbstractDispatcher dispatcher;
    private String encryptionKey;
    private MultiThreadIoEventLoopGroup workerGroup = new MultiThreadIoEventLoopGroup(NioIoHandler.newFactory());
    private Class<? extends ChannelHandler> handler;

    public NettyClient(AbstractDispatcher dispatcher, Class<? extends ChannelHandler> handler) {
        this(dispatcher, handler, null);
    }

    public NettyClient(AbstractDispatcher dispatcher, Class<? extends ChannelHandler> handler, String encryptionKey) {
        this.dispatcher = dispatcher;
        this.encryptionKey = encryptionKey;
        this.handler = handler;

        if (encryptionKey != null && !encryptionKey.isEmpty()) {
            System.out.println("Encryption enabled");
        }
    }

    public ChannelFuture connect(String host, int port) {
        Bootstrap bootstrap = new Bootstrap();

        bootstrap
            .group(this.workerGroup)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .handler(new ClientChannel(this.dispatcher, this.handler, this.encryptionKey));

        ChannelFuture future = null;

        try {
            future = bootstrap.connect(host, port).syncUninterruptibly();

            System.out.println("Connected to " + host + ":" + port);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return future;
    }

    public void delete() {
        this.workerGroup.shutdownGracefully();
    }
}
