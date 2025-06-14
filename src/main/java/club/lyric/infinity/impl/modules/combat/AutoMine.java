package club.lyric.infinity.impl.modules.combat;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.ModeSetting;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.api.util.client.math.MathUtils;
import club.lyric.infinity.api.util.client.math.StopWatch;
import club.lyric.infinity.api.util.minecraft.block.BlockUtils;
import club.lyric.infinity.api.util.minecraft.block.HoleUtils;
import club.lyric.infinity.api.util.minecraft.player.PlayerUtils;
import club.lyric.infinity.manager.Managers;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author unknown
 * used as a TESTING MODULE to check if client functionality works
 */
//TODO: delete this
@Deprecated
public final class AutoMine extends ModuleBase {
    public ModeSetting mode = new ModeSetting("Mode", this, "Vanilla", "Vanilla", "Packet", "Both");
    public NumberSetting range = new NumberSetting("Range", this, 5.0, 0.0, 6.0, 0.1, "m");
    public NumberSetting enemyRange = new NumberSetting("EnemyRange", this, 5.0, 1.0, 10.0, 0.5, "m");
    public BooleanSetting protocol = new BooleanSetting("Protocol", true, this);
    public BooleanSetting feet = new BooleanSetting("Feet", true, this);
    public BooleanSetting semi = new BooleanSetting("Semi", false, this);
    public BooleanSetting burrow = new BooleanSetting("Burrow", false, this);
    public BooleanSetting ender = new BooleanSetting("EnderChests", false, this);
    public BooleanSetting cev = new BooleanSetting("Cev", false, this);
    public BooleanSetting above = new BooleanSetting("CevAbove", false, this);
    public BooleanSetting facePlace = new BooleanSetting("FaceBlocker", false, this);
    public BooleanSetting dev = new BooleanSetting("Dev", false, this);
    private PlayerEntity player = null;
    private BlockPos attacking = null;
    private final StopWatch.Single watch = new StopWatch.Single();

    private final AtomicInteger count = new AtomicInteger();

    private boolean first;

    public AutoMine() {
        super("AutoMine", "Requires another client's speedmine to use.", Category.COMBAT);
    }

    @Override
    public void onEnable() {
        first = true;
    }


    @Override
    public void onUpdate() {
        player = null;

        if (mc.player.isCreative() || mc.player.isSpectator()) {
            return;
        }

        if (!watch.hasBeen(250L)) {
            return;
        }

        player = Managers.OTHER.getTarget(enemyRange.getValue());
        if (player == null) {
            breakEnderChests();
            return;
        }

        if (dev.value()) {
            Managers.MESSAGES.sendMessage("Target:" + player.getDisplayName() + " " + count.addAndGet(1), false);
        }

        BlockPos targetPos = player.getBlockPos();

        if (burrow.value() && PlayerUtils.isInBurrow(player)) {
            if (dev.value()) Managers.MESSAGES.sendMessage("Burrow. " + targetPos + " " + count.addAndGet(1), false);
            attack(targetPos);
            return;
        }

        if (isHole(player) && facePlace.value()) {
            for (BlockPos pos : BlockUtils.Offsets.FACE_PLACE) {
                if (mc.world.getBlockState(targetPos.add(pos)).getBlock() == Blocks.OBSIDIAN && mc.world.getBlockState(targetPos.add(pos.up())).getBlock() == Blocks.AIR) {
                    if (dev.value())
                        Managers.MESSAGES.sendMessage("Face. " + targetPos.add(pos) + " " + count.addAndGet(1), false);
                    attack(targetPos.add(pos));
                    return;
                }
            }
        }

        if (cev.value() && PlayerUtils.isTrapped(player) && isHole(player)) {
            BlockPos aaa = null;
            if (mc.world.getBlockState(targetPos.up(3)).getBlock() != Blocks.AIR && above.value()) {
                aaa = targetPos.up(3);
            } else if (mc.world.getBlockState(targetPos.up(3)).getBlock() == Blocks.AIR) {
                aaa = targetPos.up(2);
            }
            if (dev.value()) Managers.MESSAGES.sendMessage("Cev. " + count.addAndGet(1), false);
            attack(aaa);
            return;
        }

        if (dev.value()) Managers.MESSAGES.sendMessage("Checking side. " + count.addAndGet(1), false);

        checkSide();

        if (!protocol.value()) {
            if (getCity(player) == null) {
                if (dev.value()) Managers.MESSAGES.sendMessage("No city -> side. " + count.addAndGet(1), false);
                checkSide();
                breakEnderChests();
                return;
            }
        }

        if (feet.value() && PlayerUtils.isInPhase(player) && !protocol.value()) {
            attack(getCity(player));
            if (dev.value()) Managers.MESSAGES.sendMessage("Feet. " + count.addAndGet(1), false);
        } else if (feet.value() && PlayerUtils.isInPhase(player)) {
            for (Direction direction : Arrays.stream(Direction.values()).filter(direction -> direction.getAxis().isHorizontal()).toList()) {
                BlockPos pos = player.getBlockPos().offset(direction);
                boolean canPlace = mc.world.getBlockState(pos.down()).getBlock() == Blocks.BEDROCK || mc.world.getBlockState(pos.down()).getBlock() == Blocks.OBSIDIAN;
                if (mc.world.getBlockState(pos).getBlock() == Blocks.OBSIDIAN && canPlace) {
                    if (dev.value()) Managers.MESSAGES.sendMessage("Feet2. " + pos + " " + count.addAndGet(1), false);
                    attack(pos);
                }
            }
        }
    }

