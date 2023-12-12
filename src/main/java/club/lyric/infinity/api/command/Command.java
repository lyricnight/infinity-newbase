package club.lyric.infinity.api.command;


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
                //insert chat message here
                break;
            }

            case PERFORMED: {
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
