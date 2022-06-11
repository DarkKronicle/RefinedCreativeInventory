package io.github.darkkronicle.refinedcreativeinventory.hotbars;

import io.github.darkkronicle.darkkore.DarkKore;
import io.github.darkkronicle.darkkore.config.impl.ConfigObject;
import io.github.darkkronicle.darkkore.intialization.Saveable;
import io.github.darkkronicle.refinedcreativeinventory.config.ItemsConfig;
import io.github.darkkronicle.refinedcreativeinventory.items.InventoryItem;
import lombok.Getter;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SavedHotbar implements Saveable {

    private final static ItemStack blank = new ItemStack(Items.AIR);

    @Getter private List<InventoryItem> items;
    private UUID uuid;

    public SavedHotbar() {
        this(new ArrayList<>());
    }

    public SavedHotbar(List<InventoryItem> items) {
        while (items.size() < 9) {
            items.add(null);
        }
        this.items = items;
        this.uuid = UUID.randomUUID();
    }

    public void set(int index, InventoryItem item) {
        items.set(index, item);
    }

    public InventoryItem get(int index) {
        return items.get(index);
    }

    public void apply() {
        MinecraftClient client = MinecraftClient.getInstance();
        for (int i = 0; i < 9; i++) {
            InventoryItem item = items.get(i);
            ItemStack stack = item == null ? blank : item.getStack();
            client.player.getInventory().setStack(i, stack);
            client.interactionManager.clickCreativeStack(stack, 36 + i);
        }
    }

    @Override
    public void save(ConfigObject object) {
        List<ConfigObject> objects = new ArrayList<>();
        for (InventoryItem item : items) {
            ConfigObject nest = object.createNew();
            if (item != null) {
                ItemsConfig.saveInventoryItem(nest, item);
            }
            objects.add(nest);
        }
        object.set("uuid", uuid.toString());
        object.set("hotbar", objects);
    }

    @Override
    public void load(ConfigObject object) {
        List<ConfigObject> objects = object.get("hotbar");
        List<InventoryItem> items = new ArrayList<>();
        for (ConfigObject obj : objects) {
            if (obj.getValues().isEmpty()) {
                items.add(null);
                continue;
            }
            InventoryItem item = ItemsConfig.loadInventoryItem(obj, false, false);
            items.add(item);
        }
        uuid = UUID.fromString(object.get("uuid"));
        this.items = items;
    }
}
