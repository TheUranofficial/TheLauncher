package com.theuran.launcher;

import com.theuran.launcher.bridge.BridgeCamera;
import com.theuran.launcher.bridge.BridgeMenu;
import com.theuran.launcher.bridge.BridgeVideoRecorder;
import com.theuran.launcher.network.PacketHandler;
import com.theuran.launcher.settings.TheLauncherSettings;
import com.theuran.launcher.ui.KeysApp;
import com.theuran.launcher.ui.UIKeysApp;
import com.theuran.launcher.ui.UIScreen;
import com.theuran.launcher.ui.gamemode.UIGameModePanel;
import com.theuran.launcher.ui.l10n.UILanguageEditorOverlayPanel;
import com.theuran.launcher.ui.utility.UIUtilityMenu;
import com.theuran.launcher.ui.utility.UIUtilityOverlayPanel;
import io.netty.channel.ChannelFuture;
import mchorse.bbs.BBS;
import mchorse.bbs.BBSSettings;
import mchorse.bbs.bridge.IBridge;
import mchorse.bbs.bridge.IBridgeCamera;
import mchorse.bbs.bridge.IBridgeMenu;
import mchorse.bbs.bridge.IBridgeVideoScreenshot;
import mchorse.bbs.camera.controller.CameraController;
import mchorse.bbs.core.Engine;
import mchorse.bbs.core.keybinds.Keybind;
import mchorse.bbs.core.keybinds.KeybindCategory;
import mchorse.bbs.data.DataToString;
import mchorse.bbs.events.L10nReloadEvent;
import mchorse.bbs.events.UpdateEvent;
import mchorse.bbs.events.register.RegisterDashboardPanels;
import mchorse.bbs.events.register.RegisterKeybindsClassesEvent;
import mchorse.bbs.events.register.RegisterL10nEvent;
import mchorse.bbs.events.register.RegisterSettingsEvent;
import mchorse.bbs.graphics.GLStates;
import mchorse.bbs.graphics.window.IFileDropListener;
import mchorse.bbs.graphics.window.Window;
import mchorse.bbs.l10n.L10n;
import mchorse.bbs.l10n.L10nUtils;
import mchorse.bbs.network.Dispatcher;
import mchorse.bbs.network.core.client.NettyClient;
import mchorse.bbs.resources.packs.InternalAssetsSourcePack;
import mchorse.bbs.settings.values.ValueLanguage;
import mchorse.bbs.ui.framework.UIBaseMenu;
import mchorse.bbs.ui.framework.elements.overlay.UIOverlay;
import mchorse.bbs.ui.utils.icons.Icons;
import mchorse.bbs.utils.IOUtils;
import mchorse.bbs.utils.recording.ScreenshotRecorder;
import mchorse.bbs.utils.recording.VideoRecorder;
import org.greenrobot.eventbus.Subscribe;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TheLauncherEngine extends Engine implements IBridge, IFileDropListener {
    public TheLauncherRenderer renderer;
    public UIScreen screen;

    public CameraController cameraController = new CameraController();

    /* Utility */
    public VideoRecorder video;
    public ScreenshotRecorder screenshot;

    public NettyClient client;
    public ChannelFuture channel;

    private Map<Class<?>, Object> apis = new HashMap<>();

    public TheLauncherEngine(TheLauncher launcher) {
        super();

        this.apis.put(IBridgeMenu.class, new BridgeMenu(this));
        this.apis.put(IBridgeCamera.class, new BridgeCamera(this));
        this.apis.put(IBridgeVideoScreenshot.class, new BridgeVideoRecorder(this));

        BBS.events.register(this);

        BBS.registerCore(this, launcher.directory);
        BBS.registerFactories();
        BBS.registerFoundation();

        this.screen = new UIScreen(this);
        this.renderer = new TheLauncherRenderer(this);
        this.cameraController.camera.position.set(0, 0.5, 0);

        this.registerMiscellaneous();
        this.registerKeybinds();
    }

    @Subscribe
    public void registerSettings(RegisterSettingsEvent event) {
        event.register(Icons.BLOCK, "launcher", TheLauncherSettings::register);
    }

    @Subscribe
    public void registerL10n(RegisterL10nEvent event) {
        this.reloadSupportedLanguages();

        event.l10n.registerOne(lang -> TheLauncher.link("lang/" + lang + ".json"));
    }

    @Subscribe
    public void reloadL10n(L10nReloadEvent event) {
        File export = UILanguageEditorOverlayPanel.getLangEditorFolder();
        File[] files = export.listFiles();

        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isFile() && file.getName().endsWith(".json")) {
                this.overwriteLanguage(event.l10n, file);
            }
        }
    }

    @Subscribe
    public void registerKeybindsClasses(RegisterKeybindsClassesEvent event) {
        event.register(KeysApp.class);
    }

    @Subscribe
    public void registerDashboardPanels(RegisterDashboardPanels event) {
        event.dashboard.getPanels().registerPanel(new UIGameModePanel(event.dashboard), UIKeysApp.GAMEMODE_TITLE, Icons.FILE);
        event.dashboard.setPanel(event.dashboard.getPanel(UIGameModePanel.class));
    }

    private void overwriteLanguage(L10n l10n, File file) {
        try {
            l10n.overwrite(DataToString.mapFromString(IOUtils.readText(file)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void reloadSupportedLanguages() {
        BBS.getL10n().reloadSupportedLanguages(L10nUtils.readAdditionalLanguages(BBS.getAssetsPath("lang_editor/languages.json")));
    }

    private void registerMiscellaneous() {
        BBS.getProvider().register(new InternalAssetsSourcePack("launcher", TheLauncherEngine.class));

        this.video = new VideoRecorder(BBS.getGamePath("movies"), this);
        this.screenshot = new ScreenshotRecorder(BBS.getGamePath("screenshots"), this);

        Window.registerFileDropListener(this);
    }

    private void registerKeybinds() {
        KeybindCategory global = new KeybindCategory("global");
        Keybind screenshot = new Keybind("screenshot", () -> this.screenshot.take(Window.isAltPressed()));
        Keybind fullscreen = new Keybind("fullscreen", this::toggleFullScreen);
        Keybind debug = new Keybind("debug", () -> this.renderer.context.setDebug(!this.renderer.context.isDebug()));

        global.add(screenshot.keys(GLFW.GLFW_KEY_F2));
        global.add(debug.keys(GLFW.GLFW_KEY_F3));
        global.add(fullscreen.keys(GLFW.GLFW_KEY_F11));

        Keybind utilities = new Keybind("utilities", () -> {
            UIBaseMenu currentMenu = this.screen.menu;

            if (currentMenu == null) {
                this.screen.showMenu(new UIUtilityMenu(this));
            } else {
                if (UIOverlay.has(currentMenu.context)) {
                    return;
                }

                UIOverlay.addOverlay(currentMenu.context, new UIUtilityOverlayPanel(UIKeysApp.UTILITY_TITLE, null), 240, 160);
            }
        });

        global.add(utilities.keys(GLFW.GLFW_KEY_F6));

        this.keys.keybinds.add(global);
    }

    @Override
    public void init() throws Exception {
        super.init();

        TheLauncher.PROFILER.endBegin("init_bbs");
        BBS.initialize();
        TheLauncher.PROFILER.endBegin("init_launcher_data");
        TheLauncherData.load(BBS.getDataFolder(), this);

        TheLauncher.PROFILER.endBegin("init_renderer");
        this.renderer.init();
        this.screen.init();
        this.resize(Window.width, Window.height);

        Window.focus();

        TheLauncher.PROFILER.endBegin("init_callbacks");
        this.registerSettingsCallbacks();

        TheLauncher.PROFILER.endBegin("init_netty_client");

        this.client = new NettyClient(new Dispatcher(), PacketHandler.class, "nini");
        this.channel = this.client.connect(TheLauncherSettings.serverIp.get(), TheLauncherSettings.serverPort.get());
    }

    private void registerSettingsCallbacks() {
        BBSSettings.language.postCallback(value -> {
            this.reloadSupportedLanguages();

            BBS.getL10n().reload(((ValueLanguage) value).get(), BBS.getProvider());
        });
        BBSSettings.userIntefaceScale.postCallback(value -> BBS.getEngine().needsResize());
    }

    @Override
    public void delete() {
        super.delete();

        this.screen.delete();
        this.client.delete();

        TheLauncherData.delete();
        BBS.terminate();
    }

    @Override
    public void render(float transition) {
        super.render(transition);

        float worldTransition = this.screen.isPaused() ? 0 : transition;

        this.cameraController.setup(this.cameraController.camera, worldTransition);

        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);

        this.renderer.render(worldTransition);
        this.screen.render(transition);

        this.video.recordFrame();
        this.screenshot.recordFrame(Window.width, Window.height);
    }

    @Override
    public void update() {
        super.update();

        if (!this.screen.isPaused()) {
            this.cameraController.tick();
        }

        this.renderer.update();
        this.screen.update();
        this.cameraController.updateSoundPosition();

        BBS.events.post(new UpdateEvent());
    }

    @Override
    public void resize(int width, int height) {
        GLStates.resetViewport();

        if (this.video.isRecording()) {
            this.video.stopRecording();
        }

        this.cameraController.resize(width, height);
        this.screen.resize(width, height);
    }

    @Override
    public boolean handleGamepad(int button, int action) {
        return false;
    }

    @Override
    public boolean handleKey(int key, int scancode, int action, int mods) {
        return this.keys.keybinds.handleKey(key, scancode, action, mods)
                || this.screen.handleKey(key, scancode, action, mods);
    }

    @Override
    public void handleTextInput(int key) {
        this.screen.handleTextInput(key);
    }

    @Override
    public void handleMouse(int button, int action, int mode) {
        this.screen.handleMouse(button, action, mode);
    }

    @Override
    public void handleScroll(double x, double y) {
        this.screen.handleScroll(x, y);
    }

    @Override
    public Engine getEngine() {
        return this;
    }

    @Override
    public <T> T get(Class<T> apiInterface) {
        return apiInterface.cast(this.apis.get(apiInterface));
    }

    @Override
    public void acceptFilePaths(String[] paths) {
        this.screen.acceptFilePaths(paths);
    }
}
