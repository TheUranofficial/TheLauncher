package mchorse.bbs.settings.values.base;

import mchorse.bbs.network.core.utils.Side;
import mchorse.bbs.network.core.utils.SideOnly;
import mchorse.bbs.ui.framework.elements.UIElement;

import java.util.List;

@SideOnly(Side.CLIENT)
public interface IValueUIProvider {
    @SideOnly(Side.CLIENT)
    List<UIElement> getFields(UIElement ui);
}