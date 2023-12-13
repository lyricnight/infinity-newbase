package club.lyric.infinity.api.command;


import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.util.chat.ChatUtils;

/**
 * @author lyric
 * command global that can be extended.
 */

public class Command {

    /**
     * string we look for / name of the command
     */
    private final String command;

    public Command(String command)
    {
        this.command = command;
    }

    public String getCommand()
    {
        return command;
    }

    public String theCommand() {
        return "";
    }

    public void state(CommandState state) {
        switch (state) {
            case ERROR: {
                ChatUtils.sendMessagePrivate("Invalid command.");
                break;
            }

            case PERFORMED: {
                Infinity.LOGGER.info("Command success registered.");
                break;
            }
        }
    }

    /**
     * override
     * @param args the arguments in chat
     */

    public void onCommand(String[] args) {
    }
}
