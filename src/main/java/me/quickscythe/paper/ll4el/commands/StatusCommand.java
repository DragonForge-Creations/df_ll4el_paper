package me.quickscythe.paper.ll4el.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.quickscythe.dragonforge.commands.CommandExecutor;
import me.quickscythe.dragonforge.utils.gui.GuiManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static io.papermc.paper.command.brigadier.Commands.argument;
import static io.papermc.paper.command.brigadier.Commands.literal;

public class StatusCommand extends CommandExecutor {
    public StatusCommand(JavaPlugin plugin) {
        super(plugin, "status");
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> getNode() {
        return literal(getName()).executes(context -> {
            CommandSender sender = context.getSource().getSender();
            if(!(sender instanceof Player player))
                return logError(sender, "You must be a player to use this command.");
            if(!player.hasPermission("lastlife.admin.status"))
                return logError(sender, "You do not have permission to use this command.");
            GuiManager.openGui(player, GuiManager.getGui("status"));

            return Command.SINGLE_SUCCESS;
        }).then(argument("system", StringArgumentType.string())
                .suggests((context, builder) -> {
                    if(context.getSource().getSender().hasPermission("lastlife.admin.status")){
                        builder.suggest("party");
                        builder.suggest("boogie");
                        builder.suggest("player");
                    }
                    return builder.buildFuture();
                })
                .executes(context -> {
                    String system = StringArgumentType.getString(context, "system");
                    CommandSender sender = context.getSource().getSender();
                    if(!(sender instanceof Player player))
                        return logError(sender, "You must be a player to use this command.");
                    if(!player.hasPermission("lastlife.admin.status"))
                        return logError(sender, "You do not have permission to use this command.");
                    if(system.equalsIgnoreCase("party")){

                    }
                    if(system.equalsIgnoreCase("boogie")){

                    }
                    if(system.equalsIgnoreCase("player")){

                    }
                    return Command.SINGLE_SUCCESS;
                })).build();
    }
}