    private void attack(BlockPos pos) {
        if (pos == null) {
            Managers.MESSAGES.sendMessage("Attack failed: null", false);
            return;
        }
        if (dev.value()) {
            Managers.MESSAGES.sendMessage("Attacking: " + pos, false);
        }
        if (BlockUtils.getDistanceSq(pos) > MathUtils.square(this.range.getFValue())) {
            return;
        }
        if ((mc.world.getBlockState(pos).getBlock()) == Blocks.AIR) {
            return;
        }
        if (!BlockUtils.canBreak(pos)) {
            return;
        }
        if (first) {
            switch (mode.getMode()) {
                case "Vanilla" -> mc.interactionManager.attackBlock(pos, Direction.UP);
                case "Packet" ->
                        send(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, pos, Direction.UP));
                case "Both" -> {
                    mc.interactionManager.attackBlock(pos, Direction.UP);
                    send(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, pos, Direction.UP));
                }
            }
            first = false;
            attacking = pos;
            return;
        }
        if (mc.world.getBlockState(attacking).getBlock() != Blocks.AIR) {
            return;
        }

        switch (mode.getMode()) {
            case "Vanilla" -> mc.interactionManager.attackBlock(pos, Direction.UP);
            case "Packet" ->
                    send(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, pos, Direction.UP));
            case "Both" -> {
                mc.interactionManager.attackBlock(pos, Direction.UP);
                send(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, pos, Direction.UP));
            }
        }
        attacking = pos;
        watch.reset();
    }

    private boolean isHole(PlayerEntity target) {
        return isBedrockHole(target.getBlockPos()) || isMixedHole(target.getBlockPos()) || isObbyHole(target.getBlockPos());
    }

    private boolean isObbyHole(BlockPos pos) {
        for (BlockPos off : HoleUtils.holeOffsets) {
            if (!BlockUtils.isObby(pos.add(off))) {
                return false;
            }
        }
        return true;
    }

    private boolean isMixedHole(BlockPos pos) {
        if (isBedrockHole(pos)) return false;
        for (BlockPos off : HoleUtils.holeOffsets) {
            if (!BlockUtils.isSafe(pos.add(off))) {
                return false;
            }
        }
        return true;
    }

    private boolean isBedrockHole(BlockPos pos) {
        for (BlockPos off : HoleUtils.holeOffsets) {
            if (mc.world.getBlockState(pos.add(off)).getBlock() != Blocks.BEDROCK) {
                return false;
            }
        }
        return true;
    }


    private BlockPos getCity(PlayerEntity player) {
        BlockPos targetPos = player.getBlockPos();
        for (Direction direction : Arrays.stream(Direction.values()).filter(direction -> direction.getAxis().isHorizontal()).toList()) {
            BlockPos offsetPos = targetPos.offset(direction);
            if (mc.world.getBlockState(offsetPos).getBlock() == Blocks.OBSIDIAN) {
                BlockPos fullOffset = offsetPos.offset(direction);
                if (mc.world.getBlockState(fullOffset).getBlock() == Blocks.AIR) {
                    if (isPlaceable(fullOffset.down()) && mc.world.getBlockState(fullOffset.up()).getBlock() == Blocks.AIR) {
                        if (dev.value()) Managers.MESSAGES.sendMessage("City. " + offsetPos + " " + count.addAndGet(1), false);
                        return offsetPos;
                    }
                }
            }
        }
        if (dev.value()) Managers.MESSAGES.sendMessage("City failed. " + count.addAndGet(1), false);
        return null;
    }

    private void checkSide() {
        if (semi.value()) {
            for (BlockPos pos : BlockUtils.Offsets.FEET_PLACE) {
                BlockPos targetPos = player.getBlockPos();
                BlockPos offsetPos = targetPos.add(pos);
                if (mc.world.getBlockState(offsetPos).getBlock() == Blocks.OBSIDIAN && mc.world.getBlockState(offsetPos.up()).getBlock() == Blocks.AIR) {
                    if (dev.value())
                        Managers.MESSAGES.sendMessage("Side. " + targetPos.add(pos) + " " + count.addAndGet(1), false);
                    attack(offsetPos);
                }
            }
        } else {
            if (dev.value()) Managers.MESSAGES.sendMessage("Checkside failed. " + count.addAndGet(1), false);
        }
    }

    private void breakEnderChests() {
        if (ender.value()) {
            if (dev.value()) {
                Managers.MESSAGES.sendMessage("EChests." + count.addAndGet(1), false);
            }
            for (BlockPos pos : BlockUtils.getSphere(mc.player, range.getFValue(), true)) {
                if (mc.world.getBlockState(pos).getBlock() == Blocks.ENDER_CHEST) {
                    attack(pos);
                }
            }
        }
    }

    private boolean isPlaceable(BlockPos pos) {
        return mc.world.getBlockState(pos).getBlock() == Blocks.OBSIDIAN || mc.world.getBlockState(pos).getBlock() == Blocks.BEDROCK;
    }

    @Override
    public String moduleInformation() {
        if (player != null) {
            return player.getDisplayName().getString();
        } else {
            return Formatting.RED + "none";
        }
    }
}
