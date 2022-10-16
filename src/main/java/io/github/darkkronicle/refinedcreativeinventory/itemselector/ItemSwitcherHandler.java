package io.github.darkkronicle.refinedcreativeinventory.itemselector;

import io.github.darkkronicle.refinedcreativeinventory.items.BasicInventoryItem;
import io.github.darkkronicle.refinedcreativeinventory.items.InventoryItem;
import io.github.darkkronicle.refinedcreativeinventory.items.ItemFlag;
import io.github.darkkronicle.refinedcreativeinventory.items.ItemHolder;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ItemSwitcherHandler {

    private final static ItemSwitcherHandler INSTANCE = new ItemSwitcherHandler();

    @Getter
    @Setter
    @Nullable
    private ItemSwitcherScreen currentScreen = null;

    public static ItemSwitcherHandler getInstance() {
        return INSTANCE;
    }

    private ItemSwitcherHandler() {}

    public List<List<InventoryItem>> getStacks(ItemStack input) {
        ItemStack copy = input.copy();
        copy.setCount(1);
        InventoryItem base = ItemHolder.getInstance().get(copy).orElse(
                new BasicInventoryItem(copy)
        );
        List<ItemFlag> flags = base.getFlags();
        List<List<InventoryItem>> stacks = new ArrayList<>();
        for (ItemFlag flag : flags) {
            List<InventoryItem> item = new ArrayList<>(
                    ItemHolder.getInstance().getAllItems().stream().filter(i -> i.getFlags().contains(flag)).toList()
            );
            if (item.size() > 1) {
                final ItemFlag finFlag = flag;
                item.sort((i1, i2) ->
                        Integer.compare(
                                i1.getFlags().stream().filter(f -> f.equals(finFlag)).findFirst().get().getOrder(),
                                i2.getFlags().stream().filter(f -> f.equals(finFlag)).findFirst().get().getOrder()
                        ));
                stacks.add(item);
            }
        }
        stacks.sort(Comparator.comparingInt(List::size));
        return stacks;
    }

}
