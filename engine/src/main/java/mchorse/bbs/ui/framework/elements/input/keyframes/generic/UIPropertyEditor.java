package mchorse.bbs.ui.framework.elements.input.keyframes.generic;

import mchorse.bbs.data.types.ListType;
import mchorse.bbs.data.types.MapType;
import mchorse.bbs.graphics.window.Window;
import mchorse.bbs.l10n.keys.IKey;
import mchorse.bbs.settings.values.base.BaseValue;
import mchorse.bbs.ui.Keys;
import mchorse.bbs.ui.UIKeys;
import mchorse.bbs.ui.framework.UIContext;
import mchorse.bbs.ui.framework.elements.UIElement;
import mchorse.bbs.ui.framework.elements.buttons.UIIcon;
import mchorse.bbs.ui.framework.elements.buttons.UIToggle;
import mchorse.bbs.ui.framework.elements.input.UITrackpad;
import mchorse.bbs.ui.framework.elements.input.keyframes.IAxisConverter;
import mchorse.bbs.ui.framework.elements.input.keyframes.generic.factories.UIKeyframeFactory;
import mchorse.bbs.ui.utils.UI;
import mchorse.bbs.ui.utils.icons.Icons;
import mchorse.bbs.utils.keyframes.generic.GenericKeyframe;
import mchorse.bbs.utils.keyframes.generic.factories.IGenericKeyframeFactory;
import mchorse.bbs.utils.keyframes.generic.factories.KeyframeFactories;
import org.joml.Vector2i;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UIPropertyEditor extends UIElement {
    public UIElement frameButtons;
    public UIToggle instant;
    public UITrackpad tick;
    public UITrackpad duration;
    public UIIcon interp;
    public UIKeyframeFactory editor;

    private int clicks;
    private long clickTimer;

    private IAxisConverter converter;

    protected List<BaseValue> valueChannels = new ArrayList<>();


    public void updateConverter() {
    }

    @Override
    public boolean subMouseClicked(UIContext context) {
        int mouseX = context.mouseX;
        int mouseY = context.mouseY;

        if (this.area.isInside(mouseX, mouseY)) {
            /* On double-click add or remove a keyframe */
            if (context.mouseButton == 0) {
                long time = System.currentTimeMillis();

                if (time - this.clickTimer < 175) {
                    this.clicks++;

                    if (this.clicks >= 1) {
                        this.clicks = 0;
                        this.doubleClick(mouseX, mouseY);
                    }
                } else {
                    this.clicks = 0;
                }

                this.clickTimer = time;
            }
        }

        return super.subMouseClicked(context);
    }

    /**
     * Parse keyframes from clipboard
     */
    private Map<String, PastedKeyframes> parseKeyframes() {
        MapType data = Window.getClipboardMap("_CopyProperties");

        if (data == null) {
            return null;
        }

        Map<String, PastedKeyframes> temp = new HashMap<>();

        for (String key : data.keys()) {
            MapType map = data.getMap(key);
            ListType list = map.getList("keyframes");
            IGenericKeyframeFactory serializer = KeyframeFactories.FACTORIES.get(map.getString("type"));

            for (int i = 0, c = list.size(); i < c; i++) {
                PastedKeyframes pastedKeyframes = temp.computeIfAbsent(key, k -> new PastedKeyframes(serializer));
                GenericKeyframe keyframe = new GenericKeyframe("", serializer);

                keyframe.fromData(list.getMap(i));
                pastedKeyframes.keyframes.add(keyframe);
            }
        }

        return temp.isEmpty() ? null : temp;
    }

    /**
     * Copy keyframes to clipboard
     */
    private void copyKeyframes() {
        MapType keyframes = new MapType();

        Window.setClipboard(keyframes, "_CopyProperties");
    }

    /**
     * Paste copied keyframes to clipboard
     */
    protected void pasteKeyframes(Map<String, PastedKeyframes> keyframes, long offset, int mouseY) {
    }

    private void pasteKeyframesTo(UIProperty property, PastedKeyframes pastedKeyframes, long offset) {
        if (property.channel.getFactory() != pastedKeyframes.factory) {
            return;
        }

        long firstX = pastedKeyframes.keyframes.get(0).getTick();
        List<GenericKeyframe> toSelect = new ArrayList<>();

        for (GenericKeyframe keyframe : pastedKeyframes.keyframes) {
            keyframe.setTick(keyframe.getTick() - firstX + offset);

            int index = property.channel.insert(keyframe.getTick(), keyframe.getValue());
            GenericKeyframe inserted = property.channel.get(index);

            inserted.copy(keyframe);
            toSelect.add(inserted);
        }

        for (GenericKeyframe select : toSelect) {
            property.selected.add(property.channel.getKeyframes().indexOf(select));
        }
    }

    protected void doubleClick(int mouseX, int mouseY) {

    }

    public void resetView() {
    }

    public void selectAll() {
    }

    private void setInstant(boolean instant) {
    }

    public void setTick(double tick) {
    }

    public void setDuration(int value) {
    }

    public void setValue(Object value) {
    }

    public void fillData(GenericKeyframe frame) {
        boolean show = frame != null;

        this.frameButtons.setVisible(show);

        if (!show) {
            return;
        }

        double tick = frame.getTick();
        float duration = frame.getDuration();

        if (this.editor != null) {
            this.editor.removeFromParent();
            this.editor = null;
        }

        this.editor = frame.getFactory().createUI(frame, this);

        if (this.editor != null) {
            this.frameButtons.add(this.editor);
        }

        this.instant.setValue(frame.isInstant());
        this.tick.setValue(this.converter == null ? tick : this.converter.to(tick));
        this.duration.setValue(this.converter == null ? duration : this.converter.to(duration));
        this.frameButtons.resize();
    }

    public void select(List<List<Integer>> selection, Vector2i selected) {
        int i = 0;
        boolean deselect = true;

        if (deselect) {
            this.fillData(null);
        }
    }

    private static class PastedKeyframes {
        public IGenericKeyframeFactory factory;
        public List<GenericKeyframe> keyframes = new ArrayList<>();

        public PastedKeyframes(IGenericKeyframeFactory factory) {
            this.factory = factory;
        }
    }
}