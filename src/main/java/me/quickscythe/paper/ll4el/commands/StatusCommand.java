package me.quickscythe.paper.ll4el.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import json2.JSONArray;
import json2.JSONObject;
import me.quickscythe.dragonforge.commands.CommandExecutor;
import me.quickscythe.dragonforge.utils.Pagifier;
import me.quickscythe.dragonforge.utils.gui.GuiInventory;
import me.quickscythe.dragonforge.utils.gui.GuiItem;
import me.quickscythe.dragonforge.utils.gui.GuiManager;
import me.quickscythe.dragonforge.utils.storage.DataManager;
import me.quickscythe.paper.ll4el.utils.managers.BoogieManager;
import me.quickscythe.paper.ll4el.utils.managers.party.PartyManager;
import me.quickscythe.paper.ll4el.utils.managers.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import static io.papermc.paper.command.brigadier.Commands.argument;
import static io.papermc.paper.command.brigadier.Commands.literal;

public class StatusCommand extends CommandExecutor {
    public StatusCommand(JavaPlugin plugin) {
        super(plugin, "status");
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> execute() {
        return literal(getName()).executes(context -> {
            CommandSender sender = context.getSource().getSender();
            if (!(sender instanceof Player player))
                return logError(sender, "You must be a player to use this command.");
            if (!player.hasPermission("lastlife.admin.status"))
                return logError(sender, "You do not have permission to use this command.");
            GuiManager.openGui(player, GuiManager.getGui("status"));

            return Command.SINGLE_SUCCESS;
        }).then(argument("system", StringArgumentType.string()).suggests((context, builder) -> {
            if (context.getSource().getSender().hasPermission("lastlife.admin.status")) {
                builder.suggest("party");
                builder.suggest("boogie");
                builder.suggest("player");
            }
            return builder.buildFuture();
        }).executes(context -> {
            String system = StringArgumentType.getString(context, "system");
            CommandSender sender = context.getSource().getSender();
            if (!(sender instanceof Player player))
                return logError(sender, "You must be a player to use this command.");
            if (!player.hasPermission("lastlife.admin.status"))
                return logError(sender, "You do not have permission to use this command.");
            if (system.equalsIgnoreCase("party")) {
                GuiInventory inv = generatePartyList(player, 1);
                GuiManager.openGui(player, inv);
            }
            if (system.equalsIgnoreCase("boogie")) {
                GuiInventory inv = generateBoogieList(1);
                GuiManager.openGui(player, inv);
            }
            if (system.equalsIgnoreCase("player")) {
                GuiInventory inv = generatePlayerList(1);
                GuiManager.openGui(player, inv);
            }
            return Command.SINGLE_SUCCESS;
        }).then(argument("name", StringArgumentType.string())
                .suggests((context, builder) -> {
                    String system = StringArgumentType.getString(context, "system");
                    if (system.equalsIgnoreCase("party")) {
                        DataManager.getConfigManager("parties", PartyManager.class).getParties().forEach(builder::suggest);
                    }
                    if (system.equalsIgnoreCase("player")) {
                        DataManager.getConfigManager("players", PlayerManager.class).getPlayers().forEach((uuid) -> {
                            OfflinePlayer target = Bukkit.getOfflinePlayer(uuid);
                            builder.suggest(target.getName());
                        });
                    }
                    return builder.buildFuture();
                })
                .executes(context -> {
                    String system = StringArgumentType.getString(context, "system");
                    String name = StringArgumentType.getString(context, "name");
                    CommandSender sender = context.getSource().getSender();
                    try{
                        int page = Integer.parseInt(name);
                        if(system.equalsIgnoreCase("party")){
                            GuiInventory inv = generatePartyList((Player) sender, page);
                            GuiManager.openGui((Player) sender, inv);
                        }
                        if(system.equalsIgnoreCase("boogie")){
                            GuiInventory inv = generateBoogieList(page);
                            GuiManager.openGui((Player) sender, inv);
                        }
                        if(system.equalsIgnoreCase("player")){
                            GuiInventory inv = generatePlayerList(page);
                            GuiManager.openGui((Player) sender, inv);
                        }
                    } catch (NumberFormatException ignored){
                        if (system.equalsIgnoreCase("party")) {
                            GuiInventory inv = generatePartyMembers(name, 1);
                            GuiManager.openGui((Player) sender, inv);
                        }
                        if (system.equalsIgnoreCase("player")) {
                            OfflinePlayer target = Bukkit.getOfflinePlayer(name);
                            GuiInventory inv = generatePlayerStatus(target);
                            GuiManager.openGui((Player) sender, inv);
                        }
                    }

                    return Command.SINGLE_SUCCESS;
                }))).build();
    }

    private GuiInventory generateBoogieList(int page) {
        //todo:
        // Parse args to find page. Args will only be page
        // Pagify and display current boogies
        StringBuilder config = new StringBuilder();
        List<GuiItem> items = new ArrayList<>();
        Pagifier pagifier = new Pagifier(DataManager.getConfigManager("boogies", BoogieManager.class).getBoogies(), 27);
        int page_size;
        try {
            page_size = pagifier.getPage(page).size();
            for (int i = 0; i < page_size; i++) {

                OfflinePlayer target = Bukkit.getOfflinePlayer((UUID) pagifier.getPage(page).get(i));
                GuiItem item = new GuiItem(((char) i) + "");
                setupPlayerInfo(item, target);
                items.add(item);
                config.append(item.getIdentifier());
            }
        } catch (NullPointerException ex) {
            page_size = 0;
        }


        GuiItem search = new GuiItem("W");
        search.setMaterial(Material.STICK);
        search.setCustomModelData(107);
        search.setDisplayName("&bSearch");

        config.append("X".repeat(Math.max(0, 27 - page_size)));
        if (page < pagifier.getPages() && page > 1) config.append("XXXYXZXXX");
        if (page < pagifier.getPages() && page == 1) config.append("XXXXXZXXX");
        if (page >= pagifier.getPages() && pagifier.getPages() != 1) config.append("XXXYXXXXX");
        if (page == pagifier.getPages() && page == 1) config.append("XXXXXXXXX");
        GuiInventory inv = new GuiInventory(new Date().getTime() + "", "&c&lBoogie List", 36, config.toString());
        for (GuiItem item : items)
            inv.addItem(item);
        GuiItem air = new GuiItem("X");
        air.setMaterial(Material.AIR);
        inv.addItem(air);

        GuiItem next = new GuiItem("Z");
        next.setMaterial(Material.STICK);
        next.setCustomModelData(103);
        next.setDisplayName("&aNext Page (" + (page + 1) + "/" + pagifier.getPages() + ")");
        JSONArray nextArray = new JSONArray();
        JSONObject nextAction1 = new JSONObject();
        nextAction1.put("action", "command");
        nextAction1.put("command", "status boogie " + (page + 1));
        nextArray.put(nextAction1);
        next.setActions(nextArray);
        inv.addItem(next);

        GuiItem prev = new GuiItem("Y");
        prev.setMaterial(Material.STICK);
        prev.setCustomModelData(104);
        prev.setDisplayName("&aPrevious Page (" + (page - 1) + "/" + pagifier.getPages() + ")");
        JSONArray prevArray = new JSONArray();
        JSONObject prevAction1 = new JSONObject();
        prevAction1.put("action", "command");
        prevAction1.put("command", "status boogie " + (page - 1));
        prevArray.put(prevAction1);
        prev.setActions(prevArray);
        inv.addItem(prev);
        return inv;


    }

    private GuiInventory generatePartyList(Player sender, int page) {
        PartyManager partyManager = (PartyManager) DataManager.getConfigManager("parties");
        //If party == "none" list parties, otherwise list all players in named party
        GuiInventory inv;
//        String party = args.length >= 2 ? args[1] : "none";
        StringBuilder config = new StringBuilder();
        List<GuiItem> items = new ArrayList<>();
        Pagifier pagifier;
        int page_size;
        int pages;
        pagifier = new Pagifier(partyManager.getParties(), 27);
        page_size = pagifier.getPage(page).size();
        pages = pagifier.getPages();
        for (int i = 0; i < page_size; i++) {

            String pname = (String) pagifier.getPage(page).get(i);
            GuiItem item = new GuiItem(((char) i) + "");
            item.setMaterial(Material.RED_WOOL);
            item.setDisplayName("&a" + pname);

            JSONArray array = new JSONArray();
            JSONObject action1 = new JSONObject();
            action1.put("action", "command");
            action1.put("command", "status party " + pname);
            array.put(action1);
            item.setActions(array);
            items.add(item);
            config.append(item.getIdentifier());


            GuiItem search = new GuiItem("W");
            search.setMaterial(Material.STICK);
            search.setCustomModelData(107);
            search.setDisplayName("&bSearch");


        }
        config.append("X".repeat(Math.max(0, 27 - page_size)));
        if (page < pages && page > 1) config.append("XXXYXZXXX");
        if (page < pages && page == 1) config.append("XXXXXZXXX");
        if (page >= pages && pages != 1) config.append("XXXYXXXXX");
        if (page == pages && page == 1) config.append("XXXXXXXXX");
        inv = new GuiInventory(new Date().getTime() + "", "&a&lParty List", 36, config.toString());
        for (GuiItem item : items)
            inv.addItem(item);
        GuiItem air = new GuiItem("X");
        air.setMaterial(Material.AIR);
        inv.addItem(air);

        GuiItem next = new GuiItem("Z");
        next.setMaterial(Material.STICK);
        next.setCustomModelData(103);
        next.setDisplayName("&aNext Page (" + (page + 1) + "/" + pages + ")");
        JSONArray nextArray = new JSONArray();
        JSONObject nextAction1 = new JSONObject();
        nextAction1.put("action", "command");
        nextAction1.put("command", "status party " + (page + 1));
        nextArray.put(nextAction1);
        next.setActions(nextArray);
        inv.addItem(next);

        GuiItem prev = new GuiItem("Y");
        prev.setMaterial(Material.STICK);
        prev.setCustomModelData(104);
        prev.setDisplayName("&aPrevious Page (" + (page - 1) + "/" + pages + ")");
        JSONArray prevArray = new JSONArray();
        JSONObject prevAction1 = new JSONObject();
        prevAction1.put("action", "command");
        prevAction1.put("command", "status party " + (page - 1));
        prevArray.put(prevAction1);
        prev.setActions(prevArray);
        inv.addItem(prev);
        return inv;
    }

    private GuiInventory generatePartyMembers(String party, int page){
        PartyManager partyManager = (PartyManager) DataManager.getConfigManager("parties");
        //If party == "none" list parties, otherwise list all players in named party
        GuiInventory inv;
//        String party = args.length >= 2 ? args[1] : "none";
        StringBuilder config = new StringBuilder();
        List<GuiItem> items = new ArrayList<>();
        Pagifier pagifier;
        int page_size;
        int pages;
        pagifier = new Pagifier(partyManager.getParties(), 27);
        page_size = pagifier.getPage(page).size();
        pages = pagifier.getPages();
        try {
            pagifier = new Pagifier(partyManager.getPlayers(party), 27);
            page_size = pagifier.getPage(page).size();
            pages = pagifier.getPages();
            for (int i = 0; i < page_size; i++) {

                OfflinePlayer target = Bukkit.getOfflinePlayer((UUID) pagifier.getPage(page).get(i));
                GuiItem item = new GuiItem(((char) i) + "");
                setupPlayerInfo(item, target);


                items.add(item);
                config.append(item.getIdentifier());
            }
        } catch (NullPointerException ex) {
            page_size = 0;
            pages = 1;
        }
        config.append("X".repeat(Math.max(0, 27 - page_size)));
        if (page < pages && page > 1) config.append("XXXYXZXXX");
        if (page < pages && page == 1) config.append("XXXXXZXXX");
        if (page >= pages && pages != 1) config.append("XXXYXXXXX");
        if (page == pages && page == 1) config.append("XXXXXXXXX");
        inv = new GuiInventory(new Date().getTime() + "", "&a&lParty List", 36, config.toString());
        for (GuiItem item : items)
            inv.addItem(item);
        GuiItem air = new GuiItem("X");
        air.setMaterial(Material.AIR);
        inv.addItem(air);

        GuiItem next = new GuiItem("Z");
        next.setMaterial(Material.STICK);
        next.setCustomModelData(103);
        next.setDisplayName("&aNext Page (" + (page + 1) + "/" + pages + ")");
        JSONArray nextArray = new JSONArray();
        JSONObject nextAction1 = new JSONObject();
        nextAction1.put("action", "command");
        String pstr = party.equalsIgnoreCase("none") ? "" : party + " ";
        nextAction1.put("command", "status party " + pstr + (page + 1));
        nextArray.put(nextAction1);
        next.setActions(nextArray);
        inv.addItem(next);

        GuiItem prev = new GuiItem("Y");
        prev.setMaterial(Material.STICK);
        prev.setCustomModelData(104);
        prev.setDisplayName("&aPrevious Page (" + (page - 1) + "/" + pages + ")");
        JSONArray prevArray = new JSONArray();
        JSONObject prevAction1 = new JSONObject();
        prevAction1.put("action", "command");
        prevAction1.put("command", "status party " + pstr + (page - 1));
        prevArray.put(prevAction1);
        prev.setActions(prevArray);
        inv.addItem(prev);
        return inv;
    }


    private GuiInventory generatePlayerStatus(OfflinePlayer target) {
        PlayerManager playerManager = (PlayerManager) DataManager.getConfigManager("players");
        GuiInventory inv = new GuiInventory(target.getName(), "&e&lStatus&7: " + target.getName(), 36, "AXXXXXXXX" + "XXXXXXDXX" + "XXBXXXCXX" + "XXXXXXEXX");
        GuiItem head = new GuiItem("A");
        head.setMaterial(Material.PLAYER_HEAD);
        head.setSkullData(new GuiItem.SkullData(target.getUniqueId()));
        head.setDisplayName("&7" + target.getName());
        head.addAction(new JSONObject().put("action", "command").put("command", "status"));
        inv.addItem(head);

        GuiItem boogie = new GuiItem("B");
        boogie.setDisplayName("&cBoogie Status");
        boogie.setLore("&7Click to toggle.", "&7Current status: " + (playerManager.isBoogie(target) ? "&ayes" : "&cno"));
        boogie.setMaterial(Material.STICK).setCustomModelData(101);
        boogie.addAction(new JSONObject().put("action", "command").put("command", playerManager.isBoogie(target) ? "lastlife boogie remove " + target.getName() : "lastlife boogie set " + target.getName()));
        boogie.addAction(new JSONObject().put("action", "command").put("command", "status player " + target.getName()));
        inv.addItem(boogie);

        GuiItem heart = new GuiItem("C");
        heart.setDisplayName("&aLives&7: " + playerManager.getLives(target)).setMaterial(Material.TOTEM_OF_UNDYING).setCustomModelData(1000);
        inv.addItem(heart);

        GuiItem up = new GuiItem("D");
        up.setDisplayName("&aIncrease").setMaterial(Material.STICK).setCustomModelData(105);
        up.addAction(new JSONObject().put("action", "command").put("command", "lastlife life add " + target.getName() + " 1 false"));
        up.addAction(new JSONObject().put("action", "command").put("command", "status player " + target.getName()));
        inv.addItem(up);

        GuiItem down = new GuiItem("E");
        down.setDisplayName("&cDecrease").setMaterial(Material.STICK).setCustomModelData(106);
        down.addAction(new JSONObject().put("action", "command").put("command", "lastlife life add " + target.getName() + " -1"));
        down.addAction(new JSONObject().put("action", "command").put("command", "status player " + target.getName()));
        inv.addItem(down);

        inv.addItem(new GuiItem("X").setMaterial(Material.AIR));
        return inv;
    }

    private GuiInventory generatePlayerList(int page) {
        PlayerManager playerManager = (PlayerManager) DataManager.getConfigManager("players");
        StringBuilder config = new StringBuilder();
        List<GuiItem> items = new ArrayList<>();
        Pagifier pagifier = new Pagifier(playerManager.getPlayers(), 27);
        for (int i = 0; i < pagifier.getPage(page).size(); i++) {

            UUID uid = (UUID) pagifier.getPage(page).get(i);
            OfflinePlayer target = Bukkit.getOfflinePlayer(uid);
            GuiItem item = new GuiItem(((char) i) + "");
            setupPlayerInfo(item, target);


            items.add(item);
            config.append(item.getIdentifier());


        }
        for (int i = pagifier.getPage(page).size(); i < 27; i++) {
            config.append("X");
        }
        if (page < pagifier.getPages() && page > 1) config.append("XXXYXZXXX");
        if (page < pagifier.getPages() && page == 1) config.append("XXXXXZXXX");
        if (page >= pagifier.getPages() && pagifier.getPages() != 1) config.append("XXXYXXXXX");
        if (page == pagifier.getPages() && page == 1) config.append("XXXXXXXXX");
        GuiInventory inv = new GuiInventory(new Date().getTime() + "", "&e&lStatus", 36, config.toString());
        for (GuiItem item : items)
            inv.addItem(item);
        GuiItem air = new GuiItem("X");
        air.setMaterial(Material.AIR);
        inv.addItem(air);

        GuiItem next = new GuiItem("Z");
        next.setMaterial(Material.STICK);
        next.setCustomModelData(103);
        next.setDisplayName("&aNext Page (" + (page + 1) + "/" + pagifier.getPages() + ")");
        JSONArray nextArray = new JSONArray();
        JSONObject nextAction1 = new JSONObject();
        nextAction1.put("action", "command");
        nextAction1.put("command", "status admin " + (page + 1));
        nextArray.put(nextAction1);
        next.setActions(nextArray);
        inv.addItem(next);

        GuiItem prev = new GuiItem("Y");
        prev.setMaterial(Material.STICK);
        prev.setCustomModelData(104);
        prev.setDisplayName("&aPrevious Page (" + (page - 1) + "/" + pagifier.getPages() + ")");
        JSONArray prevArray = new JSONArray();
        JSONObject prevAction1 = new JSONObject();
        prevAction1.put("action", "command");
        prevAction1.put("command", "status admin " + (page - 1));
        prevArray.put(prevAction1);
        prev.setActions(prevArray);
        inv.addItem(prev);

        GuiItem search = new GuiItem("W");
        search.setMaterial(Material.STICK);
        search.setCustomModelData(107);
        search.setDisplayName("&bSearch");

        return inv;
    }

    private void setupPlayerInfo(GuiItem item, OfflinePlayer target) {
        PlayerManager playerManager = (PlayerManager) DataManager.getConfigManager("players");
        item.setMaterial(Material.PLAYER_HEAD);
        item.setSkullData(new GuiItem.SkullData(target.getUniqueId()));

        item.setDisplayName("&a" + target.getName());
        int lives = playerManager.getLives(target);
        item.setLore("&7Online: " + (target.isOnline() ? "&ayes" : "&cno"), "&7Boogie: " + (playerManager.isBoogie(target) ? "&ayes" : "&cno"), "&7Lives: " + (lives > 3 ? "&2" : lives == 3 ? "&a" : lives == 2 ? "&e" : lives == 1 ? "&c" : "&7") + lives);

        JSONArray array = new JSONArray();
        JSONObject action1 = new JSONObject();
        action1.put("action", "command");
        action1.put("command", "status player " + target.getName());
        array.put(action1);
        item.setActions(array);
    }
}
