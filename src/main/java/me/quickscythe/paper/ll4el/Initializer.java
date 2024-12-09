package me.quickscythe.paper.ll4el;

import me.quickscythe.dragonforge.commands.CommandManager;
import me.quickscythe.dragonforge.commands.executors.UpdateCommand;
import me.quickscythe.dragonforge.utils.CoreUtils;
import me.quickscythe.paper.ll4el.commands.AdminCommands;
import me.quickscythe.paper.ll4el.commands.PartyCommand;
import me.quickscythe.paper.ll4el.commands.PlayerCommands;
import me.quickscythe.paper.ll4el.listeners.ChatListener;
import me.quickscythe.paper.ll4el.listeners.PlayerListener;
import me.quickscythe.paper.ll4el.utils.Utils;
import me.quickscythe.paper.ll4el.utils.managers.LifeManager;
import me.quickscythe.paper.ll4el.utils.managers.PartyManager;
import me.quickscythe.paper.ll4el.utils.managers.PlayerManager;
import me.quickscythe.paper.ll4el.utils.managers.SettingsManager;
import org.bukkit.plugin.java.JavaPlugin;

public final class Initializer extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Utils.init(this);

        new ChatListener(this);
        new PlayerListener(this);

        new CommandManager.CommandBuilder(new PartyCommand(this)).setDescription("Last Life party command.").setAliases("p").register();


        new PlayerCommands(this,"settings", "party");
        new AdminCommands(this, "inventory", "lastlife", "status");


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
