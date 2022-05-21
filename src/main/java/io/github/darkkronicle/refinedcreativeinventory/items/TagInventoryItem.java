package io.github.darkkronicle.refinedcreativeinventory.items;

import lombok.Getter;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class TagInventoryItem implements InventoryItem {

    @Getter private final ItemStack stack;
    @Getter private final List<String> clientTags;

    public TagInventoryItem(ItemStack stack) {
        this(stack, new ArrayList<>());
    }

    public TagInventoryItem(ItemStack stack, List<String> clientTags) {
        this.stack = stack;
        this.clientTags = clientTags;
    }

    @Override
    public List<String> getFlags() {
        return clientTags;
    }

    @Override
    public List<Identifier> getTags() {
        List<Identifier> tags = TagHolder.getInstance().getTags(stack.getItem());
        if (tags == null) {
            return new ArrayList<>();
        }
        return tags;
    }

    @Override
    public List<ItemGroup> getGroups() {
        List<ItemGroup> groups = GroupHolder.getInstance().getGroups(getStack().getItem());
        if (groups == null) {
            return new ArrayList<>();
        }
        return groups;
    }
}
