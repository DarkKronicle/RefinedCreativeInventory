package io.github.darkkronicle.refinedcreativeinventory.itemselector;

import io.github.darkkronicle.darkkore.gui.components.Component;
import io.github.darkkronicle.darkkore.util.Color;
import io.github.darkkronicle.refinedcreativeinventory.gui.InventoryScreen;
import io.github.darkkronicle.refinedcreativeinventory.gui.components.CustomInventoryItemComponent;
import io.github.darkkronicle.refinedcreativeinventory.items.InventoryItem;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class ItemSwitcherScreen extends RadialScreen {

    private final Consumer<ItemStack> callback;

    public ItemSwitcherScreen(ItemStack startingItem, Consumer<ItemStack> callback) {
        super();
        setBackgroundColor(new Color(0, 0, 0, 0));
        this.callback = callback;
        for (List<InventoryItem> items : ItemSwitcherHandler.getInstance().getStacks(startingItem)) {
            List<Component> radialItems = new ArrayList<>();
            for (InventoryItem item : items) {
                radialItems.add(createComponent(item));
            }
            getRadials().add(new Radial(radialItems, radialItems.size()));
        }
    }

    public void forceClose() {
        mouseClicked(lastMouseX, lastMouseY, 0);
        close();
    }

    @Override
    public void initImpl() {
        ItemSwitcherHandler.getInstance().setCurrentScreen(this);
        super.initImpl();
    }

    protected Component createComponent(InventoryItem item) {
        CustomInventoryItemComponent component = new CustomInventoryItemComponent(this, item) {
            @Override
            public boolean mouseClickedImpl(int x, int y, int mouseX, int mouseY, int button) {
                close();
                callback.accept(item.getStack());
                return true;
            }

            @Override
            public void onHoveredImpl(int x, int y, int mouseX, int mouseY, boolean hovered) {
                if (hovered) {
                    setBackgroundColor(new Color(200, 200, 200, 200));
                } else {
                    setBackgroundColor(InventoryScreen.getComponentBackgroundColor().withAlpha(200));
                }
            }

            @Override
            public void setHover() {
            }
        };
        component.setBackgroundColor(InventoryScreen.getComponentBackgroundColor().withAlpha(200));
        return component;
    }

    @Override
    public void close() {
        super.close();
        callback.accept(null);
        ItemSwitcherHandler.getInstance().setCurrentScreen(null);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
