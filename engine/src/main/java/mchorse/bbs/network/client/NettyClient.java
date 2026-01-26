package mchorse.bbs.network.client;

import mchorse.bbs.network.AbstractDispatcher;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.nio.NioSocketChannel;
import mchorse.bbs.network.utils.ChannelHandler;
import mchorse.bbs.network.utils.Side;
import mchorse.bbs.network.utils.SideOnly;

@SideOnly(Side.CLIENT)
public class NettyClient {
    private AbstractDispatcher dispatcher;
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

    public void connect(String host, int port) {
        Bootstrap bootstrap = new Bootstrap();

        bootstrap
            .group(this.workerGroup)
            .channel(NioSocketChannel.class)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .handler(new ClientChannel(this.dispatcher, this.handler, this.encryptionKey));

        bootstrap.connect(host, port).syncUninterruptibly();
        System.out.println("Connected to " + host + ":" + port);
    }

    public void delete() {
        this.workerGroup.shutdownGracefully();
    }
}
