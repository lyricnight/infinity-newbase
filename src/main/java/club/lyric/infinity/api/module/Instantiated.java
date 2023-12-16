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
     * you can only add multiple settings IF THEY ARE OF THE SAME CAPTURE OF <?>
     * LIKE: instantiate(this, booleansetting1, booleansetting2)
     *       intstantiate(this, intsetting1, intsetting2)
     * @param module - module in
     * @param setting - setting in
     */
    protected void instantiate(ModuleBase module, Setting<?>... setting) {
        Arrays.stream(setting).iterator().forEachRemaining(s -> s.setModule(module));
        settingList.addAll(Arrays.stream(setting).toList());
    }
}
