package mchorse.bbs.network.packets;

import io.netty.buffer.ByteBuf;
import mchorse.bbs.BBS;
import mchorse.bbs.network.core.client.ClientPacket;
import mchorse.bbs.network.core.utils.ByteSerialize;
import mchorse.bbs.network.core.utils.ConnectionChannel;
import mchorse.bbs.network.core.utils.Side;
import mchorse.bbs.network.core.utils.SideOnly;
import mchorse.bbs.settings.values.ValueString;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SendFilePacket extends ClientPacket {
    private final ValueString path = new ValueString("path", "");
    private byte[] bytes;

    public SendFilePacket() {
        this.add(this.path);
    }

    public SendFilePacket(String path, byte[] bytes) {
        this();
        this.path.set(path);
        this.bytes = bytes;
    }

    @Override
    public void toBytes(ByteBuf buf) {
        super.toBytes(buf);

        ByteSerialize.writeBytes(buf, this.bytes);
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        super.fromBytes(buf);

        this.bytes = ByteSerialize.readBytes(buf);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleClient(ConnectionChannel context) {
        File file = new File(BBS.getDataPath("meta"), this.path.get());

        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try (FileOutputStream stream = new FileOutputStream(file)) {
            stream.write(this.bytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
