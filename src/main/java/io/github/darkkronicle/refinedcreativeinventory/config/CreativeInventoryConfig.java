package io.github.darkkronicle.refinedcreativeinventory.config;

import com.google.common.collect.ImmutableList;
import io.github.darkkronicle.darkkore.config.ModConfig;
import io.github.darkkronicle.darkkore.config.options.BooleanOption;
import io.github.darkkronicle.darkkore.config.options.ColorOption;
import io.github.darkkronicle.darkkore.config.options.Option;
import io.github.darkkronicle.darkkore.hotkeys.HotkeySettings;
import io.github.darkkronicle.darkkore.hotkeys.HotkeySettingsOption;
import io.github.darkkronicle.darkkore.intialization.profiles.PlayerContextCheck;
import io.github.darkkronicle.darkkore.util.Color;
import io.github.darkkronicle.darkkore.util.FileUtil;
import io.github.darkkronicle.darkkore.util.InputUtil;
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

    @Getter private final BooleanOption overrideVanilla = new BooleanOption("overrideVanilla", "rci.option.overridevanilla", "rci.option.info.overridevanilla", true);
    @Getter private final BooleanOption inventorySplit = new BooleanOption("inventorySplit", "rci.option.inventorysplit", "rci.option.info.inventorysplit", false);
    @Getter private final BooleanOption hotbarSplit = new BooleanOption("hotbarSplit", "rci.option.hotbarsplit", "rci.option.info.hotbarsplit", false);
    @Getter private final BooleanOption persistentSearch = new BooleanOption("persistentSearch", "rci.option.persistentsearch", "rci.option.info.persistentsearch", true);
    @Getter private final HotkeySettingsOption switchHotbars = new HotkeySettingsOption("switchHotbars", "rci.option.switchhotbars", "rci.option.info.switchhotbars", new HotkeySettings(false, false, false, List.of(GLFW.GLFW_KEY_X), PlayerContextCheck.getDefault()));
    @Getter private final HotkeySettingsOption nextHotbar = new HotkeySettingsOption(
            "nextHotbar", "rci.option.nexthotbar", "rci.option.info.nexthotbar",
            new HotkeySettings(false, false, false, List.of(GLFW.GLFW_KEY_UP), PlayerContextCheck.getDefault()));
    @Getter private final HotkeySettingsOption previousHotbar = new HotkeySettingsOption(
            "previousHotbar", "rci.option.previoushotbar", "rci.option.info.previoushotbar",
            new HotkeySettings(false, false, false, List.of(GLFW.GLFW_KEY_DOWN), PlayerContextCheck.getDefault()));
    @Getter private final BooleanOption narrowItems = new BooleanOption("narrowItems", "rci.option.narrowitems", "rci.option.info.narrowitems",
            true);
    @Getter private final ColorOption mainBackgroundColor = new ColorOption("mainBackground", "rci.option.mainbackground", "rci.option.info.mainbackground",
            new Color(0, 0, 0, 0));
    @Getter private final ColorOption componentBackgroundColor = new ColorOption("componentBackground", "rci.option.componentbackground", "rci.option.info.componentbackground",
            new Color(90, 90, 90, 226));
    @Getter private final ColorOption componentOutlineColor = new ColorOption("componentOutline", "rci.option.componentoutline", "rci.option.info.componentoutline",
            new Color(255, 255, 255, 85));
    @Getter private final ColorOption slotOutlineColor = new ColorOption("slotOutline", "rci.option.slotoutline", "rci.option.info.mainbackground",
            new Color(200, 200, 200, 34));

    @Getter private final HotkeySettingsOption openSelector = new HotkeySettingsOption(
            "openSelector", "rci.option.openselector", "rci.option.info.openselector",
            new HotkeySettings(
                    false, false, true, List.of(InputUtil.MOUSE_MIDDLE), PlayerContextCheck.getDefault()
            )
    );

    @Getter private final HotkeySettingsOption openRefinedScreen = new HotkeySettingsOption(
            "openRefinedScreen", "rci.option.openrefinedscreen", "rci.option.info.openrefinedscreen",
            new HotkeySettings(
                    false, false, true, List.of(), PlayerContextCheck.getDefault()
            )
    );

    private final List<Option<?>> options = ImmutableList.of(overrideVanilla, openRefinedScreen, narrowItems, inventorySplit, hotbarSplit, persistentSearch, switchHotbars, nextHotbar, previousHotbar, mainBackgroundColor, componentBackgroundColor, componentOutlineColor, slotOutlineColor, openSelector);

    @Override
    public File getFile() {
        return new File(getConfigDirectory(), "config.toml");
    }

    @Override
    public List<Option<?>> getOptions() {
        return options;
    }

}
