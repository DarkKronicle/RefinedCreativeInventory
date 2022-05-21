package io.github.darkkronicle.refinedcreativeinventory.items;

import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface InventoryItem extends Comparable<InventoryItem> {

    ItemStack getStack();

    List<String> getFlags();

    List<Identifier> getTags();

    List<ItemGroup> getGroups();

    @Override
    default int compareTo(@NotNull InventoryItem o) {
        return getStack().getName().getString().compareTo(o.getStack().getName().getString());
    }
}
