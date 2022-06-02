package io.github.darkkronicle.refinedcreativeinventory.hotbars.gui;

import io.github.darkkronicle.darkkore.gui.components.impl.IconButtonComponent;
import io.github.darkkronicle.darkkore.gui.components.impl.TextComponent;
import io.github.darkkronicle.darkkore.gui.components.transform.ListComponent;
import io.github.darkkronicle.darkkore.util.Color;
import io.github.darkkronicle.darkkore.util.FluidText;
import io.github.darkkronicle.refinedcreativeinventory.RefinedCreativeInventory;
import io.github.darkkronicle.refinedcreativeinventory.gui.InventoryScreen;
import io.github.darkkronicle.refinedcreativeinventory.hotbars.HotbarProfile;
import io.github.darkkronicle.refinedcreativeinventory.hotbars.SavedHotbar;
import lombok.Getter;
import net.minecraft.util.Identifier;

public class HotbarProfileComponent extends ListComponent {

    @Getter private final HotbarHolderComponent holder;
    @Getter private final HotbarProfile profile;

    private final InventoryScreen inventory;

    public HotbarProfileComponent(InventoryScreen parent, HotbarHolderComponent holder, HotbarProfile profile) {
        super(parent, -1, -1, true);
        this.holder = holder;
        this.profile = profile;
        this.inventory = parent;
        updateHotbars();
    }

    public void updateHotbars() {
        clear();
        addComponent(new TextComponent(parent, new FluidText(profile.getName().getValue())));
        for (SavedHotbar hotbar : profile.getHotbars()) {
            addComponent(new SavedHotbarComponent(inventory, this, hotbar));
        }
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
                    SavedHotbar hotbar = new SavedHotbar();
                    profile.add(hotbar);
                    updateHotbars();
                }
        );
        add.setRightPadding(0);
        add.setBottomPadding(0);
        add.setTopPadding(0);
        add.setLeftPadding(0);

        addComponent(add);
        holder.updateHeight();
    }

}
