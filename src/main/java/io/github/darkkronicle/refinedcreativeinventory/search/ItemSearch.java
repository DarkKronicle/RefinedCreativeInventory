package io.github.darkkronicle.refinedcreativeinventory.search;

import io.github.darkkronicle.refinedcreativeinventory.items.InventoryItem;

import java.util.List;

public interface ItemSearch {
    List<InventoryItem> search(List<InventoryItem> items);
}
