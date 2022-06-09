package io.github.darkkronicle.refinedcreativeinventory.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import io.github.darkkronicle.darkkore.gui.components.impl.InventoryItemComponent;
import io.github.darkkronicle.darkkore.util.Color;
import io.github.darkkronicle.darkkore.util.PositionedRectangle;
import io.github.darkkronicle.darkkore.util.render.RenderUtil;
import io.github.darkkronicle.refinedcreativeinventory.gui.InventoryScreen;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class IconInventoryComponent extends RefinedInventoryItemComponent {

    private final Identifier icon;

    @Getter @Setter private Color shaderColor = new Color(255, 255, 255, 255);

    public IconInventoryComponent(InventoryScreen parent, Identifier icon, int index) {
        super(parent, index);
        this.icon = icon;
    }

    @Override
    public void renderComponent(MatrixStack matrices, PositionedRectangle renderBounds, int x, int y, int mouseX, int mouseY) {
        super.renderComponent(matrices, renderBounds, x, y, mouseX, mouseY);
        if (getStack() == null || getStack().isEmpty()) {
            RenderSystem.setShaderColor(shaderColor.red() / 255f, shaderColor.green() / 255f, shaderColor.blue() / 255f, shaderColor.alpha() / 255f);
            RenderSystem.setShaderTexture(0, icon);
            DrawableHelper.drawTexture(matrices, x + 1, y + 1, 16, 16, 0, 0, 16, 16, 16, 16);
            RenderSystem.setShaderColor(1, 1, 1,1);
        }
    }
}
