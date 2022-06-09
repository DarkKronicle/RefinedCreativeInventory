package io.github.darkkronicle.refinedcreativeinventory.tabs;

import io.github.darkkronicle.darkkore.gui.components.BasicComponent;
import io.github.darkkronicle.darkkore.gui.components.Component;
import io.github.darkkronicle.darkkore.gui.components.impl.ItemComponent;
import io.github.darkkronicle.darkkore.util.Color;
import io.github.darkkronicle.darkkore.util.Dimensions;
import io.github.darkkronicle.refinedcreativeinventory.gui.InventoryScreen;
import io.github.darkkronicle.refinedcreativeinventory.hotbars.HotbarHolder;
import io.github.darkkronicle.refinedcreativeinventory.hotbars.gui.HotbarHolderComponent;
import io.github.darkkronicle.refinedcreativeinventory.items.InventoryItem;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class HotbarTab implements ItemTab {

    @Override
    public BasicComponent getIcon(InventoryScreen parent) {
        return new ItemComponent(parent, Items.BOOKSHELF)
                .setOnHoveredConsumer(button -> button.setBackgroundColor(new Color(150, 150, 150, 150)))
                .setOnHoveredStoppedConsumer(button -> button.setBackgroundColor(null)
                );
    }

    @Override
    public List<InventoryItem> getItems() {
        List<InventoryItem> items = new ArrayList<>();
        return items;
    }

    @Override
    public Integer getOrder() {
        return 500;
    }

    @Override
    public List<Component> getComponents(InventoryScreen screen, Dimensions bounds) {
        return getInventoryComponents(screen, bounds);
    }

    public static List<Component> getInventoryComponents(InventoryScreen screen, Dimensions bounds) {
        return List.of(new HotbarHolderComponent(screen, HotbarHolder.getInstance(), -1, -1));
    }

}
