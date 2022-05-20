package io.github.darkkronicle.refinedcreativeinventory.items;

import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface InventoryItem extends Comparable<InventoryItem> {

    ItemStack getStack();

    List<String> getTags();

    @Override
    default int compareTo(@NotNull InventoryItem o) {
        return getStack().getName().getString().compareTo(o.getStack().getName().getString());
    }
}
