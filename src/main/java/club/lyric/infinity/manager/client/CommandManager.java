package club.lyric.infinity.manager.client;

import club.lyric.infinity.api.command.Command;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.commands.*;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author lyric
 * simple command system
 */

public final class CommandManager implements IMinecraft {
    private static String prefix = "-";
    private static final Set<Command> commands = new HashSet<>();


    public void init()
    {
        register(
                new Prefix(),
                new Friend(),
                new List(),
                new Identifier(),
                new Toggle(),
                new Watermark(),
                new Bind()
        );
    }

    public void register(Command... command) {
        Collections.addAll(commands, command);
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

    public Set<Command> getCommands() {
        return commands;
    }

    /**
     * for -commands / -help
     * @return all commands
     */
    public StringBuilder getCommandsAsString()
    {
        StringBuilder str = new StringBuilder();
        for(Command commands : getCommands())
        {
            str.append(commands.getCommand()).append(", ");
        }
        return str;
    }

    public int getCommandAmount() {
        int counter = 0;
        for (Command commands : getCommands()) {
            counter++;
        }
        return counter;
    }
}
