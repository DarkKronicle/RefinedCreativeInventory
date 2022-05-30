package io.github.darkkronicle.refinedcreativeinventory.config;

import io.github.darkkronicle.darkkore.gui.ConfigScreen;

public class CreativeInventoryConfigScreen extends ConfigScreen {

    public CreativeInventoryConfigScreen() {
        super(CreativeInventoryConfig.getInstance().getOptions());
    }

}
