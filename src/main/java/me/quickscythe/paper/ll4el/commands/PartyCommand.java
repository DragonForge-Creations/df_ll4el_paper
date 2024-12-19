package me.quickscythe.paper.ll4el.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.quickscythe.dragonforge.commands.CommandExecutor;
import me.quickscythe.dragonforge.utils.chat.MessageUtils;
import me.quickscythe.dragonforge.utils.storage.DataManager;
import me.quickscythe.paper.ll4el.utils.managers.PlayerManager;
import me.quickscythe.paper.ll4el.utils.managers.party.Party;
import me.quickscythe.paper.ll4el.utils.managers.party.PartyManager;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static io.papermc.paper.command.brigadier.Commands.argument;
import static io.papermc.paper.command.brigadier.Commands.literal;
import static net.kyori.adventure.text.Component.text;

public class PartyCommand extends CommandExecutor {

    public PartyCommand(JavaPlugin plugin) {
        super(plugin, "party");
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> execute() {
        return literal(getName()).executes(commandContext -> {
            CommandSender sender = commandContext.getSource().getSender();
            sender.sendMessage(text("/" + getName() + " [help] ").color(NamedTextColor.GREEN).append(text("- Displays this list.").color(NamedTextColor.GRAY)));
            if (sender.hasPermission("lastlife.party.create"))
                sender.sendMessage(text("/" + getName() + " create <party> ").color(NamedTextColor.GREEN).append(text("- Creates a party.").color(NamedTextColor.GRAY)));
            if (sender.hasPermission("lastlife.party.join"))
                sender.sendMessage(text("/" + getName() + " join <party> " + (sender.hasPermission("lastlife.party.join.other") ? "[player] " : "")).color(NamedTextColor.GREEN).append(text("- " + (sender.hasPermission("lastlife.party.join.other") ? "Joins you or another player to a party" : "Joins you to a party") + ".").color(NamedTextColor.GRAY)));
            if (sender.hasPermission("lastlife.party.leave"))
                sender.sendMessage(text("/" + getName() + " leave " + (sender.hasPermission("lastlife.party.leave.other") ? "[player] " : "")).color(NamedTextColor.GREEN).append(text("- " + (sender.hasPermission("lastlife.party.leave.other") ? "Pull you or another player out of a party" : "Pulls out out of your party") + ".").color(NamedTextColor.GRAY)));
            if (sender.hasPermission("lastlife.party.chat"))
                sender.sendMessage(text("/" + getName() + " chat [message] ").color(NamedTextColor.GREEN).append(text("- Either sends a single chat to the party, or toggles party chat mode.").color(NamedTextColor.GRAY)));

            return Command.SINGLE_SUCCESS;
        }).then(argument("action", StringArgumentType.string()).suggests((context, builder) -> {
            CommandSender sender = context.getSource().getSender();
            builder.suggest("help");
            if (sender.hasPermission("lastlife.party.create")) builder.suggest("create");
            if (sender.hasPermission("lastlife.party.join")) builder.suggest("join");
            if (sender.hasPermission("lastlife.party.leave")) builder.suggest("leave");
            if (sender.hasPermission("lastlife.party.chat")) builder.suggest("chat");
            return builder.buildFuture();
        }).executes(context -> {
            PlayerManager playerManager = (PlayerManager) DataManager.getConfigManager("players");
            PartyManager partyManager = (PartyManager) DataManager.getConfigManager("parties");
            CommandSender sender = context.getSource().getSender();
            String action = StringArgumentType.getString(context, "action");
            if (action.equalsIgnoreCase("help")) {
                sender.sendMessage(text("/" + getName() + " [help] ").color(NamedTextColor.GREEN).append(text("- Displays this list.").color(NamedTextColor.GRAY)));
                if (sender.hasPermission("lastlife.party.create"))
                    sender.sendMessage(text("/" + getName() + " create <party> ").color(NamedTextColor.GREEN).append(text("- Creates a party.").color(NamedTextColor.GRAY)));
                if (sender.hasPermission("lastlife.party.join"))
                    sender.sendMessage(text("/" + getName() + " join <party> " + (sender.hasPermission("lastlife.party.join.other") ? "[player] " : "")).color(NamedTextColor.GREEN).append(text("- " + (sender.hasPermission("lastlife.party.join.other") ? "Joins you or another player to a party" : "Joins you to a party") + ".").color(NamedTextColor.GRAY)));
                if (sender.hasPermission("lastlife.party.leave"))
                    sender.sendMessage(text("/" + getName() + " leave " + (sender.hasPermission("lastlife.party.leave.other") ? "[player] " : "")).color(NamedTextColor.GREEN).append(text("- " + (sender.hasPermission("lastlife.party.leave.other") ? "Pull you or another player out of a party" : "Pulls out out of your party") + ".").color(NamedTextColor.GRAY)));
                if (sender.hasPermission("lastlife.party.chat"))
                    sender.sendMessage(text("/" + getName() + " chat [message] ").color(NamedTextColor.GREEN).append(text("- Either sends a single chat to the party, or toggles party chat mode.").color(NamedTextColor.GRAY)));
                return Command.SINGLE_SUCCESS;
            }
            if (action.equalsIgnoreCase("create")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(MessageUtils.getMessage("cmd.error.player_only"));
                    return Command.SINGLE_SUCCESS;
                }
                if (!sender.hasPermission("lastlife.party.create")) {
                    sender.sendMessage(MessageUtils.getMessage("cmd.error.no_perm"));
                    return Command.SINGLE_SUCCESS;
                }
                sender.sendMessage(MessageUtils.getMessage("cmd.error.invalid_args"));
                return Command.SINGLE_SUCCESS;
            }
            if (action.equalsIgnoreCase("join")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(MessageUtils.getMessage("cmd.error.player_only"));
                    return Command.SINGLE_SUCCESS;
                }
                if (!sender.hasPermission("lastlife.party.join")) {
                    sender.sendMessage(MessageUtils.getMessage("cmd.error.no_perm"));
                    return Command.SINGLE_SUCCESS;
                }
                sender.sendMessage(MessageUtils.getMessage("cmd.error.invalid_args"));
                return Command.SINGLE_SUCCESS;
            }
            if (action.equalsIgnoreCase("leave")) {
                if (!(sender instanceof Player player)) {
                    sender.sendMessage(MessageUtils.getMessage("cmd.error.player_only"));
                    return Command.SINGLE_SUCCESS;
                }
                if (!sender.hasPermission("lastlife.party.leave")) {
                    sender.sendMessage(MessageUtils.getMessage("cmd.error.no_perm"));
                    return Command.SINGLE_SUCCESS;
                }
                playerManager.setParty(player, "none");
                return Command.SINGLE_SUCCESS;
            }
            if (action.equalsIgnoreCase("chat")) {
                if (!(sender instanceof Player)) {
                    sender.sendMessage(MessageUtils.getMessage("cmd.error.player_only"));
                    return Command.SINGLE_SUCCESS;
                }
                if (!sender.hasPermission("lastlife.party.chat")) {
                    sender.sendMessage(MessageUtils.getMessage("cmd.error.no_perm"));
                    return Command.SINGLE_SUCCESS;
                }
                if (playerManager.getParty(((Player) sender)).equalsIgnoreCase("none")) {
                    sender.sendMessage(MessageUtils.getMessage("party.chat.no_party"));
                    return Command.SINGLE_SUCCESS;
                }

                partyManager.toggleChat(((Player) sender));
                return Command.SINGLE_SUCCESS;
            }
            return Command.SINGLE_SUCCESS;
        }).then(argument("arg2", StringArgumentType.string()).suggests((context, builder) -> {
            CommandSender sender = context.getSource().getSender();

            String action = context.getInput().split(" ")[1];
//                                    String action = StringArgumentType.getString(context, "action");
            if (action.equalsIgnoreCase("join") || action.equalsIgnoreCase("leave")) {
                if (sender.hasPermission("lastlife.party." + action.toLowerCase())) {
                    for (String s : ((PartyManager) DataManager.getConfigManager("parties")).getParties()) {
                        builder.suggest(s);
                    }
                }
            }
            return builder.buildFuture();
        }).executes(context -> {
            CommandSender sender = context.getSource().getSender();
            if (!(sender instanceof Player player)) {
                return logError(sender, "cmd.error.player_only");
            }
            String action = StringArgumentType.getString(context, "action");
            String arg2 = StringArgumentType.getString(context, "arg2");
            if (action.equalsIgnoreCase("create")) {
                if (arg2.equalsIgnoreCase("help")) {
                    sender.sendMessage(text("/" + getName() + " create <party> ").color(NamedTextColor.GREEN).append(text("- Creates a party.").color(NamedTextColor.GRAY)));
                    return Command.SINGLE_SUCCESS;
                }
                Party party = ((PartyManager) DataManager.getConfigManager("parties")).createParty(arg2);
                sender.sendMessage(MessageUtils.getMessage("cmd.party.create", party.name()));
                return Command.SINGLE_SUCCESS;
            }
            if (action.equalsIgnoreCase("join")) {
//                                        DataManager.getConfigManager("parties", PartyManager.class).getParty(arg2).
                DataManager.getConfigManager("players", PlayerManager.class).setParty(player, arg2);
                return Command.SINGLE_SUCCESS;
            }
            if (action.equalsIgnoreCase("leave")) {
                if (sender.hasPermission("lastlife.party.leave"))
                    ((PlayerManager) DataManager.getConfigManager("players")).setParty((Player) sender, "none");

                return Command.SINGLE_SUCCESS;
            }
            return Command.SINGLE_SUCCESS;
        }).then(argument("player", StringArgumentType.string()).suggests((context, builder) -> {
            CommandSender sender = context.getSource().getSender();
            String action = context.getInput().split(" ")[1];
            if (action.equalsIgnoreCase("join")) {
                for (Player p : sender.getServer().getOnlinePlayers()) {
                    builder.suggest(p.getName());
                }
            }
            return builder.buildFuture();
        }).executes(context -> {
            CommandSender sender = context.getSource().getSender();
            String action = StringArgumentType.getString(context, "action");
            String arg2 = StringArgumentType.getString(context, "arg2");
            String player = StringArgumentType.getString(context, "player");
            if (action.equalsIgnoreCase("join")) {
                Player target = sender.getServer().getPlayer(player);
                if (target == null) {
                    return logError(sender, "cmd.error.no_player");
                }
                DataManager.getConfigManager("players", PlayerManager.class).setParty(target, arg2);
                return Command.SINGLE_SUCCESS;
            }
            if (action.equalsIgnoreCase("leave")) {
                Player target = sender.getServer().getPlayer(player);
                if (target == null) {
                    return logError(sender, "cmd.error.no_player");
                }
                DataManager.getConfigManager("players", PlayerManager.class).setParty(target, "none");
                return Command.SINGLE_SUCCESS;
            }
            return Command.SINGLE_SUCCESS;
        })))).build();
    }
}

