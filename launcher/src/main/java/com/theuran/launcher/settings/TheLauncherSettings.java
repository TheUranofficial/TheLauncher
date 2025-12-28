package com.theuran.launcher.settings;

import mchorse.bbs.settings.SettingsBuilder;
import mchorse.bbs.settings.values.ValueBoolean;

public class TheLauncherSettings {
    public static ValueBoolean firstRun;

    public static void register(SettingsBuilder builder) {
        firstRun = builder.category("server").getBoolean("firstRun", false);
        firstRun.invisible();
    }
}
