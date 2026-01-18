package com.theuran.launcher.ui.gamemode;

import com.theuran.launcher.utils.ContentTypeApp;
import com.theuran.launcher.ui.UIKeysApp;
import com.theuran.launcher.gamemode.GameMode;
import mchorse.bbs.game.utils.ContentType;
import mchorse.bbs.l10n.keys.IKey;
import mchorse.bbs.ui.dashboard.UIDashboard;
import mchorse.bbs.ui.dashboard.panels.UIDataDashboardPanel;
import mchorse.bbs.ui.framework.UIContext;
import mchorse.bbs.ui.framework.elements.buttons.UIButton;
import mchorse.bbs.ui.framework.elements.utils.UIText;
import mchorse.bbs.ui.utils.icons.Icons;
import mchorse.bbs.utils.colors.Colors;

public class UIGameModePanel extends UIDataDashboardPanel<GameMode> {
    public UIButton play;
    public UIText description;

    public UIGameModePanel(UIDashboard dashboard) {
        super(dashboard);

        this.overlay.namesList.setFileIcon(Icons.ENVELOPE);

        this.play = new UIButton(UIKeysApp.GAMEMODE_PLAY, this::runClient);
        this.play.relative(this.editor).xy(0.85f, 0.03f).wh(80, 20);

        this.description = new UIText();
        this.description.relative(this.editor).xy(14, 70).full();

        this.editor.add(this.play, this.description);

        this.fill(null);
    }

    private void runClient(UIButton button) {
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
    public void fill(GameMode data) {
        super.fill(data);

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
