package io.github.darkkronicle.refinedcreativeinventory.config;


import com.electronwill.nightconfig.core.Config;
import com.electronwill.nightconfig.core.file.FileConfig;
import io.github.darkkronicle.darkkore.DarkKore;
import io.github.darkkronicle.darkkore.config.ModConfig;
import io.github.darkkronicle.darkkore.config.impl.NightFileObject;
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

public class TabsConfig extends ModConfig {

    private final static TabsConfig INSTANCE = new TabsConfig();

    public static TabsConfig getInstance() {
        return INSTANCE;
    }

    @Override
    public File getFile() {
        return new File(CreativeInventoryConfig.getConfigDirectory(), "tabs.toml");
    }

    @Override
    public List<Option<?>> getOptions() {
        return null;
    }

    @Override
    public void save() {
        setupFileConfig();
        config.load();

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
        config = new NightFileObject(FileConfig.of(getFile()));
    }

    @Override
    public void rawLoad() {
        config.load();
        List<InventoryItem> items = new ArrayList<>();
        List<Config> confs = config.getConfig().get("items");
        if (confs == null) {
            config.close();
            ItemHolder.getInstance().setDefaults();
            return;
        }
        for (Config c : confs) {
            ItemStack stack = ItemSerializer.deserialize(c.get("stack"));
            List<String> tags = c.get("tags");
            items.add(new BasicInventoryItem(stack, tags));
        }
        config.close();
        if (items.size() > 0) {
            ItemHolder.getInstance().setItems(items);
        } else {
            ItemHolder.getInstance().setDefaults();
        }
    }

    @Override
    public void addOption(Option<?> option) {

    }

}
