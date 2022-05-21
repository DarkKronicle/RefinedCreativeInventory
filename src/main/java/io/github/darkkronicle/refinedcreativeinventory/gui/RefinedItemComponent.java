package io.github.darkkronicle.refinedcreativeinventory.gui;

import io.github.darkkronicle.darkkore.gui.components.impl.ItemComponent;
import io.github.darkkronicle.darkkore.gui.components.impl.TextComponent;
import io.github.darkkronicle.darkkore.util.Color;
import io.github.darkkronicle.darkkore.util.FluidText;
import io.github.darkkronicle.darkkore.util.PositionedRectangle;
import io.github.darkkronicle.darkkore.util.text.RawText;
import io.github.darkkronicle.refinedcreativeinventory.items.GroupHolder;
import io.github.darkkronicle.refinedcreativeinventory.items.TagHolder;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.List;

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
        if (text == null) {
            text = new FluidText(stack.getName());
        }
        List<ItemGroup> groups = GroupHolder.getInstance().getGroups(stack.getItem());
        if (!groups.isEmpty()) {
            if (groups.size() == 1) {
                text.append("\n").append(new RawText("Group: " + groups.get(0).getName(), Style.EMPTY.withColor(Formatting.DARK_GRAY)));
            } else {
                text.append("\n").append(new RawText("Groups: " + String.join(", ", groups.stream().map(ItemGroup::getName).toList()), Style.EMPTY.withColor(Formatting.DARK_GRAY)));
            }
        }
        List<Identifier> tags = TagHolder.getInstance().getTags(stack.getItem());
        if (!tags.isEmpty()) {
            if (tags.size() == 1) {
                text.append("\n").append(new RawText("Tag: " + tags.get(0).getPath(), Style.EMPTY.withColor(Formatting.DARK_GRAY)));
            } else {
                text.append("\n").append(new RawText("Tags: " + String.join(", ", tags.stream().map(Identifier::getPath).toList()), Style.EMPTY.withColor(Formatting.DARK_GRAY)));
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
