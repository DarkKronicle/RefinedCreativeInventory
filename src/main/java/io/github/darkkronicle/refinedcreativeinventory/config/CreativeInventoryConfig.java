package io.github.darkkronicle.refinedcreativeinventory.config;

import com.google.common.collect.ImmutableList;
import io.github.darkkronicle.darkkore.config.ModConfig;
import io.github.darkkronicle.darkkore.config.options.BooleanOption;
import io.github.darkkronicle.darkkore.config.options.Option;
import io.github.darkkronicle.darkkore.config.options.OptionSection;
import io.github.darkkronicle.darkkore.util.FileUtil;
import lombok.Getter;

import java.io.File;
import java.util.List;

public class CreativeInventoryConfig extends ModConfig {

    private static final CreativeInventoryConfig INSTANCE = new CreativeInventoryConfig();

    public static CreativeInventoryConfig getInstance() {
        return INSTANCE;
    }

    public static File getConfigDirectory() {
        return new File(FileUtil.getConfigDirectory(), "refinedCreativeInventory");
    }

    @Getter private final BooleanOption inventorySplit = new BooleanOption("inventorySplit", "rci.config.inventorysplit", "rci.config.info.inventorysplit", true);

    private final List<Option<?>> options = ImmutableList.of(inventorySplit);

    @Override
    public File getFile() {
        return new File(getConfigDirectory(), "config.toml");
    }

    @Override
    public List<Option<?>> getOptions() {
        return options;
    }

    @Override
    public void addOption(Option<?> option) {

    }
}
