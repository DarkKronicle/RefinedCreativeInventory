package io.github.darkkronicle.refinedcreativeinventory.hotbars.gui;

import io.github.darkkronicle.darkkore.gui.ConfigScreen;
import io.github.darkkronicle.darkkore.gui.components.impl.ButtonComponent;
import io.github.darkkronicle.darkkore.gui.components.transform.PositionedComponent;
import io.github.darkkronicle.darkkore.util.Color;
import io.github.darkkronicle.darkkore.util.StringUtil;
import io.github.darkkronicle.refinedcreativeinventory.gui.InventoryScreen;
import io.github.darkkronicle.refinedcreativeinventory.hotbars.HotbarHolder;
import io.github.darkkronicle.refinedcreativeinventory.hotbars.HotbarProfile;
import io.github.darkkronicle.refinedcreativeinventory.items.ItemHolder;
import net.minecraft.client.gui.screen.Screen;

public class HotbarProfileEditor extends ConfigScreen {

    private final HotbarProfile profile;

    private final InventoryScreen inventory;

    public HotbarProfileEditor(InventoryScreen parent, HotbarProfile profile) {
        super(profile.getOptions());
        this.profile = profile;
        this.inventory = parent;
        setParent(parent);
    }

    @Override
    public void initImpl() {
        super.initImpl();
        ButtonComponent delete = new ButtonComponent(
                this,
                StringUtil.translateToText("rci.itemedit.delete"),
                new Color(100, 100, 100, 100),
                new Color(150, 150, 150, 150),
                (button) -> {
                    HotbarHolder.getInstance().getProfiles().remove(profile);
                    close();
                }
        );
        addComponent(new PositionedComponent(
                inventory,
                delete,
                10,
                10
        ));
    }
}
