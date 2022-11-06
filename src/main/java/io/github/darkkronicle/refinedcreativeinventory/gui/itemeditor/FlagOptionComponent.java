package io.github.darkkronicle.refinedcreativeinventory.gui.itemeditor;


import io.github.darkkronicle.darkkore.config.options.StringOption;
import io.github.darkkronicle.darkkore.gui.config.StringOptionComponent;
import io.github.darkkronicle.darkkore.gui.config.TextOptionComponent;
import io.github.darkkronicle.darkkore.util.FluidText;
import io.github.darkkronicle.darkkore.util.StringUtil;
import io.github.darkkronicle.darkkore.util.search.FindType;
import io.github.darkkronicle.darkkore.util.search.SearchUtil;
import io.github.darkkronicle.refinedcreativeinventory.items.BasicInventoryItem;
import io.github.darkkronicle.refinedcreativeinventory.items.ItemFlag;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

public class FlagOptionComponent extends TextOptionComponent<String, StringOption> {

    private final BasicInventoryItem item;

    private FlagOptionComponent(Screen parent, BasicInventoryItem item, StringOption option, int width) {
        super(parent, option, width);
        this.item = item;
    }

    @Override
    public Text getConfigTypeInfo() {
        return new FluidText("§7§o" + StringUtil.translate("rci.optiontype.info.flag"));
    }

    @Override
    public void setValueFromString(String string) {
        if (string == null || string.length() == 0) {
            item.setFlags(new ArrayList<>());
        } else {
            item.setFlags(new ArrayList<>(Arrays.stream(string.split(",")).map(ItemFlag::fromString).toList()));
        }
    }

    @Override
    public boolean isValid(String string) {
        return !SearchUtil.isMatch(string, ",,", FindType.LITERAL);
    }

    @Override
    public String getStringValue() {
        return option.getValue();
    }

    public static FlagOptionComponent of(Screen parent, BasicInventoryItem item, int width) {
        StringOption option = new StringOption("flags", "rci.itemedit.flags", "rci.itemedit.info.flags", "");
        option.setValue(item.getFlags().stream().map(ItemFlag::toString).collect(Collectors.joining(",")));
        return new FlagOptionComponent(parent, item, option, width);
    }


}
