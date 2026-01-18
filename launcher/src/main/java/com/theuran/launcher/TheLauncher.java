package com.theuran.launcher;

import mchorse.bbs.data.types.MapType;
import mchorse.bbs.graphics.window.Window;
import mchorse.bbs.resources.Link;
import mchorse.bbs.utils.CrashReport;
import mchorse.bbs.utils.Pair;
import mchorse.bbs.utils.Profiler;
import mchorse.bbs.utils.TimePrintStream;
import mchorse.bbs.utils.cli.ArgumentParser;
import mchorse.bbs.utils.cli.ArgumentType;
import org.lwjgl.Version;
import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFW;

import javax.swing.*;
import java.io.File;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;

public class TheLauncher {
    public static final String VERSION = "0.1";

    public static final Profiler PROFILER = new Profiler();

    /* Command line arguments */

    public File directory;
    public int windowWidth = 1280;
    public int windowHeight = 720;
    public boolean openGLDebug;

    public static Link link(String path) {
        return new Link("launcher", path);
    }

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

        System.out.println("\nBBS: 0.1, LWJGL: " + Version.getVersion() + ", GLFW: " + GLFW.glfwGetVersionString());

        System.setOut(new TimePrintStream(System.out));
        System.setErr(new TimePrintStream(System.err));

        ArgumentParser parser = new ArgumentParser();

        parser.register("directory", ArgumentType.PATH)
                .register("glDebug", "gld", ArgumentType.NUMBER)
                .register("width", "ww", ArgumentType.NUMBER)
                .register("height", "wh", ArgumentType.NUMBER);

        TheLauncher launcher = new TheLauncher();

        launcher.setup(parser.parse(args));

        File lockFile = new File(launcher.directory, "instance.lock");

        if (canLock(lockFile)) {
            launcher.launch();
        } else {
            System.err.println("An instance of BBS Studio is already running! Please shut it down.");
            System.err.println("If you're absolutely sure that it's not running, then remove " + lockFile.getAbsolutePath() + " file, and try launching again!");
            System.err.println("If you can't remove that file... then it's still running in the background!");

            JOptionPane.showMessageDialog(null, "BBS instance is already running!", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void setup(MapType data) {
        if (data.has("directory")) {
            this.directory = new File(data.getString("directory"));
        }

        this.windowWidth = data.getInt("width", this.windowWidth);
        this.windowHeight = data.getInt("height", this.windowHeight);
        this.openGLDebug = data.getBool("glDebug", this.openGLDebug);
    }

    public void launch() {
        PROFILER.endBegin("launch");

        if (this.directory == null || !this.directory.isDirectory()) {
            throw new IllegalStateException("Given game directory '" + this.directory + "' doesn't exist or not a directory...");
        }

        TheLauncherEngine engine = new TheLauncherEngine(this);
        long id = -1;

        try {
            PROFILER.endBegin("setup_window");

            /* Start the game */
            Window.initialize("TheLauncher " + VERSION, this.windowWidth, this.windowHeight, this.openGLDebug);
            Window.setupStates();

            id = Window.getWindow();

            PROFILER.endBegin("init_engine");
            engine.init();
            PROFILER.endBegin("load_dashboard");
            engine.screen.reload();
            PROFILER.end();
            PROFILER.print();
            engine.start(id);
        } catch (Exception e) {
            File crashes = new File(this.directory, "crashes");
            Pair<File, String> crash = CrashReport.writeCrashReport(crashes, e, "TheLauncher " + VERSION + " has crashed! Here is a crash stacktrace:");

            /* Here we should actually save a crash log with exception
             * and other relevant information */
            e.printStackTrace();

            CrashReport.showDialogue(crash, "TheLauncher " + VERSION + " has crashed! The crash log " + crash.a.getName() + " was generated in \"crashes\" folder, which you should send to the launcher's developer(s).\n\nIMPORTANT: don't screenshot this window!");
        }

        /* Terminate the game */
        engine.delete();

        Callbacks.glfwFreeCallbacks(id);
        GLFW.glfwDestroyWindow(id);

        GLFW.glfwSetErrorCallback(null).free();
        GLFW.glfwTerminate();
    }
}
