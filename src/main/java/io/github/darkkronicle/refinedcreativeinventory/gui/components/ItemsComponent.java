package io.github.darkkronicle.refinedcreativeinventory.gui.components;

import io.github.darkkronicle.darkkore.gui.components.transform.ListComponent;

public class ItemsComponent extends ListComponent {

    /**
     * Creates a new component for managing large amounts of {@link io.github.darkkronicle.refinedcreativeinventory.items.InventoryItem}
     *
     * @param width    Width
     */
    public ItemsComponent(int width) {
        super(width, -1, false);
    }
}
