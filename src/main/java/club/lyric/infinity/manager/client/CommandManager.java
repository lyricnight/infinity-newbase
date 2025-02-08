package club.lyric.infinity.manager.client;

import club.lyric.infinity.api.command.Command;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.commands.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author lyric
 * simple command system
 * TODO replace
 */

@Getter
public final class CommandManager implements IMinecraft {
    @Setter
    private String prefix = "-";
    private final Set<Command> commands = new HashSet<>();


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
        for (Command ignored : getCommands()) {
            counter++;
        }
        return counter;
    }
}
