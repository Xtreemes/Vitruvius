package org.xtreemes.vitruvius.misc;

import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Display;
import org.bukkit.entity.TextDisplay;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;

public class DisplayProperties {
    public static final HashMap<String, List<String>> properties = new HashMap<>();
    public static final HashMap<String, Class<?>> paramType = new HashMap<>();

    public static void register(){
        properties.put("text", List.of(new String[]{
                "Text",
                "LineWidth",
                "Alignment"
        }));
        properties.put("item", List.of(new String[]{
                "ItemStack",

        }));
        properties.put("block", List.of(new String[]{
                "Block"
        }));

        paramType.put("Billboard", Display.Billboard.class);
        paramType.put("Alignment", TextDisplay.TextAlignment.class);
        paramType.put("ItemStack", ItemStack.class);
        paramType.put("Block", BlockData.class);
        paramType.put("LineWidth", int.class);
        paramType.put("Text", String.class);
    }
}
