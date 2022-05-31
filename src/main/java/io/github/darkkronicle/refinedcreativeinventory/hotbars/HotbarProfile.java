package io.github.darkkronicle.refinedcreativeinventory.hotbars;

import io.github.darkkronicle.darkkore.config.impl.ConfigObject;
import io.github.darkkronicle.darkkore.intialization.Saveable;
import io.github.darkkronicle.refinedcreativeinventory.util.ItemSerializer;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;

public class HotbarProfile implements Saveable {

    @Getter private List<SavedHotbar> hotbars = new ArrayList<>();
    private int current = 0;

    @Setter @Getter private int mainOne = -1;
    @Setter @Getter private int mainTwo = -1;

    @Setter @Getter private String name = "Profile";
    @Setter @Getter private ItemStack stack = new ItemStack(Items.STONE);

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
        return hotbars.get(current);
    }

    public void applyMainOne() {
        if (mainOne >= 0) {
            hotbars.get(mainOne).apply();
        }
    }

    public void applyMainTwo() {
        if (mainTwo >= 0) {
            hotbars.get(mainTwo).apply();
        }
    }

    public void cycle(boolean forward) {
        current = (current + (forward ? 1 : -1)) % hotbars.size();
    }

    @Override
    public void save(ConfigObject object) {
        List<ConfigObject> savedHotbars = new ArrayList<>();
        for (SavedHotbar hotbar : hotbars) {
            ConfigObject nest = object.createNew();
            hotbar.save(nest);
            savedHotbars.add(nest);
        }
        object.set("name", name);
        ItemSerializer.serialize(object, stack);
        object.set("hotbars", savedHotbars);
    }

    @Override
    public void load(ConfigObject object) {
        name = object.get("name");
        stack = ItemSerializer.deserialize(object);
        List<ConfigObject> savedHotbars = object.get("hotbars");
        hotbars = new ArrayList<>();
        for (ConfigObject nest : savedHotbars) {
            SavedHotbar toLoad = new SavedHotbar();
            toLoad.load(nest);
            hotbars.add(toLoad);
        }
    }
}
