# Infinity

Infinity is a 1.20.4 client

# Docs

## `Modules`

Modules allow certain classes to be used as features inside of the client. 

Example module:
``` java
public class Example extends ModuleBase
{

    public ModeSetting mode = new ModeSetting("Mode", this, "Mode1", "Mode1", "Mode2");
    public BooleanSetting boolean = new BooleanSetting("Boolean", this, true);
    public NumberSetting number = new NumberSetting("Number", this, 0.0f, 0.0f, 100.0f, 0.1);
    public ColorSetting color = new ColorSetting("Color", this, new JColor(new Color(104, 71, 141)), true);
    
    public Example()
    {
        super("Example", "Example class for a module.", Category.DEV);
        setBind(GLFW.GLFW_KEY_UNKNOWN);
    }

    @Override
    public void onEnable()
    {
      // Call on module enabling.
      boolean.value();
      number.getIValue();
      number.getFValue();
      number.getValue();
      color.getColor().getRGB();
      mode.is("Mode1");
    }

    @Override
    public void onDisable()
    {
        // Call on module disabling.
    }

    @EventHandler
    public void onMove(EntityMovementEvent event) 
    {
      // Call to a method that uses EventHandler
    }
}
```
