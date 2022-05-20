package io.github.darkkronicle.refinedcreativeinventory.util;

import com.electronwill.nightconfig.core.Config;
import lombok.experimental.UtilityClass;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@UtilityClass
public class ItemSerializer {

    public ItemStack deserialize(Config config) {
        Identifier identifier = new Identifier(config.get("name"));
        Item item = Registry.ITEM.get(identifier);
        return new ItemStack(item);
    }

    public void serialize(Config config, ItemStack stack) {
        Identifier identifier = Registry.ITEM.getId(stack.getItem());
        config.set("name", identifier.toString());
    }

}
