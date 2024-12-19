package me.quickscythe.paper.ll4el.utils.timers;

import me.quickscythe.dragonforge.utils.storage.DataManager;
import me.quickscythe.paper.ll4el.utils.DonorDriveApi;
import me.quickscythe.paper.ll4el.utils.Utils;
import me.quickscythe.paper.ll4el.utils.managers.BoogieManager;
import me.quickscythe.paper.ll4el.utils.managers.PlayerManager;
import me.quickscythe.paper.ll4el.utils.managers.SettingsManager;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

public class MainTimer implements Runnable {

    private final PlayerManager playerManager;
    protected Particle.DustOptions dustoptions = new Particle.DustOptions(Color.RED, 1);
    private long lastApiUpdate = 0;
    private long lastBoogieUpdate = 0;

    public MainTimer() {
        this.playerManager = (PlayerManager) DataManager.getConfigManager("players");

    }

    @Override
    public void run() {

        long now = System.currentTimeMillis();
        if (now - lastApiUpdate >= TimeUnit.MILLISECONDS.convert(DonorDriveApi.getDonorTime(), TimeUnit.MINUTES)) {
            lastApiUpdate = now;
            Bukkit.getScheduler().runTaskLater(Utils.plugin(), DonorDriveApi::processDonations, 0);
        }

        if (DataManager.getConfigManager("boogies", BoogieManager.class).started())
            if (now - lastBoogieUpdate >= TimeUnit.MILLISECONDS.convert(DonorDriveApi.getBoogieTimer(), TimeUnit.MINUTES)) {
                lastBoogieUpdate = now;
                Bukkit.getScheduler().runTaskLater(Utils.plugin(), DonorDriveApi::rollBoogies, 0);
            }

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
