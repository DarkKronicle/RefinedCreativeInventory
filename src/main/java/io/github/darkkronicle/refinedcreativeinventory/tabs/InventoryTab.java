package io.github.darkkronicle.refinedcreativeinventory.tabs;

import io.github.darkkronicle.darkkore.gui.components.BasicComponent;
import io.github.darkkronicle.darkkore.gui.components.Component;
import io.github.darkkronicle.darkkore.gui.components.impl.ItemComponent;
import io.github.darkkronicle.darkkore.gui.components.transform.ListComponent;
import io.github.darkkronicle.darkkore.util.Color;
import io.github.darkkronicle.darkkore.util.Dimensions;
import io.github.darkkronicle.refinedcreativeinventory.gui.InventoryScreen;
import io.github.darkkronicle.refinedcreativeinventory.gui.components.RefinedInventoryItemComponent;
import io.github.darkkronicle.refinedcreativeinventory.items.BasicInventoryItem;
import io.github.darkkronicle.refinedcreativeinventory.items.InventoryItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class InventoryTab implements ItemTab {

    @Override
    public BasicComponent getIcon(InventoryScreen parent) {
        return new ItemComponent(parent, Items.CHEST);
    }

    @Override
    public List<InventoryItem> getItems() {
        List<InventoryItem> items = new ArrayList<>();
        for (int row = 3; row > 0; row--) {
            for (int column = 0; column < 9; column++) {
                items.add(new BasicInventoryItem(MinecraftClient.getInstance().player.getInventory().getStack(row * 9 + column)));
            }
        }
        return items;
    }

    @Override
    public Integer getOrder() {
        return 500;
    }

    @Override
    public List<Component> getComponents(InventoryScreen screen, Dimensions bounds) {
        ListComponent rows = new ListComponent(screen, -1, -1, true);
        rows.setComponentYPad(0);
        for (int row = 1; row <= 3; row++) {
            ListComponent columns = new ListComponent(screen, -1, -1, false);
            for (int column = 0; column < 9; column++) {
                columns.addComponent(new RefinedInventoryItemComponent(screen, row * 9 + column).setOutlineColor(new Color(0, 0, 0, 255)));
            }
            rows.addComponent(columns);
        }

        return List.of(rows);
    }

    public static List<Component> getInventoryComponents(InventoryScreen screen, Dimensions bounds) {
        ListComponent rows = new ListComponent(screen, -1, -1, true);
        rows.setComponentYPad(0);
        for (int row = 1; row <= 3; row++) {
            ListComponent columns = new ListComponent(screen, -1, -1, false);
            for (int column = 0; column < 9; column++) {
                columns.addComponent(new RefinedInventoryItemComponent(screen, row * 9 + column).setOutlineColor(new Color(0, 0, 0, 255)));
            }
            rows.addComponent(columns);
        }

        return List.of(rows);
    }
}
