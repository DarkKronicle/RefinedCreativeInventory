package io.github.darkkronicle.refinedcreativeinventory.hotbars.gui;

import io.github.darkkronicle.darkkore.gui.ConfigScreen;
import io.github.darkkronicle.refinedcreativeinventory.hotbars.HotbarProfile;
import net.minecraft.client.gui.screen.Screen;

public class HotbarProfileEditor extends ConfigScreen {

    public HotbarProfileEditor(Screen parent, HotbarProfile profile) {
        super(profile.getOptions());
        setParent(parent);
    }

}
