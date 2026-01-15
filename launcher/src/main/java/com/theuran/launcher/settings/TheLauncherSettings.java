package com.theuran.launcher.settings;

import mchorse.bbs.settings.SettingsBuilder;
import mchorse.bbs.settings.values.ValueBoolean;
import mchorse.bbs.settings.values.ValueInt;
import mchorse.bbs.settings.values.ValueString;

public class TheLauncherSettings {
    public static ValueBoolean welcome;
    public static ValueString serverIp;
    public static ValueString playerName;
    public static ValueInt serverPort;

    public static void register(SettingsBuilder builder) {
        welcome = builder.category("server").getBoolean("welcome", false);
        welcome.invisible();
        serverIp = builder.getString("server_ip", "");
        serverPort = builder.getInt("server_port", 0, 0, 65535);
        playerName = builder.getString("player_name", "");
    }
}
