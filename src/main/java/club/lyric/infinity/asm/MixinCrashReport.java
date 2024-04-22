package club.lyric.infinity.asm;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.event.bus.EventBus;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.manager.Managers;
import net.minecraft.util.crash.CrashReport;
import net.minecraft.util.crash.CrashReportSection;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author lyric
 */
@Mixin(CrashReport.class)
public final class MixinCrashReport {
    @Shadow
    @Final
    private List<CrashReportSection> otherSections;

    @Inject(method = "writeToFile", at = @At("HEAD"))
    public void writeToFile(File file, CallbackInfoReturnable<Boolean> cir)
    {
        otherSections.add(getInfinityCrashReport());
    }

    @Unique
    private CrashReportSection getInfinityCrashReport()
    {
        CrashReportSection section = new CrashReportSection("Infinity Debug");

        ArrayList<String> module = new ArrayList<>();

        Managers.MODULES.getModules().stream().filter(ModuleBase::isOn).forEach(moduleBase -> module.add(moduleBase.getName()));

        section.add("Name", Infinity.CLIENT_NAME);
        section.add("Version", Infinity.VERSION);
        section.add("Enabled Modules", module);
        section.add("EventBus", EventBus.getInstance().hashCode());

        return section;
    }
}
