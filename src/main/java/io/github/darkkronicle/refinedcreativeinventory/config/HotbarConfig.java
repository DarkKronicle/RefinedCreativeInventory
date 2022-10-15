package io.github.darkkronicle.refinedcreativeinventory.config;

import io.github.darkkronicle.darkkore.DarkKore;
import io.github.darkkronicle.darkkore.config.ModConfig;
import io.github.darkkronicle.darkkore.config.impl.ConfigObject;
import io.github.darkkronicle.darkkore.config.impl.JsonFileObject;
import io.github.darkkronicle.darkkore.config.options.Option;
import io.github.darkkronicle.darkkore.hotkeys.HotkeyHandler;
import io.github.darkkronicle.refinedcreativeinventory.hotbars.HotbarHolder;
import io.github.darkkronicle.refinedcreativeinventory.hotbars.HotbarProfile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class HotbarConfig extends ModConfig {

    private final static HotbarConfig INSTANCE = new HotbarConfig();

    public static HotbarConfig getInstance() {
        return INSTANCE;
    }

    @Override
    public File getFile() {
        return new File(CreativeInventoryConfig.getConfigDirectory(), "hotbars.json");
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
        List<ConfigObject> confs = new ArrayList<>();
        ConfigObject obj = config.getConfig();
        for (HotbarProfile profile : HotbarHolder.getInstance().getProfiles()) {
            ConfigObject nest = obj.createNew();
            profile.save(nest);
            confs.add(nest);
        }
        obj.set("profiles", confs);
        obj.set("current", HotbarHolder.getInstance().getCurrentIndex());
        config.save();
        config.close();
    }

    @Override
    public void rawLoad() {
        config.load();
        if (config.getConfig() == null || !config.getConfig().contains("profiles")) {
            HotbarHolder.getInstance().setDefaults();
            config.close();
            return;
        }
        List<ConfigObject> confs = config.getConfig().get("profiles");
        if (confs == null) {
            HotbarHolder.getInstance().setDefaults();
            config.close();
            return;
        }
        List<HotbarProfile> profiles = new ArrayList<>();
        for (ConfigObject c : confs) {
            HotbarProfile profile = new HotbarProfile();
            profile.load(c);
            profiles.add(profile);
        }
        if (profiles.size() > 0) {
            HotbarHolder.getInstance().setProfiles(profiles);
        } else {
            HotbarHolder.getInstance().setDefaults();
        }
        config.close();
        HotkeyHandler.getInstance().rebuildHotkeys();
    }

}
