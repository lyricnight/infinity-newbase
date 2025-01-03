package club.lyric.infinity.manager.client;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import lombok.Getter;
import org.reflections.Reflections;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lyric and valser
 * register the modules
*/

@Getter
public final class ModuleManager implements IMinecraft
{
    private final List<ModuleBase> modules = new ArrayList<>();

    public void init()
    {
        /**
         * uses reflections to get modules af
         */
        Set<Class<? extends ModuleBase>> set = new Reflections("club.lyric.infinity.impl.modules")
                .getSubTypesOf(ModuleBase.class);

        for (Class<? extends ModuleBase> mClass : set) {
            try {
                ModuleBase module = mClass.getDeclaredConstructor().newInstance();
                modules.add(module);
            } catch (Exception e) {
                Infinity.LOGGER.error("WhyTfThisnotwork");
            }
        }

        modules.sort(Comparator.comparing(ModuleBase::getName));
        Infinity.LOGGER.info("Initialising modules complete!");
    }

    /**
     * gets a module from a class.
     * @param clazz - class in
     * @return - the corresponding module
     * @param <T> - type parameter
     */

    @SuppressWarnings("unchecked")
    public <T extends ModuleBase> T getModuleFromClass(Class<T> clazz) {
        for (ModuleBase moduleBase : getModules())
        {
            if(clazz.isInstance(moduleBase))
            {
                return (T) moduleBase;
            }
        }
        throw new RuntimeException("Class does not match any known module! Report this!");
    }

    /**
     * for toggle command
     * @param name - name of module in
     * @return relevant module
     */
    public ModuleBase getModuleByName(String name) {
        for (ModuleBase module : this.modules) {
            if (!module.getName().equalsIgnoreCase(name)) continue;
            return module;
        }
        return null;
    }

    public List<ModuleBase> getModulesInCategory(Category c) {
        return modules.stream().filter(m -> m.getCategory() == c).collect(Collectors.toList());
    }
}
