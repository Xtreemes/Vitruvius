package org.xtreemes.vitruvius.selection;

import org.bukkit.entity.Display;

import java.util.HashMap;
import java.util.UUID;

public class SelectionHandler {
    private static final HashMap<UUID, Display> selections = new HashMap<>();

    public static void setSelection(UUID player, Display entity){
        if(selections.containsKey(player)){
            Display check = selections.get(player);
            selections.remove(player);
            if(!selections.containsValue(check)){
                check.setGlowing(false);
            }
        }
        selections.put(player, entity);
        entity.setGlowing(true);
    }
    public static void setSelection(UUID player){
        if(selections.containsKey(player)){
            Display check = selections.get(player);
            selections.remove(player);
            if(!selections.containsValue(check)){
                check.setGlowing(false);
            }
        }

    }
    public static Display getSelection(UUID player){
        return selections.getOrDefault(player, null);
    }
    public static void resetSelections(){
        for(UUID uuid : selections.keySet()){
            setSelection(uuid);
        }
    }
}
