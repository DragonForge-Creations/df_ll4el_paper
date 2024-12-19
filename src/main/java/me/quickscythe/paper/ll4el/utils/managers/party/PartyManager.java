package me.quickscythe.paper.ll4el.utils.managers.party;

import json2.JSONObject;
import me.quickscythe.dragonforge.utils.CoreUtils;
import me.quickscythe.dragonforge.utils.chat.Logger;
import me.quickscythe.dragonforge.utils.chat.MessageUtils;
import me.quickscythe.dragonforge.utils.storage.ConfigManager;
import me.quickscythe.dragonforge.utils.storage.DataManager;
import me.quickscythe.paper.ll4el.utils.managers.LifeManager;
import me.quickscythe.paper.ll4el.utils.managers.PlayerManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static net.kyori.adventure.text.Component.text;

public class PartyManager extends ConfigManager {

    List<UUID> in_chat = new ArrayList<>();

    public PartyManager(JavaPlugin plugin) {
        super(plugin, "parties");
    }

    public Party createParty(String name) {
        JSONObject data = new JSONObject();
        config().getData().put(name, data);
        config().save();
        return new Party(name, data);
    }


    public boolean inPartyChat(OfflinePlayer player) {
        return in_chat.contains(player.getUniqueId());
    }

    public void handleChat(Player player, Component message) {
        PlayerManager playerManager = (PlayerManager) DataManager.getConfigManager("players");
        Component format = text("<", NamedTextColor.WHITE).append(text(player.getName(), LifeManager.getLifeColor(player))).append(text("> ", NamedTextColor.WHITE)).append(message);
        String party = playerManager.getParty(player);
        CoreUtils.logger().log(Logger.LogLevel.INFO, "PartyChat", format);
        for (Player r : Bukkit.getOnlinePlayers()) {
            if (playerManager.getParty(r).equalsIgnoreCase(party) || r.hasPermission("lastlife.admin.see_chat"))
                r.sendMessage(text("").append(text(r.hasPermission("lastlife.admin.see_chat") ? "(" + party + ")" : "", NamedTextColor.GRAY, TextDecoration.ITALIC)).append(format));
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
        return new ArrayList<>(config().getData().keySet());
    }
}
