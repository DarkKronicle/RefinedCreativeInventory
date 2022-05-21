package io.github.darkkronicle.refinedcreativeinventory.items;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
            group.appendStacks(items);
            for (ItemStack item : items) {
                groups.compute(Registry.ITEM.getId(item.getItem()), (k, v) -> {
                    if (v == null) {
                        return List.of(group);
                    } else {
                        v.add(group);
                        return v;
                    }
                });
            }
        }
    }

    public List<ItemGroup> getGroups(Item item) {
        return groups.getOrDefault(Registry.ITEM.getId(item), null);
    }

}
