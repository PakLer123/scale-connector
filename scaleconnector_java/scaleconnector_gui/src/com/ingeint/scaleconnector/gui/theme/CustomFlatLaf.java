package com.ingeint.scaleconnector.gui.theme;

import com.formdev.flatlaf.FlatLightLaf;
import javax.swing.UIDefaults;
import javax.swing.plaf.FontUIResource;
import java.awt.Font;

public class CustomFlatLaf extends FlatLightLaf{
	public static final String NAME = "CustomFlatLaf";

    public static boolean setup() {
        return setup(new CustomFlatLaf());
    }

    @Override
    public String getName() {
        return NAME;
    }

    @Override
    protected void initClassDefaults(UIDefaults table) {
        super.initClassDefaults(table);
        // Customize default properties
        table.put("Button.arc", 10);
        table.put("Component.arc", 10);
        table.put("TextComponent.arc", 10);
        table.put("CheckBox.icon.style", "filled");
        table.put("defaultFont", new FontUIResource(new Font("Segoe UI", Font.PLAIN, 14)));
    }
}
