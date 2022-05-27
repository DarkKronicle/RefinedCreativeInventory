package io.github.darkkronicle.refinedcreativeinventory.tabs;

import io.github.darkkronicle.darkkore.gui.components.BasicComponent;
import io.github.darkkronicle.darkkore.gui.components.Component;
import io.github.darkkronicle.darkkore.gui.components.impl.ItemComponent;
import io.github.darkkronicle.darkkore.gui.components.impl.TextComponent;
import io.github.darkkronicle.darkkore.gui.components.transform.ListComponent;
import io.github.darkkronicle.darkkore.util.Color;
import io.github.darkkronicle.darkkore.util.Dimensions;
import io.github.darkkronicle.darkkore.util.StringUtil;
import io.github.darkkronicle.refinedcreativeinventory.gui.InventoryScreen;
import io.github.darkkronicle.refinedcreativeinventory.items.InventoryItem;
import io.github.darkkronicle.refinedcreativeinventory.items.ItemHolder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.text.Text;

import java.util.*;

public class AllTab implements ItemTab {

    @Override
    public BasicComponent getIcon(InventoryScreen parent) {
        ItemComponent icon = new ItemComponent(new ItemStack(Items.NETHER_STAR));
        icon.setOnHoveredConsumer(button -> button.setBackgroundColor(new Color(200, 200, 200, 200)));
        icon.setOnHoveredStoppedConsumer(button -> button.setBackgroundColor(null));
        return icon;
    }

    @Override
    public List<InventoryItem> getItems() {
        return ItemHolder.getInstance().getAllItems();
    }

    @Override
    public List<Component> getComponents(InventoryScreen screen, Dimensions bounds) {
        List<Component> components = new ArrayList<>();
        Map<ItemGroup, List<InventoryItem>> groups = ItemHolder.getInstance().getGroups();
        List<ItemGroup> keys = groups.keySet().stream().sorted((o1, o2) -> {
            if (o1 == null) {
                return 1;
            }
            if (o2 == null) {
                return -1;
            }
            return o1.getName().compareTo(o2.getName());
        }).toList();
        for (ItemGroup group : keys) {
            Text name = group == null ? StringUtil.translateToText("rci.group.other") : group.getDisplayName();
            components.add(new TextComponent(name));
            ListComponent list = new ListComponent(bounds.getWidth(), -1, false);
            for (InventoryItem c : groups.get(group)) {
                list.addComponent(screen.createItemComponent(c));
            }
            components.add(list);
        }
        return components;
    }

}
