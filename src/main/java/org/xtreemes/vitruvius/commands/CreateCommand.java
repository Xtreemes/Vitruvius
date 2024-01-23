package org.xtreemes.vitruvius.commands;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
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
import org.xtreemes.vitruvius.selection.SelectionHandler;

import java.util.*;


public class CreateCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        TextComponent errorPrefix = Component.text("FAIL").decorate(TextDecoration.BOLD).color(TextColor.fromHexString("#fa3e3e"));
        TextComponent successPrefix = Component.text("SUCCESS").decorate(TextDecoration.BOLD).color(TextColor.fromHexString("#41ea00"));
        if(!sender.isOp()){
            sender.sendMessage(errorPrefix.append(Component.text(" You must have op to use this command!").decoration(TextDecoration.BOLD, false).color(TextColor.fromHexString("#b4b4b4"))));
            sender.playSound(Sound.sound(Key.key("entity.shulker.hurt_closed"), Sound.Source.MASTER, 1f, 1f));
            return true;
        }
        Player player = Objects.requireNonNull(sender.getServer().getPlayer(sender.getName()));
        World world = player.getWorld();
        int length = args.length;
        if(length > 0) {
            Display selection;
            switch (args[0]) {
                case "text" -> {
                    TextDisplay entity = (TextDisplay) world.spawnEntity(player.getLocation(), EntityType.TEXT_DISPLAY);
                    entity.text(Component.text("Hello World!"));
                    selection = entity;
                }
                case "block" -> {
                    BlockDisplay entity = (BlockDisplay) world.spawnEntity(player.getLocation(), EntityType.BLOCK_DISPLAY);
                    Material mat = Material.STONE;
                    if(length >= 2){
                        mat = Material.matchMaterial(args[1]) != null ? Material.matchMaterial(args[1]) : (args[1].equals("hand")? player.getInventory().getItemInMainHand().getType():mat);
                    }
                    entity.setBlock(mat.createBlockData());
                    selection = entity;
                }
                case "item" -> {
                    ItemDisplay entity = (ItemDisplay) world.spawnEntity(player.getLocation(), EntityType.ITEM_DISPLAY);
                    Material mat = Material.DIAMOND_SWORD;
                    if(length >= 2){
                        mat = Material.matchMaterial(args[1]) != null ? Material.matchMaterial(args[1]) : (args[1].equals("hand")? player.getInventory().getItemInMainHand().getType():mat);
                    }
                    entity.setItemStack(new ItemStack(mat));
                    selection = entity;

                }
                default -> {
                    TextComponent error = Component.text(" Incorrect entity type!").decoration(TextDecoration.BOLD, false).color(TextColor.fromHexString("#b4b4b4"));
                    sender.sendMessage(errorPrefix.append(error));
                    sender.playSound(Sound.sound(Key.key("entity.shulker.hurt_closed"), Sound.Source.MASTER, 1f, 1f));
                    return true;
                }
            }
            SelectionHandler.setSelection(player.getUniqueId(), selection);
            player.sendMessage(successPrefix.append(Component.text(" Spawned and selected entity!").decoration(TextDecoration.BOLD, false).color(TextColor.fromHexString("#b4b4b4"))));
            player.playSound(Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.MASTER, 1f, 2f));
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
