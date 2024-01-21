package club.lyric.infinity.manager.client;

import club.lyric.infinity.api.command.Command;
import club.lyric.infinity.api.event.bus.EventHandler;
import club.lyric.infinity.api.event.client.SettingEvent;
import club.lyric.infinity.api.util.client.config.JsonElements;
import club.lyric.infinity.api.util.client.render.text.StringUtils;
import club.lyric.infinity.api.util.minecraft.IMinecraft;
import club.lyric.infinity.impl.commands.*;
import club.lyric.infinity.impl.modules.client.Manager;
import club.lyric.infinity.manager.Managers;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author lyric
 * simple command system
 */

public class CommandManager implements IMinecraft, JsonElements {
    private static String prefix = "-";
    private static final Set<Command> commands = new HashSet<>();


    public void init()
    {
        register(
                new Prefix(),
                new Friend(),
                new List(),
                new Identifier(),
                new Toggle()
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
            str.append(commands.getCommand()).append(" ");
        }
        return str;
    }

    /**
     * makes prefix a json element for configs
     * @return above
     */

    @Override
    public JsonElement toJson()
    {
        JsonObject object = new JsonObject();
        object.addProperty("prefix", prefix);
        return object;
    }

    /**
     * gets the prefix from the config json
     * @param jsonElement - the input json
     */

    @Override
    public void fromJson(JsonElement jsonElement)
    {
        setPrefix(jsonElement.getAsJsonObject().get("prefix").getAsString());
    }

    /**
     * return filename
     * @return - filename
     */

    @Override
    public String getFileName()
    {
        return "command_prefix.json";
    }
}
