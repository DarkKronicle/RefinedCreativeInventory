package io.github.darkkronicle.refinedcreativeinventory;

import io.github.darkkronicle.darkkore.config.ConfigurationManager;
import io.github.darkkronicle.darkkore.intialization.InitializationHandler;
import io.github.darkkronicle.refinedcreativeinventory.config.CreativeInventoryConfig;
import io.github.darkkronicle.refinedcreativeinventory.config.ItemsConfig;
import io.github.darkkronicle.refinedcreativeinventory.config.TabsConfig;
import io.github.darkkronicle.refinedcreativeinventory.items.TagHolder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

@Environment(EnvType.CLIENT)
public class RefinedCreativeInventory implements ClientModInitializer {

    public static final String MOD_ID = "refinedcreativeinventory";

    @Override
    public void onInitializeClient() {
        InitializationHandler.getInstance().registerInitializer(MOD_ID, 0, new InitHandler());
        ConfigurationManager.getInstance().add(CreativeInventoryConfig.getInstance());
        ConfigurationManager.getInstance().add(ItemsConfig.getInstance());
        ConfigurationManager.getInstance().add(TabsConfig.getInstance());
    }

    public static void refresh() {
        TagHolder.getInstance().populateTags();
    }
}
