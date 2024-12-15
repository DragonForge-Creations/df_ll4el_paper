package me.quickscythe.paper.ll4el.listeners;

import me.quickscythe.dragonforge.exceptions.QuickException;
import me.quickscythe.dragonforge.utils.chat.MessageUtils;
import me.quickscythe.dragonforge.utils.network.WebhookUtils;
import me.quickscythe.dragonforge.utils.storage.DataManager;
import me.quickscythe.paper.ll4el.Initializer;
import me.quickscythe.paper.ll4el.utils.managers.PlayerManager;
import me.quickscythe.paper.ll4el.utils.managers.loot.LootManager;
import org.bukkit.ChatColor;
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
    public void onPlayerDeath(PlayerDeathEvent e) throws QuickException {
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
        }
        playerManager.removeLife(player);
        WebhookUtils.send("deaths",
                msg + " " +
                        "(Lives remaining: " + playerManager.getLives(player) + ")" +
                        (boogieKill ? "(Boogie Kill)" : "") + " " +
                        (elimination ? "(Elimination)" : ""));

    }
}
