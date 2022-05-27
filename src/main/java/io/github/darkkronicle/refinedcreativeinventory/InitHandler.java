package io.github.darkkronicle.refinedcreativeinventory;

import io.github.darkkronicle.darkkore.intialization.Initializer;
import io.github.darkkronicle.kommandlib.CommandManager;
import io.github.darkkronicle.kommandlib.command.ClientCommand;
import io.github.darkkronicle.kommandlib.command.CommandInvoker;
import io.github.darkkronicle.kommandlib.invokers.BaseCommandInvoker;
import io.github.darkkronicle.kommandlib.util.CommandUtil;
import io.github.darkkronicle.kommandlib.util.InfoUtil;
import io.github.darkkronicle.refinedcreativeinventory.tabs.TabHolder;
import net.minecraft.server.command.ServerCommandSource;

public class InitHandler implements Initializer {

    @Override
    public void init() {
        CommandInvoker<ServerCommandSource> command = new BaseCommandInvoker(
                RefinedCreativeInventory.MOD_ID,
                "refinedcreative",
                CommandUtil.literal("refined").executes(ClientCommand.of(context -> InfoUtil.sendChatMessage("RefinedCreativeInventory by DarkKronicle"))).build()
        );
        CommandManager.getInstance().addCommand(command);
    }

}
