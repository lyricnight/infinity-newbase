package club.lyric.infinity.impl.modules.player;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import net.minecraft.item.ItemStack;

public class Announcer extends ModuleBase
{

    public String eatMessage;
    public String moveMessage;
    public String mineMessage;
    public String placeMessage;

    public Announcer()
    {
        super("Announcer", "Announces certain information in chat.", Category.Player);
    }

    public String getMessages(Actions actions, int amountEaten, ItemStack food, float blocksMoved) {
        switch (actions)
        {

            case EAT ->
                    eatMessage.replace("{amount}", amountEaten + "").replace("{item}", food.getName().getString()).replace("NIGGER", "");

            case MOVE ->
                    moveMessage.replace("{blocks}", String.format("%.2f", blocksMoved)).replace("NIGGER", "");

            //case MINE ->
                    //mineMessage.replace();

            //case PLACE ->
                   // placeMessage.replace();
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
