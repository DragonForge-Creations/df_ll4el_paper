package me.quickscythe.paper.ll4el.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.quickscythe.dragonforge.commands.CommandExecutor;
import me.quickscythe.dragonforge.utils.CoreUtils;
import me.quickscythe.dragonforge.utils.chat.Logger;
import me.quickscythe.dragonforge.utils.chat.MessageUtils;
import me.quickscythe.dragonforge.utils.storage.DataManager;
import me.quickscythe.paper.ll4el.utils.managers.loot.LootManager;
import me.quickscythe.paper.ll4el.utils.managers.loot.LootType;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static io.papermc.paper.command.brigadier.Commands.argument;
import static io.papermc.paper.command.brigadier.Commands.literal;

public class LootCommand extends CommandExecutor {
    public LootCommand(JavaPlugin plugin) {
        super(plugin, "lootdrop");
    }

    // /loot <drop|create> <location> [type]

    @Override
    public LiteralCommandNode<CommandSourceStack> execute() {
        return literal(getName()).executes(context -> showUsage(context, "lastlife.admin.loot")).then(argument("action", StringArgumentType.string()).executes(context -> {
            String action = context.getArgument("action", String.class);
            if(action.equalsIgnoreCase("drop")){
                CommandSender sender = context.getSource().getSender();
                if (sender.hasPermission("lastlife.admin.loot.drop")) {
                    LootManager lm = DataManager.getConfigManager("loot", LootManager.class);
                    lm.randomDrop(LootType.DEFAULT);
                    CoreUtils.logger().log(Logger.LogLevel.INFO,"LootManager", MessageUtils.getMessage("cmd.loot.drop.random"), sender);
                    return 1;
                }
            }
            showUsage(context, "lastlife.admin.loot");
            return 1;
        }).suggests((context, builder) -> {
            CommandSender sender = context.getSource().getSender();
            if (sender.hasPermission("lastlife.admin.loot.drop")) builder.suggest("drop");
            if (sender.hasPermission("lastlife.admin.loot.create")) builder.suggest("create");
            return builder.buildFuture();
        }).then(argument("location", StringArgumentType.string()).executes(context -> {
            CommandSender sender = context.getSource().getSender();
            String action = context.getArgument("action", String.class);
            String location = context.getArgument("location", String.class);
            if (action.equalsIgnoreCase("drop")) {
                if (sender.hasPermission("lastlife.admin.loot.drop")) {
                    LootType type = LootType.DEFAULT;
                    LootManager lm = DataManager.getConfigManager("loot", LootManager.class);
                    lm.dropLoot(location, type);
                    CoreUtils.logger().log(Logger.LogLevel.INFO,"LootManager", MessageUtils.getMessage("cmd.loot.drop", location), sender);
                }

            }
            if (action.equalsIgnoreCase("create")) {
                if (!(sender instanceof Player player)) {
                    return logError(sender, MessageUtils.getMessage("cmd.error.player_only"));
                }
                if (sender.hasPermission("lastlife.admin.loot.create")) {
                    LootManager lm = DataManager.getConfigManager("loot", LootManager.class);
                    lm.startEditing(player, location);
                    CoreUtils.logger().log(Logger.LogLevel.INFO,"LootManager", MessageUtils.getMessage("cmd.loot.create", location), player);
                }
            }
            return 1;
        }).suggests((context, builder) -> {
            String[] args = context.getInput().split(" ");
            if (args[1].equalsIgnoreCase("create")) return builder.buildFuture();
            LootManager lm = DataManager.getConfigManager("loot", LootManager.class);
            for (String s : lm.config().getData().keySet()) {
                builder.suggest(s);
            }
            return builder.buildFuture();
        }).then(argument("type", StringArgumentType.string()).executes(context -> {
            CommandSender sender = context.getSource().getSender();
            String action = context.getArgument("action", String.class);
            String location = context.getArgument("location", String.class);
            if (action.equalsIgnoreCase("drop")) {
                if (sender.hasPermission("lastlife.admin.loot.drop")) {
                    LootType type = LootType.valueOf(context.getArgument("type", String.class).toUpperCase());
                    LootManager lm = DataManager.getConfigManager("loot", LootManager.class);
                    lm.dropLoot(location, type);
                    CoreUtils.logger().log(Logger.LogLevel.INFO,"LootManager", MessageUtils.getMessage("cmd.loot.drop", location), sender);
                }
            }

            return 1;
        }).suggests((context, builder) -> {
            for (LootType type : LootType.values()) {
                builder.suggest(type.name().toLowerCase());
            }
            return builder.buildFuture();
        })))).build();
    }


}
