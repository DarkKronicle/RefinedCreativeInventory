package io.github.darkkronicle.refinedcreativeinventory.gui.components;

import io.github.darkkronicle.darkkore.util.Color;
import io.github.darkkronicle.refinedcreativeinventory.gui.InventoryScreen;
import io.github.darkkronicle.refinedcreativeinventory.items.InventoryItem;
import net.minecraft.client.gui.screen.Screen;

public class RefinedItemComponent extends CustomInventoryItemComponent {

    private final InventoryScreen parent;

    public RefinedItemComponent(InventoryScreen parent, InventoryItem stack) {
        super(stack);
        this.parent = parent;
    }

    @Override
    public boolean mouseClickedImpl(int x, int y, int mouseX, int mouseY, int button) {
        if (Screen.hasShiftDown() && button == 1) {
            parent.setFirstHotbarOpen(this.getStack());
            return true;
        }
        if (button == 0) {
            parent.setSelectedStack(this.getStack());
            if (Screen.hasShiftDown()) {
                parent.getSelectedStack().setCount(parent.getSelectedStack().getMaxCount());
            }
        }
        return true;
    }

    @Override
    public void onHoveredImpl(int x, int y, int mouseX, int mouseY, boolean hovered) {
        if (hovered) {
            setBackgroundColor(new Color(150, 150, 150, 150));
            parent.setHoveredSlot(this);
        } else {
            setBackgroundColor(null);
            if (parent.getHoveredSlot() == this) {
                parent.setHoveredSlot(null);
            }
        }
    }
}
