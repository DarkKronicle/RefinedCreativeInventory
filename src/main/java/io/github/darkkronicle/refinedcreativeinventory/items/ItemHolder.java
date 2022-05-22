package io.github.darkkronicle.refinedcreativeinventory.items;

import io.github.darkkronicle.refinedcreativeinventory.tabs.ItemTab;
import io.github.darkkronicle.refinedcreativeinventory.util.ItemSerializer;
import lombok.Getter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ItemHolder {

    private static final ItemHolder INSTANCE = new ItemHolder();

    public static ItemHolder getInstance() {
        return INSTANCE;
    }

    @Getter private List<InventoryItem> allItems = new ArrayList<>();

    @Getter private List<ItemTab> tabs = new ArrayList<>();

    public void setDefaults() {
        for (Item item : Registry.ITEM) {
            if (item.getGroup() != null) {
                DefaultedList<ItemStack> stackList = DefaultedList.of();
                item.appendStacks(item.getGroup(), stackList);
                for (ItemStack stack : stackList){
                    getOrCreate(stack);
                }
            } else {
                getOrCreate(new ItemStack(item));
            }

        }
        populateGroups();
        Collections.sort(allItems);
    }

    public void populateGroups() {
        for (ItemGroup group : ItemGroup.GROUPS) {
            DefaultedList<ItemStack> items = DefaultedList.of();
            if (group.equals(ItemGroup.HOTBAR) || group.equals(ItemGroup.SEARCH)) {
                continue;
            }
            group.appendStacks(items);
            for (ItemStack item : items) {
                ItemHolder.getInstance().addGroup(item, group);
            }
        }
    }

    public InventoryItem getOrCreate(ItemStack stack) {
        return allItems.stream().filter(item -> ItemSerializer.areEqual(item.getStack(), stack)).findFirst().orElseGet(() -> {
            BasicInventoryItem item = new BasicInventoryItem(stack);
            allItems.add(item);
            return item;
        });
    }

    public void setItems(List<InventoryItem> items) {
        Collections.sort(items);
        this.allItems = items;
    }

    public void addGroup(ItemStack item, ItemGroup group) {
        getOrCreate(item).addGroup(group);
    }

}
