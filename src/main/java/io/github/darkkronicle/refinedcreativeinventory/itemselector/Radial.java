package io.github.darkkronicle.refinedcreativeinventory.itemselector;

import io.github.darkkronicle.darkkore.gui.components.Component;
import io.github.darkkronicle.darkkore.gui.components.transform.MultiComponent;
import io.github.darkkronicle.darkkore.gui.components.transform.PositionedComponent;
import io.github.darkkronicle.darkkore.util.Color;
import io.github.darkkronicle.darkkore.util.PositionedRectangle;
import io.github.darkkronicle.darkkore.util.Rectangle;
import io.github.darkkronicle.darkkore.util.render.RenderUtil;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;

import java.util.Arrays;

public class Radial extends MultiComponent {

    @Getter
    private final int trueRadius;

    @Getter
    private final int radius;

    @Getter
    private final int thickness;

    @Getter
    private final int amount;

    /** Center degrees */
    @Getter
    private final float degrees;

    @Getter @Setter
    private Color circleBackgroundColor = null;

    public Radial(Screen parent, int amount, int radius, int thickness) {
        this(parent, amount, radius, radius, thickness);
    }

    public Radial(Screen parent, int amount, int radius, int trueRadius, int thickness) {
        super(parent, trueRadius * 2, trueRadius * 2);
        this.trueRadius = trueRadius;
        this.radius = radius;
        this.amount = amount;
        this.thickness = thickness;
        this.degrees = 360f / this.amount;
        this.components = Arrays.asList(new Component[amount]);
    }

    @Override
    public void addComponent(Component component) {
        int next = getNextAvailable();
        setComponent(next, component);
    }

    public int getNearestIndex(double offsetX, double offsetY) {
        float theta;
        // Calculate theta and make sure we don't get any divide by zeros
        if (offsetX == 0 && offsetY == 0) {
            theta = 0;
        } else if (offsetX == 0) {
            theta = offsetY > 0 ? 90 : 270;
        } else if (offsetY == 0) {
            theta = offsetX > 0 ? 0 : 180;
        } else {
            // x = rcos(theta)
            // x/r = cos(theta)
            // or theta = arctan(y / x), then have to flip sign since negative radius
            theta = (float) Math.toDegrees(Math.atan(offsetY / offsetX));
            if (offsetX < 0) {
                theta = theta + 180;
            }
            if (theta < 0) {
                theta += 360;
            }
        }
        if (theta >= 360 - (degrees / 2)) {
            return 0;
        }
        for (int i = 0; i < amount; i++) {
            float degree = degrees * i - degrees / 2;
            degree %= 360;
            if (theta >= degree && theta < degree + degrees) {
                return i;
            }
        }
        return -1;
    }

    public double getCompXPos(int index) {
        float innerDegrees = degrees * index;
        double cos = Math.cos(Math.toRadians(innerDegrees));
        return cos * radius;
    }

    public double getCompYPos(int index) {
        float innerDegrees = degrees * index;
        double sin = Math.sin(Math.toRadians(innerDegrees));
        return sin * radius;
    }

    @Override
    public void addComponent(int index, Component component) {
        rotate(1);
        components.set(index, component);
    }

    public void rotate(int amount) {
        Component[] inBetween = new Component[components.size()];
        for (int i = 0; i < components.size(); i++) {
            inBetween[(i + amount) % inBetween.length] = components.get(i);
        }
        components = Arrays.asList(inBetween);
    }

    @Override
    public void setComponent(int index, Component component) {
        Rectangle bounds = component.getBoundingBox();
        double xPos = getCompXPos(index) - bounds.width() / 2f + thickness / 2f;
        double yPos = getCompYPos(index) - bounds.height() / 2f + thickness / 2f;

        PositionedComponent pos = new PositionedComponent(parent, component, (int) xPos + radius, (int) yPos + radius);
        components.set(index, pos);
    }

    @Override
    public void renderComponent(MatrixStack matrices, PositionedRectangle renderBounds, int x, int y, int mouseX, int mouseY) {
        if (circleBackgroundColor != null) {
            RenderUtil.drawRing(matrices, x + trueRadius, y + trueRadius, trueRadius, trueRadius - thickness, circleBackgroundColor.color());
        }
        int index = !isHovered() ? -1 : getNearestIndex(mouseX - x - trueRadius, mouseY - y - trueRadius);
        MinecraftClient.getInstance().textRenderer.draw(matrices, String.valueOf(index), 10, 10, -1);
        if (index == -1) {
            super.renderComponent(matrices, renderBounds, x, y, mouseX, mouseY);
        } else {
            super.renderComponent(matrices, renderBounds, x, y, (int) getCompXPos(index) + 1 + x + trueRadius, (int) getCompYPos(index) + 1 + y + trueRadius);
        }
    }

    @Override
    public boolean checkIfHovered(PositionedRectangle renderBounds, int x, int y, int mouseX, int mouseY) {
        int tX = (mouseX - x) - trueRadius;
        int tY = (mouseY - y) - trueRadius;
        float r = (float) Math.sqrt(tX * tX + tY * tY);
        return r <= trueRadius && r >= trueRadius - thickness;
    }

    protected int getNextAvailable() {
        for (int i = 0; i < components.size(); i++) {
            Component c = components.get(i);
            if (c == null) {
                return i;
            }
        }
        return 0;
    }

}
