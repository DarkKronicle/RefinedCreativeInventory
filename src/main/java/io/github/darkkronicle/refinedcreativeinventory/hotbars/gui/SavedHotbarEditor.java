package io.github.darkkronicle.refinedcreativeinventory.hotbars.gui;

import io.github.darkkronicle.darkkore.gui.ConfigScreen;
import io.github.darkkronicle.darkkore.gui.components.impl.ButtonComponent;
import io.github.darkkronicle.darkkore.gui.components.transform.PositionedComponent;
import io.github.darkkronicle.darkkore.util.Color;
import io.github.darkkronicle.darkkore.util.StringUtil;
import io.github.darkkronicle.refinedcreativeinventory.hotbars.HotbarProfile;
import io.github.darkkronicle.refinedcreativeinventory.hotbars.SavedHotbar;
import io.github.darkkronicle.refinedcreativeinventory.items.ItemHolder;
import net.minecraft.client.gui.screen.Screen;

import java.util.ArrayList;

public class SavedHotbarEditor extends ConfigScreen {

    private final SavedHotbar hotbar;
    private final HotbarProfileComponent profile;

    public SavedHotbarEditor(Screen parent, HotbarProfileComponent profile, SavedHotbar hotbar) {
        super(new ArrayList<>());
        this.hotbar = hotbar;
        this.profile = profile;
        setParent(parent);
    }

    @Override
    public void initImpl() {
        super.initImpl();
        ButtonComponent delete = new ButtonComponent(
                this,
                StringUtil.translateToText("rci.itemedit.delete"),
                new Color(100, 100, 100, 100),
                new Color(150, 150, 150, 150),
                (button) -> {
                    profile.getProfile().remove(hotbar);
                    close();
                    profile.updateHotbars();
                }
        );
        addComponent(new PositionedComponent(
                this,
                delete,
                10,
                10,
                -1,
                -1
        ));
    }
}
