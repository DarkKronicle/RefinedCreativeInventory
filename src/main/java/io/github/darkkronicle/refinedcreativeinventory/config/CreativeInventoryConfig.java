package io.github.darkkronicle.refinedcreativeinventory.config;

import com.google.common.collect.ImmutableList;
import io.github.darkkronicle.darkkore.config.ModConfig;
import io.github.darkkronicle.darkkore.config.options.BooleanOption;
import io.github.darkkronicle.darkkore.config.options.Option;
import io.github.darkkronicle.darkkore.config.options.OptionSection;
import io.github.darkkronicle.darkkore.hotkeys.HotkeySettings;
import io.github.darkkronicle.darkkore.hotkeys.HotkeySettingsOption;
import io.github.darkkronicle.darkkore.intialization.profiles.PlayerContextCheck;
import io.github.darkkronicle.darkkore.util.FileUtil;
import lombok.Getter;
import org.lwjgl.glfw.GLFW;

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

    @Getter private final BooleanOption inventorySplit = new BooleanOption("inventorySplit", "rci.option.inventorysplit", "rci.option.info.inventorysplit", true);
    @Getter private final HotkeySettingsOption switchHotbars = new HotkeySettingsOption("switchHotbars", "rci.option.switchhotbars", "rci.option.info.switchhotbars", new HotkeySettings(false, false, false, List.of(GLFW.GLFW_KEY_X), PlayerContextCheck.getDefault()));

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
