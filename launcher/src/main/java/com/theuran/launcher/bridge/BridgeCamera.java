package com.theuran.launcher.bridge;

import com.theuran.launcher.TheLauncherEngine;
import mchorse.bbs.bridge.IBridgeCamera;
import mchorse.bbs.camera.controller.CameraController;

public class BridgeCamera extends BaseBridge implements IBridgeCamera {
    public BridgeCamera(TheLauncherEngine engine) {
        super(engine);
    }

    @Override
    public CameraController getCameraController() {
        return this.engine.cameraController;
    }
}
