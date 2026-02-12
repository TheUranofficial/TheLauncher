package com.theuran.launcher.utils;

import com.theuran.launcher.ui.gamemode.UIGameModePanel;
import mchorse.bbs.BBSData;
import mchorse.bbs.game.utils.ContentType;
import mchorse.bbs.utils.repos.FolderManagerRepository;

public class ContentTypeApp {
    public static final ContentType GAMEMODES = new ContentType("gameModes", () -> new FolderManagerRepository<>(BBSData.getGameModes()), dashboard -> dashboard.getPanel(UIGameModePanel.class));
}
