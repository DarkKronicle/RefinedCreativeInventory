package io.github.darkkronicle.refinedcreativeinventory.tabs;

import io.github.darkkronicle.darkkore.gui.components.impl.ItemComponent;
import io.github.darkkronicle.darkkore.util.Color;
import io.github.darkkronicle.darkkore.util.FluidText;
import lombok.Getter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TabHolder {

    private final static TabHolder INSTANCE = new TabHolder();

    public static TabHolder getInstance() {
        return INSTANCE;
    }

    @Getter  private List<ItemTab> tabs = new ArrayList<>();

    private TabHolder() {}

    public void addTab(ItemTab tab) {
        this.tabs.add(tab);
    }

    public void setVanilla() {
        addTab(new FilterTab(new FluidText("All"), () -> {
            ItemComponent icon = new ItemComponent(new ItemStack(Items.NETHER_STAR));
            icon.setOnHoveredConsumer(button -> button.setBackgroundColor(new Color(200, 200, 200, 200)));
            icon.setOnHoveredStoppedConsumer(button -> button.setBackgroundColor(null));
            return icon;
        }, Map.of()));
        for (ItemGroup group : ItemGroup.GROUPS) {
            if (group.equals(ItemGroup.HOTBAR) || group.equals(ItemGroup.SEARCH) || group.equals(ItemGroup.INVENTORY)) {
                continue;
            }
            addTab(FilterTab.fromGroup(group));
        }
    }
}
