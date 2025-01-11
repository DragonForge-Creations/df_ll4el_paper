package me.quickscythe.paper.ll4el.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import me.quickscythe.dragonforge.commands.CommandExecutor;
import me.quickscythe.dragonforge.utils.CoreUtils;
import me.quickscythe.dragonforge.utils.chat.Logger;
import me.quickscythe.dragonforge.utils.chat.MessageUtils;
import me.quickscythe.dragonforge.utils.storage.DataManager;
import me.quickscythe.paper.ll4el.utils.managers.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static io.papermc.paper.command.brigadier.Commands.argument;
import static io.papermc.paper.command.brigadier.Commands.literal;

public class LifeCommand extends CommandExecutor {
    public LifeCommand(JavaPlugin plugin) {
        super(plugin, "life");
    }

    // Usage: /life <set|add|remove> <player> [amount=1]

    @Override
    public LiteralCommandNode<CommandSourceStack> execute() {
        return literal(getName()).executes(context -> showUsage(context, "lastlife.admin.life")).then(literal("set").executes(context -> showUsage(context, "lastlife.admin.life.set")).then(argument("target", ArgumentTypes.players()).executes(context -> showUsage(context, "lastlife.admin.life.set")).then(argument("amount", IntegerArgumentType.integer()).executes(context -> {
            if (!(context.getSource().getSender() instanceof Player player))
                return logError(context, MessageUtils.getMessage("cmd.error.no_player"));
            if (!player.hasPermission("lastlife.admin.life.set"))
                return logError(context, MessageUtils.getMessage("cmd.error.no_perm"));
            int amount = IntegerArgumentType.getInteger(context, "amount");
            PlayerManager man = DataManager.getConfigManager("players", PlayerManager.class);
            PlayerSelectorArgumentResolver targetSelector = context.getArgument("target", PlayerSelectorArgumentResolver.class);
            targetSelector.resolve(context.getSource()).forEach(target -> {
                man.setLife(target, amount);
                CoreUtils.logger().log(Logger.LogLevel.INFO, "Life", MessageUtils.getMessage("cmd.life.set", amount, target.getName(), context.getSource().getSender().getName()));
            });
            return 1;
        })))).then(literal("add").executes(context -> {
            if (!(context.getSource().getSender() instanceof Player player))
                return logError(context, MessageUtils.getMessage("cmd.error.no_player"));
            if (!player.hasPermission("lastlife.admin.life.add"))
                return logError(context, MessageUtils.getMessage("cmd.error.no_perm"));
            DataManager.getConfigManager("players", PlayerManager.class).editLife(player, 1);
            CoreUtils.logger().log(Logger.LogLevel.INFO, "Life", MessageUtils.getMessage("cmd.life.add", 1, player), context.getSource().getSender());
            return 1;
        }).then(argument("target", ArgumentTypes.players()).executes(context -> {
            if (!context.getSource().getSender().hasPermission("lastlife.admin.life.add"))
                return logError(context, MessageUtils.getMessage("cmd.error.no_perm"));
            PlayerSelectorArgumentResolver targetSelector = context.getArgument("target", PlayerSelectorArgumentResolver.class);
            targetSelector.resolve(context.getSource()).forEach(target -> {
                DataManager.getConfigManager("players", PlayerManager.class).editLife(target, 1);
                CoreUtils.logger().log(Logger.LogLevel.INFO, "Life", MessageUtils.getMessage("cmd.life.add", 1, target.getName(), context.getSource().getSender().getName()));
            });
            return 1;
        }).then(argument("amount", IntegerArgumentType.integer()).executes(context -> {
            if (!context.getSource().getSender().hasPermission("lastlife.admin.life.add"))
                return logError(context, MessageUtils.getMessage("cmd.error.no_perm"));
            int amount = IntegerArgumentType.getInteger(context, "amount");
            PlayerSelectorArgumentResolver targetSelector = context.getArgument("target", PlayerSelectorArgumentResolver.class);
            targetSelector.resolve(context.getSource()).forEach(target -> {
                DataManager.getConfigManager("players", PlayerManager.class).editLife(target, amount);
                CoreUtils.logger().log(Logger.LogLevel.INFO, "Life", MessageUtils.getMessage("cmd.life.add", amount, target.getName(), context.getSource().getSender().getName()));
            });
            return 1;
        })))).then(literal("remove").executes(context -> {
            if (!(context.getSource().getSender() instanceof Player player))
                return logError(context, MessageUtils.getMessage("cmd.error.no_player"));
            if (!player.hasPermission("lastlife.admin.life.add"))
                return logError(context, MessageUtils.getMessage("cmd.error.no_perm"));
            DataManager.getConfigManager("players", PlayerManager.class).editLife(player, -1);
            CoreUtils.logger().log(Logger.LogLevel.INFO, "Life", MessageUtils.getMessage("cmd.life.add", -1, player), context.getSource().getSender());
            return 1;
        }).then(argument("target", ArgumentTypes.players()).executes(context -> {
            if (!context.getSource().getSender().hasPermission("lastlife.admin.life.add"))
                return logError(context, MessageUtils.getMessage("cmd.error.no_perm"));
            PlayerSelectorArgumentResolver targetSelector = context.getArgument("target", PlayerSelectorArgumentResolver.class);
            targetSelector.resolve(context.getSource()).forEach(target -> {
                DataManager.getConfigManager("players", PlayerManager.class).editLife(target, -1);
                CoreUtils.logger().log(Logger.LogLevel.INFO, "Life", MessageUtils.getMessage("cmd.life.add", -1, target.getName(), context.getSource().getSender().getName()));
            });
            return 1;
        }).then(argument("amount", IntegerArgumentType.integer()).executes(context -> {
            if (!context.getSource().getSender().hasPermission("lastlife.admin.life.add"))
                return logError(context, MessageUtils.getMessage("cmd.error.no_perm"));
            int amount = -IntegerArgumentType.getInteger(context, "amount");
            PlayerSelectorArgumentResolver targetSelector = context.getArgument("target", PlayerSelectorArgumentResolver.class);
            targetSelector.resolve(context.getSource()).forEach(target -> {
                DataManager.getConfigManager("players", PlayerManager.class).editLife(target, amount);
                CoreUtils.logger().log(Logger.LogLevel.INFO, "Life", MessageUtils.getMessage("cmd.life.add", amount, target.getName(), context.getSource().getSender().getName()));
            });
            return 1;
        })))).build();

    }
}
