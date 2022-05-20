package io.github.darkkronicle.refinedcreativeinventory.items;

import com.mojang.datafixers.types.templates.Named;
import com.mojang.datafixers.util.Pair;
import lombok.Getter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tag.ItemTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryEntryList;

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
    public List<String> getTags() {

        return clientTags;
    }
}
