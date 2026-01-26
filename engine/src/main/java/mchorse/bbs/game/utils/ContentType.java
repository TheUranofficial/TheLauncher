package mchorse.bbs.game.utils;

import mchorse.bbs.settings.values.ValueGroup;
import mchorse.bbs.ui.dashboard.UIDashboard;
import mchorse.bbs.ui.dashboard.panels.UIDataDashboardPanel;
import mchorse.bbs.utils.repos.IRepository;

import java.util.function.Function;
import java.util.function.Supplier;

public class ContentType {
    private final String id;
    private Supplier<IRepository<? extends ValueGroup>> manager;
    private Function<UIDashboard, UIDataDashboardPanel<?>> dashboardPanel;

    public ContentType(String id, Supplier<IRepository<? extends ValueGroup>> manager, Function<UIDashboard, UIDataDashboardPanel<?>> dashboardPanel) {
        this.id = id;
        this.manager = manager;
        this.dashboardPanel = dashboardPanel;
    }

    public String getId() {
        return this.id;
    }

    /* Every Karen be like :D */
    public IRepository<? extends ValueGroup> getRepository() {
        return this.manager.get();
    }

    public UIDataDashboardPanel<?> get(UIDashboard dashboard) {
        return this.dashboardPanel.apply(dashboard);
    }
}