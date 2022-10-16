package io.github.darkkronicle.refinedcreativeinventory.hotbars;

import io.github.darkkronicle.darkkore.hotkeys.HotkeyHandler;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

public class HotbarHolder {

    @Getter private List<HotbarProfile> profiles = new ArrayList<>();

    private final static HotbarHolder INSTANCE = new HotbarHolder();

    public static HotbarHolder getInstance() {
        return INSTANCE;
    }

    private int current;

    private HotbarHolder() {}

    public void setCurrent(HotbarProfile profile) {
        int index = profiles.indexOf(profile);
        if (index >= 0) {
            setCurrent(index);
            HotkeyHandler.getInstance().rebuildHotkeys();
        }
    }

    public HotbarProfile getCurrent() {
        return profiles.get(current);
    }

    public void setCurrent(int index) {
        current = index;
    }

    public void addProfile(HotbarProfile profile) {
        profiles.add(profile);
    }

    public int getCurrentIndex() {
        return current;
    }

    public HotbarProfile get(int index) {
        return profiles.get(index);
    }

    public void setProfiles(List<HotbarProfile> profiles) {
        this.profiles = profiles;
    }

    public void setDefaults() {
        HotbarHolder.getInstance().addProfile(new HotbarProfile());
        HotbarHolder.getInstance().get(0).add(new SavedHotbar());
    }

    public void removeProfile(HotbarProfile profile) {
        profiles.remove(profile);
        if (profiles.size() == 0) {
            setDefaults();
        }
    }

}
