package io.github.darkkronicle.refinedcreativeinventory.tabs.tabeditor;

import io.github.darkkronicle.darkkore.gui.ComponentScreen;
import io.github.darkkronicle.darkkore.gui.components.Component;
import io.github.darkkronicle.darkkore.gui.components.impl.ButtonComponent;
import io.github.darkkronicle.darkkore.gui.components.transform.ListComponent;
import io.github.darkkronicle.darkkore.gui.components.transform.PositionedComponent;
import io.github.darkkronicle.darkkore.gui.components.transform.ScrollComponent;
import io.github.darkkronicle.darkkore.util.Color;
import io.github.darkkronicle.darkkore.util.Dimensions;
import io.github.darkkronicle.darkkore.util.StringUtil;
import io.github.darkkronicle.refinedcreativeinventory.tabs.CustomTab;
import io.github.darkkronicle.refinedcreativeinventory.tabs.TabHolder;
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
        ButtonComponent delete = new ButtonComponent(
                this,
                StringUtil.translateToText("rci.tabedit.delete"),
                new Color(100, 100, 100, 100),
                new Color(150, 150, 150, 150),
                (button) -> {
                    TabHolder.getInstance().getTabs().remove(tab);
                    close();
                }
        );
        addComponent(new PositionedComponent(
                this,
                delete,
                10,
                10,
                -1,
                -1
        ));
        ListComponent list = new ListComponent(this, width, -1, true);
        int width = dimensions.getWidth() - 20;
        for (Component component : tab.getOptionComponents(this, width - 2)) {
            list.addComponent(component);
        }
        addComponent(
                new PositionedComponent(this,
                        new ScrollComponent(this, list, width, dimensions.getHeight() - 50, true),
                        10, 32).setOutlineColor(new Color(200, 200, 200, 200))
        );
    }

}
