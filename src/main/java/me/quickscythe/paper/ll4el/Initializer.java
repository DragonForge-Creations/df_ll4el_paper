package me.quickscythe.paper.ll4el;

import me.quickscythe.paper.ll4el.utils.managers.LifeManager;
import me.quickscythe.paper.ll4el.utils.managers.PartyManager;
import me.quickscythe.paper.ll4el.utils.managers.PlayerManager;
import me.quickscythe.paper.ll4el.utils.managers.SettingsManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Initializer extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        LifeManager.start();
        PartyManager.start();
        PlayerManager.start();


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
