package org.xtreemes.vitruvius.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xtreemes.vitruvius.misc.DisplayProperties;
import org.xtreemes.vitruvius.misc.SendFeedback;
import org.xtreemes.vitruvius.selection.SelectionHandler;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EditCommand implements CommandExecutor, TabCompleter {
    private static final List<String> methods = List.of(new String[]{"ViewRange", "ShadowRadius", "ShadowStrength", "DisplayWidth", "DisplayHeight", "Billboard"});
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player;
        try {
            player = Objects.requireNonNull(sender.getServer().getPlayer(sender.getName()));
        } catch (NullPointerException e) {
            return true;
        }
        Display entity = SelectionHandler.getSelection(player.getUniqueId());
        if(entity == null) {
            SendFeedback.sendFeedback(player, "You do not have a selection!", false);
            return true;
        }
        String type = "text";
        if(entity instanceof BlockDisplay){
            type = "block";
        } else if(entity instanceof ItemDisplay){
            type = "item";
        }
        List<String> combinedMethods = new java.util.ArrayList<>(DisplayProperties.properties.get(type));
        combinedMethods.addAll(methods);
        Class<?> clazz = entity.getClass();

        if(args.length == 0){
            HoverEvent<Component> hover = HoverEvent.showText(Component.text("Click to edit!"));
            for(String methodName : combinedMethods){
                try {
                    Method method = clazz.getMethod("get" + methodName);
                    Object thing = method.invoke(entity);
                    String message;
                    message = thing.toString();
                    if(thing instanceof ItemStack item){
                        message = item.getType().getKey().value();
                    } else if(thing instanceof BlockData blockdata){
                        message = blockdata.getMaterial().getKey().value();
                    }
                    ClickEvent click = ClickEvent.suggestCommand("/edit " + methodName + " " + message);

                    player.sendMessage(
                            Component.text(methodName + ": ").color(TextColor.fromHexString("#9cf78d")).append(
                                    Component.text(message).color(TextColor.fromHexString("#FFFFFF"))
                            ).hoverEvent(hover)
                                    .clickEvent(click)
                    );
                } catch (Exception e) {
                    SendFeedback.sendFeedback(player, "There was an error running this command.", false);
                    return true;
                }
            }


        } else {
            // Edit specific value
            String methodName = args[0];
            try {
                Class<?> paramClass = DisplayProperties.paramType.getOrDefault(methodName, float.class);
                Method method = clazz.getMethod("set" + methodName, paramClass);

                if(paramClass == float.class ){
                    float number;
                    try {
                        number = Float.parseFloat(args[1]);
                    } catch(NumberFormatException e) {
                        SendFeedback.sendFeedback(player, "Invalid number! (Not a double?)", false);
                        return true;
                    }
                    method.invoke(entity, number);
                } else if(paramClass == int.class ){
                    int number;
                    try {
                        number = Integer.parseInt(args[1]);
                    } catch(NumberFormatException e) {
                        SendFeedback.sendFeedback(player, "Invalid number! (Not an integer?)", false);
                        return true;
                    }
                    method.invoke(entity, number);
                } else if(paramClass.isEnum()){
                    try {
                        Enum<?> enumerator = Enum.valueOf(paramClass.asSubclass(Enum.class), args[1]);
                        method.invoke(entity, enumerator);
                    } catch (IllegalArgumentException i){
                        SendFeedback.sendFeedback(player, "Invalid option!", false);
                        return true;
                    }
                } else if(paramClass == ItemStack.class || paramClass == BlockData.class){
                    // Set material to either ID or hand
                    Material mat = Material.matchMaterial(args[1]) != null ? Material.matchMaterial(args[1]) : (args[1].equals("hand")? player.getInventory().getItemInMainHand().getType():null);
                    if(mat == null){
                        SendFeedback.sendFeedback(player, "Invalid material!", false);
                        return true;
                    }
                    if(paramClass == BlockData.class){
                        if(!mat.isBlock()){
                            SendFeedback.sendFeedback(player, "Not a block!", false);
                            return true;
                        }
                        method.invoke(entity, mat.createBlockData());
                    } else {
                        method.invoke(entity, new ItemStack(mat));
                    }
                } else if(paramClass = String.class) {
                    String text = "";
                    for(i == 1; i < args.length; i++){
                        text = text + args[i];
                        if(i < args.length-1){
                            text = text + " ";
                        }
                    }
                }
            } catch (NoSuchMethodException e) {
                SendFeedback.sendFeedback(player, "That is not a valid property!", false);
                return true;
            } catch (InvocationTargetException | IllegalAccessException e) {
                SendFeedback.sendFeedback(player, "There was an error running this command.", false);
                return true;
            }
            SendFeedback.sendFeedback(player, "Property changed!", true);
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        List<String> tab = new ArrayList<>();
        if(args.length > 1) {
            String methodName = args[0];
            Class<?> paramClass = DisplayProperties.paramType.getOrDefault(methodName, float.class);
            if(paramClass.isEnum()){
                 Enum<?>[] enumerators = (Enum<?>[]) paramClass.getEnumConstants();
                 for(Enum<?> enumerator : enumerators){
                     tab.add(enumerator.toString());
                 }
            } else if(paramClass == ItemStack.class || paramClass == BlockData.class) {
                tab.add("hand");
                tab.add("<material>");
            }
        }
        return tab;
    }
}
