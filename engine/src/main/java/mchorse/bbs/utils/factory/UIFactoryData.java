package mchorse.bbs.utils.factory;

import mchorse.bbs.network.core.utils.Side;
import mchorse.bbs.network.core.utils.SideOnly;
import mchorse.bbs.utils.colors.Colors;

@SideOnly(Side.CLIENT)
public class UIFactoryData<T> {
    public final int color;
    public final Class<? extends T> panelUI;

    public UIFactoryData(int color, Class<? extends T> panelUI) {
        this.color = color & Colors.RGB;
        this.panelUI = panelUI;
    }
}