package io.github.darkkronicle.refinedcreativeinventory.util;

import lombok.experimental.UtilityClass;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class ClientUtil {

    public void setScreenIgnoreKeybinds(@NotNull Screen screen) {
        MinecraftClient client = MinecraftClient.getInstance();

        if (client.currentScreen != null) {
            client.currentScreen.removed();
        }

        client.currentScreen = screen;
        client.mouse.unlockCursor();
        screen.init(client, client.getWindow().getScaledWidth(), client.getWindow().getScaledHeight());
        client.skipGameRender = false;
        client.updateWindowTitle();
    }

}
