package me.quickscythe.paper.ll4el.listeners;

import me.quickscythe.dragonforge.exceptions.QuickException;
import me.quickscythe.dragonforge.utils.chat.MessageUtils;
import me.quickscythe.dragonforge.utils.network.NetworkUtils;
import me.quickscythe.dragonforge.utils.network.WebhookUtils;
import me.quickscythe.dragonforge.utils.storage.DataManager;
import me.quickscythe.paper.ll4el.Initializer;
import me.quickscythe.paper.ll4el.utils.managers.PlayerManager;
import me.quickscythe.paper.ll4el.utils.managers.loot.LootManager;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Random;

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
    public void onBlockPlace(BlockPlaceEvent e) {
        if (e.getBlock().getType().equals(Material.STONE)) {
            Location loc = e.getBlock().getLocation();
            LootManager man = DataManager.getConfigManager("loot", LootManager.class);
            man.createDrop("test", loc);
            man.dropLoot("test", new Random().nextBoolean() ? LootManager.DropType.OTHER : LootManager.DropType.SHULKER);
            man.config().save();
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
