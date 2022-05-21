package io.github.darkkronicle.refinedcreativeinventory.mixin;


import io.github.darkkronicle.refinedcreativeinventory.RefinedCreativeInventory;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.SynchronizeTagsS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {

    @Inject(method = "onSynchronizeTags", at = @At("RETURN"))
    private void setWorld(SynchronizeTagsS2CPacket packet, CallbackInfo ci) {
        RefinedCreativeInventory.refresh();
    }

}
