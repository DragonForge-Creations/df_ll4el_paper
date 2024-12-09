package me.quickscythe.paper.ll4el.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.quickscythe.dragonforge.commands.CommandExecutor;
import org.bukkit.plugin.java.JavaPlugin;

import static io.papermc.paper.command.brigadier.Commands.literal;

public class LastLifeCommand extends CommandExecutor {
    public LastLifeCommand(JavaPlugin plugin) {
        super(plugin, "lastlife");
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> getNode() {
        return literal(getName())
                .executes(context -> logError(context.getSource().getSender(), "Usage: /lastlife <subcommand>"))
                .build();
    }
}
