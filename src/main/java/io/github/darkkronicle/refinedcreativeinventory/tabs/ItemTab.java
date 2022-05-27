package io.github.darkkronicle.refinedcreativeinventory.tabs;

import io.github.darkkronicle.darkkore.gui.components.BasicComponent;
import io.github.darkkronicle.darkkore.gui.components.Component;
import io.github.darkkronicle.darkkore.util.Dimensions;
import io.github.darkkronicle.refinedcreativeinventory.gui.InventoryScreen;
import io.github.darkkronicle.refinedcreativeinventory.items.InventoryItem;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public interface ItemTab extends Comparable<ItemTab> {

    BasicComponent getIcon(InventoryScreen parent);

    List<InventoryItem> getItems();

    Integer getOrder();

    default List<Component> getComponents(InventoryScreen screen, Dimensions bounds) {
        List<Component> items = new ArrayList<>();
        for (InventoryItem item : getItems()) {
            items.add(screen.createItemComponent(item));
        }
        return items;
    }

    @Override
    default int compareTo(@NotNull ItemTab o) {
        return o.getOrder().compareTo(o.getOrder());
    }
}
