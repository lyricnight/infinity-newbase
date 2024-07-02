package club.lyric.infinity.impl.modules.client;

import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.settings.NumberSetting;
import club.lyric.infinity.api.util.client.chat.ChatUtils;
import club.lyric.infinity.manager.client.TextManager;
import net.minecraft.util.Formatting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

/**
 * @author vasler
 */
public class Fonts extends ModuleBase {
    private String fontName = "Verdana";
    private String lastName;

    public NumberSetting size = new NumberSetting("Size", this, 19.0f, 14.0f, 22.0f, 1.0f);

    public Fonts() {
        super("Fonts", "ggg", Category.Client);
    }

    public String setFont(String string) {
        return fontName = string;
    }

    public String getFontName() {
        return fontName;
    }

    @Override
    public void onEnable() {
        lastName = fontName;
    }

    @Override
    public void onUpdate() {
        if (hasChanged()) {
            TextManager.nvgRenderer.reInit();
        }
    }

    public byte[] selectedFont() {
        Path fontPath = Paths.get("C:\\Windows\\Fonts", fontName + ".ttf");

        try {
            return Files.readAllBytes(fontPath);
        } catch (IOException e) {
            ChatUtils.sendOverwriteMessage(Formatting.RED + "Failed to load the selected font.", 99999);
        }
        return new byte[0];
    }

    public boolean hasChanged() {
        return !Objects.equals(lastName, fontName);
    }
}
