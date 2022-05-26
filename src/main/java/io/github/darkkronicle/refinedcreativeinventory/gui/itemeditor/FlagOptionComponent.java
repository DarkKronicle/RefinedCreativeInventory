package io.github.darkkronicle.refinedcreativeinventory.gui.itemeditor;


import io.github.darkkronicle.darkkore.config.options.StringOption;
import io.github.darkkronicle.darkkore.gui.config.StringOptionComponent;
import io.github.darkkronicle.darkkore.gui.config.TextOptionComponent;
import io.github.darkkronicle.darkkore.util.FluidText;
import io.github.darkkronicle.darkkore.util.StringUtil;
import io.github.darkkronicle.darkkore.util.search.FindType;
import io.github.darkkronicle.darkkore.util.search.SearchUtil;
import io.github.darkkronicle.refinedcreativeinventory.items.BasicInventoryItem;
import net.minecraft.text.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class FlagOptionComponent extends TextOptionComponent<String, StringOption> {

    private final BasicInventoryItem item;

    private FlagOptionComponent(BasicInventoryItem item, StringOption option, int width) {
        super(option, width);
        this.item = item;
    }

    @Override
    public Text getConfigTypeInfo() {
        return new FluidText("§7§o" + StringUtil.translate("rci.optiontype.info.flag"));
    }

    @Override
    public void setValueFromString(String string) {
        item.setFlags(new ArrayList<>(Arrays.asList(string.split(","))));
    }

    @Override
    public boolean isValid(String string) {
        return !SearchUtil.isMatch(string, ",,", FindType.LITERAL);
    }

    @Override
    public String getStringValue() {
        return option.getValue();
    }

    public static FlagOptionComponent of(BasicInventoryItem item, int width) {
        StringOption option = new StringOption("flags", "rci.itemedit.flags", "rci.itemedit.info.flags", "");
        option.setValue(String.join(",", item.getFlags()));
        return new FlagOptionComponent(item, option, width);
    }


}
