package club.lyric.infinity.asm;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.Version;
import club.lyric.infinity.api.event.bus.EventBus;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.manager.Managers;
import net.minecraft.client.MinecraftClient;
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
public final class MixinCrashReport implements IMinecraft {
    @Shadow
    @Final
    private List<CrashReportSection> otherSections;

    @Inject(method = "writeToFile", at = @At("HEAD"))
    public void writeToFile(File file, CallbackInfoReturnable<Boolean> cir) {
        otherSections.add(getInfinityCrashReport());
    }

    @Unique
    private CrashReportSection getInfinityCrashReport() {
        CrashReportSection section = new CrashReportSection("Infinity Debug (lyric was here)");
        section.add("Name", Infinity.CLIENT_NAME);
        section.add("Version", Version.VERSION);
        section.add("Nightly", Version.NIGHTLY);
        section.add("EventBus", EventBus.getInstance().hashCode());
        section.add("Listeners", EventBus.getInstance().getInvokers());
        return section;
    }
}
