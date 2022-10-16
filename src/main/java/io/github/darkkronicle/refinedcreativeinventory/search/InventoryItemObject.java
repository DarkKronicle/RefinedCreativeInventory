package io.github.darkkronicle.refinedcreativeinventory.search;

import io.github.darkkronicle.Konstruct.functions.Function;
import io.github.darkkronicle.Konstruct.functions.ObjectFunction;
import io.github.darkkronicle.Konstruct.nodes.Node;
import io.github.darkkronicle.Konstruct.parser.IntRange;
import io.github.darkkronicle.Konstruct.parser.ParseContext;
import io.github.darkkronicle.Konstruct.parser.Result;
import io.github.darkkronicle.Konstruct.type.BooleanObject;
import io.github.darkkronicle.Konstruct.type.KonstructObject;
import io.github.darkkronicle.Konstruct.type.StringObject;
import io.github.darkkronicle.refinedcreativeinventory.items.InventoryItem;
import lombok.Getter;
import net.minecraft.item.ItemStack;
import net.minecraft.util.registry.Registry;

import java.util.List;
import java.util.Locale;

public class InventoryItemObject extends KonstructObject<InventoryItemObject> {

    @Getter private InventoryItem item;

    private final static List<ObjectFunction<InventoryItemObject>> FUNCTIONS = List.of(
            new ObjectFunction<>() {
                @Override
                public Result parse(ParseContext context, InventoryItemObject self, List<Node> input) {
                    ItemStack stack = self.getItem().getStack();
                    Result nameObj = Function.parseArgument(context, input, 0);
                    if (Function.shouldReturn(nameObj)) return nameObj;
                    String name = nameObj.getContent().getString().toLowerCase(Locale.ROOT);
                    if (stack.getName().getString().toLowerCase(Locale.ROOT).contains(name)) {
                        return Result.success(new BooleanObject(true));
                    }
                    if (Registry.ITEM.getId(stack.getItem()).toString().toLowerCase(Locale.ROOT).contains(name)) {
                        return Result.success(new BooleanObject(true));
                    }
                    return Result.success(new BooleanObject(false));
                }

                @Override
                public String getName() {
                    return "name";
                }

                @Override
                public IntRange getArgumentCount() {
                    return IntRange.of(1);
                }
            },
            new ObjectFunction<>() {
                @Override
                public Result parse(ParseContext context, InventoryItemObject self, List<Node> input) {
                    Result flagObj = Function.parseArgument(context, input, 0);
                    if (Function.shouldReturn(flagObj)) return flagObj;
                    String flag = flagObj.getContent().getString().toLowerCase(Locale.ROOT);
                    if (self.getItem().getFlags().stream().anyMatch(f -> f.getName().contains(flag))) {
                        return Result.success(new BooleanObject(true));
                    }
                    return Result.success(new BooleanObject(false));
                }

                @Override
                public String getName() {
                    return "flag";
                }

                @Override
                public IntRange getArgumentCount() {
                    return IntRange.of(1);
                }
            },
            new ObjectFunction<>() {
                @Override
                public Result parse(ParseContext context, InventoryItemObject self, List<Node> input) {
                    Result groupObj = Function.parseArgument(context, input, 0);
                    if (Function.shouldReturn(groupObj)) return groupObj;
                    String group = groupObj.getContent().getString().toLowerCase(Locale.ROOT);
                    if (self.getItem().getGroups().stream().anyMatch(g -> g.getName().contains(group))) {
                        return Result.success(new BooleanObject(true));
                    }
                    return Result.success(new BooleanObject(false));
                }

                @Override
                public String getName() {
                    return "group";
                }

                @Override
                public IntRange getArgumentCount() {
                    return IntRange.of(1);
                }
            },
            new ObjectFunction<>() {
                @Override
                public Result parse(ParseContext context, InventoryItemObject self, List<Node> input) {
                    return Result.success(new BooleanObject(self.getItem().isCustom()));
                }

                @Override
                public String getName() {
                    return "custom";
                }

                @Override
                public IntRange getArgumentCount() {
                    return IntRange.of(0);
                }
            },
            new ObjectFunction<>() {
                @Override
                public Result parse(ParseContext context, InventoryItemObject self, List<Node> input) {
                    return Result.success(new StringObject(self.getItem().getStack().getName().getString()));
                }

                @Override
                public String getName() {
                    return "getName";
                }

                @Override
                public IntRange getArgumentCount() {
                    return IntRange.of(0);
                }
            }
        );

    public InventoryItemObject(InventoryItem item) {
        super(FUNCTIONS);
        this.item = item;
    }

    @Override
    public String getString() {
        return item.getStack().getName().getString();
    }

    @Override
    public String getTypeName() {
        return "inventory_items";
    }

}
