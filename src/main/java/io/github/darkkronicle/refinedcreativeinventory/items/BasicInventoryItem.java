package io.github.darkkronicle.refinedcreativeinventory.items;

import io.github.darkkronicle.darkkore.gui.components.Component;
import io.github.darkkronicle.darkkore.util.Color;
import io.github.darkkronicle.refinedcreativeinventory.gui.itemeditor.FlagOptionComponent;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class BasicInventoryItem implements InventoryItem {

    @Getter private final ItemStack stack;
    private List<String> flags;
    private List<ItemGroup> groups = new ArrayList<>();

    @Setter @Getter private boolean custom = false;

    public BasicInventoryItem(ItemStack stack) {
        this(stack, new ArrayList<>());
    }

    public BasicInventoryItem(ItemStack stack, List<String> flags) {
        this.stack = stack;
        this.flags = flags;
    }

    @Override
    public List<String> getFlags() {
        return flags;
    }

    @Override
    public List<Identifier> getTags() {
        return TagHolder.getInstance().getTags(getStack().getItem());
    }

    @Override
    public List<ItemGroup> getGroups() {
        return groups;
    }

    @Override
    public List<Component> getOptionComponents(Screen parent, int width) {
        List<Component> components = new ArrayList<>();
        FlagOptionComponent flags = FlagOptionComponent.of(parent, this, width);
        components.add(flags);
        return components;
    }

    public void setFlags(List<String> flags) {
        this.flags = flags;
    }

    @Override
    public void addGroup(ItemGroup group) {
        if (!groups.contains(group)) {
            groups.add(group);
        }
    }

    @Override
    public void addFlag(String flag) {
        this.flags.add(flag);
    }
}
