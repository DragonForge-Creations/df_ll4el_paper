package me.quickscythe.paper.ll4el.utils.managers;

import json2.JSONObject;
import me.quickscythe.dragonforge.utils.CoreUtils;
import me.quickscythe.dragonforge.utils.config.Config;
import me.quickscythe.dragonforge.utils.config.ConfigFileManager;
import me.quickscythe.dragonforge.utils.storage.ConfigManager;
import me.quickscythe.dragonforge.utils.storage.DataManager;
import me.quickscythe.paper.ll4el.utils.timers.BoogieTimer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class BoogieManager extends ConfigManager {

    private boolean stop = false;

    private List<UUID> available = new ArrayList<>();


    public BoogieManager(JavaPlugin plugin) {
        super(plugin, "boogies");
        updateAvailable();
    }

    private void updateAvailable() {
        available.clear();
        PlayerManager playerManager = (PlayerManager) DataManager.getConfigManager("players");
        for (UUID uuid : playerManager.getPlayers()) {
            if (playerManager.getLives(Bukkit.getOfflinePlayer(uuid)) > 0) {
                available.add(uuid);
            }
        }
    }

    public void rollBoogies(int amount, boolean timer) {
        if(stop) return;
        if (timer) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(CoreUtils.plugin(), new BoogieTimer(amount), 0);

        } else selectBoogies(amount);

    }


    public void selectBoogies(int amount) {
        if(stop) return;
        if(available.size() < amount) {
            updateAvailable();
        }
        int selected = 0;
        while (selected < amount) {
            PlayerManager playerManager = (PlayerManager) DataManager.getConfigManager("players");
            UUID randomUUID = playerManager.getPlayers().get(new Random().nextInt(playerManager.getPlayers().size()));
            while(playerManager.getLives(Bukkit.getOfflinePlayer(randomUUID)) < 1) {
                randomUUID = playerManager.getPlayers().get(new Random().nextInt(playerManager.getPlayers().size()));
            }
            while(!available.contains(randomUUID)) {
                randomUUID = playerManager.getPlayers().get(new Random().nextInt(playerManager.getPlayers().size()));
            }
            available.remove(randomUUID);
            OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(randomUUID);
            JSONObject potd = playerManager.getPlayerData(offlinePlayer);
            CoreUtils.logger().log("BoogieManager", "Selected " + offlinePlayer.getName() + " as a boogieman");
            if (!potd.getBoolean("boogie") && new Date().getTime() - potd.getLong("last_selected") > 10000) {
                selected = selected + 1;
                ((PlayerManager) DataManager.getConfigManager("players")).setBoogie(offlinePlayer);
            }
        }
    }

    public void stop(){
        stop = true;
    }
}
