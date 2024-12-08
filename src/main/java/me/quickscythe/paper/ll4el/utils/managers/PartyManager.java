package me.quickscythe.paper.ll4el.utils.managers;

import json2.JSONObject;
import me.quickscythe.dragonforge.utils.chat.ChatManager;
import me.quickscythe.dragonforge.utils.chat.MessageUtils;
import me.quickscythe.dragonforge.utils.chat.placeholder.PlaceholderUtils;
import me.quickscythe.dragonforge.utils.config.ConfigFile;
import me.quickscythe.dragonforge.utils.config.ConfigFileManager;
import me.quickscythe.dragonforge.utils.storage.ConfigManager;
import me.quickscythe.dragonforge.utils.storage.DataManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class PartyManager extends ConfigManager {

    static ConfigFile parties;
    static List<UUID> in_chat = new ArrayList<>();

    public PartyManager(JavaPlugin plugin) {
        super(plugin, "parties");
    }

    public JSONObject createParty(String name) {
        JSONObject party = new JSONObject();
        parties.getData().put(name, party);
        return party;
    }

    public JSONObject getParty(String name) {
        try {
            return parties.getData().getJSONObject(name);
        } catch (NullPointerException ex) {
            throw new NullPointerException("Couldn't find party: " + name);
        }
    }

    public void removeParty(String name) {
        parties.getData().remove(name);
    }

    public boolean inPartyChat(OfflinePlayer player) {
        return in_chat.contains(player.getUniqueId());
    }

    public void handleChat(Player player, String message) {
        PlayerManager playerManager = (PlayerManager) DataManager.getConfigManager("players");
        String format = PlaceholderUtils.replace(player, ChatManager.getFormat("party") + ChatManager.getFormat("player"));
        String party = playerManager.getParty(player);
        for (Player r : Bukkit.getOnlinePlayers()) {
            if (playerManager.getParty(r).equalsIgnoreCase(party) || r.hasPermission("lastlife.admin.see_chat"))
                r.sendMessage(MessageUtils.colorize((r.hasPermission("lastlife.admin.see_chat") ? "&7&o(" + party + ")" : "") + format) + message);
        }
    }

    public void joinChat(Player player) {
        in_chat.add(player.getUniqueId());
        player.sendMessage(MessageUtils.getMessage("party.chat.join"));
    }

    public void leaveChat(Player player) {
        in_chat.remove(player.getUniqueId());
        player.sendMessage(MessageUtils.getMessage("party.chat.leave"));
    }

    public void toggleChat(Player player) {
        if (in_chat.contains(player.getUniqueId())) leaveChat(player);
        else joinChat(player);
    }

    public List<String> getParties() {
        return new ArrayList<>(PartyManager.parties.getData().keySet());
    }

    public List<UUID> getPlayers(String party) {
        List<UUID> uids = new ArrayList<>();
        PlayerManager playerManager = (PlayerManager) DataManager.getConfigManager("players");
        for(UUID uid : playerManager.getPlayers())
            if(playerManager.getParty(Bukkit.getOfflinePlayer(uid)).equalsIgnoreCase(party))
                uids.add(uid);
        return uids;
    }
}
