package io.github.darkkronicle.refinedcreativeinventory.items;

import io.github.darkkronicle.darkkore.gui.components.Component;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface InventoryItem extends Comparable<InventoryItem> {

    ItemStack getStack();

    List<ItemFlag> getFlags();

    List<Identifier> getTags();

    List<ItemGroup> getGroups();

    List<Component> getOptionComponents(Screen parent, int width);

    boolean isCustom();

    void setCustom(boolean custom);

    void addGroup(ItemGroup group);

    @Override
    default int compareTo(@NotNull InventoryItem o) {
        return getStack().getName().getString().compareTo(o.getStack().getName().getString());
    }

    void addFlag(ItemFlag flag);

    default void addFlag(String flag) {
        addFlag(ItemFlag.fromString(flag));
    }

}
