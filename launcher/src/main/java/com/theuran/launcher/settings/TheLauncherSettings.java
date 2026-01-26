package com.theuran.launcher.settings;

import mchorse.bbs.BBS;
import mchorse.bbs.settings.SettingsBuilder;
import mchorse.bbs.settings.values.ValueBoolean;
import mchorse.bbs.settings.values.ValueInt;
import mchorse.bbs.settings.values.ValueString;

public class TheLauncherSettings {
    public static ValueBoolean welcome;
    public static ValueString serverIp;
    public static ValueInt serverPort;
    public static ValueString playerName;
    public static ValueString playerUUID;
    public static ValueString javaArgs;
    public static ValueInt ram;

    public static void register(SettingsBuilder builder) {
        welcome = builder.category("server").getBoolean("welcome", false);
        welcome.invisible();
        serverIp = builder.getString("server_ip", "");
        serverPort = builder.getInt("server_port", 0, 0, 65535);
        playerName = builder.category("client").getString("player_name", "");
        playerUUID = builder.getString("player_uuid", "");
        playerUUID.invisible();
        ram = builder.getInt("ram", 2048);
        javaArgs = builder.getString("java_args", "-XX:-UseAdaptiveSizePolicy -XX:-OmitStackTraceInFastThrow -Dfml.ignorePatchDiscrepancies=true -Dfml.ignoreInvalidMinecraftCertificates=true -Djava.library.path=" + BBS.getGamePath(".minecraft/natives") + " -XX:HeapDumpPath=MojangTricksIntelDriversForPerformance_javaw.exe_minecraft.exe.heapdump -Dminecraft.api.auth.host=localhost -Dminecraft.api.account.host=localhost -Dminecraft.api.session.host=localhost -Dminecraft.api.services.host=localhost");
    }
}