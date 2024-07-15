package club.lyric.infinity.impl.modules.player;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.api.util.client.math.MathUtils;
import club.lyric.infinity.api.util.client.render.colors.ColorUtils;
import club.lyric.infinity.api.util.client.render.util.Interpolation;
import club.lyric.infinity.api.util.client.render.util.Render3DUtils;
import club.lyric.infinity.asm.accessors.IClientPlayerInteractionManager;
import club.lyric.infinity.impl.events.mc.mine.MineBlockEvent;
import club.lyric.infinity.impl.events.network.PacketEvent;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.network.packet.s2c.play.BlockUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

import java.awt.*;

/**
 * @author vasler
 */
public class Reminer extends ModuleBase {

    public NumberSetting range = new NumberSetting("Range", this, 5.0f, 1.0f, 6.0f, 0.1f);
    public BooleanSetting fast = new BooleanSetting("Fast", true, this);
    private BlockPos selectedPos;
    private Direction direction;
    private boolean cool;

    public Reminer() {
        super("Reminer", "aa", Category.Player);
    }

    @Override
    public void onEnable()
    {
        reset();
    }

    @EventHandler
    public void onMineBlock(MineBlockEvent event) {
        BlockPos pos = event.getPos();
        Direction dir = event.getDir();

        if (!isBlockBreakable(pos)) return;

        if (selectedPos == null || !selectedPos.equals(pos)) {
            selectedPos = pos;
            direction = dir;
            send(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, selectedPos, direction));
        }

        cool = true;
    }

    @EventHandler
    public void onPacketReceive(PacketEvent.Receive event) {
        if (event.getPacket() instanceof BlockUpdateS2CPacket packet) {
            if (cool && packet.getPos().equals(selectedPos) && packet.getState() == mc.world.getBlockState(selectedPos)) {
                send(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, selectedPos, direction));
                cool = false;
            }
        }
    }

    @Override
    public void onUpdate()
    {
        if (selectedPos == null) return;
        
        if (mc.player.squaredDistanceTo(selectedPos.toCenterPos()) <= MathUtils.square(range.getFValue()))
        {
            send(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, selectedPos, direction));
            send(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, selectedPos, direction));

            if (fast.value())
            {
                send(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, selectedPos, direction));
                send(new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.STOP_DESTROY_BLOCK, selectedPos, direction));
            }
        }
        else
        {
            reset();
        }
    }

    @Override
    public String moduleInformation()
    {
        IClientPlayerInteractionManager inter = (IClientPlayerInteractionManager) mc.interactionManager;

        return String.format("%.2f", inter.getCurrentBreakingProgress());
    }

    @Override
    public void onRender3D(MatrixStack matrixStack) {
        if (selectedPos == null) return;

        Box boundingBox = Interpolation.interpolatePos(selectedPos, 1.0f);

        Render3DUtils.enable3D();
        matrixStack.push();

        Render3DUtils.drawBox(matrixStack, boundingBox, ColorUtils.alpha(new Color(-1), 70).getRGB());
        Render3DUtils.drawOutline(matrixStack, boundingBox, ColorUtils.alpha(new Color(-1), 255).getRGB());

        matrixStack.pop();
        Render3DUtils.disable3D();
    }

    public void reset()
    {
        cool = false;
        direction = null;
        selectedPos = null;
    }

    private boolean isBlockBreakable(BlockPos pos) {
        BlockState blockState = mc.world.getBlockState(pos);
        Block block = blockState.getBlock();

        return block.getHardness() != -1.0F;
    }
}