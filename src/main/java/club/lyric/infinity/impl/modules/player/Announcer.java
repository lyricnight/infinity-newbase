package club.lyric.infinity.impl.modules.player;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
public class Announcer extends ModuleBase
{

    public String eatMessage;
    public String moveMessage;
    public String mineMessage;
    public String placeMessage;
    ItemStack food;
    Block mine;
    ItemStack place;

    public Announcer()
    {
        super("Announcer", "Announces certain information in chat.", Category.Player);
    }

    public String getMessages(Actions actions, float count) {
        switch (actions)
        {

            case EAT ->
                    eatMessage.replace("{amount}", count + "").replace("{item}", food.getName().getString()).replace("NIGGER", "");

            case MOVE ->
                    moveMessage.replace("{blocks}", String.format("%.2f", count)).replace("NIGGER", "");

            case MINE ->
                    mineMessage.replace("{amount}", count + "").replace("{item}", mine.getName().getString()).replace("NIGGER", "");

            case PLACE ->
                   placeMessage.replace("{amount}", count + "").replace("{item}", place.getName().getString()).replace("NIGGER", "");
        }
        return "Infinity";
    }

    public enum Actions
    {

        MOVE,
        EAT,
        MINE,
        PLACE

    }
}
