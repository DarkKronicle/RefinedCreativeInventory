package io.github.darkkronicle.refinedcreativeinventory.tabs;

import io.github.darkkronicle.darkkore.gui.components.BasicComponent;
import io.github.darkkronicle.darkkore.gui.components.Component;
import io.github.darkkronicle.darkkore.gui.components.impl.ButtonComponent;
import io.github.darkkronicle.darkkore.gui.components.impl.IconButtonComponent;
import io.github.darkkronicle.darkkore.gui.components.impl.ItemComponent;
import io.github.darkkronicle.darkkore.gui.components.impl.TextComponent;
import io.github.darkkronicle.darkkore.gui.components.transform.ListComponent;
import io.github.darkkronicle.darkkore.util.Color;
import io.github.darkkronicle.darkkore.util.Dimensions;
import io.github.darkkronicle.refinedcreativeinventory.gui.InventoryScreen;
import io.github.darkkronicle.refinedcreativeinventory.items.InventoryItem;
import io.github.darkkronicle.refinedcreativeinventory.items.ItemHolder;
import io.github.darkkronicle.refinedcreativeinventory.search.ItemSearch;
import io.github.darkkronicle.refinedcreativeinventory.tabs.ItemTab;
import net.minecraft.item.ItemGroup;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class FilterTab implements ItemTab {

    private Supplier<BasicComponent> icon;
    private Map<ItemSearch.SearchFilter, String> query;
    private Text name;

    public FilterTab(Text name, Supplier<BasicComponent> icon, Map<ItemSearch.SearchFilter, String> query) {
        this.icon = icon;
        this.query = query;
        this.name = name;
    }

    @Override
    public BasicComponent getIcon() {
        return icon.get();
    }

    @Override
    public List<InventoryItem> getItems() {
        return new ItemSearch(query).search(ItemHolder.getInstance().getAllItems());
    }

    @Override
    public List<Component> getComponents(InventoryScreen screen, Dimensions bounds) {
        List<Component> components = new ArrayList<>();
        components.add(new TextComponent(name));
        ListComponent list = new ListComponent(bounds.getWidth(), -1, false);
        for (Component c : ItemTab.super.getComponents(screen, bounds)) {
            list.addComponent(c);
        }
        components.add(list);
        return components;
    }

    public static FilterTab fromGroup(ItemGroup group) {
        return new FilterTab(group.getDisplayName(), () -> {
            ItemComponent icon = new ItemComponent(group.getIcon());
            icon.setOnHoveredConsumer(button -> button.setBackgroundColor(new Color(200, 200, 200, 200)));
            icon.setOnHoveredStoppedConsumer(button -> button.setBackgroundColor(null));
            return icon;
        }, Map.of(ItemSearch.SearchFilter.GROUP, group.getName()));
    }

}
