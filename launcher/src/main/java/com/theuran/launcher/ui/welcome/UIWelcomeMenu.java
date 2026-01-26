package com.theuran.launcher.ui.welcome;

import com.theuran.launcher.TheLauncher;
import com.theuran.launcher.TheLauncherEngine;
import com.theuran.launcher.settings.TheLauncherSettings;
import com.theuran.launcher.ui.KeysApp;
import com.theuran.launcher.ui.UIKeysApp;
import mchorse.bbs.BBSSettings;
import mchorse.bbs.bridge.IBridge;
import mchorse.bbs.graphics.text.FontRenderer;
import mchorse.bbs.graphics.texture.Texture;
import mchorse.bbs.l10n.L10n;
import mchorse.bbs.l10n.keys.IKey;
import mchorse.bbs.resources.Link;
import mchorse.bbs.ui.UIKeys;
import mchorse.bbs.ui.dashboard.UIDashboard;
import mchorse.bbs.ui.framework.UIBaseMenu;
import mchorse.bbs.ui.framework.UIRenderingContext;
import mchorse.bbs.ui.framework.elements.UIElement;
import mchorse.bbs.ui.framework.elements.buttons.UICirculate;
import mchorse.bbs.ui.framework.elements.buttons.UIIcon;
import mchorse.bbs.ui.framework.elements.input.UITrackpad;
import mchorse.bbs.ui.framework.elements.input.text.UITextbox;
import mchorse.bbs.ui.framework.elements.utils.UILabel;
import mchorse.bbs.ui.utils.Area;
import mchorse.bbs.ui.utils.UI;
import mchorse.bbs.ui.utils.icons.Icons;
import mchorse.bbs.utils.colors.Colors;
import mchorse.bbs.utils.math.Interpolation;

public class UIWelcomeMenu extends UIBaseMenu {
    private static final Link KEYBOARD = TheLauncher.link("textures/keyboard.png");

    private static final int WELCOME = 0;
    private static final int KEYS = 1;
    private static final int INPUT_DATA = 2;

    public UILabel title;
    public UIIcon previous;
    public UIIcon next;

    public UIElement keys;
    public UITextureRect keyF1;
    public UITextureRect keyF2;
    public UITextureRect keyF3;
    public UITextureRect keyF6;
    public UITextureRect keyF9;
    public UITextureRect keyF11;
    public UITextureRect keyBackslash;

    public UIElement inputData;
    public UITextbox playerName;
    public UITextbox serverIp;
    public UITrackpad port;

    private int page;
    private int counter;

    public UIWelcomeMenu(IBridge bridge) {
        super(bridge);

        this.title = UI.label(IKey.EMPTY).background().labelAnchor(0.5f, 0.5f);
        this.previous = new UIIcon(Icons.MOVE_LEFT, this::previous);
        this.next = new UIIcon(Icons.MOVE_RIGHT, this::next);

        this.title.relative(this.main).x(0.5f).y(20).wh(200, 20).anchorX(0.5f);
        this.previous.relative(this.main).x(0.5f, -100).y(20).anchorX(1f);
        this.next.relative(this.main).x(0.5f, 100).y(20);

        this.keys = new UIElement();
        this.keys.relative(this.main).xy(0.5f, 0.5f).wh(375, 151).anchor(0.5f, 0.5f);

        /* Keys */
        this.keyF1 = new UITextureRect(KEYBOARD, new Area(52, 2, 21, 21), "F1");
        this.keyF1.tooltip(UIKeysApp.WELCOME_KEYS_F1);
        this.keyF2 = new UITextureRect(KEYBOARD, new Area(77, 2, 21, 21), "F2");
        this.keyF2.tooltip(UIKeysApp.WELCOME_KEYS_F2);
        this.keyF3 = new UITextureRect(KEYBOARD, new Area(102, 2, 21, 21), "F3");
        this.keyF3.tooltip(UIKeysApp.WELCOME_KEYS_F3);
        this.keyF6 = new UITextureRect(KEYBOARD, new Area(191, 2, 21, 21), "F6");
        this.keyF6.tooltip(UIKeysApp.WELCOME_KEYS_F6);
        this.keyF9 = new UITextureRect(KEYBOARD, new Area(277, 2, 21, 21), "F9");
        this.keyF9.tooltip(UIKeysApp.WELCOME_KEYS_F9);
        this.keyF11 = new UITextureRect(KEYBOARD, new Area(327, 2, 21, 21), "F11");
        this.keyF11.tooltip(UIKeysApp.WELCOME_KEYS_F11);
        this.keyBackslash = new UITextureRect(KEYBOARD, new Area(297, 82, 21, 21), "\\");
        this.keyBackslash.tooltip(UIKeysApp.WELCOME_KEYS_BACKSLASH);

        this.keys.add(this.keyF1, this.keyF2, this.keyF3, this.keyF6, this.keyF9, this.keyF11, this.keyBackslash);

        for (UITextureRect rect : this.keys.getChildren(UITextureRect.class)) {
            rect.relative(this.keys).set(rect.rect.x, rect.rect.y, rect.rect.w, rect.rect.h);
        }

        /* Input data */
        this.playerName = new UITextbox(TheLauncherSettings.playerName::set);
        this.playerName.tooltip(IKey.lang("launcher.config.client.player_name-comment"));
        this.playerName.setText(TheLauncherSettings.playerName.get());
        this.serverIp = new UITextbox(TheLauncherSettings.serverIp::set);
        this.serverIp.tooltip(IKey.lang("launcher.config.server.server_ip-comment"));
        this.serverIp.setText(TheLauncherSettings.serverIp.get());
        this.port = new UITrackpad(number -> TheLauncherSettings.serverPort.set(number.intValue()));
        this.port.tooltip(IKey.lang("launcher.config.server.server_port-comment"));
        this.port.limit(0, 65535).setValue(TheLauncherSettings.serverPort.get());
        this.inputData = UI.column(UI.row(new UIIcon(Icons.JOYSTICK, null), this.playerName), UI.row(new UIIcon(Icons.GLOBE, null), this.serverIp), UI.row(new UIIcon(Icons.SETTINGS, null), this.port));
        this.inputData.relative(this.main).xy(0.5f, 0.5f).wh(300, 212).anchor(0.5f);

        this.main.add(this.title, this.previous, this.next);
        this.main.add(this.keys, this.inputData);

        this.switchToPage(WELCOME);

        this.main.keys().register(KeysApp.PREV_PAGE, () -> this.switchToPage(this.page - 1)).active(() -> this.page > 0);
        this.main.keys().register(KeysApp.NEXT_PAGE, () -> this.switchToPage(this.page + 1)).active(() -> this.page < INPUT_DATA);
    }

