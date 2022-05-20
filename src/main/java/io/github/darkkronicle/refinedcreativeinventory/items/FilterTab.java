package io.github.darkkronicle.refinedcreativeinventory.items;

import io.github.darkkronicle.darkkore.gui.components.Component;

import java.util.List;

public class FilterTab implements ItemTab {

    private Component icon;

    public FilterTab(Component icon) {
        this.icon = icon;
    }

    @Override
    public Component getIcon() {
        return icon;
    }

    @Override
    public List<InventoryItem> getItems() {
        return null;
    }

}
