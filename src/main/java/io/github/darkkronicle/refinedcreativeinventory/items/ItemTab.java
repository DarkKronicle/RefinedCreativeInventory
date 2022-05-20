package io.github.darkkronicle.refinedcreativeinventory.items;

import io.github.darkkronicle.darkkore.gui.components.Component;

import java.util.List;

public interface ItemTab {

    Component getIcon();

    List<InventoryItem> getItems();

}
