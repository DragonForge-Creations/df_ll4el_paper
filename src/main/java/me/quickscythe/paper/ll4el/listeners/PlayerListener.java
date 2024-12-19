package me.quickscythe.paper.ll4el.listeners;

import json2.JSONArray;
import json2.JSONObject;
import me.quickscythe.dragonforge.exceptions.QuickException;
import me.quickscythe.dragonforge.utils.CoreUtils;
import me.quickscythe.dragonforge.utils.chat.Logger;
import me.quickscythe.dragonforge.utils.chat.MessageUtils;
import me.quickscythe.dragonforge.utils.network.WebhookUtils;
import me.quickscythe.dragonforge.utils.storage.DataManager;
import me.quickscythe.paper.ll4el.Initializer;
import me.quickscythe.paper.ll4el.utils.managers.PlayerManager;
import me.quickscythe.paper.ll4el.utils.managers.loot.LootManager;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import static net.kyori.adventure.text.Component.text;

public class PlayerListener implements Listener {
    public PlayerListener(Initializer plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        ((PlayerManager) DataManager.getConfigManager("players")).checkData(e.getPlayer());
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e) {
        if(e.getAction().isLeftClick()){
            LootManager lootManager = DataManager.getConfigManager("loot", LootManager.class);
            if(lootManager.isEditing(e.getPlayer())){
                lootManager.createDrop(lootManager.getEditingLocation(e.getPlayer()), e.getClickedBlock().getLocation());
                e.getPlayer().sendMessage(MessageUtils.getMessage("loot.create", lootManager.getEditingLocation(e.getPlayer())));
                lootManager.finishEditing(e.getPlayer());
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e)  {
        Player player = e.getEntity();
        boolean boogieKill = false;
        boolean elimination = false;
        String msg = ChatColor.stripColor(e.getDeathMessage());
        PlayerManager playerManager = (PlayerManager) DataManager.getConfigManager("players");
        if (player.getKiller() != null && playerManager.isBoogie(player.getKiller()) && !player.equals(player.getKiller())) {
            playerManager.removeBoogie(player.getKiller());
            boogieKill = true;
        }
        if (playerManager.getLives(player) == 1) {
            e.setCancelled(true);
            player.setGameMode(GameMode.SPECTATOR);
            player.getWorld().strikeLightningEffect(player.getLocation());
            msg = MessageUtils.getMessage("action.elimination", player.getName(), (player.hasMetadata("last_damager") ? " by " + ((Player) player.getMetadata("last_damager").get(0).value()).getName() : ""));
            e.getEntity().getServer().broadcast(text(msg));
            elimination = true;
            playerManager.removeBoogie(player);
            playerManager.setParty(player, "none");
        }
        playerManager.removeLife(player);
        try {
            sendDeathEmbed(msg, player, boogieKill, elimination);
        } catch (QuickException quickException) {
            CoreUtils.logger().log(Logger.LogLevel.ERROR, "DeathManager", quickException);
        }
    }

    private void sendDeathEmbed(String msg, Player player, boolean boogieKill, boolean elimination) throws QuickException {
        JSONObject embed = new JSONObject();
        PlayerManager playerManager = (PlayerManager) DataManager.getConfigManager("players");
        embed.put("title", player.getName() + " has died!");
        embed.put("description", msg);
        embed.put("fields", new JSONArray());
        JSONObject f_lives = new JSONObject();
        f_lives.put("name", "Lives");
        f_lives.put("value", String.valueOf(playerManager.getLives(player)));

        JSONObject f_boogie = new JSONObject();
        f_boogie.put("name", "Boogie Kill");
        f_boogie.put("value", String.valueOf(boogieKill));

        JSONObject f_elim = new JSONObject();
        f_elim.put("name", "Elimination");
        f_elim.put("value", String.valueOf(elimination));

        embed.getJSONArray("fields").put(f_lives);
        embed.getJSONArray("fields").put(f_boogie);
        embed.getJSONArray("fields").put(f_elim);

        embed.put("color", TextColor.color(0xE9334B).value());
        JSONObject send = new JSONObject();
        send.put("embeds", new JSONArray());
        send.getJSONArray("embeds").put(embed);
        WebhookUtils.send("deaths", send);
    }
}
