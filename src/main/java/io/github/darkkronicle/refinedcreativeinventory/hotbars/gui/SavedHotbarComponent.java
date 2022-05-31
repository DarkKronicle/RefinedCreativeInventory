package io.github.darkkronicle.refinedcreativeinventory.hotbars.gui;

import io.github.darkkronicle.darkkore.gui.components.transform.ListComponent;
import io.github.darkkronicle.darkkore.util.Color;
import io.github.darkkronicle.refinedcreativeinventory.gui.InventoryScreen;
import io.github.darkkronicle.refinedcreativeinventory.gui.components.RefinedItemComponent;
import io.github.darkkronicle.refinedcreativeinventory.gui.itemeditor.ItemEditorScreen;
import io.github.darkkronicle.refinedcreativeinventory.hotbars.SavedHotbar;
import io.github.darkkronicle.refinedcreativeinventory.items.BasicInventoryItem;
import io.github.darkkronicle.refinedcreativeinventory.items.InventoryItem;
import io.github.darkkronicle.refinedcreativeinventory.items.ItemHolder;
import lombok.Getter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.Optional;

public class SavedHotbarComponent extends ListComponent {

    private final HotbarProfileComponent profile;
    @Getter private final SavedHotbar hotbar;
    private final InventoryScreen inventory;

    public SavedHotbarComponent(InventoryScreen parent, HotbarProfileComponent profile, SavedHotbar hotbar) {
        super(parent, -1, -1, false);
        this.profile = profile;
        this.hotbar = hotbar;
        this.inventory = parent;
        updateItems();
    }

    public void updateItems() {
        clear();
        for (int i = 0; i < 9; i++) {
            InventoryItem item = hotbar.get(i);
            if (item == null) {
                item = ItemHolder.getInstance().getOrCreate(new ItemStack(Items.AIR));
            }
            final int index = i;
            RefinedItemComponent component = new RefinedItemComponent(inventory, item) {
                @Override
                public boolean mouseClickedImpl(int x, int y, int mouseX, int mouseY, int button) {
                    if (button == 0) {
                        if (inventory.getSelectedStack() != null) {
                            ItemStack sel = inventory.getSelectedStack();
                            inventory.setSelectedStack(getStack());
                            hotbar.set(index, ItemHolder.getInstance().get(sel).orElseGet(() -> new BasicInventoryItem(sel)));
                            updateItems();
                        } else {
                            inventory.setSelectedStack(getStack());
                        }
                    } else if (button == 1) {
                        ItemStack stack = getStack();
                        Optional<InventoryItem> item = ItemHolder.getInstance().get(stack);
                        InventoryItem invItem = item.orElseGet(() -> new BasicInventoryItem(stack));
                        if (item.isEmpty()) {
                            invItem.setCustom(true);
                        }
                        MinecraftClient.getInstance().setScreen(new ItemEditorScreen(inventory, invItem));
                        return true;
                    }
                    return true;
                }
            };
            component.setOutlineColor(new Color(0, 0, 0, 255));
            addComponent(component);
        }
    }

}