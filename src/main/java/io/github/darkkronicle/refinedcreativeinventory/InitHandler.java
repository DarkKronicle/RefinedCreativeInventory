package io.github.darkkronicle.refinedcreativeinventory;

import io.github.darkkronicle.darkkore.hotkeys.BasicHotkey;
import io.github.darkkronicle.darkkore.hotkeys.Hotkey;
import io.github.darkkronicle.darkkore.hotkeys.HotkeyHandler;
import io.github.darkkronicle.darkkore.intialization.Initializer;
import io.github.darkkronicle.darkkore.intialization.profiles.PlayerContextCheck;
import io.github.darkkronicle.refinedcreativeinventory.config.CreativeInventoryConfig;
import io.github.darkkronicle.refinedcreativeinventory.hotbars.HotbarHolder;
import io.github.darkkronicle.refinedcreativeinventory.hotbars.HotbarProfile;
import io.github.darkkronicle.refinedcreativeinventory.itemselector.InventoryItemSwitcherScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.world.GameMode;

import java.util.ArrayList;
import java.util.List;

public class InitHandler implements Initializer {

    private static boolean switched = true;

    @Override
    public void init() {
        HotkeyHandler.getInstance().add(RefinedCreativeInventory.MOD_ID, "globalhotbar", () -> {
            List<Hotkey> hotkeys = new ArrayList<>();
            hotkeys.add(new BasicHotkey(CreativeInventoryConfig.getInstance().getSwitchHotbars().getValue(), () -> {
                if (new PlayerContextCheck(null, null, GameMode.CREATIVE, null).check()) {
                    HotbarProfile current = HotbarHolder.getInstance().getCurrent();
                    if (switched) {
                        current.applyMainOne();
                    } else {
                        current.applyMainTwo();
                    }
                    switched = !switched;
                }
            }));
            hotkeys.add(new BasicHotkey(CreativeInventoryConfig.getInstance().getNextHotbar().getValue(), () -> {
                if (new PlayerContextCheck(null, null, GameMode.CREATIVE, null).check()) {
                    HotbarProfile profile = HotbarHolder.getInstance().getCurrent();
                    profile.cycle(true);
                    profile.getCurrent().apply();
                }
            }));
            hotkeys.add(new BasicHotkey(CreativeInventoryConfig.getInstance().getPreviousHotbar().getValue(), () -> {
                if (new PlayerContextCheck(null, null, GameMode.CREATIVE, null).check()) {
                    HotbarProfile profile = HotbarHolder.getInstance().getCurrent();
                    profile.cycle(false);
                    profile.getCurrent().apply();
                }
            }));
            hotkeys.add(new BasicHotkey(CreativeInventoryConfig.getInstance().getOpenSelector().getValue(), () -> {
                if (new PlayerContextCheck(true, null, GameMode.CREATIVE, null).check()) {
                    MinecraftClient client = MinecraftClient.getInstance();
                    int selectedSlot = client.player.getInventory().selectedSlot;
                    if (!client.player.getInventory().getStack(selectedSlot).isEmpty()) {
                        client.setScreen(new InventoryItemSwitcherScreen(selectedSlot));
                    }
                }
            }));
            return hotkeys;
        });
    }

}
