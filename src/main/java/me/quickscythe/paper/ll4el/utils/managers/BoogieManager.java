package me.quickscythe.paper.ll4el.utils.managers;

import json2.JSONObject;
import me.quickscythe.dragonforge.utils.CoreUtils;
import me.quickscythe.dragonforge.utils.storage.ConfigManager;
import me.quickscythe.dragonforge.utils.storage.DataManager;
import me.quickscythe.paper.ll4el.utils.timers.BoogieTimer;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class BoogieManager extends ConfigManager {

    boolean started = false;

    public BoogieManager(JavaPlugin plugin) {
        super(plugin, "boogies");
    }

    public void rollBoogies(int amount, boolean timer) {
        if (timer) {
            Bukkit.getScheduler().runTaskLaterAsynchronously(CoreUtils.plugin(), new BoogieTimer(amount), 0);

        } else selectBoogies(amount);

    }

    public boolean started(){
        return started;
    }

    public void selectBoogies(int amount) {
        int selected = 0;
        int check = 0;
        while (selected < amount && check < 2) {
            check = check + 1;
            Player pot = (Player) ((List) Bukkit.getOnlinePlayers()).get(new Random().nextInt(Bukkit.getOnlinePlayers().size()));
            JSONObject potd = ((PlayerManager) DataManager.getConfigManager("players")).getPlayerData(pot);
            if (!potd.getBoolean("boogie") && new Date().getTime() - potd.getLong("last_selected") > 10000) {
                selected = selected + 1;
                ((PlayerManager) DataManager.getConfigManager("players")).setBoogie(pot);
            }
        }
    }




    public List<UUID> getBoogies() {
        List<UUID> uids = new ArrayList<>();
        PlayerManager playerManager = (PlayerManager) DataManager.getConfigManager("players");
        for (UUID uid : playerManager.getPlayers())
            if (playerManager.isBoogie(Bukkit.getOfflinePlayer(uid))) uids.add(uid);
        return uids;

    }

    public void startSession() {
        started = true;
    }

    public void stopSession() {
        started = false;
    }
}
