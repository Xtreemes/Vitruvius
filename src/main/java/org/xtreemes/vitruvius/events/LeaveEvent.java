package org.xtreemes.vitruvius.events;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.xtreemes.vitruvius.selection.SelectionHandler;

public class LeaveEvent implements Listener {

    @EventHandler
    public void playerLeave(PlayerQuitEvent e){
        SelectionHandler.setSelection(e.getPlayer().getUniqueId());
    }
}
