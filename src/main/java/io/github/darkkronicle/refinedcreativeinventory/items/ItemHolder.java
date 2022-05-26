package io.github.darkkronicle.refinedcreativeinventory.items;

import io.github.darkkronicle.refinedcreativeinventory.tabs.ItemTab;
import io.github.darkkronicle.refinedcreativeinventory.util.ItemSerializer;
import lombok.Getter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

import java.util.*;

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

    public void addIfNotExist(InventoryItem item) {
        if (!allItems.contains(item)) {
            allItems.add(item);
            Collections.sort(allItems);
        }
    }

    public boolean contains(InventoryItem item) {
        return allItems.contains(item);
    }

    public Optional<InventoryItem> get(ItemStack stack) {
        return allItems.stream().filter(item -> ItemSerializer.areEqual(item.getStack(), stack)).findFirst();
    }

    public void setItems(List<InventoryItem> items) {
        Collections.sort(items);
        this.allItems = items;
    }

    public Map<ItemGroup, List<InventoryItem>> getGroups() {
        HashMap<ItemGroup, List<InventoryItem>> stacks = new HashMap<>();
        for (InventoryItem item : ItemHolder.getInstance().getAllItems()) {
            if (item.getGroups().isEmpty()) {
                stacks.compute(null, (k, v) -> {
                    if (v == null) {
                        return new ArrayList<>(List.of(item));
                    }
                    v.add(item);
                    return v;
                });
                continue;
            }
            for (ItemGroup group : item.getGroups()) {
                stacks.compute(group, (k, v) -> {
                    if (v == null) {
                        return new ArrayList<>(List.of(item));
                    }
                    v.add(item);
                    return v;
                });
            }
        }
        return stacks;
    }

    public void addGroup(ItemStack item, ItemGroup group) {
        getOrCreate(item).addGroup(group);
    }

}
