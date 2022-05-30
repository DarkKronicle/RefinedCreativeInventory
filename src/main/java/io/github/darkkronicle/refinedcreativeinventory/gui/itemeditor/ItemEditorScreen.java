package io.github.darkkronicle.refinedcreativeinventory.gui.itemeditor;

import io.github.darkkronicle.darkkore.gui.ComponentScreen;
import io.github.darkkronicle.darkkore.gui.components.Component;
import io.github.darkkronicle.darkkore.gui.components.impl.ButtonComponent;
import io.github.darkkronicle.darkkore.gui.components.transform.ListComponent;
import io.github.darkkronicle.darkkore.gui.components.transform.PositionedComponent;
import io.github.darkkronicle.darkkore.gui.components.transform.ScrollComponent;
import io.github.darkkronicle.darkkore.util.Color;
import io.github.darkkronicle.darkkore.util.Dimensions;
import io.github.darkkronicle.darkkore.util.StringUtil;
import io.github.darkkronicle.refinedcreativeinventory.items.InventoryItem;
import io.github.darkkronicle.refinedcreativeinventory.items.ItemHolder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;

public class ItemEditorScreen extends ComponentScreen {

    private final InventoryItem item;

    public ItemEditorScreen(Screen parent, InventoryItem item) {
        this.item = item;
        setParent(parent);
    }

    @Override
    public void initImpl() {
        if (item.isCustom()) {
            if (ItemHolder.getInstance().contains(item)) {
                ButtonComponent delete = new ButtonComponent(
                        this,
                        StringUtil.translateToText("rci.itemedit.delete"),
                        new Color(100, 100, 100, 100),
                        new Color(150, 150, 150, 150),
                        (button) -> {
                            ItemHolder.getInstance().getAllItems().remove(item);
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
            } else {
                ButtonComponent save = new ButtonComponent(
                        this,
                        StringUtil.translateToText("rci.itemedit.add"),
                        new Color(100, 100, 100, 100),
                        new Color(150, 150, 150, 150),
                        (button) -> {
                            ItemHolder.getInstance().addIfNotExist(item);
                            MinecraftClient.getInstance().setScreen(new ItemEditorScreen(getParent(), item));
                        }
                );
                addComponent(new PositionedComponent(
                        this,
                        save,
                        10,
                        10,
                        -1,
                        -1
                ));
            }
        }
        Dimensions dimensions = Dimensions.getScreen();
        int width = dimensions.getWidth() - 20;
        ListComponent list = new ListComponent(this, width, -1, true);
        for (Component component : item.getOptionComponents(this, width - 2)) {
            list.addComponent(component);
        }
        addComponent(
                new PositionedComponent(this,
                        new ScrollComponent(this, list, width, dimensions.getHeight() - 50, true),
                        10, 32, -1, -1).setOutlineColor(new Color(200, 200, 200, 200))
        );
    }

}
