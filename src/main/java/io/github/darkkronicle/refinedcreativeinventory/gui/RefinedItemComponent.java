package io.github.darkkronicle.refinedcreativeinventory.gui;

import io.github.darkkronicle.darkkore.gui.components.impl.ItemComponent;
import io.github.darkkronicle.darkkore.gui.components.impl.TextComponent;
import io.github.darkkronicle.darkkore.util.Color;
import io.github.darkkronicle.darkkore.util.FluidText;
import io.github.darkkronicle.darkkore.util.PositionedRectangle;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class RefinedItemComponent extends ItemComponent {

    private final TextComponent hoverComponent;

    public RefinedItemComponent(Item item) {
        this(new ItemStack(item));
    }

    public RefinedItemComponent(ItemStack stack) {
        super(stack);
        FluidText text = null;
        for (Text line : stack.getTooltip(null, TooltipContext.Default.ADVANCED)) {
            if (text == null) {
                text = new FluidText(line);
            } else {
                text.append("\n").append(line);
            }
        }
        hoverComponent = new TextComponent(200, -1, text);
        hoverComponent.setBackgroundColor(new Color(20, 20, 20, 255));
        hoverComponent.setOutlineColor(new Color(76, 13, 127, 255));
        hoverComponent.setZOffset(500);
    }

    @Override
    public boolean shouldPostRender() {
        return true;
    }

    @Override
    public void postRender(MatrixStack matrices, PositionedRectangle renderBounds, int x, int y, int mouseX, int mouseY) {
        if (this.isHovered()) {
            hoverComponent.render(matrices, renderBounds, x, y + 18, mouseX, mouseY);
        }
    }
}
