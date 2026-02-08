package mchorse.bbs.network.core.server;

import mchorse.bbs.network.core.AbstractDispatcher;
import mchorse.bbs.network.core.utils.ChannelHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.MultiThreadIoEventLoopGroup;
import io.netty.channel.nio.NioIoHandler;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import mchorse.bbs.network.core.utils.Side;
import mchorse.bbs.network.core.utils.SideOnly;

@SideOnly(Side.SERVER)
public class NettyServer {
    private MultiThreadIoEventLoopGroup bossGroup;
    private MultiThreadIoEventLoopGroup workerGroup;
    private AbstractDispatcher dispatcher;
    private String encryptionKey;
    private Class<? extends ChannelHandler> handler;

    public NettyServer(AbstractDispatcher dispatcher, Class<? extends ChannelHandler> handler) {
        this(dispatcher, handler, null);
    }

    public NettyServer(AbstractDispatcher dispatcher, Class<? extends ChannelHandler> handler, String encryptionKey) {
        this.bossGroup = new MultiThreadIoEventLoopGroup(NioIoHandler.newFactory());
        this.workerGroup = new MultiThreadIoEventLoopGroup(NioIoHandler.newFactory());
        this.dispatcher = dispatcher;
        this.encryptionKey = encryptionKey;
        this.handler = handler;

        if (encryptionKey != null && !encryptionKey.isEmpty()) {
            System.out.println("Encryption enabled with key: " + maskKey(encryptionKey));
        }
    }

    public ChannelFuture startup(int port) {
        ServerBootstrap bootstrap = new ServerBootstrap();

        bootstrap
            .group(this.bossGroup, this.workerGroup)
            .channel(NioServerSocketChannel.class)
            .option(ChannelOption.SO_BACKLOG, 1024)
            .option(ChannelOption.AUTO_CLOSE, true)
            .option(ChannelOption.SO_REUSEADDR, true)
            .childOption(ChannelOption.SO_KEEPALIVE, true)
            .childOption(ChannelOption.TCP_NODELAY, true)
            .childHandler(new ServerChannel(this.dispatcher, this.handler, this.encryptionKey));

        ChannelFuture future = bootstrap.bind(port).syncUninterruptibly();

        System.out.println("Server started on " + port);

        return future;
    }

    private String maskKey(String key) {
        if (key.length() <= 4) {
            return "****";
        } else {
            return key.substring(0, 2) + "****" + key.substring(key.length() - 2);
        }
    }

    public void delete() {
        this.bossGroup.shutdownGracefully();
        this.workerGroup.shutdownGracefully();
    }
}
