package club.lyric.infinity.impl.modules.misc;

import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.impl.events.mc.chat.ReceiveChatEvent;
import me.bush.translator.Language;
import me.bush.translator.Translation;
import me.bush.translator.Translator;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class Translate extends ModuleBase
{

    //Translator translator = new Translator();

    public Translate()
    {
        super("Translator", "ggg", Category.Misc);
    }

    /**@EventHandler
    public void onChatReceive(ReceiveChatEvent event)
    {
        Text message = event.getMessage();

        if (message.contains(Text.of("[Infinity]")))
        {
            return;
        }

        Translation translation = translator.translateBlocking(message.getString(), Language.ENGLISH, Language.AUTO);

        if (translation.getSourceLanguage() == Language.ENGLISH) return;

        message = Text.of(Formatting.GRAY + "Translated: " + translation.getTranslatedText());

        event.setMessage(message);
    }*/
}
