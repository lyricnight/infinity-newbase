package club.lyric.infinity.impl.modules.combat;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.BooleanSetting;
import club.lyric.infinity.api.setting.settings.ModeSetting;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.api.util.minecraft.player.PlayerUtils;
import club.lyric.infinity.manager.Managers;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClickSlotC2SPacket;
import net.minecraft.registry.tag.EntityTypeTags;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.math.MathHelper;

//TODO: make a service for this
@SuppressWarnings("ConstantConditions")
public class Offhand extends ModuleBase
{
    public ModeSetting mode = new ModeSetting("Mode", this, "Totem", "Totem", "Crystal", "Gapple", "Sword");
    public NumberSetting health = new NumberSetting("Health", this, 16.0f, 1.0f, 36.0f, 0.1f);
    public BooleanSetting swordGap = new BooleanSetting("SwordGap", true, this);
    public BooleanSetting smart = new BooleanSetting("Smart", true, this);

    public Offhand()
    {
        super("Offhand", "Allows you to put different items in your offhand slot.", Category.Combat);
    }

    protected Int2ObjectMap<ItemStack> int2ObjectMap = new Int2ObjectOpenHashMap<>();


    @Override
    public void onUpdate()
    {
        ScreenHandler screenHandler = mc.player.currentScreenHandler;

        Item item = getWeldingItemType();

        // if the item is in their offhand it doesn't switch.
        if (mc.player.getOffHandStack().getItem() == item) return;

        // closes the inventory gui if its in it
        if (mc.currentScreen instanceof InventoryScreen) mc.player.closeScreen();

        // xtra check, not rlly needed
        if (mc.currentScreen != null) return;

        int slot = getItemSlot(item);

        if (slot == -1) return;

        // drags a new item to the offhand slot
        send(new ClickSlotC2SPacket(0, screenHandler.getRevision(), slot, 0, SlotActionType.PICKUP, screenHandler.getCursorStack().copy(), int2ObjectMap));
        send(new ClickSlotC2SPacket(0, screenHandler.getRevision(), 45, 0, SlotActionType.PICKUP, screenHandler.getCursorStack().copy(), int2ObjectMap));
        send(new ClickSlotC2SPacket(0, screenHandler.getRevision(), slot, 0, SlotActionType.PICKUP, screenHandler.getCursorStack().copy(), int2ObjectMap));

    }

    public Item getWeldingItemType()
    {

        float healthin = health.getFValue();

        if (PlayerUtils.getPlayerHealth() <= healthin) return Items.TOTEM_OF_UNDYING;

        if (getFallDmg(mc.player.fallDistance, 1.0f) + 0.5f >= healthin) return Items.TOTEM_OF_UNDYING;

        if (mc.player.getMainHandStack().getItem() == getSword() && mc.options.useKey.isPressed() && swordGap.value()) return getGoldenApple();

        if (Managers.SERVER.isServerNotResponding()) return Items.TOTEM_OF_UNDYING;

        if (smart.value())
        {
            for (Entity ent : mc.world.getEntities())
            {
                if (ent == null && !(ent instanceof EndCrystalEntity))
                {
                    continue;
                }
            }
        }

        return getItemFromMode();
    }

    public Item getItemFromMode()
    {
        if (mode.is("Crystal"))
        {
            return Items.END_CRYSTAL;
        }
        else if (mode.is("Gapple"))
        {

            return getGoldenApple();
        }
        else if (mode.is("Sword"))
        {
            return getSword();
        }

        return Items.TOTEM_OF_UNDYING;
    }

    public Item getGoldenApple()
    {
        if (getItemCount(Items.ENCHANTED_GOLDEN_APPLE) == -1) return Items.GOLDEN_APPLE;

        return Items.ENCHANTED_GOLDEN_APPLE;
    }

    public Item getSword()
    {
        // DUMB AF LMFAO
        if (getItemCount(Items.NETHERITE_SWORD) == -1) return Items.DIAMOND_SWORD;

        if (getItemCount(Items.NETHERITE_SWORD) == -1 && getItemCount(Items.DIAMOND_SWORD) == -1) return Items.IRON_SWORD;

        if (getItemCount(Items.NETHERITE_SWORD) == -1 && getItemCount(Items.DIAMOND_SWORD) == -1 && getItemCount(Items.IRON_SWORD) == -1) return Items.STONE_SWORD;

        if (getItemCount(Items.NETHERITE_SWORD) == -1 && getItemCount(Items.DIAMOND_SWORD) == -1 && getItemCount(Items.IRON_SWORD) == -1 && getItemCount(Items.STONE_SWORD) == -1) return Items.WOODEN_SWORD;

        return Items.NETHERITE_SWORD;
    }


    public int getItemCount(Item item)
    {
        if (mc.player == null) return 0;

        int counter = 0;

        for (int i = 0; i <= 44; ++i)
        {
            ItemStack itemStack = mc.player.getInventory().getStack(i);
            if (itemStack.getItem() != item) continue;
            counter += itemStack.getCount();
        }

        return counter;
    }

    public int getItemSlot(Item item)
    {

        if (mc.player == null) return 0;

        for (int i = 0; i < mc.player.getInventory().size(); ++i)
        {
            if (i != 0 && i != 5 && i != 6 && i != 7)
            {
                if (i != 8)
                {
                    ItemStack s = mc.player.getInventory().getStack(i);
                    if (!s.isEmpty())
                    {
                        if (s.getItem() == item)
                        {
                            return i;
                        }
                    }
                }
            }
        }
        return -1;
    }

    public static int getFallDmg(float fallDistance, float damageMultiplier)
    {
        if (mc.player.getType().isIn(EntityTypeTags.FALL_DAMAGE_IMMUNE))
        {
            return 0;
        }
        else
        {
            final StatusEffectInstance statusEffectInstance = mc.player.getStatusEffect(StatusEffects.JUMP_BOOST);
            final float f = statusEffectInstance == null ? 0.0F : (float)(statusEffectInstance.getAmplifier() + 1);
            return MathHelper.ceil((fallDistance - 3.0F - f) * damageMultiplier);
        }
    }

    @Override
    public String moduleInformation()
    {
        return getItemCount(Items.TOTEM_OF_UNDYING) + "";
    }
}
