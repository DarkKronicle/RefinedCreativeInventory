package io.github.darkkronicle.refinedcreativeinventory.gui;

import io.github.darkkronicle.darkkore.gui.ComponentScreen;
import io.github.darkkronicle.darkkore.gui.components.BasicComponent;
import io.github.darkkronicle.darkkore.gui.components.Component;
import io.github.darkkronicle.darkkore.gui.components.impl.*;
import io.github.darkkronicle.darkkore.gui.components.transform.ListComponent;
import io.github.darkkronicle.darkkore.gui.components.transform.MultiComponent;
import io.github.darkkronicle.darkkore.gui.components.transform.PositionedComponent;
import io.github.darkkronicle.darkkore.gui.components.transform.ScrollComponent;
import io.github.darkkronicle.darkkore.util.Color;
import io.github.darkkronicle.darkkore.util.Dimensions;
import io.github.darkkronicle.darkkore.util.StringUtil;
import io.github.darkkronicle.darkkore.util.render.RenderUtil;
import io.github.darkkronicle.refinedcreativeinventory.RefinedCreativeInventory;
import io.github.darkkronicle.refinedcreativeinventory.config.CreativeInventoryConfig;
import io.github.darkkronicle.refinedcreativeinventory.config.CreativeInventoryConfigScreen;
import io.github.darkkronicle.refinedcreativeinventory.gui.components.CustomInventoryItemComponent;
import io.github.darkkronicle.refinedcreativeinventory.gui.components.HotbarComponent;
import io.github.darkkronicle.refinedcreativeinventory.gui.components.ItemsComponent;
import io.github.darkkronicle.refinedcreativeinventory.gui.components.RefinedItemComponent;
import io.github.darkkronicle.refinedcreativeinventory.search.tabeditor.TabEditorScreen;
import io.github.darkkronicle.refinedcreativeinventory.items.InventoryItem;
import io.github.darkkronicle.refinedcreativeinventory.tabs.CustomTab;
import io.github.darkkronicle.refinedcreativeinventory.tabs.InventoryTab;
import io.github.darkkronicle.refinedcreativeinventory.tabs.ItemTab;
import io.github.darkkronicle.refinedcreativeinventory.tabs.TabHolder;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.lwjgl.glfw.GLFW;


public class InventoryScreen extends ComponentScreen {

    @Setter @Getter private ItemStack selectedStack = null;
    private ItemsComponent items;
    private ListComponent tabs;
    private TextBoxComponent searchBox;
    private Runnable refocusSearch;
    private Runnable unfocusSearch;

    @Getter @Setter protected ItemComponent hoveredSlot = null;

    private final static ItemStack blank = new ItemStack(Items.AIR);

    @Getter private static ItemTab tab = null;

    private static String lastSearch = "";

    protected void onChange(String string) {
        lastSearch = string;
        items.setItems(string);
    }

    public void clearTabOutline() {
        for (Component component : tabs.getComponents()) {
            if (component instanceof BasicComponent c){
                c.setOutlineColor(null);
            }
        }
    }

    public void setTab(ItemTab tab) {
        InventoryScreen.tab = tab;
    }

