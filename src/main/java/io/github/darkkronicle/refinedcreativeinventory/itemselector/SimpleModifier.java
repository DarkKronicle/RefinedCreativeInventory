package io.github.darkkronicle.refinedcreativeinventory.itemselector;

import io.github.darkkronicle.darkkore.gui.components.Component;
import io.github.darkkronicle.darkkore.gui.components.impl.ItemComponent;
import io.github.darkkronicle.darkkore.util.Color;
import io.github.darkkronicle.darkkore.util.PositionedRectangle;
import io.github.darkkronicle.darkkore.util.Rectangle;
import io.github.darkkronicle.refinedcreativeinventory.gui.InventoryScreen;
import lombok.Getter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

import java.util.function.Consumer;
import java.util.function.Function;

public class SimpleModifier implements ItemModifier {

    private boolean active = true;

    @Getter
    private final ItemStack icon;
    @Getter
    private final Function<ItemStack, ItemStack> consumer;

    public SimpleModifier(ItemStack icon, Function<ItemStack, ItemStack> consumer) {
        this.icon = icon;
        this.consumer = consumer;
    }

    @Override
    public Component getComponent(Screen parent, Consumer<ItemStack> onClick, ItemStack base) {
        ItemComponent comp = new ItemComponent(parent, icon) {
            @Override
            public void postRender(MatrixStack matrices, PositionedRectangle renderBounds, int x, int y, int mouseX, int mouseY) {
                if (isHovered()) {
                    matrices.translate(0, 0, 500);
                    Rectangle rect = getBoundingBox();
                    int width1 = MinecraftClient.getInstance().textRenderer.getWidth(icon.getName());
                    DrawableHelper.fill(matrices, x + rect.width() / 2 - width1 / 2 - 2, y - 11, x + rect.width() / 2 + width1 / 2 + 2, y - 1, 0xAA000000);
                    DrawableHelper.drawCenteredText(matrices, MinecraftClient.getInstance().textRenderer, icon.getName(), x + rect.width() / 2, y - 10, -1);
                    matrices.translate(0, 0, -500);
                }
            }

            @Override
            public boolean shouldPostRender() {
                return true;
            }
        };
        comp.setOnClickedConsumer(component -> onClick.accept(accept(base)));
        comp.setOnHoveredConsumer(button -> button.setBackgroundColor(new Color(150, 150, 150, 150)))
            .setOnHoveredStoppedConsumer(button -> button.setBackgroundColor(InventoryScreen.getComponentBackgroundColor()));
        comp.setBackgroundColor(InventoryScreen.getComponentBackgroundColor());
        return comp;
    }

    @Override
    public ItemStack accept(ItemStack stack) {
        return consumer.apply(stack);
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }
}
