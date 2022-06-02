package io.github.darkkronicle.refinedcreativeinventory.config;


import io.github.darkkronicle.darkkore.DarkKore;
import io.github.darkkronicle.darkkore.config.ModConfig;
import io.github.darkkronicle.darkkore.config.impl.ConfigObject;
import io.github.darkkronicle.darkkore.config.impl.JsonFileObject;
import io.github.darkkronicle.darkkore.config.options.Option;
import io.github.darkkronicle.darkkore.util.FileUtil;
import io.github.darkkronicle.refinedcreativeinventory.items.InventoryItem;
import io.github.darkkronicle.refinedcreativeinventory.items.ItemHolder;
import io.github.darkkronicle.refinedcreativeinventory.items.BasicInventoryItem;
import io.github.darkkronicle.refinedcreativeinventory.util.ItemSerializer;
import net.minecraft.item.ItemStack;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
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
    public void setupFileConfig() {
        if (!getFile().exists()) {
            try {
                getFile().getParentFile().mkdirs();
                getFile().createNewFile();
                try {
                    try(OutputStream outputStream = new FileOutputStream(getFile())) {
                        IOUtils.copy( FileUtil.getResource("default_items.json"), outputStream);
                    }
                } catch (URISyntaxException exception) {
                    DarkKore.LOGGER.error("Couldn't copy over default_items.json!", exception);
                }
            } catch (IOException e) {
                DarkKore.LOGGER.error("Couldn't initialize config!", e);
            }
        }
        config = new JsonFileObject(getFile());
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
                saveInventoryItem(nest, item);
                confs.add(nest);
            }
        }
        config.getConfig().set("items", confs);
        config.save();
        config.close();
    }

    @Override
    public void rawLoad() {
        ItemHolder.getInstance().setDefaults();
        config.load();
        if (config.getConfig() == null) {
            config.close();
            return;
        }
        List<ConfigObject> confs = config.getConfig().get("items");
        if (confs == null) {
            config.close();
            return;
        }
        for (ConfigObject c : confs) {
            loadInventoryItem(c, true);
        }
        config.close();
    }

    public static void saveInventoryItem(ConfigObject nest, InventoryItem item) {
        ItemSerializer.serialize(nest, item.getStack());
        nest.set("flags", item.getFlags());
        nest.set("custom", item.isCustom());
    }

    public static InventoryItem loadInventoryItem(ConfigObject nest, boolean add) {
        ItemStack stack = ItemSerializer.deserialize(nest);
        List<String> flags = nest.get("flags");
        InventoryItem item = add ? ItemHolder.getInstance().getOrCreate(stack) : ItemHolder.getInstance().get(stack).orElseGet(() -> new BasicInventoryItem(stack));
        for (String flag : flags) {
            item.addFlag(flag);
        }
        item.setCustom(nest.get("custom"));
        return item;
    }

    @Override
    public void addOption(Option<?> option) {

    }

}
