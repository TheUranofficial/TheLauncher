package com.theuran;

import mchorse.bbs.data.types.MapType;
import mchorse.bbs.utils.JavaLauncher;

import java.io.File;
import java.util.List;

public class Launcher {
    public static void main(String[] strings) {
        JavaLauncher launcher = new JavaLauncher();
        MapType defaultSettings = new MapType();

        defaultSettings.putString("server.directory", "data");
        defaultSettings.putInt("server.port", 1337);

        List<String> args = launcher.getArguments("com.theuran.server.TheLauncherServer");
        MapType settings = launcher.readSettings(new File("server.json"), defaultSettings);

        String serverDirectory = settings.getString("server.directory");

        args.add("--serverDirectory");
        args.add(serverDirectory);

        args.add("-port");
        args.add(String.valueOf(settings.getInt("server.port")));

        try {
            File logFile = launcher.getLogFile(serverDirectory);

            launcher.launch(args, logFile);

            System.out.println(String.join(" ", args));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
