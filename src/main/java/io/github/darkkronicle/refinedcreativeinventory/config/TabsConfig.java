package io.github.darkkronicle.refinedcreativeinventory.config;


import io.github.darkkronicle.darkkore.DarkKore;
import io.github.darkkronicle.darkkore.config.ModConfig;
import io.github.darkkronicle.darkkore.config.impl.ConfigObject;
import io.github.darkkronicle.darkkore.config.impl.JsonFileObject;
import io.github.darkkronicle.darkkore.config.options.Option;
import io.github.darkkronicle.refinedcreativeinventory.items.ItemHolder;
import io.github.darkkronicle.refinedcreativeinventory.tabs.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

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
        return new File(CreativeInventoryConfig.getConfigDirectory(), "tabs.json");
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
        List<ConfigObject> tabsSaved = new ArrayList<>();
        for (ItemTab tab : TabHolder.getInstance().getTabs()) {
            if (tab instanceof CustomTab) {
                ConfigObject nest = config.getConfig().createNew();
                for (Option<?> option : ((CustomTab) tab).getOptions()) {
                    option.save(nest);
                }
                tabsSaved.add(nest);
            }
        }
        config.getConfig().set("tabs", tabsSaved);
        config.save();
        config.close();
    }

    @Override
    public void rawLoad() {
        TabHolder.getInstance().getTabs().clear();
        TabHolder.getInstance().addTab(new AllTab());
        config.load();
        List<CustomTab> tabs = new ArrayList<>();
        if (config.getConfig() == null || config.getConfig().getValues().isEmpty()) {
            config.close();
            TabHolder.getInstance().setVanilla();
            return;
        }
        List<ConfigObject> confs = config.getConfig().get("tabs");
        if (confs == null) {
            config.close();
            TabHolder.getInstance().setVanilla();
            return;
        }
        for (ConfigObject c : confs) {
            CustomTab tab = new CustomTab("Custom Tab", new ItemStack(Items.STONE), "stone", true, 1);
            for (Option<?> option : tab.getOptions()) {
                option.load(c);
            }
            tab.refreshOptions();
            tabs.add(tab);
        }
        config.close();
        if (tabs.size() > 0) {
            for (CustomTab tab : tabs) {
                TabHolder.getInstance().addTab(tab);
            }
            TabHolder.getInstance().addTab(new InventoryTab());
        } else {
            ItemHolder.getInstance().setDefaults();
        }
    }

    @Override
    public void addOption(Option<?> option) {

    }

}
