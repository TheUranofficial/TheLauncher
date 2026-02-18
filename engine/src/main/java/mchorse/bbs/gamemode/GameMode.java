package mchorse.bbs.gamemode;

import mchorse.bbs.settings.values.ValueGroup;
import mchorse.bbs.settings.values.ValueString;

public class GameMode extends ValueGroup {
    public ValueString version = new ValueString("version", "");
    public ValueString modLoader = new ValueString("modLoader", "");
    public ValueString description = new ValueString("description", "");

    public GameMode() {
        super("");

        this.add(this.version);
        this.add(this.modLoader);
        this.add(this.description);
    }
}
