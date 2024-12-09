package me.quickscythe.paper.ll4el.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.quickscythe.dragonforge.commands.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

public class StatusCommand extends CommandExecutor {
    public StatusCommand(JavaPlugin plugin) {
        super(plugin, "status");
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> getNode() {
        return null;
    }
}
