package io.github.darkkronicle.refinedcreativeinventory.search;

import io.github.darkkronicle.darkkore.config.options.OptionListEntry;
import io.github.darkkronicle.darkkore.util.search.FindType;
import io.github.darkkronicle.darkkore.util.search.SearchResult;
import io.github.darkkronicle.darkkore.util.search.StringMatch;
import io.github.darkkronicle.refinedcreativeinventory.items.InventoryItem;
import lombok.AllArgsConstructor;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ItemSearch {

    @AllArgsConstructor
    public enum SearchFilter implements OptionListEntry<SearchFilter> {
        NAME("name"),
        TAG("tag"),
        FLAG("flag"),
        GROUP("group"),
        ;
        public final String key;

        @Override
        public List<SearchFilter> getAll() {
            return List.of(values());
        }

        @Override
        public String getSaveKey() {
            return key;
        }

        @Override
        public String getDisplayKey() {
            return "refinedcreativeinventory.searchfilter." + key;
        }

        @Override
        public String getInfoKey() {
            return "refinedcreativeinventory.searchfilter.info" + key;
        }
    }

    private final Map<SearchFilter, String> parameters;

    public ItemSearch() {
        this(new HashMap<>());
    }

    public ItemSearch(Map<SearchFilter, String> parameters) {
        this.parameters = parameters;
    }

    public List<InventoryItem> search(List<InventoryItem> items) {
        if (parameters.containsKey(SearchFilter.NAME)) {
            String query = parameters.get(SearchFilter.NAME);
            items = items.stream().filter(item -> {
                if (item.getStack().getName().getString().toLowerCase(Locale.ROOT).contains(query)) {
                    return true;
                }
                if (Registry.ITEM.getId(item.getStack().getItem()).toString().toLowerCase(Locale.ROOT).contains(query)) {
                    return true;
                }
                return false;
            }).toList();
        }
        if (parameters.containsKey(SearchFilter.FLAG)) {
            String query = parameters.get(SearchFilter.FLAG);
            items = items.stream().filter(item -> {
                return item.getFlags().stream().anyMatch(tag -> tag.contains(query));
            }).toList();
        }
        if (parameters.containsKey(SearchFilter.TAG)) {
            String query = parameters.get(SearchFilter.TAG);
            items = items.stream().filter(item -> {
                return item.getTags().stream().anyMatch(tag -> tag.toString().contains(query));
            }).toList();
        }
        if (parameters.containsKey(SearchFilter.GROUP)) {
            String query = parameters.get(SearchFilter.GROUP);
            items = items.stream().filter(item -> {
                return item.getGroups().stream().anyMatch(tag -> tag.getName().toLowerCase(Locale.ROOT).contains(query));
            }).toList();
        }
        return items;
    }

    public static ItemSearch fromQuery(String query) {
        if (!query.contains(":")) {
            return new ItemSearch(Map.of(ItemSearch.SearchFilter.NAME, query));
        }

        Map<SearchFilter, String> options = new HashMap<>();
        SearchResult result = SearchResult.searchOf(query, "((\\w+):)", FindType.REGEX);
        for (int i = 0; i < result.size(); i++) {
            StringMatch match = result.getMatches().get(i);
            if (i == 0 && match.start != 0) {
                options.put(SearchFilter.NAME, query.substring(0, match.start).strip());
            }
            if (i == result.size() - 1) {
                SearchFilter filter = SearchFilter.TAG.fromString(match.match.substring(0, match.match.length() - 1));
                if (filter != null) {
                    options.put(filter, query.substring(match.end).strip());
                }
                break;
            }
            StringMatch next = result.getMatches().get(i + 1);
            SearchFilter filter = SearchFilter.TAG.fromString(match.match.substring(0, match.match.length() - 1));
            if (filter != null) {
                options.put(filter, query.substring(match.end, next.start).strip());
            }
        }
        return new ItemSearch(options);
    }

}
