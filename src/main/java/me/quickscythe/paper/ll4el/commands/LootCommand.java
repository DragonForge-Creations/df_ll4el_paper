package me.quickscythe.paper.ll4el.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.quickscythe.dragonforge.commands.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public class LootCommand extends CommandExecutor {
    public LootCommand(JavaPlugin plugin) {
        super(plugin, "loot");
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> getNode() {
        return null;
    }
}