    @Override
    public void initImpl() {
        Dimensions screen = Dimensions.getScreen();

        boolean showInventory = CreativeInventoryConfig.getInstance().getInventorySplit().getValue();
        int mainWidth = screen.getWidth() - 34;
        int itemsWidth = mainWidth;

        if (showInventory) {
            ListComponent inventory = new ListComponent(this, -1, -1, true);
            for (Component component : InventoryTab.getInventoryComponents(this, screen)) {
                inventory.addComponent(component);
            }
            addComponent(new PositionedComponent(this,
                    new ScrollComponent(this, inventory, inventory.getWidth(), screen.getHeight() - 100, true
            ), 24 + mainWidth - inventory.getWidth(), 40, -1, -1).setOutlineColor(new Color(0, 0, 0, 255)));
            itemsWidth = itemsWidth - inventory.getWidth();
        }

        int mainX = 24;

        tabs = new ListComponent(this, 22, -1, true);
        tabs.setTopPad(0);
        if (tab == null) {
            tab = TabHolder.getInstance().getTabs().get(0);
        }

        addComponent(new PositionedComponent(this,
                new ScrollComponent(this, tabs, 22, screen.getHeight() - 100, true
        ), 2, 40, -1, -1).setOutlineColor(new Color(0, 0, 0, 255)));

        items = new ItemsComponent(this, new Dimensions(itemsWidth, screen.getHeight() - 100), itemsWidth);
        items.setItems(lastSearch);
        addComponent(new PositionedComponent(this,
                new ScrollComponent(this, items, itemsWidth, screen.getHeight() - 100, true
        ), mainX, 40, -1, -1).setOutlineColor(new Color(0, 0, 0, 255)));

        for (ItemTab tab : TabHolder.getInstance().getTabs()) {
            BasicComponent icon = tab.getIcon(this);
            icon.setOnClickedConsumer((button) -> {
                InventoryScreen.tab = tab;
                searchBox.setText("");
                items.setItems(lastSearch);
                button.setOutlineColor(new Color(255, 255, 255, 255));
            });
            if (tab.equals(InventoryScreen.tab)) {
                icon.setOutlineColor(new Color(255, 255, 255, 255));
            }
            tabs.addComponent(icon);
        }
        IconButtonComponent add = new IconButtonComponent(
                this,
                new Identifier(RefinedCreativeInventory.MOD_ID, "textures/gui/icon/add.png"),
                18,
                18,
                32,
                32,
                null,
                new Color(150, 150, 150, 150),
                button -> {
                    CustomTab tab = new CustomTab("Custom Tab", new ItemStack(Items.STONE), "stone", true, 10);
                    TabHolder.getInstance().addTab(tab);
                    MinecraftClient.getInstance().setScreen(new TabEditorScreen(this, tab));
                }
        );
        add.setRightPadding(0);
        add.setBottomPadding(0);
        add.setTopPadding(0);
        add.setLeftPadding(0);
        tabs.addComponent(add);

        // Hotbar
        ListComponent hotbar = new HotbarComponent(this);
        addComponent(new PositionedComponent(
                this, hotbar, mainX, screen.getHeight() - 52, hotbar.getBoundingBox().width(), hotbar.getBoundingBox().height()
        ));
        hotbar.setOutlineColor(new Color(0, 0, 0, 255));

        searchBox = new TextBoxComponent(this, lastSearch, mainWidth, 14, this::onChange);


        searchBox.setBackgroundColor(new Color(0, 0, 0, 255));
        PositionedComponent textBoxPos = new PositionedComponent(
                this,
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
                this,
                -1,
                16,
                StringUtil.translateToText("rci.button.vanilla"),
                new Color(100, 100, 100, 100),
                new Color(150, 150, 150, 150), button -> {
                client.setScreen(new CreativeInventoryScreen(client.player));
        });
        vanilla.setLeftPadding(2);

        addComponent(new PositionedComponent(
                this,
                 vanilla,
                2,
                screen.getHeight() - 20,
                -1,
                -1
        ));
        ButtonComponent settings = new ButtonComponent(
                this,
                -1,
                16,
                StringUtil.translateToText("rci.button.settings"),
                new Color(100, 100, 100, 100),
                new Color(150, 150, 150, 150), button -> {
            client.setScreen(new CreativeInventoryConfigScreen());
        });
        vanilla.setLeftPadding(2);
        addComponent(new PositionedComponent(
                this,
                settings,
                4 + vanilla.getWidth(),
                screen.getHeight() - 20,
                -1,
                -1
        ));
    }

    public void setHotbarSlot(@Nullable ItemStack item, int slot) {
        if (item == null) {
            item = blank;
        }
        client.player.getInventory().setStack(slot, item);
        client.interactionManager.clickCreativeStack(item, slot);
        client.player.playerScreenHandler.sendContentUpdates();
    }

    public CustomInventoryItemComponent createItemComponent(InventoryItem item) {
        return new RefinedItemComponent(this, item);
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

    @Override
    public boolean shouldPause() {
        return false;
    }

}
