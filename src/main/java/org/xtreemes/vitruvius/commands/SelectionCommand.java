package org.xtreemes.vitruvius.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.xtreemes.vitruvius.misc.SendFeedback;
import org.xtreemes.vitruvius.selection.SelectionHandler;

import java.util.Collection;
import java.util.Objects;

public class SelectionCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player;
        try {
            player = Objects.requireNonNull(sender.getServer().getPlayer(sender.getName()));
        } catch (NullPointerException e) {
            return true;
        }
        double radius;
        if(!sender.isOp()){
            SendFeedback.sendFeedback(player, "You must have op to use this command!", false);
            return true;
        }
        if(args.length == 0){
            SelectionHandler.setSelection(player.getUniqueId());
            SendFeedback.sendFeedback(player, "Selection reset.", true);
            return true;
        }
        try {
            radius = Double.parseDouble(args[0]);
        } catch (NumberFormatException e) {
            SendFeedback.sendFeedback(player, "Could not parse radius! (Not a double?)", false);
            return true;
        }
        Collection<Display> nearby = player.getWorld().getNearbyEntitiesByType(Display.class, player.getLocation(), radius);
        Display closest = null;
        if(nearby.isEmpty()){
            SendFeedback.sendFeedback(player,"No entities within the radius!", false);
            return true;
        } else {
            double dis = 99999;
            for(Display entity : nearby){
                double edis = entity.getLocation().subtract(player.getLocation()).length();
                if(edis < dis){
                    dis = edis;
                    closest = entity;
                }
            }
            SelectionHandler.setSelection(player.getUniqueId(), closest);
            SendFeedback.sendFeedback(player, "Selection updated!", true);
        }
        return true;
    }
}
