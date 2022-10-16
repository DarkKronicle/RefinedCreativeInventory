package io.github.darkkronicle.refinedcreativeinventory.mixin;

import io.github.darkkronicle.refinedcreativeinventory.config.CreativeInventoryConfig;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.ingame.InventoryScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InventoryScreen.class)
public class InventoryScreenMixin {

    @Inject(method = "handledScreenTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V"), cancellable = true)
    private void handledScreenTick(CallbackInfo ci) {
        if (CreativeInventoryConfig.getInstance().getOverrideVanilla().getValue()) {
            MinecraftClient.getInstance().setScreen(new io.github.darkkronicle.refinedcreativeinventory.gui.InventoryScreen());
            ci.cancel();
        }
    }

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;setScreen(Lnet/minecraft/client/gui/screen/Screen;)V"), cancellable = true)
    private void init(CallbackInfo ci) {
        if (CreativeInventoryConfig.getInstance().getOverrideVanilla().getValue()) {
            MinecraftClient.getInstance().setScreen(new io.github.darkkronicle.refinedcreativeinventory.gui.InventoryScreen());
            ci.cancel();
        }
    }

}
