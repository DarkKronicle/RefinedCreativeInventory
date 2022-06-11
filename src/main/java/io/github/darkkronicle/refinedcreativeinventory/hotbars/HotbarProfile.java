package io.github.darkkronicle.refinedcreativeinventory.hotbars;

import io.github.darkkronicle.darkkore.config.impl.ConfigObject;
import io.github.darkkronicle.darkkore.config.options.Option;
import io.github.darkkronicle.darkkore.config.options.StringOption;
import io.github.darkkronicle.darkkore.intialization.Saveable;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

import java.util.ArrayList;
import java.util.List;

public class HotbarProfile implements Saveable {

    @Getter private List<SavedHotbar> hotbars = new ArrayList<>();
    private int current = 0;

    @Setter @Getter private int mainOne = -1;
    @Setter @Getter private int mainTwo = -1;

    @Getter private final StringOption name = new StringOption("name", "rci.profileeditor.name", "rci.profileeditor.info.name", "Profile");
    @Getter private final StringOption stack = new StringOption(
            "item",
            "rci.profileeditor.item",
            "rci.profileeditor.info.item", "minecraft:stone",
            "rci.option.info.item", string -> Registry.ITEM.containsId(new Identifier(string))
    );

    public HotbarProfile() {

    }

    public void add(SavedHotbar hotbar) {
        hotbars.add(hotbar);
    }

    public void remove(SavedHotbar hotbar) {
        hotbars.remove(hotbar);
    }

    public void setCurrent(int index) {
        current = index;
    }

    public SavedHotbar getCurrent() {
        if (current < 0) {
            return hotbars.get(0);
        }
        if (current >= hotbars.size()) {
            return hotbars.get(hotbars.size() - 1);
        }
        return hotbars.get(current);
    }

    public void applyMainOne() {
        if (mainOne >= 0 && mainOne < hotbars.size()) {
            hotbars.get(mainOne).apply();
            current = mainOne;
        }
    }

    public void applyMainTwo() {
        if (mainTwo >= 0 && mainTwo < hotbars.size()) {
            hotbars.get(mainTwo).apply();
            current = mainTwo;
        }
    }

    public void cycle(boolean forward) {
        current = (current + (forward ? 1 : -1)) % hotbars.size();
        if (current < 0) {
            current = hotbars.size() - 1;
        }
    }

    @Override
    public void save(ConfigObject object) {
        List<ConfigObject> savedHotbars = new ArrayList<>();
        for (SavedHotbar hotbar : hotbars) {
            ConfigObject nest = object.createNew();
            hotbar.save(nest);
            savedHotbars.add(nest);
        }
        object.set("main1", mainOne);
        object.set("main2", mainTwo);
        for (Option<?> option : getOptions()) {
            option.save(object);
        }
        object.set("hotbars", savedHotbars);
    }

    @Override
    public void load(ConfigObject object) {
        for (Option<?> option : getOptions()) {
            option.load(object);
        }
        List<ConfigObject> savedHotbars = object.get("hotbars");
        hotbars = new ArrayList<>();
        mainOne = ((Number) object.get("main1")).intValue();
        mainTwo = ((Number) object.get("main2")).intValue();
        for (ConfigObject nest : savedHotbars) {
            SavedHotbar toLoad = new SavedHotbar();
            toLoad.load(nest);
            hotbars.add(toLoad);
        }
    }

    public List<Option<?>> getOptions() {
        return List.of(name, stack);
    }

    public int indexOf(SavedHotbar hotbar) {
        return hotbars.indexOf(hotbar);
    }
}
