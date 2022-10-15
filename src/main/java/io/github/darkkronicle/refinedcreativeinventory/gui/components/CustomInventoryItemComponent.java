package io.github.darkkronicle.refinedcreativeinventory.gui.components;

import io.github.darkkronicle.darkkore.gui.components.impl.ItemComponent;
import io.github.darkkronicle.darkkore.gui.components.impl.TextComponent;
import io.github.darkkronicle.darkkore.util.*;
import io.github.darkkronicle.darkkore.util.text.RawText;
import io.github.darkkronicle.refinedcreativeinventory.items.InventoryItem;
import lombok.Getter;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.Items;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

import java.util.List;

public class CustomInventoryItemComponent extends ItemComponent {

    protected TextComponent hoverComponent;

    @Getter protected InventoryItem item;

    public CustomInventoryItemComponent(Screen parent, InventoryItem item) {
        super(parent, item.getStack());
        this.item = item;
        setHover();
    }

    public void setHover() {
        FluidText text = null;
        if (getStack() == null) {
            return;
        }
        for (Text line : getStack().getTooltip(null, TooltipContext.Default.ADVANCED)) {
            if (text == null) {
                text = new FluidText(line);
            } else {
                text.append("\n").append(line);
            }
        }
        if (text == null) {
            text = new FluidText(item.getStack().getName());
        }
        List<ItemGroup> groups = item.getGroups();
        if (!groups.isEmpty()) {
            if (groups.size() == 1) {
                text.append("\n").append(new RawText("Group: " + groups.get(0).getName(), Style.EMPTY.withColor(Formatting.DARK_GRAY)));
            } else {
                text.append("\n").append(new RawText("Groups: " + String.join(", ", groups.stream().map(ItemGroup::getName).toList()), Style.EMPTY.withColor(Formatting.DARK_GRAY)));
            }
        }
        List<Identifier> tags = item.getTags();
        if (!tags.isEmpty()) {
            if (tags.size() == 1) {
                text.append("\n").append(new RawText("Tag: " + tags.get(0).getPath(), Style.EMPTY.withColor(Formatting.DARK_GRAY)));
            } else {
                text.append("\n").append(new RawText("Tags: " + String.join(", ", tags.stream().map(Identifier::getPath).toList()), Style.EMPTY.withColor(Formatting.DARK_GRAY)));
            }
        }
        List<String> flags = item.getFlags();
        if (!flags.isEmpty()) {
            if (flags.size() == 1) {
                text.append("\n").append(new RawText("Flag: " + flags.get(0), Style.EMPTY.withColor(Formatting.DARK_GRAY)));
            } else {
                text.append("\n").append(new RawText("Flags: " + String.join(", ", flags), Style.EMPTY.withColor(Formatting.DARK_GRAY)));
            }
        }
        hoverComponent = new TextComponent(parent, 200, -1, text);
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
        if (this.isHovered() && hoverComponent != null && getStack() != null && !getStack().getItem().equals(Items.AIR)) {
            y = y + 18;
            Dimensions screen = Dimensions.getScreen();
            Rectangle bounds = hoverComponent.getBoundingBox();
            if (y + bounds.height() > screen.getHeight()) {
                y = screen.getHeight() - bounds.height();
            }
            if (x + bounds.width() > screen.getWidth()) {
                x = screen.getWidth() - bounds.width();
            }
            hoverComponent.render(matrices, renderBounds, x, y, mouseX, mouseY);
        }
    }
}
