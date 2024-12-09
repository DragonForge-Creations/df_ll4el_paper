package me.quickscythe.paper.ll4el.utils.timers;

import me.quickscythe.dragonforge.utils.CoreUtils;
import me.quickscythe.dragonforge.utils.storage.DataManager;
import me.quickscythe.paper.ll4el.utils.Utils;
import me.quickscythe.paper.ll4el.utils.managers.PlayerManager;
import me.quickscythe.paper.ll4el.utils.managers.SettingsManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class MainTimer implements Runnable {

    protected Particle.DustOptions dustoptions = new Particle.DustOptions(Color.RED, 1);
    private final PlayerManager playerManager;

    public MainTimer() {
        this.playerManager = (PlayerManager) DataManager.getConfigManager("players");

    }

    @Override
    public void run() {

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (playerManager.isBoogie(player)) {
                if (SettingsManager.getSettings(player).particles())
                    player.spawnParticle(Particle.DUST, player.getLocation(), 1, dustoptions);
                if (SettingsManager.getSettings(player).icon())
                    player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new ComponentBuilder("                                                                                          \ue001").create());
                if (playerManager.getLives(player) < 1) playerManager.removeBoogie(player);
            }
        }
        Bukkit.getScheduler().runTaskLaterAsynchronously(Utils.plugin(), this, 0);
    }
}
