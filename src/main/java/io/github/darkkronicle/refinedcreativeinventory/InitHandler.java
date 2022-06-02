package io.github.darkkronicle.refinedcreativeinventory;

import io.github.darkkronicle.darkkore.hotkeys.BasicHotkey;
import io.github.darkkronicle.darkkore.hotkeys.Hotkey;
import io.github.darkkronicle.darkkore.hotkeys.HotkeyHandler;
import io.github.darkkronicle.darkkore.intialization.Initializer;
import io.github.darkkronicle.kommandlib.CommandManager;
import io.github.darkkronicle.kommandlib.command.ClientCommand;
import io.github.darkkronicle.kommandlib.command.CommandInvoker;
import io.github.darkkronicle.kommandlib.invokers.BaseCommandInvoker;
import io.github.darkkronicle.kommandlib.util.CommandUtil;
import io.github.darkkronicle.kommandlib.util.InfoUtil;
import io.github.darkkronicle.refinedcreativeinventory.config.CreativeInventoryConfig;
import io.github.darkkronicle.refinedcreativeinventory.hotbars.HotbarHolder;
import io.github.darkkronicle.refinedcreativeinventory.hotbars.HotbarProfile;
import io.github.darkkronicle.refinedcreativeinventory.tabs.TabHolder;
import net.minecraft.server.command.ServerCommandSource;

import java.util.ArrayList;
import java.util.List;

public class InitHandler implements Initializer {

    private static boolean switched = true;

    @Override
    public void init() {
        CommandInvoker<ServerCommandSource> command = new BaseCommandInvoker(
                RefinedCreativeInventory.MOD_ID,
                "refinedcreative",
                CommandUtil.literal("refined").executes(ClientCommand.of(context -> InfoUtil.sendChatMessage("RefinedCreativeInventory by DarkKronicle"))).build()
        );
        CommandManager.getInstance().addCommand(command);
        HotkeyHandler.getInstance().add(RefinedCreativeInventory.MOD_ID, "globalhotbar", () -> {
            List<Hotkey> hotkeys = new ArrayList<>();
            hotkeys.add(new BasicHotkey(CreativeInventoryConfig.getInstance().getSwitchHotbars().getValue(), () -> {
                HotbarProfile current = HotbarHolder.getInstance().getCurrent();
                if (switched) {
                    current.applyMainOne();
                } else {
                    current.applyMainTwo();
                }
                switched = !switched;
            }));
            return hotkeys;
        });
    }

}
