package mchorse.bbs.network.packets;

import mchorse.bbs.BBSData;
import mchorse.bbs.network.core.server.ServerPacket;
import mchorse.bbs.network.core.utils.ConnectionChannel;
import mchorse.bbs.network.core.utils.Side;
import mchorse.bbs.network.core.utils.SideOnly;
import mchorse.bbs.settings.values.ValueString;
import mchorse.bbs.utils.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;

public class RequestVersionManifestPacket extends ServerPacket {
    private final ValueString version = new ValueString("version", "");

    public RequestVersionManifestPacket() {
        this.add(this.version);
    }

    public RequestVersionManifestPacket(String version) {
        this();
        this.version.set(version);
    }

    @Override
    @SideOnly(Side.SERVER)
    public void handle(ConnectionChannel channel) {
        try {
            File manifest = new File(BBSData.folder, "meta/versions/" + version + "/manifest.json");

            channel.send(new SendFilePacket("versions/" + version + "/manifest.json", IOUtils.readText(manifest)));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
