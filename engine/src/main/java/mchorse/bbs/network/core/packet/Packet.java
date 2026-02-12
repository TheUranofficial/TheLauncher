package mchorse.bbs.network.core.packet;

import io.netty.buffer.ByteBuf;
import mchorse.bbs.data.DataStorageUtils;
import mchorse.bbs.settings.values.ValueGroup;
import mchorse.bbs.settings.values.base.BaseValue;

public abstract class Packet extends ValueGroup {
    public Packet() {
        super("");

        this.setId(this.getClass().getSimpleName());
    }

    public void add(BaseValue... values) {
        for (BaseValue value : values) {
            this.add(value);
        }
    }

    public void fromBytes(ByteBuf buf) {
        for (BaseValue value : this.getAll()) {
            value.fromData(DataStorageUtils.readFromPacket(buf));
        }
    }

    public void toBytes(ByteBuf buf) {
        for (BaseValue value : this.getAll()) {
            DataStorageUtils.writeToPacket(buf, value.toData());
        }
    }
}
