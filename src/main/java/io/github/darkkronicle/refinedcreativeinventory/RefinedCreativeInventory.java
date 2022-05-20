package io.github.darkkronicle.refinedcreativeinventory;

import io.github.darkkronicle.darkkore.config.ConfigurationManager;
import io.github.darkkronicle.darkkore.intialization.InitializationHandler;
import io.github.darkkronicle.refinedcreativeinventory.config.ItemsConfig;
import io.github.darkkronicle.refinedcreativeinventory.gui.InventoryScreen;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class RefinedCreativeInventory implements ClientModInitializer {

    public static final String MOD_ID = "refinedcreativeinventory";

    @Override
    public void onInitializeClient() {
        InitializationHandler.getInstance().registerInitializer(MOD_ID, 0, new InitHandler());
        ConfigurationManager.getInstance().add(ItemsConfig.getInstance());
        KeyBinding keyBinding =
                new KeyBinding(
                        "refinedcreativeinventory.key.test",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_U,
                        "category.keys");
        ClientTickEvents.START_CLIENT_TICK.register(
                s -> {
                    if (keyBinding.wasPressed()) {
                        MinecraftClient.getInstance().setScreen(new InventoryScreen());
                    }
                });
    }
}
