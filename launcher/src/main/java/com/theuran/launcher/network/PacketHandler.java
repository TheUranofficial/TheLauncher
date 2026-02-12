package com.theuran.launcher.network;

import com.theuran.launcher.TheLauncherEngine;
import com.theuran.launcher.ui.UIKeysApp;
import io.netty.channel.ChannelHandlerContext;
import mchorse.bbs.BBSSettings;
import mchorse.bbs.network.core.packet.Packet;
import mchorse.bbs.network.core.utils.ChannelHandler;
import mchorse.bbs.network.core.utils.ConnectionChannel;
import mchorse.bbs.network.core.utils.Side;

public class PacketHandler extends ChannelHandler {
    @Override
    protected void channelRead0(ChannelHandlerContext context, Packet packet) {
        this.handlePacket(packet, new ConnectionChannel(context.channel()), Side.CLIENT);
    }

    @Override
    public void channelActive(ChannelHandlerContext context) {
        System.out.println("Connected to server");
    }

    @Override
    public void channelInactive(ChannelHandlerContext context) {
        System.out.println("Disconnected from server");

        TheLauncherEngine.getInstance().screen.menu.context.notify(UIKeysApp.NETWORK_DISCONNECTED, BBSSettings.primaryColor());
    }
}
