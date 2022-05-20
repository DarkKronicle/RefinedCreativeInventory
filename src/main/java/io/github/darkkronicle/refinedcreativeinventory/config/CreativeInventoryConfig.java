package io.github.darkkronicle.refinedcreativeinventory.config;

import io.github.darkkronicle.darkkore.config.ModConfig;
import io.github.darkkronicle.darkkore.config.options.Option;
import io.github.darkkronicle.darkkore.util.FileUtil;

import java.io.File;
import java.util.List;

public class CreativeInventoryConfig extends ModConfig {

    public static File getConfigDirectory() {

        return new File(FileUtil.getConfigDirectory(), "refinedCreativeInventory");
    }

    @Override
    public File getFile() {
        return null;
    }

    @Override
    public List<Option<?>> getOptions() {
        return null;
    }

    @Override
    public void addOption(Option<?> option) {

    }
}
