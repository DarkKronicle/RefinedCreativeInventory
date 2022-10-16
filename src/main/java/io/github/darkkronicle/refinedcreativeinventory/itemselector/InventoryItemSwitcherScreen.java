package io.github.darkkronicle.refinedcreativeinventory.itemselector;

import io.github.darkkronicle.refinedcreativeinventory.gui.InventoryScreen;
import lombok.Getter;
import net.minecraft.client.MinecraftClient;

public class InventoryItemSwitcherScreen extends ItemSwitcherScreen {

    @Getter
    private final int inventoryIndex;

    public InventoryItemSwitcherScreen(int inventoryIndex) {
        super(MinecraftClient.getInstance().player.getInventory().getStack(inventoryIndex), item -> {
            if (item == null) {
                return;
            }
            InventoryScreen.setSlot(MinecraftClient.getInstance(), item.copy(), inventoryIndex);
        });
        this.inventoryIndex = inventoryIndex;
    }

}
