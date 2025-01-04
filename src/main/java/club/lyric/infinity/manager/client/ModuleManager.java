package club.lyric.infinity.manager.client;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.modules.client.*;
import club.lyric.infinity.impl.modules.combat.*;
import club.lyric.infinity.impl.modules.exploit.*;
import club.lyric.infinity.impl.modules.misc.*;
import club.lyric.infinity.impl.modules.movement.*;
import club.lyric.infinity.impl.modules.player.*;
import club.lyric.infinity.impl.modules.player.Timer;
import club.lyric.infinity.impl.modules.visual.*;
import lombok.Getter;

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
        register(
                new Notifications(),
                new ClickGUI(),
                new Configuration(),
                new AntiCheat(),
                new RichPresence(),
                new AutoMine(),
                new Chat(),
                new HUD(),
                new Aura(),
                new Sprint(),
                new Step(),
                new HitboxDesync(),
                new PhaseWalk(),
                new FastLatency(),
                new Clip(),
                new FakePlayer(),
                new HoleESP(),
                new HoleSnap(),
                new AutoCrystal(),
                new Resolver(),
                new Delays(),
                new Colours(),
                new Nametags(),
                new Velocity(),
                new Speed(),
                new NoAccelerate(),
                new PingSpoof(),
                new AntiAscii(),
                new CustomFont(),
                new FastProjectile(),
                new MCF(),
                new FakeBrand(),
                new AutoReply(),
                new FullBright(),
                new Offhand(),
                new CameraClip(),
                new ESP(),
                new Criticals(),
                new Ambience(),
                new MultiTask(),
                new RaytraceBypass(),
                new IRC(),
                new Freelook(),
                new BlockHighlight(),
                new AntiLevitation(),
                new KickPrevent(),
                new Replenish(),
                new Timer(),
                new Reminer(),
                new AutoRespawn(),
                new AirPlace(),
                new Burrow(),
                new InventoryWalk(),
                new Zoom()
        );
        modules.sort(Comparator.comparing(ModuleBase::getName));
        Infinity.LOGGER.info("Initialising modules complete!");
    }

    private void register(ModuleBase... moduleBases)
    {
        Collections.addAll(modules, moduleBases);
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
