package club.lyric.infinity.manager.client;

import club.lyric.infinity.api.command.Command;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.commands.Prefix;

import java.util.HashSet;
import java.util.Set;

/**
 * @author lyric
 * simple command system
 */

public class CommandManager implements IMinecraft {

    private static String prefix = "-";
    private static final Set<Command> commands = new HashSet<>();


    public void init()
    {
        commands.add(new Prefix());
    }


    /**
     * this returns the command we need to get
     * @param commandStr - string of the command
     * @return the Command corresponding to commandStr
     */

    public static Command get(String commandStr) {
        Command command = null;

        for (Command commands : getCommands()) {
            if (commands.getCommand().equalsIgnoreCase(commandStr)) {
                command = commands;

                break;
            }
        }

        return command;
    }

    //prefix methods

    public String getPrefix() {
        return prefix;
    }

    public void setPrefix(String in) {
        prefix = in;
    }

    /**
     * returns all initialised commands
     * @return the commands
     */

    public static Set<Command> getCommands() {
        return commands;
    }

    /**
     * for -commands / -help
     * @return all commands
     */
    public static StringBuilder getCommandsAsString()
    {
        StringBuilder str = new StringBuilder();
        for(Command commands : getCommands())
        {
            str.append(commands.getCommand()).append(" ");
        }
        return str;
    }
}
