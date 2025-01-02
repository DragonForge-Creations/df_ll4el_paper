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
        new CommandManager.CommandBuilder(new LifeCommand(this)).setDescription("Last Life life command.").setAliases("l").register();
        new CommandManager.CommandBuilder(new BoogieCommand(this)).setDescription("Last Life boogie command.").register();
        new CommandManager.CommandBuilder(new LootCommand(this)).setDescription("Last Life loot command.").register();
        new CommandManager.CommandBuilder(new LinkCommand(this)).setDescription("Last Life link command. Link to DonorDrive.").register();
        new CommandManager.CommandBuilder(new TaskCommand(this)).setDescription("Dragonfell weekly task command.").setAliases("task", "tasks", "wtask", "wtasks").register();
//        new CommandManager.CommandBuilder(new DonationCommand(this)).setDescription("Donation test command.").register();


    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }


}
