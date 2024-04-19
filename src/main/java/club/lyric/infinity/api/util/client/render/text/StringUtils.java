package club.lyric.infinity.api.util.client.render.text;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.impl.modules.client.Colours;
import club.lyric.infinity.manager.Managers;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

/**
 * @author lyric
 * for some string methods we may need...
 */
public class StringUtils {

    public static String coloredString(String string, String color) {
        String coloredString;

        coloredString = getCodeFromSetting(color) + string + Formatting.RESET;

        return coloredString;
    }

    public Text clientName()
    {
        MutableText clientText = Text.literal("[" + Formatting.WHITE + "Infinity" + "]");
        return clientText.setStyle(clientText.getStyle().withColor(Managers.MODULES.getModuleFromClass(Colours.class).getColor().getRGB()));
    }

    public static boolean contains(String name, String... items) {
        boolean flag = false;

        for (String i : items) {
            if (i.equalsIgnoreCase(name)) {
                flag = true;

                break;
            }
        }

        return flag;
    }

    /**
     * method returns color code from setting.
     * @param value - string setting value
     * @return color.
     */

    public static String getCodeFromSettingString(String value)
    {
        switch (value) {
            case "None" -> {
                return "";
            }
            case "Black" -> {
                return "§0";
            }
            case "DarkGray" -> {
                return "§8";
            }
            case "Gray" -> {
                return "§7";
            }
            case "DarkBlue" -> {
                return "§1";
            }
            case "Blue" -> {
                return "§9";
            }
            case "DarkGreen" -> {
                return "§2";
            }
            case "Green" -> {
                return "§a";
            }
            case "DarkAqua" -> {
                return "§3";
            }
            case "Aqua" -> {
                return "§b";
            }
            case "DarkRed" -> {
                return "§4";
            }
            case "Red" -> {
                return "§c";
            }
            case "DarkPurple" -> {
                return "§5";
            }
            case "Purple" -> {
                return "§d";
            }
            case "Gold" -> {
                return "§6";
            }
            case "Yellow" -> {
                return "§e";
            }
        }
        Infinity.LOGGER.error("Couldn't return a proper colour! String value passed to method: " + value);
        return "";
    }


    public static Formatting getCodeFromSetting(String value)
    {
        switch (value) {
            case "None" -> {
                return Formatting.WHITE;
            }
            case "Black" -> {
                return Formatting.BLACK;
            }
            case "DarkGray" -> {
                return Formatting.DARK_GRAY;
            }
            case "Gray" -> {
                return Formatting.GRAY;
            }
            case "DarkBlue" -> {
                return Formatting.DARK_BLUE;
            }
            case "Blue" -> {
                return Formatting.BLUE;
            }
            case "DarkGreen" -> {
                return Formatting.DARK_GREEN;
            }
            case "Green" -> {
                return Formatting.GREEN;
            }
            case "DarkAqua" -> {
                return Formatting.DARK_AQUA;
            }
            case "Aqua" -> {
                return Formatting.AQUA;
            }
            case "DarkRed" -> {
                return Formatting.DARK_RED;
            }
            case "Red" -> {
                return Formatting.RED;
            }
            case "DarkPurple" -> {
                return Formatting.DARK_PURPLE;
            }
            case "Purple" -> {
                return Formatting.LIGHT_PURPLE;
            }
            case "Gold" -> {
                return Formatting.GOLD;
            }
            case "Yellow" -> {
                return Formatting.YELLOW;
            }
        }
        Infinity.LOGGER.error("Couldn't return a proper colour! String value passed to method: " + value);
        return Formatting.WHITE;
    }
}
