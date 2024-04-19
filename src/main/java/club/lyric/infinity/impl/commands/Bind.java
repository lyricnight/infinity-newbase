package club.lyric.infinity.impl.commands;

import club.lyric.infinity.api.command.Command;
import club.lyric.infinity.api.command.CommandState;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.manager.Managers;
import org.lwjgl.glfw.GLFW;

public class Bind extends Command {
    public Bind()
    {
        super("bind");
    }

    @Override
    public String theCommand()
    {
        return "bind <module> <key>";
    }

    @Override
    public void onCommand(String[] args) {
        if (args.length > 3 || args.length < 1)
        {
            state(CommandState.ERROR);
            return;
        }

        ModuleBase module = Managers.MODULES.getModuleByName(args[1]);

        if (module == null)
        {
            state(CommandState.ERROR);
            return;
        }

        int bind = getKey(args[2]);

        module.setBind(bind);
    }

    public int getKey(String bind) {
        for (int key = 39; key < 97; key++)
        {
            if (bind.equalsIgnoreCase(GLFW.glfwGetKeyName(key, GLFW.glfwGetKeyScancode(key))))
            {
                return key;
            }
        }
        return GLFW.GLFW_KEY_UNKNOWN;
    }
}
