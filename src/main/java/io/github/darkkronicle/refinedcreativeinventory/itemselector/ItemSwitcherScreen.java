package io.github.darkkronicle.refinedcreativeinventory.itemselector;

import io.github.darkkronicle.darkkore.gui.components.Component;
import io.github.darkkronicle.darkkore.gui.components.transform.PositionedComponent;
import io.github.darkkronicle.darkkore.util.Color;
import io.github.darkkronicle.darkkore.util.PositionedRectangle;
import io.github.darkkronicle.darkkore.util.Rectangle;
import io.github.darkkronicle.refinedcreativeinventory.gui.InventoryScreen;
import io.github.darkkronicle.refinedcreativeinventory.gui.components.CustomInventoryItemComponent;
import io.github.darkkronicle.refinedcreativeinventory.items.BasicInventoryItem;
import io.github.darkkronicle.refinedcreativeinventory.items.InventoryItem;
import io.github.darkkronicle.refinedcreativeinventory.items.ItemHolder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;


public class ItemSwitcherScreen extends RadialScreen {

    private final Consumer<ItemStack> callback;
    private final ItemStack startingItem;

    public ItemSwitcherScreen(ItemStack startingItem, Consumer<ItemStack> callback) {
        super();
        this.startingItem = startingItem;
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
    public PositionedComponent getCenterComponent() {
        Component base = createComponent(ItemHolder.getInstance().get(startingItem).orElseGet(() -> new BasicInventoryItem(startingItem)));
        Rectangle bounds = base.getBoundingBox();
        return new PositionedComponent(this, base, (width - bounds.width()) / 2, (height - bounds.height()) / 2);
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
            public void postRender(MatrixStack matrices, PositionedRectangle renderBounds, int x, int y, int mouseX, int mouseY) {
                if (isHovered()) {
                    matrices.translate(0, 0, 20);
                    Rectangle rect = getBoundingBox();
                    drawCenteredText(matrices, client.textRenderer, item.getStack().getName(), x + rect.width() / 2, y - 20, -1);
                    matrices.translate(0, 0, -20);
                }
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
