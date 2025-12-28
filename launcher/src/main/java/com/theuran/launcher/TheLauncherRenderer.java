package com.theuran.launcher;

import mchorse.bbs.BBS;
import mchorse.bbs.core.IComponent;
import mchorse.bbs.graphics.RenderingContext;
import mchorse.bbs.graphics.text.FontRenderer;
import mchorse.bbs.graphics.ubo.ProjectionViewUBO;
import mchorse.bbs.resources.Link;
import mchorse.bbs.world.entities.Entity;
import mchorse.bbs.world.entities.architect.EntityArchitect;

public class TheLauncherRenderer implements IComponent {
    public TheLauncherEngine engine;

    public RenderingContext context;

    public ProjectionViewUBO ubo;

    private int ticks;

    private Entity dummy = EntityArchitect.createDummy();

    public TheLauncherRenderer(TheLauncherEngine engine) {
        this.engine = engine;
    }

    @Override
    public void init() throws Exception {
        this.context = BBS.getRender();

        this.ubo = new ProjectionViewUBO(0);
        this.ubo.init();
        this.ubo.bindUnit();

        this.context.getLights().init();
        this.context.getLights().bindUnit();

        this.context.setup(BBS.getFonts().getRenderer(Link.assets("fonts/bbs_round.json")), BBS.getVAOs(), BBS.getTextures());
        this.context.setUBO(this.ubo);
    }

    @Override
    public void delete() {
        this.ubo.delete();
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void render(float transition) {
        for (FontRenderer value : BBS.getFonts().fontRenderers.values()) {
            if (value != null)
                value.setTime(this.ticks + transition);
        }

        this.context.setTransition(transition);

        this.context.runRunnables();
    }

    @Override
    public void update() {
        this.dummy.basic.ticks++;
        this.ticks++;
    }
}
