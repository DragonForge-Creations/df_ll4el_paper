package me.quickscythe.paper.ll4el.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.quickscythe.dragonforge.commands.CommandExecutor;
import me.quickscythe.dragonforge.utils.chat.MessageUtils;
import me.quickscythe.dragonforge.utils.storage.DataManager;
import me.quickscythe.paper.ll4el.utils.managers.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import static io.papermc.paper.command.brigadier.Commands.argument;
import static io.papermc.paper.command.brigadier.Commands.literal;

public class LifeCommand extends CommandExecutor {
    public LifeCommand(JavaPlugin plugin) {
        super(plugin, "life");
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> execute() {
        return literal(getName()).executes(context -> logError(context.getSource().getSender(), MessageUtils.getMessage("cmd.usage.life")))
                .then(argument("operator", StringArgumentType.string())
                        .executes(context -> logError(context.getSource().getSender(), MessageUtils.getMessage("cmd.usage.life")))
                        .suggests((context, builder) -> {
                            builder.suggest("set");
                            builder.suggest("add");
                            builder.suggest("remove");
                            return builder.buildFuture();
                        }).then(argument("player", StringArgumentType.string())
                                .executes(context -> {
                                    String operator = context.getArgument("operator", String.class);
                                    if (operator.equalsIgnoreCase("set"))
                                        return logError(context.getSource().getSender(), MessageUtils.getMessage("cmd.usage.life"));
                                    int amount = operator.equalsIgnoreCase("remove") ? -1 : 1;
                                    String player = context.getArgument("player", String.class);
                                    DataManager.getConfigManager("players", PlayerManager.class).editLife(Bukkit.getPlayer(player), amount);

                                    return 1;
                                })
                                .suggests((context, builder) -> {
                                    Bukkit.getOnlinePlayers().forEach(player -> builder.suggest(player.getName()));
                                    return builder.buildFuture();
                                }).then(argument("amount", IntegerArgumentType.integer())
                                        .executes(context -> {
                                            String operator = context.getArgument("operator", String.class);
                                            int amount = context.getArgument("amount", Integer.class);
                                            String player = context.getArgument("player", String.class);
                                            PlayerManager man = DataManager.getConfigManager("players", PlayerManager.class);
                                            if (operator.equalsIgnoreCase("set"))
                                                man.setLife(Bukkit.getOfflinePlayer(player), amount);
                                            else
                                                man.editLife(Bukkit.getPlayer(player), amount * (operator.equalsIgnoreCase("remove") ? -1 : 1));
                                            return 1;
                                        })
                                )
                        )
                )
                .build();
    }
}
