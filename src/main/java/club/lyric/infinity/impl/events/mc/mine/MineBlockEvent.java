package club.lyric.infinity.impl.events.mc.mine;

import club.lyric.infinity.api.event.Event;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

/**
 * @author lyric
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class MineBlockEvent extends Event {
    private final BlockPos pos;
    private final BlockState state;
    private final Direction direction;
}