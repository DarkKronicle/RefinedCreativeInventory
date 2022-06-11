package io.github.darkkronicle.refinedcreativeinventory.hotbars.gui;

import io.github.darkkronicle.darkkore.gui.components.impl.IconButtonComponent;
import io.github.darkkronicle.darkkore.gui.components.impl.ItemComponent;
import io.github.darkkronicle.darkkore.gui.components.impl.TextComponent;
import io.github.darkkronicle.darkkore.gui.components.transform.ListComponent;
import io.github.darkkronicle.darkkore.util.Color;
import io.github.darkkronicle.darkkore.util.StringUtil;
import io.github.darkkronicle.refinedcreativeinventory.RefinedCreativeInventory;
import io.github.darkkronicle.refinedcreativeinventory.gui.InventoryScreen;
import io.github.darkkronicle.refinedcreativeinventory.hotbars.HotbarHolder;
import io.github.darkkronicle.refinedcreativeinventory.hotbars.HotbarProfile;
import lombok.Getter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class HotbarHolderComponent extends ListComponent {

    @Getter private final HotbarHolder holder;
    private final ListComponent profiles;
    private HotbarProfileComponent profileComponent;

    private final InventoryScreen inventory;

    public HotbarHolderComponent(InventoryScreen parent, HotbarHolder holder, int width, int height) {
        super(parent, width, height, true);
        this.inventory = parent;
        this.holder = holder;
        profiles = new ListComponent(parent, -1, -1, false);
        updateProfiles();
    }

    private void onClick(HotbarProfile profile) {
        holder.setCurrent(profile);
        updateProfiles();
    }

    public void updateProfiles() {
        this.clear();
        profiles.clear();
        for (HotbarProfile profile : holder.getProfiles()) {
            ItemComponent component = new ItemComponent(parent, new ItemStack(Registry.ITEM.get(new Identifier(profile.getStack().getValue())))) {
                @Override
                public boolean mouseClickedImpl(int x, int y, int mouseX, int mouseY, int button) {
                    if (button == 0) {
                        onClick(profile);
                        return true;
                    }
                    if (button == 1) {
                        MinecraftClient.getInstance().setScreen(new HotbarProfileEditor(inventory, profile));
                    }
                    return false;
                }
            };
            component.setOnHoveredConsumer(button -> button.setBackgroundColor(new Color(150, 150, 150, 150)));
            component.setOnHoveredStoppedConsumer(button -> button.setBackgroundColor(null));
            profiles.addComponent(component);
        }
        profileComponent = new HotbarProfileComponent(inventory, this, holder.getCurrent());
        IconButtonComponent add = new IconButtonComponent(
                parent,
                new Identifier(RefinedCreativeInventory.MOD_ID, "textures/gui/icon/add.png"),
                18,
                18,
                48,
                48,
                null,
                new Color(150, 150, 150, 150),
                button -> {
                    HotbarProfile profile = new HotbarProfile();
                    HotbarHolder.getInstance().addProfile(profile);
                    updateProfiles();
                }
        );
        add.setRightPadding(0);
        add.setBottomPadding(0);
        add.setTopPadding(0);
        add.setLeftPadding(0);
        profiles.addComponent(add);
        addComponent(new TextComponent(parent, StringUtil.translateToText("rci.inventory.hotbar")));
        addComponent(profiles);
        addComponent(profileComponent);
    }


}
