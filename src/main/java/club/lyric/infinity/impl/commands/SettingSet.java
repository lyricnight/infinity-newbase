package club.lyric.infinity.impl.commands;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.command.Command;
import club.lyric.infinity.api.command.CommandState;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.setting.Setting;
import club.lyric.infinity.api.setting.settings.util.Bind;
import club.lyric.infinity.api.setting.settings.util.EnumConverter;
import club.lyric.infinity.api.util.client.chat.ChatUtils;
import club.lyric.infinity.manager.Managers;
import net.minecraft.util.Formatting;

/**
 * @author lyric
 * command for changing setting values.
 */
public class SettingSet extends Command {

    public SettingSet()
    {
        super("setting");
    }

    @Override
    public String theCommand()
    {
        return "setting <module> <setting name> <value to set to>";
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void onCommand(String[] args)
    {
        String module;
        String setting;
        String value;

        if (args.length < 1)
        {
            state(CommandState.ERROR);
            return;
        }
        if (args.length > 3)
        {
            module = args[1];
            setting = args[2];
            value = args[3];

            if (module == null || setting == null || value == null) return;

            boolean isModule = false;
            boolean isSetting = false;

            for (ModuleBase moduleBase : Managers.MODULES.getModules())
            {
                if (module.equalsIgnoreCase(moduleBase.getName()))
                {
                    isModule = true;
                    Infinity.LOGGER.info("Command found an appropriate module.");
                }
            }

            if (!isModule)
            {
                ChatUtils.sendMessagePrivate(Formatting.RED + "The module entered did not match any of Infinity's modules. You entered:" + module);
                return;
            }

            for (Setting<?> settings : Managers.MODULES.getModuleByName(module).getSettings())
            {
                if (setting.equalsIgnoreCase(settings.getName()))
                {
                    isSetting = true;
                    Infinity.LOGGER.info("Command found an appropriate setting from module.");
                }
            }

            if (!isSetting)
            {
                ChatUtils.sendMessagePrivate(Formatting.RED + "The setting entered did not match any setting. You entered:" + setting);
                return;
            }
            //TODO: make this null-safe / check if the value is one of these before doing it
            switch (Managers.MODULES.getSettingFromModule(module, setting).getType())
            {
                case "Boolean" -> Managers.MODULES.getSettingFromModule(module, setting).setValue(Boolean.parseBoolean(value));
                case "Double" -> Managers.MODULES.getSettingFromModule(module, setting).setValue(Double.parseDouble(value));
                case "Float" -> Managers.MODULES.getSettingFromModule(module, setting).setValue(Float.parseFloat(value));
                case "Integer" -> Managers.MODULES.getSettingFromModule(module, setting).setValue(Integer.parseInt(value));
                case "String" -> Managers.MODULES.getSettingFromModule(module, setting).setValue(value);
                case "Bind" -> Managers.MODULES.getSettingFromModule(module, setting).setValue(new Bind(Integer.parseInt(value)));
                case "Enum" -> {
                    try {
                        EnumConverter converter = new EnumConverter(((Enum) Managers.MODULES.getSettingFromModule(module, setting).getValue()).getClass());
                        Enum valueEnum = converter.doBackward(value);
                        Managers.MODULES.getSettingFromModule(module, setting).setValue((valueEnum == null) ? Managers.MODULES.getSettingFromModule(module, setting).getDefaultValue() : value);
                    } catch (Exception exception) {
                        Infinity.LOGGER.atError();
                    }
                }
            }
            state(CommandState.PERFORMED);
            Infinity.LOGGER.info("Tried to set a value to the setting.");
        }
        else
        {
            state(CommandState.ERROR);
        }
    }
}
