package me.quickscythe.paper.ll4el.commands;

import me.quickscythe.dragonforge.utils.Pagifier;
import me.quickscythe.dragonforge.utils.chat.MessageUtils;
import me.quickscythe.dragonforge.utils.gui.GuiInventory;
import me.quickscythe.dragonforge.utils.gui.GuiItem;
import me.quickscythe.dragonforge.utils.gui.GuiManager;
import me.quickscythe.dragonforge.utils.storage.DataManager;
import me.quickscythe.paper.ll4el.Initializer;
import me.quickscythe.paper.ll4el.commands.listeners.AdminTabCompleter;
import me.quickscythe.paper.ll4el.utils.managers.BoogieManager;
import me.quickscythe.paper.ll4el.utils.managers.PartyManager;
import me.quickscythe.paper.ll4el.utils.managers.PlayerManager;
import me.quickscythe.paper.ll4el.utils.managers.loot.LootManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.entity.Player;
import json2.JSONArray;
import json2.JSONObject;

import java.util.*;

public class AdminCommands implements CommandExecutor {

    public AdminCommands(Initializer plugin, String... commands) {
        for (String s : commands) {
            PluginCommand cmd = plugin.getCommand(s);
            cmd.setExecutor(this);
            cmd.setTabCompleter(new AdminTabCompleter());
        }

    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        PlayerManager playerManager = (PlayerManager) DataManager.getConfigManager("players");
//        if (cmd.getName().equalsIgnoreCase("status")) {
//            if (!sender.hasPermission("lastlife.admin.status")) {
//                sender.sendMessage(MessageUtils.getMessage("cmd.error.no_perm"));
//                return true;
//            }
//            if (sender instanceof Player player) {
//                if (args.length == 0 || args[0].equalsIgnoreCase("admin")) {
////                    GuiInventory inv =
////                    GuiManager.openGui(player, inv);
//                    return true;
//                }
//                if (args[0].equalsIgnoreCase("party")) {
//                    GuiManager.openGui(player, generatePartyList(player, 1));
//                }
//                if (args[0].equalsIgnoreCase("boogie")) {
//                    GuiManager.openGui(player, generateBoogieList(1));
//                }
//                if (args[0].equalsIgnoreCase("player")) {
//                    if (args.length == 1) {
//                        GuiManager.openGui(player, generatePlayerList(1));
//                    } else {
//                        if (Bukkit.getOfflinePlayer(playerManager.getUUID(args[1])) == null) {
//                            sender.sendMessage(MessageUtils.colorize("&cSorry that player couldn't be found. If the player is offline their username must be typed exactly."));
//                        } else
//                            GuiManager.openGui(player, generatePlayerStatus(Bukkit.getOfflinePlayer(Objects.requireNonNull(playerManager.getUUID(args[1])))));
//                    }
//                }
//
//            } else {
//                sender.sendMessage(MessageUtils.getMessage("cmd.error.player_only"));
//            }
//        }
        if (cmd.getName().equalsIgnoreCase("lastlife")) {
            if (!sender.hasPermission("lastlife.admin")) {
                sender.sendMessage(MessageUtils.getMessage("cmd.error.no_perm"));
                return true;
            }
            if (args.length == 0 || args[0].equalsIgnoreCase("help")) {
                sender.sendMessage(MessageUtils.colorize("&a/" + label + " [help] &7- Displays this list."));
                sender.sendMessage(MessageUtils.colorize("&a/" + label + " loot &7- Displays loot commands."));
                sender.sendMessage(MessageUtils.colorize("&a/" + label + " life &7- Displays life commands."));
                return true;
            }
            if (args[0].equalsIgnoreCase("boogie")) {
                if (!sender.hasPermission("lastlife.admin.boogie")) {
                    sender.sendMessage(MessageUtils.getMessage("cmd.error.no_perm"));
                    return true;
                }
                if (args.length == 1 || args[1].equalsIgnoreCase("help")) {
                    sender.sendMessage(MessageUtils.colorize("&a/" + label + " boogie [help] &7- Displays this list."));
                    sender.sendMessage(MessageUtils.colorize("&a/" + label + " boogie roll &7- Rolls for random boogie. Amount of boogies can be selected."));
                    sender.sendMessage(MessageUtils.colorize("&a/" + label + " boogie set <player> &7- Sets a player as a boogie."));
                    sender.sendMessage(MessageUtils.colorize("&a/" + label + " boogie remove <player> &7- Removes a player's boogie status"));
                    return true;
                }
                if (args[1].equalsIgnoreCase("roll")) {
                    if (!sender.hasPermission("lastlife.admin.boogie.roll")) {
                        sender.sendMessage(MessageUtils.getMessage("cmd.error.no_perm"));
                        return true;
                    }
                    int amount = args.length == 3 ? Integer.parseInt(args[2]) : 1;
                    ((BoogieManager) DataManager.getConfigManager("boogies")).rollBoogies(amount, true);
                    sender.sendMessage(MessageUtils.getMessage("cmd.boogie.roll", amount));
                }
                if (args[1].equalsIgnoreCase("remove")) {
                    if (!sender.hasPermission("lastlife.admin.boogie.remove")) {
                        sender.sendMessage(MessageUtils.getMessage("cmd.error.no_perm"));
                        return true;
                    }
                    if (args.length < 3) {
                        sender.sendMessage(MessageUtils.colorize("&a/" + label + " boogie remove <player> &7- Removes a player's boogie status"));
                        return true;
                    }
                    if (Bukkit.getPlayer(args[2]) == null && playerManager.getUUID(args[2]) == null) {
                        sender.sendMessage(MessageUtils.getMessage("cmd.error.no_player"));
                        return true;
                    }
                    OfflinePlayer player = Bukkit.getPlayer(args[2]) == null ? Bukkit.getOfflinePlayer(playerManager.getUUID(args[2])) : Bukkit.getPlayer(args[2]);
                    playerManager.removeBoogie(player);
                    sender.sendMessage(MessageUtils.getMessage("cmd.boogie.remove.success", player.getName()));
                }
                if (args[1].equalsIgnoreCase("set")) {
                    if (!sender.hasPermission("lastlife.admin.boogie.set")) {
                        sender.sendMessage(MessageUtils.getMessage("cmd.error.no_perm"));
                        return true;
                    }
                    if (args.length < 3) {
                        sender.sendMessage(MessageUtils.colorize("&a/" + label + " boogie set <player> &7- Sets a player as a boogie."));
                        return true;
                    }
                    if (Bukkit.getPlayer(args[2]) == null && playerManager.getUUID(args[2]) == null) {
                        sender.sendMessage(MessageUtils.getMessage("cmd.error.no_player", args[2]));
                        return true;
                    }
                    OfflinePlayer player = Bukkit.getPlayer(args[2]) == null ? Bukkit.getOfflinePlayer(playerManager.getUUID(args[2])) : Bukkit.getPlayer(args[2]);
                    playerManager.setBoogie(player);
                    sender.sendMessage(MessageUtils.getMessage("cmd.boogie.set.success", player.getName()));
                }

            }
            if (args[0].equalsIgnoreCase("loot")) {
                if (!sender.hasPermission("lastlife.admin.loot")) {
                    sender.sendMessage(MessageUtils.getMessage("cmd.error.no_perm"));
                    return true;
                }
                if (sender instanceof Player player) {
                    if (args.length == 1 || args[1].equalsIgnoreCase("help")) {
                        sender.sendMessage(MessageUtils.colorize("&a/" + label + " loot [help] &7- Displays this list."));
                        sender.sendMessage(MessageUtils.colorize("&a/" + label + " loot create <name> &7- Creates a loot drop location."));
                        sender.sendMessage(MessageUtils.colorize("&a/" + label + " loot drop <name> <type> &7- Drops loot in a location."));
                        return true;
                    }
                    if (args[1].equalsIgnoreCase("drop")) {
                        if (!sender.hasPermission("lastlife.admin.loot.drop")) {
                            sender.sendMessage(MessageUtils.getMessage("cmd.error.no_perm"));
                            return true;
                        }
                        DataManager.getConfigManager("loot", LootManager.class).dropLoot(new Random().nextBoolean() ? LootManager.DropType.SHULKER : LootManager.DropType.OTHER);
                    }
                    if (args[1].equalsIgnoreCase("create")) {
                        if (!sender.hasPermission("lastlife.admin.loot.drop")) {
                            sender.sendMessage(MessageUtils.getMessage("cmd.error.no_perm"));
                            return true;
                        }
                        if (args.length == 2) {
                            sender.sendMessage(MessageUtils.colorize("&a/" + label + " loot create <name> &7- Creates a loot drop location."));
                            return true;
                        }
                        String name = args[2];
                        Location loc = player.getTargetBlock(null, 5).getLocation();
                        DataManager.getConfigManager("loot", LootManager.class).createDrop(name, loc);
                        player.sendMessage(MessageUtils.getMessage("cmd.loot.create.success", name, "(" + loc.getBlockX() + ", " + loc.getBlockY() + ", " + loc.getBlockZ() + ")"));
                    }
                }

            }
            if (args[0].equalsIgnoreCase("life")) {
                if (!sender.hasPermission("lastlife.admin.life")) {
                    sender.sendMessage(MessageUtils.getMessage("cmd.error.no_perm"));
                    return true;
                }
                if (args.length == 1 || args[1].equalsIgnoreCase("help")) {
                    sender.sendMessage(MessageUtils.colorize("&a/" + label + " life [help] &7- Displays this list."));
                    sender.sendMessage(MessageUtils.colorize("&a/" + label + " life add <player> [amount] &7- Edits a player's lives. Amount can be negative. Default amount=1."));
                    sender.sendMessage(MessageUtils.colorize("&a/" + label + " life set <player> <amount> &7- Sets a player's lives."));
                    return true;
                }

                if (args[1].equalsIgnoreCase("add") || args[1].equalsIgnoreCase("set")) {
                    if (!sender.hasPermission("lastlife.admin.life.edit")) {
                        sender.sendMessage(MessageUtils.getMessage("cmd.error.no_perm"));
                        return true;
                    }
                    boolean animation = args.length != 5 || Boolean.parseBoolean(args[4]);
                    if (args.length < 3) {
                        sender.sendMessage(MessageUtils.colorize("&a/" + label + " life add|set <player> [amount] &7- Edits a player's lives. Amount can be negative. Default amount=1."));
                        return true;
                    }
                    if (Bukkit.getPlayer(args[2]) == null && playerManager.getUUID(args[2]) == null) {
                        sender.sendMessage(MessageUtils.getMessage("cmd.error.no_player"));
                        return true;
                    }
                    OfflinePlayer player = Bukkit.getPlayer(args[2]) == null ? Bukkit.getOfflinePlayer(playerManager.getUUID(args[2])) : Bukkit.getPlayer(args[2]);
                    int amount = args.length == 3 ? 1 : Integer.parseInt(args[3]);
                    if (args[1].equalsIgnoreCase("add")) playerManager.editLife(player, amount, animation);
                    if (args[1].equalsIgnoreCase("set")) playerManager.setLife(player, amount);
                    sender.sendMessage(MessageUtils.getMessage("cmd.life.edit.success", player.getName()));
                }
            }
        }
        return true;
    }


}
