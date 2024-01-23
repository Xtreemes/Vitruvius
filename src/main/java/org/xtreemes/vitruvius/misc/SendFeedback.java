package org.xtreemes.vitruvius.misc;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.TextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;

public class SendFeedback {
    private static final TextComponent errorPrefix = Component.text("FAIL").decorate(TextDecoration.BOLD).color(TextColor.fromHexString("#fa3e3e"));
    private static final TextComponent successPrefix = Component.text("SUCCESS").decorate(TextDecoration.BOLD).color(TextColor.fromHexString("#41ea00"));

    private static final Sound errorSound = Sound.sound(Key.key("entity.shulker.hurt_closed"), Sound.Source.MASTER, 1f, 1f);
    private static final Sound successSound = Sound.sound(Key.key("entity.experience_orb.pickup"), Sound.Source.MASTER, 1f, 2f);
    public static void sendFeedback(Player player, String message, boolean success){
        final TextComponent result = (success ? successPrefix : errorPrefix).append(Component.text(
                " "+message).color(TextColor.fromHexString("#b4b4b4")).decoration(TextDecoration.BOLD, false)
        );
        player.sendMessage(result);
        player.playSound(success ? successSound : errorSound);
    }
}
