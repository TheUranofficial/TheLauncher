package com.theuran.launcher.settings;

import mchorse.bbs.settings.SettingsBuilder;
import mchorse.bbs.settings.values.ValueBoolean;
import mchorse.bbs.settings.values.ValueInt;
import mchorse.bbs.settings.values.ValueString;

public class TheLauncherSettings {
    public static ValueBoolean firstRun;
    public static ValueString serverIp;
    public static ValueInt serverPort;

    public static void register(SettingsBuilder builder) {
        firstRun = builder.category("server").getBoolean("first_run", false);
        firstRun.invisible();
        serverIp = builder.getString("server_ip", "");
        serverPort = builder.getInt("server_port", 0);
    }
}
