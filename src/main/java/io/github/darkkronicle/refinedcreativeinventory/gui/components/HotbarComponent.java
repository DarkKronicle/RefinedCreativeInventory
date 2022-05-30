package io.github.darkkronicle.refinedcreativeinventory.gui.components;

import io.github.darkkronicle.darkkore.gui.components.transform.ListComponent;
import io.github.darkkronicle.refinedcreativeinventory.gui.InventoryScreen;

public class HotbarComponent extends ListComponent {

    /**
     * Creates a new {@link HotbarComponent} that will manage the items within the hotbar
     */
    public HotbarComponent(InventoryScreen parent) {
        super(parent, -1, -1, false);
        for (int i = 0; i < 9; i++) {
            addComponent(new RefinedInventoryItemComponent(parent, i));
        }
    }


}
