package mchorse.bbs.game.utils;

import mchorse.bbs.l10n.keys.IKey;
import mchorse.bbs.settings.values.ValueGroup;
import mchorse.bbs.ui.dashboard.UIDashboard;
import mchorse.bbs.ui.dashboard.panels.UIDataDashboardPanel;
import mchorse.bbs.utils.manager.IManager;

import java.util.function.Function;
import java.util.function.Supplier;

public class ContentType {
    private final String id;
    private IKey label;
    private Supplier<IManager<? extends ValueGroup>> manager;
    private Function<UIDashboard, UIDataDashboardPanel> dashboardPanel;

    public ContentType(String id, IKey label, Supplier<IManager<? extends ValueGroup>> manager, Function<UIDashboard, UIDataDashboardPanel> dashboardPanel) {
        this.id = id;
        this.label = label;
        this.manager = manager;
        this.dashboardPanel = dashboardPanel;
    }

    public String getId() {
        return this.id;
    }

    public IKey getPickLabel() {
        return this.label;
    }

    /* Every Karen be like :D */
    public IManager<? extends ValueGroup> getManager() {
        return this.manager.get();
    }

    public UIDataDashboardPanel get(UIDashboard dashboard) {
        return this.dashboardPanel.apply(dashboard);
    }
}