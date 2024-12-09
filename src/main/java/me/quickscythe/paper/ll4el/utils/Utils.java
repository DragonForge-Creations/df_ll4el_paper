package me.quickscythe.paper.ll4el.utils;

import me.quickscythe.dragonforge.utils.storage.DataManager;
import me.quickscythe.paper.ll4el.Initializer;
import me.quickscythe.paper.ll4el.utils.managers.BoogieManager;
import me.quickscythe.paper.ll4el.utils.managers.LifeManager;
import me.quickscythe.paper.ll4el.utils.managers.PartyManager;
import me.quickscythe.paper.ll4el.utils.managers.PlayerManager;
import me.quickscythe.paper.ll4el.utils.timers.MainTimer;
import org.bukkit.Bukkit;

import javax.xml.crypto.Data;

public class Utils {

    private static Initializer plugin;

    public static void init(Initializer plugin){
        Utils.plugin = plugin;
        DataManager.registerConfigManager(new BoogieManager(plugin));
        DataManager.registerConfigManager(new PartyManager(plugin));
        DataManager.registerConfigManager(new PlayerManager(plugin));
        LifeManager.start();
//        PartyManager.start();
//        PlayerManager.start();

        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, new MainTimer(), 0);
    }

    public static Initializer plugin(){
        return plugin;
    }
}
