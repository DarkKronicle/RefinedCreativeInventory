package io.github.darkkronicle.refinedcreativeinventory.items;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ItemFlag implements Comparable<ItemFlag> {

    private final static Pattern INDEX_REGEX = Pattern.compile("\\[(\\d+)\\]$");
    private final static Pattern INVALID_NAME_REGEX = Pattern.compile("[^\\w]+");

    @Getter
    private final String name;

    @Getter
    private final int order;

    public ItemFlag(String name, int order) {
        this.name = name;
        this.order = order;
    }


    @Override
    public String toString() {
        if (order == 0) {
            return name;
        }
        return name + "[" + order + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof ItemFlag flag) {
            return name.equals(flag.name);
        }
        return false;
    }

    public static ItemFlag fromString(String string) {
        Matcher index = INDEX_REGEX.matcher(string);
        String name = string;
        int order = 0;
        if (index.find()) {
            order = Integer.parseInt(index.group(1));
            name = string.substring(0, index.start(0));
        }
        name = INVALID_NAME_REGEX.matcher(name).replaceAll("");
        if (name.length() == 0) {
            throw new IllegalArgumentException("Flag " + string + " has an invalid name length!");
        }
        return new ItemFlag(name, order);
    }

    @Override
    public int compareTo(@NotNull ItemFlag o) {
        int compared = name.compareTo(o.name);
        if (compared != 0) {
            return compared;
        }
        return Integer.compare(order, o.order);
    }
}
