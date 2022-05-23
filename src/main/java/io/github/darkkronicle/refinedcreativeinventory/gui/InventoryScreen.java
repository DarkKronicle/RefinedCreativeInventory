package io.github.darkkronicle.refinedcreativeinventory.gui;

import io.github.darkkronicle.darkkore.gui.ComponentScreen;
import io.github.darkkronicle.darkkore.gui.components.BasicComponent;
import io.github.darkkronicle.darkkore.gui.components.Component;
import io.github.darkkronicle.darkkore.gui.components.impl.ButtonComponent;
import io.github.darkkronicle.darkkore.gui.components.impl.InventoryItemComponent;
import io.github.darkkronicle.darkkore.gui.components.impl.ItemComponent;
import io.github.darkkronicle.darkkore.gui.components.impl.TextBoxComponent;
import io.github.darkkronicle.darkkore.gui.components.transform.ListComponent;
import io.github.darkkronicle.darkkore.gui.components.transform.PositionedComponent;
import io.github.darkkronicle.darkkore.gui.components.transform.ScrollComponent;
import io.github.darkkronicle.darkkore.gui.elements.TextBox;
import io.github.darkkronicle.darkkore.util.Color;
import io.github.darkkronicle.darkkore.util.Dimensions;
import io.github.darkkronicle.darkkore.util.StringUtil;
import io.github.darkkronicle.darkkore.util.render.RenderUtil;
import io.github.darkkronicle.refinedcreativeinventory.items.InventoryItem;
import io.github.darkkronicle.refinedcreativeinventory.items.ItemHolder;
import io.github.darkkronicle.refinedcreativeinventory.search.ItemSearch;
import io.github.darkkronicle.refinedcreativeinventory.tabs.FilterTab;
import io.github.darkkronicle.refinedcreativeinventory.tabs.ItemTab;
import io.github.darkkronicle.refinedcreativeinventory.tabs.TabHolder;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class InventoryScreen extends ComponentScreen {

    private ItemStack selectedStack = null;
    private ListComponent items;
    private ListComponent tabs;
    private TextBoxComponent searchBox;
    private Runnable refocusSearch;
    private Runnable unfocusSearch;

    private ItemComponent hoveredSlot = null;

    private final static ItemStack blank = new ItemStack(Items.AIR);

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
            InventoryItemComponent item = new InventoryItemComponent(client.player.getInventory(), i) {
                @Override
                public boolean mouseClickedImpl(int x, int y, int mouseX, int mouseY, int button) {
                    if (button == 0) {
                        if (selectedStack != null) {
                            setHotbarSlot(selectedStack, j);
                            selectedStack = null;
                        } else {
                            if (hasShiftDown()) {
                                setHotbarSlot(null, j);
                            } else {
                                ItemStack stack = client.player.getInventory().getStack(j);
                                if (stack != null && !stack.isEmpty()) {
                                    selectedStack = stack;
                                    setHotbarSlot(null, j);
                                }
                            }
                        }
                    }
                    return true;
                }
            };
            item.setOnHoveredConsumer((comp) -> {
                comp.setBackgroundColor(new Color(150, 100, 100, 150));
                hoveredSlot = item;
            });
            item.setOnHoveredStoppedConsumer((comp) -> {
                comp.setBackgroundColor(null);
                if (hoveredSlot == item) {
                    // In case both switch at once
                    hoveredSlot = null;
                }
            });
            hotbar.addComponent(item);
        }
        hotbar.setOutlineColor(new Color(0, 0, 0, 255));
        addComponent(new PositionedComponent(
                hotbar, mainX, screen.getHeight() - 52, hotbar.getBoundingBox().width(), hotbar.getBoundingBox().height()
        ));

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
        unfocusSearch = () -> {
            searchBox.setSelected(false);
            textBoxPos.setSelected(false);
        };
        if (!lastSearch.isEmpty()) {
            searchBox.getTextField().setSelectionStart(0);
            searchBox.getTextField().setSelectionEnd(lastSearch.length());
        }

        ButtonComponent vanilla = new ButtonComponent(
                -1,
                16,
                StringUtil.translateToText("rci.button.vanilla"),
                new Color(100, 100, 100, 100),
                new Color(150, 150, 150, 150), button -> {
                client.setScreen(new CreativeInventoryScreen(client.player));
        });
        vanilla.setLeftPadding(2);

        addComponent(new PositionedComponent(
                 vanilla,
                2,
                screen.getHeight() - 20,
                -1,
                -1
        ));
    }

    private void setHotbarSlot(@Nullable ItemStack item, int slot) {
        if (item == null) {
            item = blank;
        }
        client.player.getInventory().setStack(slot, item);
        client.interactionManager.clickCreativeStack(item, 36 + slot);
    }

    public RefinedItemComponent createItemComponent(InventoryItem item) {
        RefinedItemComponent component = new RefinedItemComponent(item) {
            @Override
            public boolean mouseClickedImpl(int x, int y, int mouseX, int mouseY, int button) {
                if (hasShiftDown() && button == 1) {
                    setFirstHotbarOpen(this.getStack());
                    return true;
                }
                if (button == 0) {
                    selectedStack = this.getStack();
                    if (hasShiftDown()) {
                        selectedStack.setCount(selectedStack.getMaxCount());
                    }
                }
                return true;
            }
        };
        component.setOnHoveredConsumer(comp -> {
            comp.setBackgroundColor(new Color(150, 150, 150, 150));
            hoveredSlot = component;
        });
        component.setOnHoveredStoppedConsumer(comp -> {
            comp.setBackgroundColor(null);
            if (hoveredSlot == component) {
                hoveredSlot = null;
            }
        });
        return component;
    }

    public void setFirstHotbarOpen(ItemStack stack) {
        for (int i = 0; i < 9; i++) {
            ItemStack current = client.player.getInventory().getStack(i);
            if (current.isEmpty()) {
                setHotbarSlot(stack, i);
                return;
            }
        }
        setHotbarSlot(stack, 0);
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

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        if (client.options.inventoryKey.matchesKey(keyCode, scanCode) && !searchBox.isSelected()) {
            this.close();
            return true;
        }
        if (hoveredSlot != null) {
            if (hoveredSlot instanceof InventoryItemComponent && this.client.options.swapHandsKey.matchesKey(keyCode, scanCode)) {
                if (searchBox.isSelected()) {
                    unfocusSearch.run();
                }
                this.client.interactionManager.clickSlot(0, ((InventoryItemComponent) hoveredSlot).getIndex(), 40, SlotActionType.SWAP, client.player);
                return true;
            }

            for (int i = 0; i < 9; ++i) {
                if (this.client.options.hotbarKeys[i].matchesKey(keyCode, scanCode)) {
                    if (searchBox.isSelected()) {
                        unfocusSearch.run();
                    }
                    setHotbarSlot(hoveredSlot.getStack(), i);
                    return true;
                }
            }
        }
        if (keyCode == GLFW.GLFW_KEY_LEFT_SHIFT || keyCode == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            return false;
        }
        refocusSearch.run();
        return searchBox.keyPressed(keyCode, scanCode, modifiers);
    }
}
