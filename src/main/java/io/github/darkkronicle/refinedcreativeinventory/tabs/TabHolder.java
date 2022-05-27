package io.github.darkkronicle.refinedcreativeinventory.tabs;

import lombok.Getter;
import lombok.Setter;
import net.minecraft.item.ItemGroup;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TabHolder {

    private final static TabHolder INSTANCE = new TabHolder();

    public static TabHolder getInstance() {
        return INSTANCE;
    }

    @Getter @Setter private List<ItemTab> tabs = new ArrayList<>();

    private TabHolder() {}

    public void addTab(ItemTab tab) {
        this.tabs.add(tab);
        Collections.sort(tabs);
    }

    public void setVanilla() {
        int i = 0;
        for (ItemGroup group : ItemGroup.GROUPS) {
            i++;
            if (group.equals(ItemGroup.HOTBAR) || group.equals(ItemGroup.SEARCH) || group.equals(ItemGroup.INVENTORY)) {
                continue;
            }
            addTab(CustomTab.fromGroup(group, i));
        }
        addTab(new InventoryTab());
        Collections.sort(tabs);
    }
}
