package com.theuran.launcher.ui.gamemode;

import com.theuran.launcher.settings.TheLauncherSettings;
import com.theuran.launcher.utils.ContentTypeApp;
import com.theuran.launcher.ui.UIKeysApp;
import com.theuran.launcher.gamemode.GameMode;
import mchorse.bbs.BBS;
import mchorse.bbs.game.utils.ContentType;
import mchorse.bbs.l10n.keys.IKey;
import mchorse.bbs.ui.dashboard.UIDashboard;
import mchorse.bbs.ui.dashboard.panels.UIDataDashboardPanel;
import mchorse.bbs.ui.framework.UIContext;
import mchorse.bbs.ui.framework.elements.buttons.UIButton;
import mchorse.bbs.ui.framework.elements.utils.UIText;
import mchorse.bbs.ui.utils.icons.Icons;
import mchorse.bbs.utils.colors.Colors;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;
import java.util.stream.Collectors;

public class UIGameModePanel extends UIDataDashboardPanel<GameMode> {
    public UIButton play;
    public UIText description;

    public UIGameModePanel(UIDashboard dashboard) {
        super(dashboard);

        this.overlay.namesList.setFileIcon(Icons.ENVELOPE);

        this.play = new UIButton(UIKeysApp.GAMEMODE_PLAY, this::runMinecraftClient);
        this.play.relative(this.editor).xy(0.015f, 0.9f).wh(80, 20);

        this.description = new UIText();
        this.description.relative(this.editor).xy(14, 70).full();

        this.editor.add(this.play, this.description);

        this.fill(null);
    }

