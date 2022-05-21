package io.github.darkkronicle.refinedcreativeinventory.util;

import com.electronwill.nightconfig.core.Config;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import lombok.experimental.UtilityClass;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

@UtilityClass
public class ItemSerializer {

    public ItemStack deserialize(Config config) {
        String nbt = config.get("nbt");
        StringNbtReader reader = new StringNbtReader(new StringReader(nbt));
        try {
            return ItemStack.fromNbt(reader.parseCompound());
        } catch (CommandSyntaxException e) {
            return new ItemStack(Items.STONE);
        }
    }

    public void serialize(Config config, ItemStack stack) {
        NbtCompound compound = new NbtCompound();
        stack.writeNbt(compound);
        config.set("nbt", compound.toString());
    }

    public boolean areEqual(ItemStack one, ItemStack two) {
        if (!one.isItemEqual(two)) {
            return false;
        }
        if (one.getCount() != two.getCount()) {
            return false;
        }
        if (one.hasNbt() != two.hasNbt()) {
            return false;
        }
        if (!one.hasNbt() && !two.hasNbt()) {
            return true;
        }
        return one.getNbt().equals(two.getNbt());
    }

}
