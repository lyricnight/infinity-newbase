package club.lyric.infinity.impl.clickgui.clickgui;

import club.lyric.infinity.Infinity;
import club.lyric.infinity.api.module.Category;
import club.lyric.infinity.api.module.ModuleBase;
import club.lyric.infinity.api.util.gui.HoveringUtils;
import club.lyric.infinity.impl.clickgui.Screen;
import club.lyric.infinity.impl.clickgui.clickgui.impl.ModuleRect;

import java.util.ArrayList;
import java.util.Comparator;

public class MainComponent implements Screen {

    private ArrayList<ModuleRect> moduleRects = new ArrayList();
    private final Category category;
    private final float width = 130;
    private final float height = 24;

    public MainComponent(Category category) {
        this.category = category;
    }

    @Override
    public void initGui() {
        if (moduleRects == null) {
            moduleRects = new ArrayList<>();
            for (ModuleBase module : Infinity.MODULES.getModulesInCategory(category).stream().sorted(Comparator.comparing(ModuleBase::getName)).toList()) {
                moduleRects.add(new ModuleRect(module));
            }
        }

        if (moduleRects != null) {
            moduleRects.forEach(ModuleRect::initGui);
        }
    }

    @Override
    public void keyTyped(char typedChar, int keyCode) {
        if (moduleRects != null) {
            moduleRects.forEach(moduleRect -> moduleRect.keyTyped(typedChar, keyCode));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY) {

    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int button) {
        boolean canDrag = HoveringUtils.isHovering(category.getDrag().getX(), category.getDrag().getY(), width, height, mouseX, mouseY);
        category.getDrag().onClick(mouseX, mouseY, button, canDrag);
        moduleRects.forEach(moduleRect -> moduleRect.mouseClicked(mouseX, mouseY, button));
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state) {
        category.getDrag().onRelease(state);
        moduleRects.forEach(moduleRect -> moduleRect.mouseReleased(mouseX, mouseY, state));
    }
}
