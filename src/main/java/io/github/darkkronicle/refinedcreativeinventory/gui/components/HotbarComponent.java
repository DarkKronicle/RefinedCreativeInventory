package io.github.darkkronicle.refinedcreativeinventory.gui.components;

import io.github.darkkronicle.darkkore.gui.components.impl.IconButtonComponent;
import io.github.darkkronicle.darkkore.gui.components.transform.ListComponent;
import io.github.darkkronicle.darkkore.util.Color;
import io.github.darkkronicle.refinedcreativeinventory.RefinedCreativeInventory;
import io.github.darkkronicle.refinedcreativeinventory.gui.InventoryScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class HotbarComponent extends ListComponent {

    /**
     * Creates a new {@link HotbarComponent} that will manage the items within the hotbar
     */
    public HotbarComponent(InventoryScreen parent) {
        super(parent, -1, -1, false);
        final InventoryScreen inv = parent;
        IconButtonComponent icon = new IconButtonComponent(
                parent,
                new Identifier(RefinedCreativeInventory.MOD_ID, "textures/gui/icon/close.png"),
                18,
                18,
                48,
                48,
                null,
                new Color(150, 150, 150, 150),
                null
        ) {
            @Override
            public boolean mouseClickedImpl(int x, int y, int mouseX, int mouseY, int button) {
                if (Screen.hasShiftDown()) {
                    for (int i = 0; i < 9; ++i) {
                        inv.setSlot(ItemStack.EMPTY, i);
                    }
                    return true;
                }
                if (inv.getSelectedStack() != null) {
                    inv.setSelectedStack(null);
                    return true;
                }
                return false;
            }
        };
        icon.setOutlineColor(InventoryScreen.getSlotOutlineColor());
        icon.setShaderColor(new Color(200, 100, 100, 255));
        icon.setLeftPadding(0);
        icon.setRightPadding(0);
        icon.setTopPadding(0);
        icon.setBottomPadding(0);
        for (int i = 0; i < 9; i++) {
            addComponent(new RefinedInventoryItemComponent(parent, i).setOutlineColor(InventoryScreen.getSlotOutlineColor()));
        }
        addComponent(icon);
    }


}
