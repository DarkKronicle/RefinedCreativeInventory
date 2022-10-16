package io.github.darkkronicle.refinedcreativeinventory.mixin;

import io.github.darkkronicle.refinedcreativeinventory.gui.InventoryScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.ingame.CreativeInventoryScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreativeInventoryScreen.class)
public abstract class CreativeInventoryMixin extends Screen {

    protected CreativeInventoryMixin(Text title) {
        super(title);
    }

    @Inject(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/screen/PlayerScreenHandler;addListener(Lnet/minecraft/screen/ScreenHandlerListener;)V"))
    private void init(CallbackInfo ci) {
        Text text = Text.translatable("rci.button.openscreen");
        ButtonWidget openRCI = new ButtonWidget(2, height - 22, client.textRenderer.getWidth(text) + 4, 20, text, button -> {
            MinecraftClient.getInstance().setScreen(new InventoryScreen());
        });
        addDrawableChild(openRCI);
    }

}