    @Override
    public Link getMenuId() {
        return TheLauncher.link("welcome");
    }

    @Override
    public boolean canPause() {
        return false;
    }

    private void previous(UIIcon icon) {
        this.switchToPage(this.page - 1);
    }

    private void next(UIIcon icon) {
        if (this.page < INPUT_DATA) {
            this.switchToPage(this.page + 1);
        } else {
            TheLauncherSettings.welcome.set(true);

            this.closeMenu();
        }
    }

    public void switchToPage(int page) {
        this.page = page;

        this.previous.setVisible(page != WELCOME);
        this.next.both(page == INPUT_DATA ? Icons.CLOSE : Icons.MOVE_RIGHT);
        this.title.setVisible(page != WELCOME);

        this.keys.setVisible(page == KEYS);
        this.inputData.setVisible(page == INPUT_DATA);

        if (this.page == WELCOME) {
            this.counter = 0;
        } else if (this.page == KEYS) {
            this.title.label = UIKeysApp.WELCOME_SECTIONS_KEYS;
        } else if (this.page == INPUT_DATA) {
            this.title.label = UIKeysApp.WELCOME_SECTIONS_INPUT_DATA;
        }
    }

    @Override
    protected void closeMenu() {
        if (this.page == INPUT_DATA && this.port.getValue() != 0 && !this.playerName.getText().isEmpty() && !this.serverIp.getText().isEmpty()) {
            TheLauncherEngine engine = (TheLauncherEngine) this.bridge.getEngine();

            engine.screen.showMenu(engine.screen.getDashboard());
        } else if (this.page == INPUT_DATA) {
            this.context.notify(UIKeysApp.WELCOME_INPUT_DATA_NOTIFICATION, BBSSettings.primaryColor());
        }
    }

    @Override
    public void update() {
        super.update();

        this.counter++;
    }

    @Override
    protected void preRenderMenu(UIRenderingContext context) {
        this.renderDefaultBackground();

        FontRenderer font = context.getFont();
        int x = this.width / 2;
        int y = this.height / 2;

        this.renderDropArea(this.next.area);

        if (this.previous.isVisible()) {
            this.renderDropArea(this.previous.area);
        }

        if (this.page == WELCOME) {
            String label = UIKeysApp.WELCOME_WELCOME_TITLE.get();
            float factor = Math.min((this.counter + context.getTransition()) / 25f, 1);
            float scale = Interpolation.EXP_OUT.interpolate(20, 4, factor);

            context.stack.push();
            context.stack.translate(x, y, 0);
            context.stack.scale(scale, scale, 1);

            context.batcher.textShadow(font, label, (float) -font.getWidth(label) / 2, -4);

            context.stack.pop();

            String subtext = UIKeysApp.WELCOME_WELCOME_SUBTITLE.get();
            int ly = y + 2 * font.getHeight() + 16;

            ly = (int) Interpolation.EXP_OUT.interpolate(this.height * 1.25, ly, factor);

            for (String line : font.split(subtext, 320)) {
                context.batcher.textShadow(font, line, x - (float) font.getWidth(line) / 2, ly);

                ly += font.getHeight() * 2;
            }
        } else if (this.page == KEYS) {
            Texture texture = context.getTextures().getTexture(KEYBOARD);
            String label = UIKeysApp.WELCOME_KEYS_SUBTITLE.get();
            int tx = this.keys.area.x;
            int ty = this.keys.area.y;

            context.batcher.texturedBox(texture, Colors.GRAY, tx, ty, texture.width, texture.height, 0, 0);
            context.batcher.textShadow(font, label, x - (float) font.getWidth(label) / 2, y + (float) texture.height / 2 + 8);
        } else if (this.page == INPUT_DATA) {
            String label = UIKeysApp.WELCOME_INPUT_DATA_SUBTITLE.get();

            context.batcher.textShadow(font, label, x - (float) font.getWidth(label) / 2, y - 50);
        }
    }

    private void renderDropArea(Area area) {
        boolean hover = area.isInside(this.context);

        int opaque = hover ? Colors.A50 | BBSSettings.primaryColor.get() : Colors.A50;
        int shadow = hover ? BBSSettings.primaryColor.get() : 0;

        this.context.batcher.dropShadow(area.x + 4, area.y + 4, area.ex() - 4, area.ey() - 4, 4, opaque, shadow);
    }
}
