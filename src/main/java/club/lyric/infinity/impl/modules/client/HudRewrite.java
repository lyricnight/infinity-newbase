package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.api.hud.HudEditor;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;

public final class HudRewrite extends ModuleBase {

    public HudRewrite() {
        super("HudRewrite", "", Category.Client);
    }

    @Override
    public void onEnable() {
        if (mc.world == null) {
            disable();
            return;
        }

        mc.setScreen(new HudEditor());
    }

}
