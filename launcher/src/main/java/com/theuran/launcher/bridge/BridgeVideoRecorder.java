package com.theuran.launcher.bridge;

import com.theuran.launcher.TheLauncherEngine;
import mchorse.bbs.bridge.IBridgeVideoScreenshot;
import mchorse.bbs.utils.recording.ScreenshotRecorder;
import mchorse.bbs.utils.recording.VideoRecorder;

public class BridgeVideoRecorder extends BaseBridge implements IBridgeVideoScreenshot {
    public BridgeVideoRecorder(TheLauncherEngine engine) {
        super(engine);
    }

    @Override
    public ScreenshotRecorder getScreenshotRecorder() {
        return this.engine.screenshot;
    }

    @Override
    public VideoRecorder getVideoRecorder() {
        return this.engine.video;
    }
}
