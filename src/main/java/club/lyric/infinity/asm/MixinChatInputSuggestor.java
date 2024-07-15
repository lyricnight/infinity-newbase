package club.lyric.infinity.asm;

import club.lyric.infinity.api.command.Command;
import club.lyric.infinity.api.util.client.render.util.Render2DUtils;
import club.lyric.infinity.manager.Managers;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.widget.TextFieldWidget;
import org.apache.commons.lang3.StringUtils;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Mixin(ChatInputSuggestor.class)
public abstract class MixinChatInputSuggestor {

    @Final
    @Shadow
    TextFieldWidget textField;

    @Shadow
    private CompletableFuture<Suggestions> pendingSuggestions;

    @Unique
    private boolean render = false;

    @Shadow
    public abstract void show(boolean suggestion);

    @Inject(at = {@At("HEAD")}, method = {"render"})
    private void render(DrawContext context, int mouseX, int mouseY, CallbackInfo ci) {
        if (render) {
            int x = textField.getWidth() - 3;
            int y = textField.getHeight() - 3;
            Render2DUtils.drawOutlineRect(context.getMatrices(), x, y, textField.getWidth(), textField.getHeight(), -1);
        }
    }

    @Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/TextFieldWidget;getCursor()I", ordinal = 0), method = "refresh()V")
    private void onRefresh(CallbackInfo ci) {

        String prefix = Managers.COMMANDS.getPrefix();
        String inputText = textField.getText();

        if (inputText.isEmpty()) return;

        int cursorPosition = textField.getCursor();
        String textUpToCursor = inputText.substring(0, cursorPosition);

        render = inputText.startsWith(prefix);

        if (textUpToCursor.startsWith(prefix)) {

            int whitespaceEndPosition = getLastWhitespacePosition(textUpToCursor);
            SuggestionsBuilder suggestionsBuilder = new SuggestionsBuilder(textUpToCursor, whitespaceEndPosition);

            handleCommandSuggestions(prefix, suggestionsBuilder, textUpToCursor.substring(prefix.length()));

            pendingSuggestions = suggestionsBuilder.buildFuture();

            show(false);
        }
    }

    @Unique
    private int getLastWhitespacePosition(String text) {

        Matcher matcher = Pattern.compile("\\s+").matcher(text);
        int lastPosition = 0;

        while (matcher.find()) {
            lastPosition = matcher.end();
        }

        return lastPosition;
    }

    @Unique
    private void handleCommandSuggestions(String prefix, SuggestionsBuilder builder, String text) {
        int spaceCount = StringUtils.countMatches(text, " ");
        List<String> separatedText = Arrays.asList(text.split(" "));

        if (spaceCount == 0) {
            Managers.COMMANDS.getCommands().forEach(command -> builder.suggest(prefix + command.getCommand() + " "));
        } else {
            suggestCommandArguments(builder, separatedText);
        }
    }

    @Unique
    private void suggestCommandArguments(SuggestionsBuilder builder, List<String> separatedText) {
        if (separatedText.isEmpty()) return;

        Command command = Managers.COMMANDS.getCommandByName(separatedText.get(0));

        if (command == null) {
            return;
        }

        String[] suggestions = command.syntax(separatedText.get(separatedText.size() - 1));

        if (suggestions == null) return;

        for (String suggestion : suggestions) {
            builder.suggest(suggestion + " ");
        }
    }
}