package club.lyric.infinity.api.util.minecraft.block.hole;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * class representing holes of 3 different types
 */


@Getter
public class Hole {
    @Setter
    private BlockPos first;
    private BlockPos second;
    private final HoleTypes holeTypes;

    public Hole(BlockPos first, HoleTypes holeTypes) {
        this.first = first;
        this.holeTypes = holeTypes;
    }

    public Hole(BlockPos first, BlockPos second, HoleTypes holeTypes) {
        this.first = first;
        this.second = second;
        this.holeTypes = holeTypes;
    }

    public String toString() {
        return "Hole(" + this.first + ", " + this.second + ", " + (this.holeTypes) + ")";
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Hole hole)) {
            return false;
        }
        if (this.first != hole.first) {
            return false;
        }
        if (this.second != hole.second) {
            return false;
        }
        return this.holeTypes == hole.holeTypes;
    }

    public int hashCode() {
        return new HashCodeBuilder(17, 31).append(this.first).append(this.first).toHashCode();
    }
}
