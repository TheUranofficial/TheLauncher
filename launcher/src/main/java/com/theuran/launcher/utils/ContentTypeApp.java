package com.theuran.launcher.utils;

import com.theuran.launcher.ui.gamemode.UIGameModePanel;
import com.theuran.launcher.utils.repos.GameModeRepository;
import mchorse.bbs.game.utils.ContentType;

public class ContentTypeApp {
    public static final ContentType GAMEMODES = new ContentType("gameModes", GameModeRepository::new, dashboard -> dashboard.getPanel(UIGameModePanel.class));
}
