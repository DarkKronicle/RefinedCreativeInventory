package io.github.darkkronicle.refinedcreativeinventory.items;

import io.github.darkkronicle.refinedcreativeinventory.search.ItemSearch;
import lombok.Getter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ItemHolder {

    private static final ItemHolder INSTANCE = new ItemHolder();

    public static ItemHolder getInstance() {
        return INSTANCE;
    }

    @Getter private List<InventoryItem> allItems = new ArrayList<>();

    @Getter private List<ItemTab> tabs = new ArrayList<>();

    public void setDefaults() {
        allItems = new ArrayList<>();
        tabs = new ArrayList<>();
        for (Item item : Registry.ITEM) {
            List<String> tags = new ArrayList<>();
            if (item.getGroup() != null) {
                tags.add(item.getGroup().getName());
            } else {
                tags.add("no_group");
            }
            allItems.add(new TagInventoryItem(new ItemStack(item), tags));
        }
        Collections.sort(allItems);
    }

    public List<InventoryItem> search(String query) {
        ItemSearch search;
        if (!query.contains(":")) {
            search = new ItemSearch(Map.of(ItemSearch.SearchFilter.NAME, query));
        } else {

            search = new ItemSearch();
        }
        return search.search(allItems);
    }

    public void setItems(List<InventoryItem> items) {
        Collections.sort(items);
        this.allItems = items;
    }
}
