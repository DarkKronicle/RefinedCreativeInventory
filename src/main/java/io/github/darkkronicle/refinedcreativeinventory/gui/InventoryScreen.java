package io.github.darkkronicle.refinedcreativeinventory.gui;

import io.github.darkkronicle.darkkore.gui.ComponentScreen;
import io.github.darkkronicle.darkkore.gui.components.BasicComponent;
import io.github.darkkronicle.darkkore.gui.components.Component;
import io.github.darkkronicle.darkkore.gui.components.impl.InventoryItemComponent;
import io.github.darkkronicle.darkkore.gui.components.impl.ItemComponent;
import io.github.darkkronicle.darkkore.gui.components.impl.TextBoxComponent;
import io.github.darkkronicle.darkkore.gui.components.transform.ListComponent;
import io.github.darkkronicle.darkkore.gui.components.transform.PositionedComponent;
import io.github.darkkronicle.darkkore.gui.components.transform.ScrollComponent;
import io.github.darkkronicle.darkkore.gui.elements.TextBox;
import io.github.darkkronicle.darkkore.util.Color;
import io.github.darkkronicle.darkkore.util.Dimensions;
import io.github.darkkronicle.darkkore.util.render.RenderUtil;
import io.github.darkkronicle.refinedcreativeinventory.items.InventoryItem;
import io.github.darkkronicle.refinedcreativeinventory.items.ItemHolder;
import io.github.darkkronicle.refinedcreativeinventory.search.ItemSearch;
import io.github.darkkronicle.refinedcreativeinventory.tabs.FilterTab;
import io.github.darkkronicle.refinedcreativeinventory.tabs.ItemTab;
import io.github.darkkronicle.refinedcreativeinventory.tabs.TabHolder;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

import java.util.List;

public class InventoryScreen extends ComponentScreen {

    private ItemStack selectedStack = null;
    private ListComponent items;
    private ListComponent tabs;
    private TextBoxComponent searchBox;
    private Runnable refocusSearch;

    private Dimensions bounds;

    private static ItemTab tab = null;

    private static String lastSearch = "";

    protected void onChange(String string) {
        lastSearch = string;
        setItems(string);
    }

    public void setItems(String query) {
        if (query == null || query.isEmpty()) {
            setItems(tab);
            return;
        }
        clearTabOutline();
        setItems(ItemSearch.fromQuery(query).search(ItemHolder.getInstance().getAllItems()));
    }

    public void clearTabOutline() {
        for (Component component : tabs.getComponents()) {
            if (component instanceof BasicComponent c){
                c.setOutlineColor(null);
            }
        }
    }

    public void setItems(List<InventoryItem> list) {
        items.clear();
        for (InventoryItem item : list) {
            items.addComponent(createItemComponent(item));
        }
    }

    public void setItems(ItemTab tab) {
        items.clear();
        InventoryScreen.tab = tab;
        clearTabOutline();
        for (Component c : tab.getComponents(this, bounds)) {
            items.addComponent(c);
        }
    }

    @Override
    public void initImpl() {
        Dimensions screen = Dimensions.getScreen();

        int mainWidth = screen.getWidth() - 34;
        int mainX = 24;

        bounds = new Dimensions(mainWidth, screen.getHeight() - 100);

        searchBox = new TextBoxComponent(lastSearch, mainWidth, 14, this::onChange);
        searchBox.setBackgroundColor(new Color(0, 0, 0, 255));
        PositionedComponent textBoxPos = new PositionedComponent(
                searchBox,
                mainX,
                22,
                searchBox.getBoundingBox().width(),
                searchBox.getBoundingBox().height()
        );
        addComponent(textBoxPos);
        refocusSearch = () -> {
            searchBox.setSelected(true);
            textBoxPos.setSelected(true);
        };

        tabs = new ListComponent(22, -1, true);
        if (tab == null) {
            tab = TabHolder.getInstance().getTabs().get(0);
        }

        addComponent(new PositionedComponent(
                new ScrollComponent(tabs, 22, screen.getHeight() - 100, true
        ), 2, 40, 22, screen.getHeight() - 100).setOutlineColor(new Color(0, 0, 0, 255)));

        items = new ListComponent(mainWidth, -1, false);
        items.setBottomPad(10);
        items.setTopPad(10);
        setItems(lastSearch);
        addComponent(new PositionedComponent(
                new ScrollComponent(items, mainWidth, screen.getHeight() - 100, true
        ), mainX, 40, mainWidth, screen.getHeight() - 100).setOutlineColor(new Color(0, 0, 0, 255)));

        for (ItemTab tab : TabHolder.getInstance().getTabs()) {
            BasicComponent icon = tab.getIcon();
            icon.setOnClickedConsumer((button) -> {
                InventoryScreen.tab = tab;
                searchBox.setText("");
                setItems(lastSearch);
                button.setOutlineColor(new Color(255, 255, 255, 255));
            });
            if (tab.equals(InventoryScreen.tab)) {
                icon.setOutlineColor(new Color(255, 255, 255, 255));
            }
            tabs.addComponent(icon);
        }

        // Hotbar
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
                hotbar, mainX, screen.getHeight() - 52, hotbar.getBoundingBox().width(), hotbar.getBoundingBox().height()
        ));
    }

    public RefinedItemComponent createItemComponent(InventoryItem item) {
        RefinedItemComponent component = new RefinedItemComponent(item) {
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
    public boolean charTyped(char chr, int modifiers) {
        if (super.charTyped(chr, modifiers)) {
            return true;
        }
        refocusSearch.run();
        return searchBox.charTyped(chr, modifiers);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

}
