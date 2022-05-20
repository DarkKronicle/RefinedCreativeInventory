package io.github.darkkronicle.refinedcreativeinventory.items;

import lombok.Getter;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class TagInventoryItem implements InventoryItem {

    @Getter private final ItemStack stack;
    @Getter private final List<String> tags;

    public TagInventoryItem(ItemStack stack) {
        this(stack, new ArrayList<>());
    }

    public TagInventoryItem(ItemStack stack, List<String> tags) {
        this.stack = stack;
        this.tags = tags;
    }

}
