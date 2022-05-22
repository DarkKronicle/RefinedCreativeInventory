package io.github.darkkronicle.refinedcreativeinventory.tabs;

import io.github.darkkronicle.darkkore.gui.components.BasicComponent;
import io.github.darkkronicle.darkkore.gui.components.Component;
import io.github.darkkronicle.darkkore.util.Dimensions;
import io.github.darkkronicle.refinedcreativeinventory.gui.InventoryScreen;
import io.github.darkkronicle.refinedcreativeinventory.items.InventoryItem;

import java.util.ArrayList;
import java.util.List;

public interface ItemTab {

    BasicComponent getIcon();

    List<InventoryItem> getItems();

    default List<Component> getComponents(InventoryScreen screen, Dimensions bounds) {
        List<Component> items = new ArrayList<>();
        for (InventoryItem item : getItems()) {
            items.add(screen.createItemComponent(item));
        }
        return items;
    }

}
