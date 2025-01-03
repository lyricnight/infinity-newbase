package club.lyric.infinity.manager.client;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.command.Command;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import org.reflections.Reflections;

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
        Set<Class<? extends Command>> set = new Reflections("club.lyric.infinity.impl.commands")
                .getSubTypesOf(Command.class);

        for (Class<? extends Command> mClass : set) {
            try {
                Command command = mClass.getDeclaredConstructor().newInstance();
                commands.add(command);
            } catch (Exception e) {
                Infinity.LOGGER.error("WhyTfThisnotwork (Command Edition)");
            }
        }
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

    public Command getCommandByName(String name) {
        for (Command command : commands) {
            if (!command.getCommand().equalsIgnoreCase(name)) continue;
            return command;
        }

        return null;
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
