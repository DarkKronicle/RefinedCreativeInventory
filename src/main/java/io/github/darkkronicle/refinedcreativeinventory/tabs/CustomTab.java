package io.github.darkkronicle.refinedcreativeinventory.tabs;

import com.google.common.collect.ImmutableList;
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
import lombok.Getter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CustomTab extends FilterTab {

    @Getter private final StringOption nameOption = new StringOption("name", "rci.tabeditor.name", "rci.tabeditor.info.name", "Cool Tab") {
        @Override
        public void setValue(String value) {
            super.setValue(value);
            updateName();
        }
    };

    @Getter private final StringOption searchOption = new StringOption("search", "rci.tabeditor.search", "rci.tabeditor.info.search", "flag:util") {
        @Override
        public void setValue(String value) {
            super.setValue(value);
            updateSearch();
        }
    };

    private ItemStack stack;

    @Getter private final List<Option<?>> options = ImmutableList.of(nameOption, searchOption);

    private void updateName() {
        name = new FluidText(nameOption.getValue());
    }

    private void updateSearch() {
        query = BasicItemSearch.fromQuery(searchOption.getValue()).getParameters();
    }

    public CustomTab(String name, ItemStack icon, Map<BasicItemSearch.SearchFilter, String> query) {
        super(new FluidText(name), () -> {
            ItemComponent comp = new ItemComponent(icon);
            comp.setOnHoveredConsumer(button -> button.setBackgroundColor(new Color(200, 200, 200, 200)));
            comp.setOnHoveredStoppedConsumer(button -> button.setBackgroundColor(null));
            return comp;
        }, query);
        this.stack = icon;
    }

    public List<Component> getOptionComponents(int width) {
        List<Component> components = new ArrayList<>();
        components.add(OptionComponentHolder.getInstance().convert(nameOption, width));
        components.add(OptionComponentHolder.getInstance().convert(searchOption, width));
        return components;
    }

    @Override
    public BasicComponent getIcon(InventoryScreen parent) {
        CustomTab tab = this;
        ItemComponent comp = new ItemComponent(stack) {
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
}
