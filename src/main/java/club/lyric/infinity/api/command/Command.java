package club.lyric.infinity.api.command;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.util.client.chat.ChatUtils;
import lombok.Getter;
import net.minecraft.util.Formatting;

/**
 * @author lyric
 * command global that can be extended.
 */

@Getter
public abstract class Command {

    /**
     * string we look for / name of the command
     */
    private final String command;

    public Command(String command) {
        this.command = command;
    }

    //overridden methods

    public String theCommand() {
        return "";
    }

    /**
     * success or failure
     *
     * @param state - state of command to throw
     */
    public void state(CommandState state) {
        switch (state) {
            case ERROR -> ChatUtils.sendMessagePrivate(Formatting.RED + "Command failed. Correct syntax: " + theCommand());
            case PERFORMED -> Infinity.LOGGER.info("Command success registered. Command: {}", theCommand());
        }
    }

    /**
     * override
     *
     * @param args the arguments in chat
     */

    public void onCommand(String[] args) {
    }

    public abstract String[] syntax(String string);
}
