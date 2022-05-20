package io.github.darkkronicle.refinedcreativeinventory.gui;

import io.github.darkkronicle.darkkore.gui.ComponentScreen;
import io.github.darkkronicle.darkkore.gui.components.impl.InventoryItemComponent;
import io.github.darkkronicle.darkkore.gui.components.impl.ItemComponent;
import io.github.darkkronicle.darkkore.gui.components.impl.TextBoxComponent;
import io.github.darkkronicle.darkkore.gui.components.transform.ListComponent;
import io.github.darkkronicle.darkkore.gui.components.transform.PositionedComponent;
import io.github.darkkronicle.darkkore.gui.components.transform.ScrollComponent;
import io.github.darkkronicle.darkkore.util.Color;
import io.github.darkkronicle.darkkore.util.Dimensions;
import io.github.darkkronicle.darkkore.util.render.RenderUtil;
import io.github.darkkronicle.refinedcreativeinventory.items.InventoryItem;
import io.github.darkkronicle.refinedcreativeinventory.items.ItemHolder;
import io.github.darkkronicle.refinedcreativeinventory.search.ItemSearch;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

import java.util.List;

public class InventoryScreen extends ComponentScreen {

    private ItemStack selectedStack = null;
    private ListComponent items;

    protected void onChange(String string) {
        setItems(string);
    }

    public void setItems(String query) {
        if (query == null || query.isEmpty()) {
            setItems(ItemHolder.getInstance().getAllItems());
            return;
        }
        setItems(ItemSearch.fromQuery(query).search(ItemHolder.getInstance().getAllItems()));
    }

    public void setItems(List<InventoryItem> list) {
        items.clear();
        for (InventoryItem item : list) {
            items.addComponent(createItemComponent(item));
        }
    }

    @Override
    public void initImpl() {
        Dimensions screen = Dimensions.getScreen();
        TextBoxComponent searchBox = new TextBoxComponent("", screen.getWidth() - 50, 14, this::onChange);
        searchBox.setBackgroundColor(new Color(0, 0, 0, 255));
        addComponent(new PositionedComponent(
                searchBox,
               40,
               22,
                searchBox.getBoundingBox().width(),
                searchBox.getBoundingBox().height()
        ));

        items = new ListComponent(screen.getWidth() - 50, -1, false);
        items.setBottomPad(10);
        items.setTopPad(10);
        setItems(ItemHolder.getInstance().getAllItems());
        addComponent(new PositionedComponent(
                new ScrollComponent(items, screen.getWidth() - 50, screen.getHeight() - 100, true
        ), 40, 40, screen.getWidth() - 50, screen.getHeight() - 100).setOutlineColor(new Color(0, 0, 0, 255)));

        ListComponent hotbar = new ListComponent(-1, -1, false);
        for (int i = 0; i < 9; i++) {
            final int j = i;
            ItemComponent item = new InventoryItemComponent(client.player.getInventory(), i) {
                @Override
                public boolean mouseClickedImpl(int x, int y, int mouseX, int mouseY, int button) {
                    if (selectedStack != null) {
                        client.player.getInventory().setStack(j, selectedStack);
                        client.interactionManager.clickCreativeStack(selectedStack, 36 + j);
                        selectedStack = null;
                    }
                    return true;
                }
            };
            item.setOnHoveredConsumer((comp) -> comp.setBackgroundColor(new Color(150, 100, 100, 150)));
            item.setOnHoveredStoppedConsumer((comp) -> comp.setBackgroundColor(null));
            hotbar.addComponent(item);
        }
        hotbar.setOutlineColor(new Color(0, 0, 0, 255));
        addComponent(new PositionedComponent(
                hotbar, 40, screen.getHeight() - 52, hotbar.getBoundingBox().width(), hotbar.getBoundingBox().height()
        ));
    }

    public RefinedItemComponent createItemComponent(InventoryItem item) {
        RefinedItemComponent component = new RefinedItemComponent(item.getStack()) {
            @Override
            public boolean mouseClickedImpl(int x, int y, int mouseX, int mouseY, int button) {
                selectedStack = this.getStack();
                return true;
            }
        };
        component.setOnHoveredConsumer(comp -> comp.setBackgroundColor(new Color(150, 150, 150, 150)));
        component.setOnHoveredStoppedConsumer(comp -> comp.setBackgroundColor(null));
        return component;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) {
            return true;
        }
        selectedStack = null;
        return false;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float partialTicks) {
        super.render(matrices, mouseX, mouseY, partialTicks);
        if (selectedStack != null) {
            RenderUtil.drawItem(matrices, selectedStack, mouseX - 8, mouseY - 8, true, 50);
        }
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

}
