package me.quickscythe.paper.ll4el.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import me.quickscythe.dragonforge.commands.CommandExecutor;
import me.quickscythe.dragonforge.utils.chat.MessageUtils;
import me.quickscythe.dragonforge.utils.storage.DataManager;
import me.quickscythe.paper.ll4el.commands.arguments.PartyArgumentType;
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
        return literal(getName()).executes(context -> {
            context.getSource().getSender().getServer().dispatchCommand(context.getSource().getSender(), "party help");
            return 1;
        }).then(literal("help").executes(context -> {
            CommandSender sender = context.getSource().getSender();
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
        })).then(literal("create").executes(context -> {
            CommandSender sender = context.getSource().getSender();
            if (!sender.hasPermission("lastlife.party.create")) {
                sender.sendMessage(MessageUtils.getMessage("cmd.error.no_perm"));
                return Command.SINGLE_SUCCESS;
            }
            sender.sendMessage(MessageUtils.getMessage("cmd.error.invalid_args"));
            return Command.SINGLE_SUCCESS;
        }).then(argument("party", StringArgumentType.string()).executes(context -> {
            CommandSender sender = context.getSource().getSender();
            String partyName = StringArgumentType.getString(context, "party");
            if (partyName.equalsIgnoreCase("help")) {
                sender.sendMessage(text("/" + getName() + " create <party> ").color(NamedTextColor.GREEN).append(text("- Creates a party.").color(NamedTextColor.GRAY)));
                return Command.SINGLE_SUCCESS;
            }
            Party party = ((PartyManager) DataManager.getConfigManager("parties")).createParty(partyName);
            sender.sendMessage(MessageUtils.getMessage("cmd.party.create", party.name()));
            return Command.SINGLE_SUCCESS;
        }))).then(literal("join").executes(context -> logError(context, "Usage: /party join <party>")).then(argument("party", new PartyArgumentType()).executes(context -> {
            if (!(context.getSource().getSender() instanceof Player player)) {
                return logError(context, "cmd.error.player_only");
            }
            if (!player.hasPermission("lastlife.party.join")) {
                return logError(context, "cmd.error.no_perm");
            }
            Party party = context.getArgument("party", Party.class);
            DataManager.getConfigManager("players", PlayerManager.class).setParty(player, party.name());
            return 1;

        }).then(argument("player", ArgumentTypes.players()).executes(context -> {
            if (!(context.getSource().getSender() instanceof Player player)) {
                return logError(context, "cmd.error.player_only");
            }
            if (!player.hasPermission("lastlife.party.join")) {
                return logError(context, "cmd.error.no_perm");
            }
            Party party = context.getArgument("party", Party.class);
            PlayerSelectorArgumentResolver selector = context.getArgument("player", PlayerSelectorArgumentResolver.class);
            for (Player target : selector.resolve(context.getSource())) {
                DataManager.getConfigManager("players", PlayerManager.class).setParty(target, party.name());
            }
            return 1;

        })))).then(literal("leave").executes(context -> {
            if (!(context.getSource().getSender() instanceof Player player)) {
                return logError(context, "cmd.error.player_only");
            }
            if (!player.hasPermission("lastlife.party.leave")) {
                return logError(context, "cmd.error.no_perm");
            }
            DataManager.getConfigManager("players", PlayerManager.class).setParty(player, "none");
            return 1;
        }).then(argument("player", ArgumentTypes.players()).executes(context -> {
            if (!(context.getSource().getSender() instanceof Player player)) {
                return logError(context, "cmd.error.player_only");
            }
            if (!player.hasPermission("lastlife.party.leave")) {
                return logError(context, "cmd.error.no_perm");
            }
            PlayerSelectorArgumentResolver selector = context.getArgument("player", PlayerSelectorArgumentResolver.class);
            for (Player target : selector.resolve(context.getSource())) {
                context.getSource().getSender().sendMessage(text("Pulled ", NamedTextColor.YELLOW).append(target.displayName()).append(text(" out of their party.", NamedTextColor.YELLOW)));
                DataManager.getConfigManager("players", PlayerManager.class).setParty(target, "none");
            }
            return 1;
        }))).then(literal("chat").executes(context -> {
            CommandSender sender = context.getSource().getSender();
            if (!(sender instanceof Player)) {
                sender.sendMessage(MessageUtils.getMessage("cmd.error.player_only"));
                return Command.SINGLE_SUCCESS;
            }
            if (!sender.hasPermission("lastlife.party.chat")) {
                sender.sendMessage(MessageUtils.getMessage("cmd.error.no_perm"));
                return Command.SINGLE_SUCCESS;
            }
            PlayerManager playerManager = (PlayerManager) DataManager.getConfigManager("players");
            PartyManager partyManager = (PartyManager) DataManager.getConfigManager("parties");
            if (playerManager.getParty(((Player) sender)).equalsIgnoreCase("none")) {
                sender.sendMessage(MessageUtils.getMessage("party.chat.no_party"));
                return Command.SINGLE_SUCCESS;
            }

            partyManager.toggleChat(((Player) sender));
            return Command.SINGLE_SUCCESS;
        }).then(argument("message", StringArgumentType.greedyString()).executes(context -> {
            CommandSender sender = context.getSource().getSender();
            if (!(sender instanceof Player)) {
                sender.sendMessage(MessageUtils.getMessage("cmd.error.player_only"));
                return Command.SINGLE_SUCCESS;
            }
            if (!sender.hasPermission("lastlife.party.chat")) {
                sender.sendMessage(MessageUtils.getMessage("cmd.error.no_perm"));
                return Command.SINGLE_SUCCESS;
            }
            if (context.getArgument("message", String.class).isEmpty()) {
                sender.sendMessage(MessageUtils.getMessage("cmd.error.invalid_args"));
                return Command.SINGLE_SUCCESS;
            }
            PlayerManager playerManager = (PlayerManager) DataManager.getConfigManager("players");
            PartyManager partyManager = (PartyManager) DataManager.getConfigManager("parties");
            if (playerManager.getParty(((Player) sender)).equalsIgnoreCase("none")) {
                sender.sendMessage(MessageUtils.getMessage("party.chat.no_party"));
                return Command.SINGLE_SUCCESS;
            }

            partyManager.handleChat(((Player) sender), text(context.getArgument("message", String.class)));
            return Command.SINGLE_SUCCESS;
        }))).build();
    }

}

