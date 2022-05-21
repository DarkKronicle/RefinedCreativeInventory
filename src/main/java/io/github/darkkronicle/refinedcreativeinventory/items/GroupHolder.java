package io.github.darkkronicle.refinedcreativeinventory.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

import java.util.*;

public class GroupHolder {

    private final static GroupHolder INSTANCE = new GroupHolder();

    private Map<Identifier, List<ItemGroup>> groups = new HashMap<>();

    public static GroupHolder getInstance() {
        return INSTANCE;
    }

    private GroupHolder() {}

    public void populateGroups() {
        groups.clear();
        for (ItemGroup group : ItemGroup.GROUPS) {
            DefaultedList<ItemStack> items = DefaultedList.of();
            if (group.equals(ItemGroup.HOTBAR)) {
                continue;
            }
            if (group.equals(ItemGroup.SEARCH)) {
                continue;
            }
            group.appendStacks(items);
            for (ItemStack item : items) {
                groups.compute(Registry.ITEM.getId(item.getItem()), (k, v) -> {
                    if (v == null) {
                        return new ArrayList<>(List.of(group));
                    } else {
                        if (!v.contains(group)) {
                            v.add(group);
                        }
                        return v;
                    }
                });
            }
        }
    }

    public List<ItemGroup> getGroups(Item item) {
        return Optional.ofNullable(groups.get(Registry.ITEM.getId(item))).orElseGet(ArrayList::new);
    }

}
