package mchorse.bbs.network.packets;

import mchorse.bbs.network.core.client.ClientPacket;
import mchorse.bbs.network.core.utils.ConnectionChannel;
import mchorse.bbs.network.core.utils.Side;
import mchorse.bbs.network.core.utils.SideOnly;
import mchorse.bbs.settings.values.ValueString;

public class SendFilePacket extends ClientPacket {
    private final ValueString path = new ValueString("path", "");
    private final ValueString content = new ValueString("content", "");

    public SendFilePacket() {
        this.add(this.path, this.content);
    }

    public SendFilePacket(String path, String content) {
        this();
        this.path.set(path);
        this.content.set(content);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleClient(ConnectionChannel context) {

    }
}
