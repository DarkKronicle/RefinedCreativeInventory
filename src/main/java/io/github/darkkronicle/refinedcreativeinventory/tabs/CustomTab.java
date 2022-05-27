package io.github.darkkronicle.refinedcreativeinventory.tabs;

import com.google.common.collect.ImmutableList;
import io.github.darkkronicle.darkkore.config.options.BooleanOption;
import io.github.darkkronicle.darkkore.config.options.Option;
import io.github.darkkronicle.darkkore.config.options.StringOption;
import io.github.darkkronicle.darkkore.gui.OptionComponentHolder;
import io.github.darkkronicle.darkkore.gui.components.BasicComponent;
import io.github.darkkronicle.darkkore.gui.components.Component;
import io.github.darkkronicle.darkkore.gui.components.impl.ItemComponent;
import io.github.darkkronicle.darkkore.util.Color;
import io.github.darkkronicle.darkkore.util.FluidText;
import io.github.darkkronicle.refinedcreativeinventory.gui.InventoryScreen;
import io.github.darkkronicle.refinedcreativeinventory.gui.tabeditor.TabEditorScreen;
import io.github.darkkronicle.refinedcreativeinventory.search.BasicItemSearch;
import io.github.darkkronicle.refinedcreativeinventory.search.KonstructSearch;
import lombok.Getter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public class CustomTab extends FilterTab {

    @Getter private final StringOption nameOption = new StringOption("name", "rci.tabeditor.name", "rci.tabeditor.info.name", "Cool Tab") {
        @Override
        public void setValue(String value) {
            super.setValue(value);
            updateName();
        }
    };

    @Getter private final StringOption item = new StringOption(
            "item",
            "rci.tabeditor.item",
            "rci.tabeditor.info.item", "minecraft:stone",
            "rci.tabeditor.itemtype", string -> Registry.ITEM.containsId(new Identifier(string))
    );

    @Getter private final BooleanOption basicSearch = new BooleanOption("basicSearch", "rci.tabeditor.basicSearch", "rci.tabeditor.info.basicSearch", true);

    @Getter private final StringOption searchOption = new StringOption("search", "rci.tabeditor.search", "rci.tabeditor.info.search", "flag:util") {
        @Override
        public void setValue(String value) {
            super.setValue(value);
            updateSearch();
        }
    };

    @Getter private final List<Option<?>> options = ImmutableList.of(nameOption, item, basicSearch, searchOption);

    private void updateName() {
        name = new FluidText(nameOption.getValue());
    }

    private void updateSearch() {
        if (basicSearch.getValue()) {
            search = BasicItemSearch.fromQuery(searchOption.getValue());
        } else {
            search = KonstructSearch.fromString(searchOption.getValue());
        }
    }

    public CustomTab(String name, ItemStack icon, String query, boolean basicSearch) {
        super(new FluidText(name), () -> {
            ItemComponent comp = new ItemComponent(icon);
            comp.setOnHoveredConsumer(button -> button.setBackgroundColor(new Color(200, 200, 200, 200)));
            comp.setOnHoveredStoppedConsumer(button -> button.setBackgroundColor(null));
            return comp;
        }, null);
        this.nameOption.setValue(name);
        this.basicSearch.setValue(basicSearch);
        this.searchOption.setValue(query);
        this.item.setValue(Registry.ITEM.getId(icon.getItem()).toString());
    }

    public List<Component> getOptionComponents(int width) {
        List<Component> components = new ArrayList<>();
        components.add(OptionComponentHolder.getInstance().convert(nameOption, width));
        components.add(OptionComponentHolder.getInstance().convert(item, width));
        components.add(OptionComponentHolder.getInstance().convert(basicSearch, width));
        components.add(OptionComponentHolder.getInstance().convert(searchOption, width));
        return components;
    }

    @Override
    public BasicComponent getIcon(InventoryScreen parent) {
        CustomTab tab = this;
        ItemComponent comp = new ItemComponent(Registry.ITEM.get(new Identifier(item.getValue()))) {
            @Override
            public boolean mouseClickedImpl(int x, int y, int mouseX, int mouseY, int button) {
                if (button == 1) {
                    MinecraftClient.getInstance().setScreen(new TabEditorScreen(parent, tab));
                    return true;
                }
                return false;
            }
        };
        comp.setOnHoveredConsumer(button -> button.setBackgroundColor(new Color(200, 200, 200, 200)));
        comp.setOnHoveredStoppedConsumer(button -> button.setBackgroundColor(null));
        return comp;
    }

    public static CustomTab fromGroup(ItemGroup group) {
        return new CustomTab(group.getDisplayName().getString(), group.getIcon(), "item.group('" + group.getName() + "') and not(item.flag('hidden'))", false);
    }
}
