package me.quickscythe.paper.ll4el;

import me.quickscythe.dragonforge.commands.CommandManager;
import me.quickscythe.paper.ll4el.commands.*;
import me.quickscythe.paper.ll4el.listeners.ChatListener;
import me.quickscythe.paper.ll4el.listeners.PlayerListener;
import me.quickscythe.paper.ll4el.utils.Utils;
import org.bukkit.plugin.java.JavaPlugin;

public final class Initializer extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Utils.init(this);

        new ChatListener(this);
        new PlayerListener(this);

        new CommandManager.CommandBuilder(new PartyCommand(this)).setDescription("Last Life party command.").setAliases("p").register();
        new CommandManager.CommandBuilder(new SettingsCommand(this)).setDescription("Last Life settings command.").setAliases("set").register();
        new CommandManager.CommandBuilder(new LastLifeCommand(this)).setDescription("Last Life admin command.").setAliases("ll").register();
        new CommandManager.CommandBuilder(new StatusCommand(this)).setDescription("Last Life status command.").setAliases("st", "s").register();
        new CommandManager.CommandBuilder(new LifeCommand(this)).setDescription("Last Life life command.").setAliases("l").register();
        new CommandManager.CommandBuilder(new BoogieCommand(this)).setDescription("Last Life boogie command.").register();


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
