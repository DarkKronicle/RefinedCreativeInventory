package io.github.darkkronicle.refinedcreativeinventory.itemselector;

import io.github.darkkronicle.darkkore.gui.ComponentScreen;
import io.github.darkkronicle.darkkore.gui.components.Component;
import io.github.darkkronicle.darkkore.gui.components.transform.PositionedComponent;
import io.github.darkkronicle.darkkore.util.Rectangle;
import io.github.darkkronicle.darkkore.util.render.RenderUtil;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.List;

public class RadialScreen extends ComponentScreen {

    @Getter
    @Setter
    private int startingRadius = 50;

    @Getter
    @Setter
    private int increasingRadius = 30;

    @Getter
    private List<Radial> radials = new ArrayList<>();

    private List<List<PositionedComponent>> built;

    public RadialScreen() {

    }

    public RadialScreen(List<Radial> radials, int startingRadius, int increasingRadius) {
        this.startingRadius = startingRadius;
        this.increasingRadius = increasingRadius;
        this.radials = radials;
    }

    @Override
    public void initImpl() {
        built = new ArrayList<>();
        int radius = startingRadius;
        if (getRadials().size() == 0) {
            close();
            return;
        }
        for (Radial radial : getRadials()) {
            addRadial(width / 2, height / 2, radius, radial);
            radius += increasingRadius;
        }
    }

    private PositionedComponent getClosestComponent(float x, float y) {
        // x = rcos(theta)
        // y = rsin(theta)
        // r = sqrt(x^2 + y^2)
        // theta = arctan(x/y)
        float centerX = width / 2f;
        float centerY = height / 2f;
        float offX = x - centerX;
        float offY = y - centerY;
        float r = (float) Math.sqrt(offX * offX + offY * offY);
        float theta;
        if (offX == 0 && offY == 0) {
            theta = 0;
        } else if (offX == 0) {
            theta = offY > 0 ? 90 : -90;
        } else if (offY == 0) {
            theta = offX > 0 ? 0 : 180;
        } else {
            // x = rcos(theta)
            // x/r = cos(theta)
            // or theta = arctan(y / x), then have to flip sign since negative radius
            theta = (float) Math.toDegrees(Math.atan(offY / offX));
            if (offX < 0) {
                theta = theta + 180;
            }
        }
        while (theta < 0) {
            theta += 360;
        }

        int currentR = startingRadius - increasingRadius / 2;
        // Start at the last one
        List<Radial> radialList = getRadials();
        Radial found = getRadials().get(radialList.size() - 1);
        int index = radialList.size() - 1;
        for (int i = 0; i < radialList.size(); i++) {
            Radial radial = radialList.get(i);
            if (radial == found) {
                // We're at the end
                break;
            }
            currentR += increasingRadius;
            if (r <= currentR) {
                index = i;
                found = radial;
                break;
            }
        }
        float anglesBetween = 360f / found.amount();
        List<PositionedComponent> components = built.get(index);
        if (anglesBetween >= 360) {
            return components.get(0);
        }
        for (int i = 0; i < components.size(); i++) {
            PositionedComponent component = components.get(i);
            // Let us start at the top
            float degree = anglesBetween * i + 90 - anglesBetween / 2;
            degree %= 360;
            if (theta >= degree % 360 && theta < degree + anglesBetween) {
                return component;
            }
        }
        return null;
    }

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float partialTicks) {
        PositionedComponent found = getClosestComponent(mouseX, mouseY);
        if (found == null) {
            // This shouldn't happen, but ok
            super.render(matrices, mouseX, mouseY, partialTicks);
            return;
        }
        Rectangle bounds = found.getBoundingBox();
        super.render(matrices, found.getXOffset() + bounds.width() / 2, found.getYOffset() + bounds.height() / 2, partialTicks);
    }

    private void addRadial(int centerX, int centerY, int radius, Radial radial) {
        float anglesBetween = 360f / radial.amount();
        List<Component> components = radial.components();
        List<PositionedComponent> comps = new ArrayList<>();
        for (int i = 0; i < components.size(); i++) {
            Component component = components.get(i);
            // Let us start at the top
            float degree = anglesBetween * i + 90;
            double rad = Math.toRadians(degree);

            int xOffset = (int) (radius * Math.cos(rad));
            int yOffset = (int) (radius * Math.sin(rad));

            Rectangle bounds = component.getBoundingBox();
            PositionedComponent pos = new PositionedComponent(this, component, centerX + xOffset - bounds.width() / 2, centerY + yOffset - bounds.height() / 2);
            addComponent(pos);
            comps.add(pos);
        }
        built.add(comps);
    }

}
