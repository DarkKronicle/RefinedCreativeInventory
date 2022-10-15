package io.github.darkkronicle.refinedcreativeinventory.config;


import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import io.github.darkkronicle.darkkore.DarkKore;
import io.github.darkkronicle.darkkore.config.ModConfig;
import io.github.darkkronicle.darkkore.config.impl.ConfigObject;
import io.github.darkkronicle.darkkore.config.impl.JsonConfigObject;
import io.github.darkkronicle.darkkore.config.impl.JsonFileObject;
import io.github.darkkronicle.darkkore.config.options.Option;
import io.github.darkkronicle.darkkore.util.FileUtil;
import io.github.darkkronicle.darkkore.util.JsonUtil;
import io.github.darkkronicle.refinedcreativeinventory.items.InventoryItem;
import io.github.darkkronicle.refinedcreativeinventory.items.ItemHolder;
import io.github.darkkronicle.refinedcreativeinventory.items.BasicInventoryItem;
import io.github.darkkronicle.refinedcreativeinventory.util.ItemSerializer;
import lombok.Getter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ItemsConfig extends ModConfig {

    private final static Item DEFAULT_ITEM = Registry.ITEM.get(Registry.ITEM.getDefaultId());

    private final static ItemsConfig INSTANCE = new ItemsConfig();

    public static final int LATEST_VERSION = 2;

    @Getter
    private int currentVersion = 1;

    private boolean shouldSetFlags = false;

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
            shouldSetFlags = true;
            try {
                getFile().getParentFile().mkdirs();
                getFile().createNewFile();
                try {
                    try (OutputStream outputStream = new FileOutputStream(getFile())) {
                        IOUtils.copy(FileUtil.getResource("default_items.json"), outputStream);
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
        config.getConfig().set("version", LATEST_VERSION);
        config.save();
        config.close();
    }

    private void setDefaultFlags() {
        try {
            InputStream stream = FileUtil.getResource("default_flags.json");
            JsonElement element = JsonParser.parseReader(JsonUtil.GSON.newJsonReader(new InputStreamReader(stream, StandardCharsets.UTF_8)));
            if (!element.isJsonObject()) {
                return;
            }
            JsonConfigObject obj = new JsonConfigObject(element.getAsJsonObject());
            loadFlags(obj);
        } catch (IOException | URISyntaxException e) {
            DarkKore.LOGGER.error("Couldn't set up default_flags.json!", e);
            return;
        }
    }

    private void loadCompound(List<ConfigObject> compound) {
        for (ConfigObject config : compound) {
            String name = "Name not defined";
            try {
                name = config.get("name");
                loadIndividualCompound(config);
            } catch (Exception e) {
                DarkKore.LOGGER.error("Error loading compound " + name + "!", e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void loadIndividualCompound(ConfigObject config) {
        List<?> iterate = (List<?>) config.getOptional("iterate").orElseGet(() -> List.of(""));
        List<String> replace = (List<String>) config.getOptional("replace").orElseGet(ArrayList::new);
        String name = config.get("name");
        loadList(name, replace, iterate, 0);
    }

    private void loadList(String name, List<String> replace, List<?> values, int depth) {
        for (Object o : values) {
            if (o instanceof String string) {
                String flag = name.replace("{" + depth + "}", string);
                for (String r : replace) {
                    r = r.replace("{" + depth + "}", string);
                    if (!r.contains(":")) {
                        r = "minecraft:" + r;
                    }
                    r = r.toLowerCase(Locale.ROOT).strip();
                    Identifier identifier = Identifier.splitOn(r, ':');
                    Item item = Registry.ITEM.get(identifier);
                    if (item == DEFAULT_ITEM) {
                        DarkKore.LOGGER.info("Couldn't find item " + r + " (safe to ignore)");
                        continue;
                    }

                    InventoryItem stack = ItemHolder.getInstance().getOrCreate(new ItemStack(item));
                    stack.addFlag(flag);
                }
            } else {
                List<?> list = (List<?>) o;
                for (Object inner : list) {
                    if (inner instanceof String string) {
                        String innerName = name.replace("{" + depth + "}", string);
                        List<String> newReplace = replace.stream().map(rep -> rep.replace("{" + depth + "}", string)).toList();
                        for (Object value : values.subList(1, values.size())) {
                            loadList(innerName, newReplace, (List<?>) value, depth + 1);
                        }
                    } else {
                        loadList(name, replace, (List<?>) inner, depth);
                    }
                }

                return;

            }
        }
    }

    private void loadFlags(ConfigObject config) {

        if (config.contains("compound")) {
            Object compound = config.get("compound");
            if (compound instanceof List<?>) {
                loadCompound((List<ConfigObject>) compound);
            }
        }
        Map<String, Object> values = config.getValues();
        values.remove("compound");
        for (Map.Entry<String, Object> entry : values.entrySet()) {
            if (!(entry.getValue() instanceof List<?>)) {
                continue;
            }
            String flag = entry.getKey();

            // This is a bit scary, but we should be in a try catch
            List<String> list = (List<String>) entry.getValue();

            for (String value : list) {
                if (!value.contains(":")) {
                    value = "minecraft:" + value;
                }
                value = value.toLowerCase(Locale.ROOT).strip();
                Identifier identifier = Identifier.splitOn(value, ':');
                Item item = Registry.ITEM.get(identifier);
                if (item == DEFAULT_ITEM) {
                    DarkKore.LOGGER.info("Couldn't find item " + value);
                }

                InventoryItem stack = ItemHolder.getInstance().getOrCreate(new ItemStack(item));
                stack.addFlag(flag);
            }
        }
    }

    @Override
    public void rawLoad() {
        ItemHolder.getInstance().setDefaults();
        config.load();
        if (config.getConfig() == null) {
            config.close();
            return;
        }
        currentVersion = ((Number) config.getConfig().getOptional("version").orElse(1)).intValue();
        List<ConfigObject> confs = config.getConfig().get("items");
        if (confs != null) {
            for (ConfigObject c : confs) {
                loadInventoryItem(c, true);
            }
        }
        if (currentVersion < 2 || shouldSetFlags) {
            shouldSetFlags = false;
            setDefaultFlags();
        }
        config.close();
    }

    public static void saveInventoryItem(ConfigObject nest, InventoryItem item) {
        ItemSerializer.serialize(nest, item.getStack());
        nest.set("flags", item.getFlags());
        nest.set("custom", item.isCustom());
    }

    public static InventoryItem loadInventoryItem(ConfigObject nest, boolean add) {
        return loadInventoryItem(nest, add, true);
    }

    public static InventoryItem loadInventoryItem(ConfigObject nest, boolean add, boolean setData) {
        ItemStack stack = ItemSerializer.deserialize(nest);
        List<String> flags = nest.get("flags");
        InventoryItem item = add ? ItemHolder.getInstance().getOrCreate(stack)
                                 : ItemHolder.getInstance().get(stack).orElseGet(() -> new BasicInventoryItem(stack));
        if (setData) {
            for (String flag : flags) {
                item.addFlag(flag);
            }
            item.setCustom(nest.get("custom"));
        }
        return item;
    }

    @Override
    public void addOption(Option<?> option) {

    }

}
