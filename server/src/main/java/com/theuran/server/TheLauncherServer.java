package com.theuran.server;

import com.theuran.server.network.PacketHandler;
import mchorse.bbs.BBSData;
import mchorse.bbs.data.types.MapType;
import mchorse.bbs.network.Dispatcher;
import mchorse.bbs.network.core.server.NettyServer;
import mchorse.bbs.utils.CrashReport;
import mchorse.bbs.utils.Profiler;
import mchorse.bbs.utils.TimePrintStream;
import mchorse.bbs.utils.cli.ArgumentParser;
import mchorse.bbs.utils.cli.ArgumentType;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;

public class TheLauncherServer {
    public static final String VERSION = "0.1";

    public static final Profiler PROFILER = new Profiler();

    /* Command line arguments */
    public File directory;
    public int port;

    private static boolean canLock(File file) {
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(file, "rw");
            FileLock fileLock = randomAccessFile.getChannel().tryLock();

            if (fileLock != null) {
                Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                    try {
                        fileLock.release();
                        randomAccessFile.close();
                        file.delete();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }));

                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public static void main(String[] args) {
        PROFILER.begin("bootstrap");

        System.out.println("\nBBS: 0.1");

        System.setOut(new TimePrintStream(System.out));
        System.setErr(new TimePrintStream(System.err));

        ArgumentParser parser = new ArgumentParser();

        parser.register("directory", ArgumentType.PATH)
            .register("port", ArgumentType.NUMBER);

        TheLauncherServer server = new TheLauncherServer();

        server.setup(parser.parse(args));

        File lockFile = new File(server.directory, "instance.lock");

        if (canLock(lockFile)) {
            server.launch();
        } else {
            System.err.println("An instance of TheLauncherServer is already running! Please shut it down.");
            System.err.println("If you're absolutely sure that it's not running, then remove " + lockFile.getAbsolutePath() + " file, and try launching again!");
            System.err.println("If you can't remove that file... then it's still running in the background!");
        }
    }

    private void setup(MapType data) {
        if (data.has("directory")) {
            this.directory = new File(data.getString("directory"));
        }

        this.port = data.getInt("port");
    }

    public void launch() {
        PROFILER.endBegin("launch");

        if (this.directory == null || !this.directory.isDirectory()) {
            throw new IllegalStateException("Given game directory '" + this.directory + "' doesn't exist or not a directory...");
        }

        NettyServer server = new NettyServer(new Dispatcher(), PacketHandler.class, "nini");

        try {
            PROFILER.endBegin("init_bbs_data");
            BBSData.load(this.directory, null);
            PROFILER.endBegin("startup_netty_server");
            server.startup(this.port);
            PROFILER.end();
            PROFILER.print();
        } catch (Exception e) {
            File crashes = new File(this.directory, "crashes");
            CrashReport.writeCrashReport(crashes, e, "TheLauncherServer " + VERSION + " has crashed! Here is a crash stacktrace:");

            /* Here we should actually save a crash log with exception
             * and other relevant information */
            e.printStackTrace();
            server.delete();
        }
    }
}
