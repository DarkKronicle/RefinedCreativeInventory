package io.github.darkkronicle.refinedcreativeinventory.tabs;

import io.github.darkkronicle.darkkore.gui.components.BasicComponent;
import io.github.darkkronicle.darkkore.gui.components.Component;
import io.github.darkkronicle.darkkore.gui.components.impl.IconButtonComponent;
import io.github.darkkronicle.darkkore.gui.components.impl.ItemComponent;
import io.github.darkkronicle.darkkore.gui.components.impl.TextComponent;
import io.github.darkkronicle.darkkore.gui.components.transform.ListComponent;
import io.github.darkkronicle.darkkore.util.Color;
import io.github.darkkronicle.darkkore.util.Dimensions;
import io.github.darkkronicle.darkkore.util.StringUtil;
import io.github.darkkronicle.refinedcreativeinventory.RefinedCreativeInventory;
import io.github.darkkronicle.refinedcreativeinventory.gui.InventoryScreen;
import io.github.darkkronicle.refinedcreativeinventory.gui.components.ArmorInventoryComponent;
import io.github.darkkronicle.refinedcreativeinventory.gui.components.IconInventoryComponent;
import io.github.darkkronicle.refinedcreativeinventory.gui.components.RefinedInventoryItemComponent;
import io.github.darkkronicle.refinedcreativeinventory.items.BasicInventoryItem;
import io.github.darkkronicle.refinedcreativeinventory.items.InventoryItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.PlayerScreenHandler;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class InventoryTab implements ItemTab {

    @Override
    public BasicComponent getIcon(InventoryScreen parent) {
        return new ItemComponent(parent, Items.CHEST)
                .setOnHoveredConsumer(button -> button.setBackgroundColor(new Color(150, 150, 150, 150)))
                .setOnHoveredStoppedConsumer(button -> button.setBackgroundColor(null)
                );
    }

    @Override
    public List<InventoryItem> getItems() {
        List<InventoryItem> items = new ArrayList<>();
        for (int row = 3; row > 0; row--) {
            for (int column = 0; column < 9; column++) {
                items.add(new BasicInventoryItem(MinecraftClient.getInstance().player.getInventory().getStack(row * 9 + column)));
            }
        }
        return items;
    }

    @Override
    public Integer getOrder() {
        return 500;
    }

    @Override
    public List<Component> getComponents(InventoryScreen screen, Dimensions bounds) {
        return getInventoryComponents(screen, bounds);
    }

    public static List<Component> getInventoryComponents(InventoryScreen screen, Dimensions bounds) {
        ListComponent rows = new ListComponent(screen, -1, -1, true);
        rows.setComponentYPad(0);
        rows.setComponentXPad(0);
        rows.addComponent(new TextComponent(screen, StringUtil.translateToText("rci.inventory.inventory")));
        ListComponent other = new ListComponent(screen, -1, -1, false);
        other.addComponent(new ArmorInventoryComponent(screen, EquipmentSlot.HEAD, new Identifier("textures/" + PlayerScreenHandler.EMPTY_HELMET_SLOT_TEXTURE.getPath() + ".png"), 39).setOutlineColor(InventoryScreen.getSlotOutlineColor()));
        other.addComponent(new ArmorInventoryComponent(screen, EquipmentSlot.CHEST, new Identifier("textures/" + PlayerScreenHandler.EMPTY_CHESTPLATE_SLOT_TEXTURE.getPath() + ".png"), 38).setOutlineColor(InventoryScreen.getSlotOutlineColor()));
        other.addComponent(new ArmorInventoryComponent(screen, EquipmentSlot.LEGS, new Identifier("textures/" + PlayerScreenHandler.EMPTY_LEGGINGS_SLOT_TEXTURE.getPath() + ".png"), 37).setOutlineColor(InventoryScreen.getSlotOutlineColor()));
        other.addComponent(new ArmorInventoryComponent(screen, EquipmentSlot.FEET, new Identifier("textures/" + PlayerScreenHandler.EMPTY_BOOTS_SLOT_TEXTURE.getPath() + ".png"), 36).setOutlineColor(InventoryScreen.getSlotOutlineColor()));
        other.addComponent(new IconInventoryComponent(screen, new Identifier("textures/" + PlayerScreenHandler.EMPTY_OFFHAND_ARMOR_SLOT.getPath() + ".png"), 40).setOutlineColor(InventoryScreen.getSlotOutlineColor()));
        other.setComponentYPad(0);
        other.setComponentXPad(0);
        rows.addComponent(other);
        for (int row = 1; row <= 3; row++) {
            ListComponent columns = new ListComponent(screen, -1, -1, false);
            columns.setComponentYPad(0);
            columns.setComponentXPad(0);
            columns.setTopPad(0);
            columns.setBottomPad(0);
            for (int column = 0; column < 9; column++) {
                columns.addComponent(new RefinedInventoryItemComponent(screen, row * 9 + column).setOutlineColor(InventoryScreen.getSlotOutlineColor()));
            }
            rows.addComponent(columns);
        }
        IconButtonComponent icon = new IconButtonComponent(
                screen,
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
                    MinecraftClient client = MinecraftClient.getInstance();
                    client.player.getInventory().clear();
                    for (int i = 0; i < client.player.playerScreenHandler.getStacks().size(); ++i) {
                        client.interactionManager.clickCreativeStack(ItemStack.EMPTY, i);
                    }
                    return true;
                }
                if (screen.getSelectedStack() != null) {
                    screen.setSelectedStack(null);
                    return true;
                }
                return false;
            }
        };
        icon.setShaderColor(new Color(200, 100, 100, 255));
        icon.setLeftPadding(0);
        icon.setRightPadding(0);
        icon.setTopPadding(0);
        icon.setBottomPadding(0);
        rows.setTopPad(0);
        rows.setComponentYPad(0);
        rows.setBottomPad(0);
        rows.addComponent(icon);
        return List.of(rows);
    }
}
