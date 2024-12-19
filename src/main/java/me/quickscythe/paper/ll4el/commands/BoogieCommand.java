package me.quickscythe.paper.ll4el.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.quickscythe.dragonforge.commands.CommandExecutor;
import me.quickscythe.dragonforge.utils.CoreUtils;
import me.quickscythe.dragonforge.utils.chat.Logger;
import me.quickscythe.dragonforge.utils.chat.MessageUtils;
import me.quickscythe.dragonforge.utils.storage.DataManager;
import me.quickscythe.paper.ll4el.utils.managers.BoogieManager;
import me.quickscythe.paper.ll4el.utils.managers.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import static io.papermc.paper.command.brigadier.Commands.argument;
import static io.papermc.paper.command.brigadier.Commands.literal;


public class BoogieCommand extends CommandExecutor {
    public BoogieCommand(JavaPlugin plugin) {
        super(plugin, "boogie");
    }

    // /boogie roll [amount]
    // boogie set [player]
    // boogie remove [player|all]

    // /boogie start
    // /boogie stop

    @Override
    public LiteralCommandNode<CommandSourceStack> execute() {
        return literal(getName()).executes(context -> logError(context.getSource().getSender(), MessageUtils.getMessage("cmd.boogie.usage"))).then(argument("action", StringArgumentType.string()).executes(context -> {
            CommandSender sender = context.getSource().getSender();
            String action = StringArgumentType.getString(context, "action");
            if (action.equalsIgnoreCase("start")) {
                if (sender.hasPermission("lastlife.boogie.start")) {
                    DataManager.getConfigManager("boogies", BoogieManager.class).startSession();
                    CoreUtils.logger().log(Logger.LogLevel.INFO, "Boogie", MessageUtils.getMessage("cmd.boogie.start"), sender);
                }
            }
            if (action.equalsIgnoreCase("stop")) {
                if (sender.hasPermission("lastlife.boogie.stop")) {
                    DataManager.getConfigManager("boogies", BoogieManager.class).stopSession();
                    CoreUtils.logger().log(Logger.LogLevel.INFO, "Boogie", MessageUtils.getMessage("cmd.boogie.stop"), sender);
                }
            }
            if (action.equalsIgnoreCase("roll")) {
                if (!sender.hasPermission("lastlife.boogie.roll"))
                    return logError(sender, MessageUtils.getMessage("cmd.error.no_perm"));
                DataManager.getConfigManager("boogies", BoogieManager.class).rollBoogies(1, true);
                CoreUtils.logger().log(Logger.LogLevel.INFO, "Boogie", MessageUtils.getMessage("cmd.boogie.roll", 1), sender);
                return 1;
            }
            return logError(sender, sender.hasPermission("lastlife.boogie") ? MessageUtils.getMessage("cmd.boogie.usage") : MessageUtils.getMessage("cmd.error.no_perm"));
        }).suggests((context, builder) -> {
            CommandSender sender = context.getSource().getSender();
            String key = "lastlife.boogie";
            if (sender.hasPermission(key)) {
                if (sender.hasPermission(key + ".roll")) builder.suggest("roll");
                if (sender.hasPermission(key + ".set")) builder.suggest("set");
                if (sender.hasPermission(key + ".remove")) builder.suggest("remove");
                if (sender.hasPermission(key + ".start")) builder.suggest("start");
                if (sender.hasPermission(key + ".stop")) builder.suggest("stop");
            }
            return builder.buildFuture();
        }).then(argument("player", StringArgumentType.string()).executes(context -> {
            CommandSender sender = context.getSource().getSender();
            String action = StringArgumentType.getString(context, "action");
            String target = StringArgumentType.getString(context, "player");
            if (action.equalsIgnoreCase("set")) {
                if (!sender.hasPermission("lastlife.boogie.set"))
                    return logError(sender, MessageUtils.getMessage("cmd.error.no_perm"));
                OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(target);
                if (!targetPlayer.hasPlayedBefore())
                    return logError(sender, MessageUtils.getMessage("cmd.error.player_not_found"));
                DataManager.getConfigManager("players", PlayerManager.class).setBoogie(targetPlayer);
                CoreUtils.logger().log(Logger.LogLevel.INFO, "Boogie", MessageUtils.getMessage("cmd.boogie.set", target), sender);
            }
            if (action.equalsIgnoreCase("remove")) {
                if (!sender.hasPermission("lastlife.boogie.remove"))
                    return logError(sender, MessageUtils.getMessage("cmd.error.no_perm"));
                if (target.equalsIgnoreCase("all")) {
                    DataManager.getConfigManager("players", PlayerManager.class).getPlayers().forEach((uuid) -> {
                        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(uuid);
                        PlayerManager man = DataManager.getConfigManager("players", PlayerManager.class);
                        if (man.isBoogie(targetPlayer)) man.removeBoogie(targetPlayer);
                    });
                    CoreUtils.logger().log(Logger.LogLevel.INFO, "Boogie", MessageUtils.getMessage("cmd.boogie.remove.all"), sender);
                } else {
                    OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(target);
                    if (!targetPlayer.hasPlayedBefore())
                        return logError(sender, MessageUtils.getMessage("cmd.error.player_not_found"));
                    DataManager.getConfigManager("players", PlayerManager.class).removeBoogie(targetPlayer);
                    CoreUtils.logger().log(Logger.LogLevel.INFO, "Boogie", MessageUtils.getMessage("cmd.boogie.remove.player", target), sender);
                }
            }
            if (action.equalsIgnoreCase("roll")) {
                if (!sender.hasPermission("lastlife.boogie.roll"))
                    return logError(sender, MessageUtils.getMessage("cmd.error.no_perm"));
                try {
                    int amount = Integer.parseInt(target);
                    DataManager.getConfigManager("boogies", BoogieManager.class).rollBoogies(amount, true);
                    CoreUtils.logger().log(Logger.LogLevel.INFO, "Boogie", MessageUtils.getMessage("cmd.boogie.roll", amount), sender);
                } catch (NumberFormatException e) {
                    return logError(sender, MessageUtils.getMessage("cmd.error.invalid_number", target));
                }
            }
            return 1;
        }).suggests((context, builder) -> {
            CommandSender sender = context.getSource().getSender();
            if (sender.hasPermission("lastlife.boogie")) {
                String[] input = context.getInput().split(" ");
                String action = input[1];
                if (action.equalsIgnoreCase("set") || action.equalsIgnoreCase("remove")) {
                    DataManager.getConfigManager("players", PlayerManager.class).getPlayers().forEach((uuid) -> {
                        OfflinePlayer targetPlayer = Bukkit.getOfflinePlayer(uuid);
                        if (action.equalsIgnoreCase("set") || (action.equalsIgnoreCase("remove") && DataManager.getConfigManager("players", PlayerManager.class).isBoogie(targetPlayer)))
                            builder.suggest(targetPlayer.getName());
                    });
                    builder.suggest("all");
                }
                if (action.equalsIgnoreCase("roll")) {
                    builder.suggest(1);
                    builder.suggest(2);
                    builder.suggest(3);
                    builder.suggest(4);
                    builder.suggest(5);
                }
            }
            return builder.buildFuture();
        }))).build();
    }
}
