package club.lyric.infinity.manager.client;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.modules.client.*;
import club.lyric.infinity.impl.modules.client.ClickGui;
import club.lyric.infinity.impl.modules.combat.Aura;
import club.lyric.infinity.impl.modules.combat.AutoCrystal;
import club.lyric.infinity.impl.modules.combat.AutoMine;
import club.lyric.infinity.impl.modules.exploit.*;
import club.lyric.infinity.impl.modules.movement.*;
import club.lyric.infinity.impl.modules.player.*;
import club.lyric.infinity.impl.modules.render.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author lyric and valser
 * register the modules
*/

public final class ModuleManager implements IMinecraft
{
    private final List<ModuleBase> modules = new ArrayList<>();

    public void init()
    {
        register(
                new Notifications(),
                new ClickGui(),
                new Configuration(),
                new AntiCheat(),
                new DiscordRPC(),
                new AutoMine(),
                new Chat(),
                new HUD(),
                new Aura(),
                new Sprint(),
                new Step(),
                new NoJumpDelay(),
                new HitboxDesync(),
                new PhaseWalk(),
                new FastLatency(),
                new Clip(),
                new RaytraceBypass(),
                new FakePlayer(),
                new HoleESP(),
                new HoleSnap(),
                new AutoCrystal(),
                new Resolver(),
                new Delays(),
                new GuiRewrite(),
                new Colours(),
                new Nametags(),
                new Velocity(),
                new Speed(),
                new InstantAcceleration(),
                new SpeedMine()
        );
        Infinity.LOGGER.info("Initialising modules.");
    }

    /**
     * gets modules
     */
    public List<ModuleBase> getModules()
    {
        return modules;
    }

    /**
     * gets a module from a class.
     * @param clazz - class in
     * @return - the corresponding module
     * @param <T> - type parameter
     */

    @SuppressWarnings("unchecked")
    public <T extends ModuleBase> T getModuleFromClass(Class<T> clazz)
    {
        for (ModuleBase moduleBase : getModules())
        {
            if(clazz.isInstance(moduleBase))
            {
                return (T) moduleBase;
            }
        }
        return null;
    }

    /**
     * Allows you to register every module at once
     */
    public void register(ModuleBase... module)
    {
        Collections.addAll(modules, module);
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
