package org.xtreemes.vitruvius.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Display;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.xtreemes.vitruvius.misc.SendFeedback;
import org.xtreemes.vitruvius.selection.SelectionHandler;

import java.util.Objects;

public class DeleteCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        Player player;
        try {
            player = Objects.requireNonNull(sender.getServer().getPlayer(sender.getName()));
        } catch (NullPointerException e) {
            return true;
        }
        if(!player.isOp()){
            SendFeedback.sendFeedback(player, "You must have op to use this command!", false);
            return true;
        }
        Display entity = SelectionHandler.getSelection(player.getUniqueId());
        if(entity == null){
            SendFeedback.sendFeedback(player, "No selection!", false);
            return true;
        }
        SelectionHandler.deleteSelection(entity);
        SendFeedback.sendFeedback(player, "Selected entity deleted!", true);
        return true;
    }
}
