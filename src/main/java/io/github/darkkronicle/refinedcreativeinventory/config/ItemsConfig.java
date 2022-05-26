package io.github.darkkronicle.refinedcreativeinventory.config;


import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.FileConfig;
import io.github.darkkronicle.darkkore.DarkKore;
import io.github.darkkronicle.darkkore.config.ModConfig;
import io.github.darkkronicle.darkkore.config.impl.ConfigObject;
import io.github.darkkronicle.darkkore.config.impl.JsonFileObject;
import io.github.darkkronicle.darkkore.config.options.Option;
import io.github.darkkronicle.refinedcreativeinventory.items.InventoryItem;
import io.github.darkkronicle.refinedcreativeinventory.items.ItemHolder;
import io.github.darkkronicle.refinedcreativeinventory.items.BasicInventoryItem;
import io.github.darkkronicle.refinedcreativeinventory.util.ItemSerializer;
import net.minecraft.item.ItemStack;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ItemsConfig extends ModConfig {

    private final static ItemsConfig INSTANCE = new ItemsConfig();

    public static ItemsConfig getInstance() {
        return INSTANCE;
    }

    @Override
    public File getFile() {
        return new File(CreativeInventoryConfig.getConfigDirectory(), "items.json");
    }

    @Override
    public List<Option<?>> getOptions() {
        return null;
    }

    @Override
    public void save() {
        setupFileConfig();
        config.load();
        List<ConfigObject> confs = new ArrayList<>();
        for (InventoryItem item : ItemHolder.getInstance().getAllItems()) {
            if (item instanceof BasicInventoryItem) {
                if (!item.isCustom() && item.getFlags().isEmpty()) {
                    continue;
                }
                ConfigObject nest = config.getConfig().createNew();
                ItemSerializer.serialize(nest, item.getStack());
                nest.set("flags", item.getFlags());
                nest.set("custom", item.isCustom());
                confs.add(nest);
            }
        }
        config.getConfig().set("items", confs);
        config.save();
        config.close();
    }


    @Override
    public void setupFileConfig() {
        if (!getFile().exists()) {
            try {
                getFile().getParentFile().mkdirs();
                getFile().createNewFile();
            } catch (IOException e) {
                DarkKore.LOGGER.error("Couldn't initialize config!", e);
            }
        }
        config = new JsonFileObject(getFile());
    }

    @Override
    public void rawLoad() {
        ItemHolder.getInstance().setDefaults();
        config.load();
        List<ConfigObject> confs = config.getConfig().get("items");
        if (confs == null) {
            config.close();
            return;
        }
        for (ConfigObject c : confs) {
            ItemStack stack = ItemSerializer.deserialize(c);
            List<String> flags = c.get("flags");
            InventoryItem item = ItemHolder.getInstance().getOrCreate(stack);
            for (String flag : flags) {
                item.addFlag(flag);
            }
            item.setCustom(c.get("custom"));
        }
        config.close();
    }

    @Override
    public void addOption(Option<?> option) {

    }

}
