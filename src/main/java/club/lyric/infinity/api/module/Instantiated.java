package club.lyric.infinity.api.module;

import club.lyric.infinity.api.setting.Setting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * @author lyric
 * convenience function for instantiating settings.
 */
public class Instantiated {

    protected List<Setting<?>> settingList = new ArrayList<>();

    /**
     * function that adds all the settings of a module to its setting list.
     * may cause issues, due to unchecked cast.
     * @param module - module in
     * @param setting - setting in
     */
    protected void instantiate(ModuleBase module, Setting... setting) {
        Arrays.stream(setting).iterator().forEachRemaining(s -> s.setModule(module));
        settingList.addAll((Collection<? extends Setting<?>>) Arrays.stream(setting).toList());
    }
}
