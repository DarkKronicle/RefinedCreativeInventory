package io.github.darkkronicle.refinedcreativeinventory.itemselector;

import io.github.darkkronicle.refinedcreativeinventory.items.BasicInventoryItem;
import io.github.darkkronicle.refinedcreativeinventory.items.InventoryItem;
import io.github.darkkronicle.refinedcreativeinventory.items.ItemFlag;
import io.github.darkkronicle.refinedcreativeinventory.items.ItemHolder;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.text.Text;
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

    @Getter
    private List<ItemModifier> modifiers = new ArrayList<>();

    private ItemSwitcherHandler() {
        modifiers.add(new SimpleModifier(new ItemStack(Items.SHULKER_BOX).setCustomName(Text.literal("Fill Shulker")), stack -> {
            if (!stack.getItem().canBeNested()) {
                return stack;
            }
            ItemStack inner = stack.copy();
            NbtElement tag = inner.getOrCreateNbt().get("tag");
            if (tag instanceof NbtCompound compound) {
                compound.remove("BlockEntityTag");
            }
            inner.setCount(inner.getItem().getMaxCount());
            NbtCompound innerNbt = inner.getOrCreateNbt();
            innerNbt.remove("BlockEntityTag");
            innerNbt.remove("pages");
            ItemStack newStack = new ItemStack(Items.SHULKER_BOX);
            NbtCompound nbt = newStack.getOrCreateNbt();
            NbtCompound blockEntity = new NbtCompound();
            NbtList items = new NbtList();
            for (int i = 0; i < 27; i++) {
                NbtCompound item = new NbtCompound();
                inner.writeNbt(item);
                item.putInt("Slot", i);
                items.add(item);
            }
            blockEntity.put("Items", items);
            nbt.put("BlockEntityTag", blockEntity);
            return newStack;
        }));
    }

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
