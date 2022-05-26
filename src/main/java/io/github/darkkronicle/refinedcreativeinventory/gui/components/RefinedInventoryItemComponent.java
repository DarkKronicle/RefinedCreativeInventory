package io.github.darkkronicle.refinedcreativeinventory.gui.components;

import io.github.darkkronicle.darkkore.gui.components.impl.InventoryItemComponent;
import io.github.darkkronicle.darkkore.util.Color;
import io.github.darkkronicle.refinedcreativeinventory.gui.InventoryScreen;
import io.github.darkkronicle.refinedcreativeinventory.gui.itemeditor.ItemEditorScreen;
import io.github.darkkronicle.refinedcreativeinventory.items.BasicInventoryItem;
import io.github.darkkronicle.refinedcreativeinventory.items.InventoryItem;
import io.github.darkkronicle.refinedcreativeinventory.items.ItemHolder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;

import java.util.Optional;

public class RefinedInventoryItemComponent extends InventoryItemComponent {

    private final InventoryScreen parent;

    public RefinedInventoryItemComponent(InventoryScreen parent, int index) {
        super(MinecraftClient.getInstance().player.getInventory(), index);
        this.parent = parent;
    }

    @Override
    public boolean mouseClickedImpl(int x, int y, int mouseX, int mouseY, int button) {
        if (button == 0) {
            if (parent.getSelectedStack() != null) {
                parent.setHotbarSlot(parent.getSelectedStack(), index);
                parent.setSelectedStack(null);
            } else {
                if (Screen.hasShiftDown()) {
                    parent.setHotbarSlot(null, index);
                } else {
                    ItemStack stack = MinecraftClient.getInstance().player.getInventory().getStack(index);
                    if (stack != null && !stack.isEmpty()) {
                        parent.setSelectedStack(stack.copy());
                        parent.setHotbarSlot(null, index);
                    }
                }
            }
        } else if (button == 1) {
            ItemStack stack = getStack();
            Optional<InventoryItem> item = ItemHolder.getInstance().get(stack);
            InventoryItem invItem = item.orElseGet(() -> new BasicInventoryItem(stack));
            if (item.isEmpty()) {
                invItem.setCustom(true);
            }
            MinecraftClient.getInstance().setScreen(new ItemEditorScreen(parent, invItem));
            return true;
        }
        return true;
    }

    @Override
    public void onHoveredImpl(int x, int y, int mouseX, int mouseY, boolean hovered) {
        super.onHoveredImpl(x, y, mouseX, mouseY, hovered);
        if (hovered) {
            setBackgroundColor(new Color(150, 100, 100, 150));
            parent.setHoveredSlot(this);
        } else {
            setBackgroundColor(null);
            if (parent.getHoveredSlot() == this) {
                // In case both switch at once
                parent.setHoveredSlot(this);
            }
        }
    }
}
