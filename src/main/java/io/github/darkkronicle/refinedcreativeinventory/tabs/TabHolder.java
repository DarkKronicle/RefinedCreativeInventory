package io.github.darkkronicle.refinedcreativeinventory.tabs;

import lombok.Getter;
import net.minecraft.item.ItemGroup;

import java.util.ArrayList;
import java.util.List;

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
        addTab(new AllTab());
        for (ItemGroup group : ItemGroup.GROUPS) {
            if (group.equals(ItemGroup.HOTBAR) || group.equals(ItemGroup.SEARCH) || group.equals(ItemGroup.INVENTORY)) {
                continue;
            }
            addTab(FilterTab.fromGroup(group));
        }
    }
}
