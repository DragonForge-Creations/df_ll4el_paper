package me.quickscythe.paper.ll4el.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.quickscythe.dragonforge.commands.CustomCommand;
import me.quickscythe.dragonforge.utils.chat.MessageUtils;
import me.quickscythe.paper.ll4el.utils.managers.PartyManager;
import me.quickscythe.paper.ll4el.utils.managers.PlayerManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static io.papermc.paper.command.brigadier.Commands.argument;
import static io.papermc.paper.command.brigadier.Commands.literal;

public class PartyCommand extends CustomCommand {

    public PartyCommand(JavaPlugin plugin) {
        super(plugin, "party");
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> getNode() {
        return literal(getName())
                .executes(commandContext -> {
                    CommandSender sender = commandContext.getSource().getSender();
                    sender.sendMessage(MessageUtils.colorize("&a/" + getName() + " [help] &7- Displays this list."));
                    if (sender.hasPermission("lastlife.party.create"))
                        sender.sendMessage(MessageUtils.colorize("&a/" + getName() + " create <party> &7- Creates a party."));
                    if (sender.hasPermission("lastlife.party.join"))
                        sender.sendMessage(MessageUtils.colorize("&a/" + getName() + " join <party> " + (sender.hasPermission("lastlife.party.join.other") ? "[player] " : "") + "&7- " + (sender.hasPermission("lastlife.party.join.other") ? "Joins you or another player to a party" : "Joins you to a party") + "."));
                    if (sender.hasPermission("lastlife.party.leave"))
                        sender.sendMessage(MessageUtils.colorize("&a/" + getName() + " leave " + (sender.hasPermission("lastlife.party.leave.other") ? "[player] " : "") + "&7- " + (sender.hasPermission("lastlife.party.leave.other") ? "Pull you or another player out of a party" : "Pulls out out of your party") + "."));
                    if (sender.hasPermission("lastlife.party.chat"))
                        sender.sendMessage(MessageUtils.colorize("&a/" + getName() + " chat [message] &7- Either sends a single chat to the party, or toggles party chat mode."));

                    return Command.SINGLE_SUCCESS;
                }).then(argument("action", StringArgumentType.string())
                        .suggests((context, builder) -> {
                            CommandSender sender = context.getSource().getSender();
                            builder.suggest("help");
                            if (sender.hasPermission("lastlife.party.create"))
                                builder.suggest("create");
                            if (sender.hasPermission("lastlife.party.join"))
                                builder.suggest("join");
                            if (sender.hasPermission("lastlife.party.leave"))
                                builder.suggest("leave");
                            if (sender.hasPermission("lastlife.party.chat"))
                                builder.suggest("chat");
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            CommandSender sender = context.getSource().getSender();
                            String action = StringArgumentType.getString(context, "action");
                            if (action.equalsIgnoreCase("help")) {
                                sender.sendMessage(MessageUtils.colorize("&a/" + getName() + " [help] &7- Displays this list."));
                                if (sender.hasPermission("lastlife.party.create"))
                                    sender.sendMessage(MessageUtils.colorize("&a/" + getName() + " create <party> &7- Creates a party."));
                                if (sender.hasPermission("lastlife.party.join"))
                                    sender.sendMessage(MessageUtils.colorize("&a/" + getName() + " join <party> " + (sender.hasPermission("lastlife.party.join.other") ? "[player] " : "") + "&7- " + (sender.hasPermission("lastlife.party.join.other") ? "Joins you or another player to a party" : "Joins you to a party") + "."));
                                if (sender.hasPermission("lastlife.party.leave"))
                                    sender.sendMessage(MessageUtils.colorize("&a/" + getName() + " leave " + (sender.hasPermission("lastlife.party.leave.other") ? "[player] " : "") + "&7- " + (sender.hasPermission("lastlife.party.leave.other") ? "Pull you or another player out of a party" : "Pulls out out of your party") + "."));
                                if (sender.hasPermission("lastlife.party.chat"))
                                    sender.sendMessage(MessageUtils.colorize("&a/" + getName() + " chat [message] &7- Either sends a single chat to the party, or toggles party chat mode."));
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
                                PlayerManager.setParty(player, "none");
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
                                if (PlayerManager.getParty(((Player) sender)).equalsIgnoreCase("none")) {
                                    sender.sendMessage(MessageUtils.getMessage("party.chat.no_party"));
                                    return Command.SINGLE_SUCCESS;
                                }

                                PartyManager.toggleChat(((Player) sender));
                                return Command.SINGLE_SUCCESS;
                            }
                            return Command.SINGLE_SUCCESS;
                        })
                        .then(argument("arg2", StringArgumentType.string())
                                .suggests((context, builder) -> {
                                    CommandSender sender = context.getSource().getSender();
                                    String action = StringArgumentType.getString(context, "action");
                                    if (action.equalsIgnoreCase("join") || action.equalsIgnoreCase("leave")) {
                                        if(sender.hasPermission("lastlife.party." + action.toLowerCase())){
                                            for(String s : PartyManager.getParties()){
                                                builder.suggest(s);
                                            }
                                        }
                                    }
                                    return builder.buildFuture();
                                })
                                .executes(context -> {
                                    CommandSender sender = context.getSource().getSender();
                                    String action = StringArgumentType.getString(context, "action");
                                    String arg2 = StringArgumentType.getString(context, "arg2");
                                    if(action.equalsIgnoreCase("create")) {
                                        if (arg2.equalsIgnoreCase("help")) {
                                            sender.sendMessage(MessageUtils.colorize("&a/" + getName() + " create <party> &7- Creates a party."));
                                            return Command.SINGLE_SUCCESS;
                                        }
                                        PartyManager.createParty(arg2);
                                        sender.sendMessage(MessageUtils.getMessage("cmd.party.create", arg2));
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    if(action.equalsIgnoreCase("join")){
                                        sender.sendMessage(MessageUtils.getMessage("cmd.error.invalid_args"));
                                        return Command.SINGLE_SUCCESS;
                                    }
                                    if(action.equalsIgnoreCase("leave")){
                                        if(sender.hasPermission("lastlife.party.leave"))
                                            PlayerManager.setParty((Player) sender, "none");

                                        return Command.SINGLE_SUCCESS;
                                    }
                                    return Command.SINGLE_SUCCESS;
                                })
                        )
                ).build();
    }
}

