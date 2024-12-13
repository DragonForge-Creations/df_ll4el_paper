package me.quickscythe.paper.ll4el.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.quickscythe.dragonforge.commands.CommandExecutor;
import me.quickscythe.dragonforge.utils.storage.DataManager;
import me.quickscythe.paper.ll4el.utils.managers.BoogieManager;
import org.bukkit.plugin.java.JavaPlugin;

import static io.papermc.paper.command.brigadier.Commands.literal;


public class BoogieCommand extends CommandExecutor {
    public BoogieCommand(JavaPlugin plugin) {
        super(plugin, "boogie");
    }


    @Override
    public LiteralCommandNode<CommandSourceStack> getNode() {
        return literal(getName()).executes(context -> {
            DataManager.getConfigManager("boogies", BoogieManager.class).rollBoogies(1, true);
            return 1;
        }).build();
    }
}
