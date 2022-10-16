package io.github.darkkronicle.refinedcreativeinventory.hotbars.gui;

import io.github.darkkronicle.darkkore.DarkKore;
import io.github.darkkronicle.darkkore.config.options.Option;
import io.github.darkkronicle.darkkore.config.options.OptionSection;
import io.github.darkkronicle.darkkore.gui.ConfigScreen;
import io.github.darkkronicle.darkkore.gui.Tab;
import io.github.darkkronicle.darkkore.gui.components.impl.ButtonComponent;
import io.github.darkkronicle.darkkore.gui.components.transform.PositionedComponent;
import io.github.darkkronicle.darkkore.util.Color;
import io.github.darkkronicle.darkkore.util.StringUtil;
import io.github.darkkronicle.refinedcreativeinventory.gui.InventoryScreen;
import io.github.darkkronicle.refinedcreativeinventory.hotbars.HotbarHolder;
import io.github.darkkronicle.refinedcreativeinventory.hotbars.HotbarProfile;
import io.github.darkkronicle.refinedcreativeinventory.items.ItemHolder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class HotbarProfileEditor extends ConfigScreen {

    private final HotbarProfile profile;

    private final InventoryScreen inventory;

    public HotbarProfileEditor(InventoryScreen parent, HotbarProfile profile) {
        super();
        // TODO put into function
        List<Tab> tabs = new ArrayList<>();
        boolean allSections = true;
        for (Option<?> option : profile.getOptions()) {
            if (!(option instanceof OptionSection)) {
                allSections = false;
                break;
            }
        }
        if (allSections) {
            for (Option<?> opt : profile.getOptions()) {
                tabs.add(populate((OptionSection) opt));
            }
            super.tabs = tabs;
        }
        super.tabs = List.of(Tab.ofOptions(new Identifier(DarkKore.MOD_ID, "main"), "main", profile.getOptions()));
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
                    HotbarHolder.getInstance().removeProfile(profile);
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
