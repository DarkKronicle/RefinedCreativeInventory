package io.github.darkkronicle.refinedcreativeinventory.items;

import com.mojang.datafixers.util.Pair;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryEntryList;

import java.util.*;

public class TagHolder {

    private final static TagHolder INSTANCE = new TagHolder();

    private Map<Identifier, List<Identifier>> tags = new HashMap<>();

    public static TagHolder getInstance() {
        return INSTANCE;
    }

    private TagHolder() {}

    public void populateTags() {
        tags.clear();
        for (Pair<TagKey<Item>, RegistryEntryList.Named<Item>> pair : Registry.ITEM.streamTagsAndEntries().toList()) {
            TagKey<Item> tagKey = pair.getFirst();
            RegistryEntryList.Named<Item> list = pair.getSecond();
            for (RegistryEntry<Item> item : list) {
                Identifier id = Registry.ITEM.getId(item.value());
                tags.compute(id, (k, v) -> {
                    if (v == null) {
                        return new ArrayList<>(List.of(tagKey.id()));
                    } else {
                        if (!v.contains(tagKey.id())) {
                            v.add(tagKey.id());
                        }
                        return v;
                    }
                });
            }
        }
    }

    public List<Identifier> getTags(Item item) {
        return Optional.ofNullable(tags.get(Registry.ITEM.getId(item))).orElseGet(ArrayList::new);
    }

}
