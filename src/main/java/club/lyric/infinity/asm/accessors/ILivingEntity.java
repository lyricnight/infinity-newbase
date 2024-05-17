package club.lyric.infinity.asm.accessors;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.TrackedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LivingEntity.class)
public interface ILivingEntity {
    @Accessor("HEALTH")
    static TrackedData<Float> getHealthId() {
        throw new IllegalStateException("HEALTH accessor has not been mixed in. Report this!");
    }
}
