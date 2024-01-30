package org.xtreemes.vitruvius.commands;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.xtreemes.vitruvius.misc.SendFeedback;
import org.xtreemes.vitruvius.selection.SelectionHandler;

import java.util.*;


public class CreateCommand implements CommandExecutor, TabCompleter {

    private final MiniMessage mm = MiniMessage.miniMessage();

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player;
        try {
            player = Objects.requireNonNull(sender.getServer().getPlayer(sender.getName()));
        } catch (NullPointerException e) {
            return true;
        }
        if(!sender.isOp()){
            SendFeedback.sendFeedback(player, "You must have op to use this command!", false);
            return true;
        }
        World world = player.getWorld();
        int length = args.length;
        if(length > 0) {
            Display selection;
            switch (args[0]) {
                case "text" -> {
                    TextDisplay entity = (TextDisplay) world.spawnEntity(player.getLocation(), EntityType.TEXT_DISPLAY);
                    Component text = Component.text("Hello World!");

                    // connect the string :3
                    if(length >= 2){
                        StringBuilder stringBuilder = new StringBuilder();
                        for(int i = 1; i < length-1; i++){
                            stringBuilder.append(args[i]).append(" ");
                        }
                        // avoid the stupid space in an easy way
                        stringBuilder.append(args[length-1]);
                        text = mm.deserialize(String.valueOf(stringBuilder));

                    }
                    entity.text(text);
                    selection = entity;
                }
                case "block" -> {
                    BlockDisplay entity = (BlockDisplay) world.spawnEntity(player.getLocation(), EntityType.BLOCK_DISPLAY);
                    Material mat = Material.STONE;
                    if(length >= 2){
                        // If argument specifies, set material to either ID, hand, or default
                        mat = Material.matchMaterial(args[1]) != null ? Material.matchMaterial(args[1]) : (args[1].equals("hand")? player.getInventory().getItemInMainHand().getType():mat);
                        if(!mat.isBlock()){
                            SendFeedback.sendFeedback(player, "Not a block!", false);
                            return true;
                        }
                    }
                    entity.setBlock(mat.createBlockData());
                    selection = entity;
                }
                case "item" -> {
                    ItemDisplay entity = (ItemDisplay) world.spawnEntity(player.getLocation(), EntityType.ITEM_DISPLAY);
                    Material mat = Material.DIAMOND_SWORD;
                    if(length >= 2){
                        // If argument specifies, set material to either ID, hand, or default
                        mat = Material.matchMaterial(args[1]) != null ? Material.matchMaterial(args[1]) : (args[1].equals("hand")? player.getInventory().getItemInMainHand().getType():mat);
                    }
                    entity.setItemStack(new ItemStack(mat));
                    selection = entity;

                }
                default -> {
                    SendFeedback.sendFeedback(player, "Incorrect entity type!", false);
                    return true;
                }
            }
            SelectionHandler.setSelection(player.getUniqueId(), selection);
            SendFeedback.sendFeedback(player, "Spawned and selected entity!", true);
        }
        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {

        List<String> fill;
        if(args.length == 1){
            fill = Arrays.asList("text", "item", "block");
        } else {
            fill = args[0].equals("text") ? Collections.singletonList("<text>"): Arrays.asList("<material>", "hand");
        }
        return fill;
    }
}
