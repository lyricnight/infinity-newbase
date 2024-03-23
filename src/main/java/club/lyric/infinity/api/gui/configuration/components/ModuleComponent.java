package club.lyric.infinity.api.gui.configuration.components;

import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.Setting;

public class ModuleComponent {

    public ModuleComponent(ModuleBase moduleBase) {
        for (Setting setting : moduleBase.getSettings()) {

        }
    }
}
