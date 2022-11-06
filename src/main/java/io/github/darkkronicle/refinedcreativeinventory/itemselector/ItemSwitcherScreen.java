package io.github.darkkronicle.refinedcreativeinventory.itemselector;

import io.github.darkkronicle.darkkore.gui.ComponentScreen;
import io.github.darkkronicle.darkkore.gui.components.Component;
import io.github.darkkronicle.darkkore.gui.components.transform.ListComponent;
import io.github.darkkronicle.darkkore.gui.components.transform.PositionedComponent;
import io.github.darkkronicle.darkkore.util.Color;
import io.github.darkkronicle.darkkore.util.PositionedRectangle;
import io.github.darkkronicle.darkkore.util.Rectangle;
import io.github.darkkronicle.darkkore.util.render.RenderUtil;
import io.github.darkkronicle.refinedcreativeinventory.gui.components.CustomInventoryItemComponent;
import io.github.darkkronicle.refinedcreativeinventory.items.BasicInventoryItem;
import io.github.darkkronicle.refinedcreativeinventory.items.InventoryItem;
import io.github.darkkronicle.refinedcreativeinventory.items.ItemHolder;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import org.lwjgl.glfw.GLFW;

import java.util.List;
import java.util.function.Consumer;


public class ItemSwitcherScreen extends ComponentScreen {

    private final Consumer<ItemStack> callback;
    private final ItemStack startingItem;
    private double lastMouseX = 0;
    private double lastMouseY = 0;

    public ItemSwitcherScreen(ItemStack startingItem, Consumer<ItemStack> callback) {
        super();
        this.startingItem = startingItem;
        setBackgroundColor(new Color(0, 0, 0, 0));
        this.callback = callback;
    }

    public void forceClose() {
        mouseClicked(lastMouseX, lastMouseY, 0);
        close();
    }

    @Override
    public void renderComponents(MatrixStack matrices, int mouseX, int mouseY) {
        lastMouseX = mouseX;
        lastMouseY = mouseY;
        super.renderComponents(matrices, mouseX, mouseY);
    }

    public PositionedComponent getCenterComponent() {
        Component base = createComponent(ItemHolder.getInstance().get(startingItem).orElseGet(() -> new BasicInventoryItem(startingItem)));
        Rectangle bounds = base.getBoundingBox();
        return new PositionedComponent(this, base, (width - bounds.width()) / 2, (height - bounds.height()) / 2);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            // Exit out
            forceClose();
            return true;
        }
        InputUtil.Key key = InputUtil.fromKeyCode(keyCode, scanCode);
        KeyBinding.setKeyPressed(key, true);
        KeyBinding.onKeyPressed(key);
        return true;
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        InputUtil.Key key = InputUtil.fromKeyCode(keyCode, scanCode);
        KeyBinding.setKeyPressed(key, false);
        return false;
    }

    @Override
    public void initImpl() {
        addComponent(getCenterComponent());
        int radius = 50;
        for (List<InventoryItem> items : ItemSwitcherHandler.getInstance().getStacks(startingItem)) {
            int trueRadius = radius + 12;
            Radial radial = new Radial(this, items.size(), radius, trueRadius, 25);
            radial.setCircleBackgroundColor(new Color(0x28222222));
            for (InventoryItem item : items) {
                radial.addComponent(createComponent(item));
            }
            addComponent(new PositionedComponent(this, radial, width / 2 - trueRadius, height / 2 - trueRadius));
            radius += 30;
        }
        ListComponent modifiers = new ListComponent(this, -1, 20, false);
        for (ItemModifier modifier : ItemSwitcherHandler.getInstance().getModifiers()) {
            modifiers.addComponent(modifier.getComponent(this, callback, startingItem));
        }
        if (modifiers.getComponents().size() > 0) {
            PositionedComponent positioned = new PositionedComponent(this, modifiers, (width - modifiers.getWidth()) / 2, 10);
            addComponent(positioned);
        }
        ItemSwitcherHandler.getInstance().setCurrentScreen(this);
    }

    protected Component createComponent(InventoryItem item) {
        return new CustomInventoryItemComponent(ItemSwitcherScreen.this, item) {
            @Override
            public boolean mouseClickedImpl(int x, int y, int mouseX, int mouseY, int button) {
                close();
                callback.accept(item.getStack());
                return true;
            }

            @Override
            public void render(MatrixStack matrices, PositionedRectangle renderBounds, int x, int y, int mouseX, int mouseY) {
                if (isHovered()) {
                    RenderUtil.drawCircle(matrices, x + getBoundingBox().width() / 2f, y + getBoundingBox().height() / 2f, 11, new Color(150, 150, 150, 170).color());
                } else {
                    RenderUtil.drawCircle(matrices, x + getBoundingBox().width() / 2f, y + getBoundingBox().height() / 2f, 11, new Color(20, 20, 20, 170).color());
                }
                super.render(matrices, renderBounds, x, y, mouseX, mouseY);
            }

            @Override
            public void postRender(MatrixStack matrices, PositionedRectangle renderBounds, int x, int y, int mouseX, int mouseY) {
                if (isHovered()) {
                    matrices.translate(0, 0, 500);
                    Rectangle rect = getBoundingBox();
                    int width1 = client.textRenderer.getWidth(item.getStack().getName());
                    fill(matrices, x + rect.width() / 2 - width1 / 2 - 2, y - 11, x + rect.width() / 2 + width1 / 2 + 2, y - 1, 0xAA000000);
                    drawCenteredText(matrices, client.textRenderer, item.getStack().getName(), x + rect.width() / 2, y - 10, -1);
                    matrices.translate(0, 0, -500);
                }
            }
        };
    }

    @Override
    public void close() {
        super.close();
        ItemSwitcherHandler.getInstance().setCurrentScreen(null);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
