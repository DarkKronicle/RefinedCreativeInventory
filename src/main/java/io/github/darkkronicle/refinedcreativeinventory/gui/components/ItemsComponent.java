package io.github.darkkronicle.refinedcreativeinventory.gui.components;

import io.github.darkkronicle.darkkore.gui.components.Component;
import io.github.darkkronicle.darkkore.gui.components.transform.ListComponent;
import io.github.darkkronicle.darkkore.util.Dimensions;
import io.github.darkkronicle.refinedcreativeinventory.gui.InventoryScreen;
import io.github.darkkronicle.refinedcreativeinventory.items.InventoryItem;
import io.github.darkkronicle.refinedcreativeinventory.items.ItemHolder;
import io.github.darkkronicle.refinedcreativeinventory.search.ItemSearch;
import io.github.darkkronicle.refinedcreativeinventory.tabs.ItemTab;

import java.util.List;

public class ItemsComponent extends ListComponent {

    private final InventoryScreen parent;
    private final Dimensions bounds;

    /**
     * Creates a new component for managing large amounts of {@link io.github.darkkronicle.refinedcreativeinventory.items.InventoryItem}
     *
     * @param width    Width
     */
    public ItemsComponent(InventoryScreen parent, Dimensions bounds, int width) {
        super(width, -1, false);
        this.parent = parent;
        this.bounds = bounds;
    }

    public void setItems(List<InventoryItem> list) {
        clear();
        for (InventoryItem item : list) {
            addComponent(parent.createItemComponent(item));
        }
    }

    public void setItems(ItemTab tab) {
        clear();
        parent.clearTabOutline();
        for (Component c : tab.getComponents(parent, bounds)) {
            addComponent(c);
        }
    }

    public void setItems(String query) {
        if (query == null || query.isEmpty()) {
            setItems(InventoryScreen.getTab());
            return;
        }
        parent.clearTabOutline();
        setItems(ItemSearch.fromQuery(query).search(ItemHolder.getInstance().getAllItems()));
    }

}