    private void runMinecraftClient(UIButton button) {
        String uuid = TheLauncherSettings.playerUUID.get().isEmpty() ? UUID.randomUUID().toString() : TheLauncherSettings.playerUUID.get();
        String[] version = this.data.version.get().split(" ");
        String[] libraries = new String[] {"com/google/code/gson/gson/2.8.0/gson-2.8.0.jar", "com/google/guava/guava/21.0/guava-21.0.jar", "com/ibm/icu/icu4j-core-mojang/51.2/icu4j-core-mojang-51.2.jar", "com/mojang/authlib/1.5.25/authlib-1.5.25.jar", "com/mojang/patchy/1.3.9/patchy-1.3.9.jar", "com/mojang/realms/1.10.22/realms-1.10.22.jar", "com/mojang/text2speech/1.10.3/text2speech-1.10.3.jar", "com/paulscode/codecjorbis/20101023/codecjorbis-20101023.jar", "com/paulscode/codecwav/20101023/codecwav-20101023.jar", "com/paulscode/libraryjavasound/20101123/libraryjavasound-20101123.jar", "com/paulscode/librarylwjglopenal/20100824/librarylwjglopenal-20100824.jar", "com/paulscode/soundsystem/20120107/soundsystem-20120107.jar", "com/typesafe/akka/akka-actor_2.11/2.3.3/akka-actor_2.11-2.3.3.jar", "com/typesafe/config/1.2.1/config-1.2.1.jar", "commons-codec/commons-codec/1.10/commons-codec-1.10.jar", "commons-io/commons-io/2.5/commons-io-2.5.jar", "commons-logging/commons-logging/1.1.3/commons-logging-1.1.3.jar", "io/netty/netty-all/4.1.9.Final/netty-all-4.1.9.Final.jar", "it/unimi/dsi/fastutil/7.1.0/fastutil-7.1.0.jar", "java3d/vecmath/1.5.2/vecmath-1.5.2.jar", "lzma/lzma/0.0.1/lzma-0.0.1.jar", "net/java/dev/jna/jna/4.4.0/jna-4.4.0.jar", "net/java/dev/jna/platform/3.4.0/platform-3.4.0.jar", "net/java/jinput/jinput/2.0.5/jinput-2.0.5.jar", "net/java/jutils/jutils/1.0.0/jutils-1.0.0.jar", "net/minecraft/launchwrapper/1.12/launchwrapper-1.12.jar", "net/minecraftforge/forge/1.12.2-14.23.5.2860/forge-1.12.2-14.23.5.2860.jar", "net/sf/jopt-simple/jopt-simple/5.0.3/jopt-simple-5.0.3.jar", "net/sf/trove4j/trove4j/3.0.3/trove4j-3.0.3.jar", "org/apache/commons/commons-compress/1.8.1/commons-compress-1.8.1.jar", "org/apache/commons/commons-lang3/3.5/commons-lang3-3.5.jar", "org/apache/httpcomponents/httpclient/4.3.3/httpclient-4.3.3.jar", "org/apache/httpcomponents/httpcore/4.3.2/httpcore-4.3.2.jar", "org/apache/logging/log4j/log4j-api/2.15.0/log4j-api-2.15.0.jar", "org/apache/logging/log4j/log4j-api/2.8.1/log4j-api-2.8.1.jar", "org/apache/logging/log4j/log4j-core/2.15.0/log4j-core-2.15.0.jar", "org/apache/logging/log4j/log4j-core/2.8.1/log4j-core-2.8.1.jar", "org/apache/maven/maven-artifact/3.5.3/maven-artifact-3.5.3.jar", "org/jline/jline/3.5.1/jline-3.5.1.jar", "org/lwjgl/lwjgl/lwjgl-platform/2.9.4-nightly-20150209/lwjgl-platform-2.9.4-nightly-20150209.jar", "org/lwjgl/lwjgl/lwjgl/2.9.4-nightly-20150209/lwjgl-2.9.4-nightly-20150209.jar", "org/lwjgl/lwjgl/lwjgl_util/2.9.4-nightly-20150209/lwjgl_util-2.9.4-nightly-20150209.jar", "org/ow2/asm/asm-debug-all/5.2/asm-debug-all-5.2.jar", "org/scala-lang/plugins/scala-continuations-library_2.11/1.0.2_mc/scala-continuations-library_2.11-1.0.2_mc.jar", "org/scala-lang/plugins/scala-continuations-plugin_2.11.1/1.0.2_mc/scala-continuations-plugin_2.11.1-1.0.2_mc.jar", "org/scala-lang/scala-actors-migration_2.11/1.1.0/scala-actors-migration_2.11-1.1.0.jar", "org/scala-lang/scala-compiler/2.11.1/scala-compiler-2.11.1.jar", "org/scala-lang/scala-library/2.11.1/scala-library-2.11.1.jar", "org/scala-lang/scala-parser-combinators_2.11/1.0.1/scala-parser-combinators_2.11-1.0.1.jar", "org/scala-lang/scala-reflect/2.11.1/scala-reflect-2.11.1.jar", "org/scala-lang/scala-swing_2.11/1.0.1/scala-swing_2.11-1.0.1.jar", "org/scala-lang/scala-xml_2.11/1.0.2/scala-xml_2.11-1.0.2.jar", "oshi-project/oshi-core/1.1/oshi-core-1.1.jar", version[1] + "-" + version[0].toLowerCase() + ".jar"};

        if (!uuid.equals(TheLauncherSettings.playerUUID.get())) {
            TheLauncherSettings.playerUUID.set(uuid);
        }

        ArrayList<String> finalCommand = new ArrayList<>();

        finalCommand.add(Paths.get(System.getProperty("java.home"), "bin", "java.exe").toString());
        finalCommand.addAll(Arrays.asList(TheLauncherSettings.javaArgs.get().split(" ")));
        finalCommand.add("-Xms512M");
        finalCommand.add("-Xmx" + TheLauncherSettings.ram.get() + "M");
        finalCommand.add("-cp");
        finalCommand.add(Arrays.stream(libraries)
            .map(library -> BBS.getGamePath(".minecraft/libraries/" + library).toString())
            .collect(Collectors.joining(";")));

        finalCommand.add("net.minecraft.launchwrapper.Launch");
        finalCommand.add("--version");
        finalCommand.add(version[1]);
        finalCommand.add("--assetsDir");
        finalCommand.add(BBS.getGamePath(".minecraft/assets").toString());
        finalCommand.add("--assetIndex");
        finalCommand.add(version[1] + "-" + version[0].toLowerCase());
        finalCommand.add("--userType");
        finalCommand.add("mojang");
        finalCommand.add("--tweakClass");
        finalCommand.add("net.minecraftforge.fml.common.launcher.FMLTweaker");
        finalCommand.add("--versionType");
        finalCommand.add(version[0]);

        finalCommand.add("--username");
        finalCommand.add(TheLauncherSettings.playerName.get());
        finalCommand.add("--gameDir");
        finalCommand.add(BBS.getGamePath(".minecraft").toString());
        finalCommand.add("--uuid");
        finalCommand.add(uuid);
        finalCommand.add("--accessToken");
        finalCommand.add(uuid);

        try {
            ProcessBuilder processBuilder = new ProcessBuilder(finalCommand);
            processBuilder.directory(BBS.getGamePath(".minecraft"));

            processBuilder.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ContentType getType() {
        return ContentTypeApp.GAMEMODES;
    }

    @Override
    protected IKey getTitle() {
        return UIKeysApp.GAMEMODE_TITLE;
    }

    @Override
    protected void fillData(GameMode data) {
        this.play.setVisible(data != null);
        this.description.setVisible(data != null);

        if (data != null) {
            this.description.text(data.description.get());
        }
    }


    @Override
    public void render(UIContext context) {
        super.render(context);

        if (this.data != null) {
            context.render.stack.push();
            context.render.stack.translate(0, 0, 0);
            context.render.stack.scale(2, 2, 1);

            context.batcher.textShadow(context.font, this.data.getId(), 7, 7);

            context.render.stack.pop();

            context.batcher.textShadow(context.font, this.data.version.get(), 14, 42, Colors.LIGHTEST_GRAY);
            context.batcher.box(14, 60, (float) context.menu.width - 28, 61, Colors.DARKER_GRAY);
        }
    }
}
