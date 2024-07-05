package club.lyric.infinity.asm.accessors;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MinecraftClient.class)
public interface IMinecraft {
    @Accessor(value = "itemUseCooldown")
    void setItemUseCooldown(int cooldown);

    @Accessor(value = "itemUseCooldown")
    int getItemUseCooldown();
}
