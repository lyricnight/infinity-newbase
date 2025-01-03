package club.lyric.infinity.impl.events.mc.mine;

import club.lyric.infinity.api.event.Event;
import lombok.Getter;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

/**
 * @author lyric
 */
@Getter
public class MineBlockEvent extends Event {
    private final BlockPos pos;
    private final BlockState state;
    private final Direction direction;

    public MineBlockEvent(BlockPos pos, BlockState state, Direction direction) {
        this.pos = pos;
        this.state = state;
        this.direction = direction;
    }

}