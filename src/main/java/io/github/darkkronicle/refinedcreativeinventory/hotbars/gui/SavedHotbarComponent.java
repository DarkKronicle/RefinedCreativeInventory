package io.github.darkkronicle.refinedcreativeinventory.hotbars.gui;

import io.github.darkkronicle.darkkore.gui.components.impl.ButtonComponent;
import io.github.darkkronicle.darkkore.gui.components.impl.TextComponent;
import io.github.darkkronicle.darkkore.gui.components.transform.ListComponent;
import io.github.darkkronicle.darkkore.util.Color;
import io.github.darkkronicle.darkkore.util.FluidText;
import io.github.darkkronicle.darkkore.util.text.RawText;
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
        ButtonComponent button = new ButtonComponent(
                parent,
                new FluidText(String.valueOf(profile.getProfile().indexOf(hotbar) + 1)),
                new Color(100, 100, 100, 100),
                new Color(150, 150, 150, 150),
                (but) -> getHotbar().apply()
        ) {
            @Override
            public boolean mouseClickedImpl(int x, int y, int mouseX, int mouseY, int button) {
                if (isDisabled()) {
                    return true;
                }
                playInterfaceSound();
                if (Screen.hasShiftDown()) {
                    if (button == 0) {
                        profile.getProfile().setMainOne(profile.getProfile().indexOf(hotbar));
                        profile.updateHotbars();
                    } else if (button == 1) {
                        profile.getProfile().setMainTwo(profile.getProfile().indexOf(hotbar));
                        profile.updateHotbars();
                    }
                    return true;
                }
                if (button == 0) {
                    profile.getProfile().setCurrent(profile.getProfile().indexOf(hotbar));
                    getOnClick().accept(this);
                    profile.updateHotbars();
                } else if (button == 1) {
                    MinecraftClient.getInstance().setScreen(new SavedHotbarEditor(parent, profile, hotbar));
                }
                return true;
            }
        };
        button.setCenter(true);
        button.setWidth(18);
        button.setHeight(18);
        int ind = profile.getProfile().indexOf(hotbar);
        if (hotbar.equals(profile.getProfile().getCurrent())) {
            button.setBackground(new Color(100, 100, 255, 150));
            button.setBackgroundColor(new Color(100, 100, 255, 150));
            button.setHover(new Color(150, 150, 255, 150));
        }
        if (profile.getProfile().getMainOne() == ind) {
            button.setOutlineColor(new Color(200, 100, 100, 255));
        } else if (profile.getProfile().getMainTwo() == ind) {
            button.setOutlineColor(new Color(100, 200, 100, 255));
        }
        addComponent(button);
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
                            hotbar.set(index, ItemHolder.getInstance().getOrCreate(sel));
                            updateItems();
                        } else {
                            if (getStack() != null) {
                                ItemStack stack = getStack();
                                inventory.setSelectedStack(stack);
                                hotbar.set(index, ItemHolder.getInstance().getOrCreate(new ItemStack(Items.AIR)));
                                updateItems();
                            }
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
            component.setOutlineColor(InventoryScreen.getSlotOutlineColor());
            addComponent(component);
        }
    }

}