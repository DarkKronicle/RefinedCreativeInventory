package io.github.darkkronicle.refinedcreativeinventory.tabs;

import io.github.darkkronicle.darkkore.gui.components.BasicComponent;
import io.github.darkkronicle.darkkore.gui.components.Component;
import io.github.darkkronicle.darkkore.gui.components.impl.ItemComponent;
import io.github.darkkronicle.darkkore.gui.components.impl.TextComponent;
import io.github.darkkronicle.darkkore.gui.components.transform.ListComponent;
import io.github.darkkronicle.darkkore.util.Color;
import io.github.darkkronicle.darkkore.util.Dimensions;
import io.github.darkkronicle.refinedcreativeinventory.gui.InventoryScreen;
import io.github.darkkronicle.refinedcreativeinventory.items.InventoryItem;
import io.github.darkkronicle.refinedcreativeinventory.items.ItemHolder;
import io.github.darkkronicle.refinedcreativeinventory.search.ItemSearch;
import io.github.darkkronicle.refinedcreativeinventory.search.KonstructSearch;
import lombok.Setter;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.ItemGroup;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class FilterTab implements ItemTab {

    protected Function<Screen, BasicComponent> icon;
    protected ItemSearch search;
    protected Text name;
    @Setter protected int order;

    public FilterTab(Text name, Function<Screen, BasicComponent> icon, ItemSearch search, int order) {
        this.icon = icon;
        this.search = search;
        this.name = name;
        this.order = order;
    }

    @Override
    public BasicComponent getIcon(InventoryScreen parent) {
        return icon.apply(parent);
    }

    @Override
    public List<InventoryItem> getItems() {
        if (search != null) {
            return search.search(ItemHolder.getInstance().getAllItems());
        } else {
            return ItemHolder.getInstance().getAllItems();
        }
    }

    @Override
    public Integer getOrder() {
        return order;
    }

    @Override
    public List<Component> getComponents(InventoryScreen screen, Dimensions bounds) {
        List<Component> components = new ArrayList<>();
        components.add(new TextComponent(screen, name));
        ListComponent list = new ListComponent(screen, bounds.getWidth(), -1, false);
        for (Component c : ItemTab.super.getComponents(screen, bounds)) {
            list.addComponent(c);
        }
        components.add(list);
        return components;
    }

    public static FilterTab fromGroup(Screen parent, ItemGroup group, int order) {
        return new FilterTab(group.getDisplayName(), (screen) -> {
            ItemComponent icon = new ItemComponent(screen, group.getIcon());
            icon.setOnHoveredConsumer(button -> button.setBackgroundColor(new Color(200, 200, 200, 200)));
            icon.setOnHoveredStoppedConsumer(button -> button.setBackgroundColor(null));
            return icon;
        }, KonstructSearch.fromString("item.group('" + group.getName() + "') and not(item.flag('hidden'))"), order);
    }

}
