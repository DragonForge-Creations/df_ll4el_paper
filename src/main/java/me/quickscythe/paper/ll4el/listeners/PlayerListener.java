package me.quickscythe.paper.ll4el.listeners;

import me.quickscythe.dragonforge.utils.chat.MessageUtils;
import me.quickscythe.dragonforge.utils.storage.DataManager;
import me.quickscythe.paper.ll4el.Initializer;
import me.quickscythe.paper.ll4el.utils.managers.PlayerManager;
import me.quickscythe.paper.ll4el.utils.managers.loot.LootManager;
import org.bukkit.Bukkit;
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

public class PlayerListener implements Listener {
    public PlayerListener(Initializer plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }


    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        ((PlayerManager) DataManager.getConfigManager("players")).checkData(e.getPlayer());
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        if(e.getBlock().getType().equals(Material.STONE)){
            Location loc = e.getBlock().getLocation();
            LootManager man = DataManager.getConfigManager("loot", LootManager.class);
            man.createDrop("test", loc);
            man.dropLoot("test", new Random().nextBoolean() ? LootManager.DropType.OTHER : LootManager.DropType.SHULKER);
            man.config().save();
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        PlayerManager playerManager = (PlayerManager) DataManager.getConfigManager("players");
        if (player.getKiller() != null && playerManager.isBoogie(player.getKiller()) && !player.equals(player.getKiller()))
            playerManager.removeBoogie(player.getKiller());
        if (playerManager.getLives(player) == 1) {
            player.setGameMode(GameMode.SPECTATOR);
            player.getWorld().strikeLightningEffect(player.getLocation());
            String msg = MessageUtils.getMessage("action.elimination", player.getName(), (player.hasMetadata("last_damager") ? " by " + ((Player) player.getMetadata("last_damager").get(0).value()).getName() : ""));
            Bukkit.broadcastMessage(msg);
        }
        playerManager.removeLife(player);

    }
}
