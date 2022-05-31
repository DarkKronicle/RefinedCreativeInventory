package io.github.darkkronicle.refinedcreativeinventory.gui.components;

import io.github.darkkronicle.darkkore.util.Color;
import io.github.darkkronicle.darkkore.util.PositionedRectangle;
import io.github.darkkronicle.refinedcreativeinventory.gui.InventoryScreen;
import io.github.darkkronicle.refinedcreativeinventory.gui.itemeditor.ItemEditorScreen;
import io.github.darkkronicle.refinedcreativeinventory.items.BasicInventoryItem;
import io.github.darkkronicle.refinedcreativeinventory.items.InventoryItem;
import io.github.darkkronicle.refinedcreativeinventory.items.ItemHolder;
import io.github.darkkronicle.refinedcreativeinventory.util.ItemSerializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class RefinedInventoryItemComponent extends CustomInventoryItemComponent {

    private final InventoryScreen parent;
    private final Inventory inventory;
    private final int index;
    private ItemStack previous;

    public RefinedInventoryItemComponent(InventoryScreen parent, int index) {
        super(
                parent, ItemHolder.getInstance().get(
                        MinecraftClient.getInstance().player.getInventory().getStack(index)
                ).orElse(
                        new BasicInventoryItem(MinecraftClient.getInstance().player.getInventory().getStack(index))
                )
        );
        this.parent = parent;
        this.index = index;
        inventory = MinecraftClient.getInstance().player.getInventory();
        previous = inventory.getStack(index);
        setHover();
    }

    @Override
    public @Nullable ItemStack getStack() {
        if (inventory == null) {
            return null;
        }
        if (!ItemSerializer.areEqual(inventory.getStack(index), previous)) {
            previous = inventory.getStack(index);
            item = ItemHolder.getInstance().get(previous).orElse(new BasicInventoryItem(previous)
            );
            setHover();
        }
        return inventory.getStack(index);
    }

    @Override
    public void postRender(MatrixStack matrices, PositionedRectangle renderBounds, int x, int y, int mouseX, int mouseY) {
        if (!ItemSerializer.areEqual(inventory.getStack(index), previous)) {
            previous = inventory.getStack(index);
            item = ItemHolder.getInstance().get(previous).orElse(new BasicInventoryItem(previous));
            setHover();
        }
        super.postRender(matrices, renderBounds, x, y, mouseX, mouseY);
    }

    @Override
    public boolean mouseClickedImpl(int x, int y, int mouseX, int mouseY, int button) {
        if (button == 0) {
            if (parent.getSelectedStack() != null) {
                ItemStack sel = parent.getSelectedStack();
                parent.setSelectedStack(getStack());
                parent.setSlot(sel, index);
            } else {
                if (Screen.hasShiftDown()) {
                    parent.setSlot(null, index);
                } else {
                    ItemStack stack = MinecraftClient.getInstance().player.getInventory().getStack(index);
                    if (stack != null && !stack.isEmpty()) {
                        parent.setSelectedStack(stack.copy());
                        parent.setSlot(null, index);
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
            setBackgroundColor(new Color(150, 150, 150, 150));
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
