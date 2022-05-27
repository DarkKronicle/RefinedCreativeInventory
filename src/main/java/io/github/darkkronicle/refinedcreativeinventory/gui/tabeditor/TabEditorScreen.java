package io.github.darkkronicle.refinedcreativeinventory.gui.tabeditor;

import io.github.darkkronicle.darkkore.gui.ComponentScreen;
import io.github.darkkronicle.darkkore.gui.components.Component;
import io.github.darkkronicle.darkkore.gui.components.transform.ListComponent;
import io.github.darkkronicle.darkkore.gui.components.transform.PositionedComponent;
import io.github.darkkronicle.darkkore.gui.components.transform.ScrollComponent;
import io.github.darkkronicle.darkkore.util.Color;
import io.github.darkkronicle.darkkore.util.Dimensions;
import io.github.darkkronicle.refinedcreativeinventory.tabs.CustomTab;
import net.minecraft.client.gui.screen.Screen;

public class TabEditorScreen extends ComponentScreen {

    private final CustomTab tab;

    public TabEditorScreen(Screen parent, CustomTab tab) {
        setParent(parent);
        this.tab = tab;
    }

    @Override
    public void initImpl() {
        Dimensions dimensions = Dimensions.getScreen();
        ListComponent list = new ListComponent(width, -1, true);
        int width = dimensions.getWidth() - 20;
        for (Component component : tab.getOptionComponents(width - 2)) {
            list.addComponent(component);
        }
        addComponent(
                new PositionedComponent(
                        new ScrollComponent(list, width, dimensions.getHeight() - 50, true),
                        10, 32, -1, -1).setOutlineColor(new Color(200, 200, 200, 200))
        );
    }

}
