package me.quickscythe.paper.ll4el.listeners;

import json2.JSONObject;
import me.quickscythe.dragonforge.exceptions.QuickException;
import me.quickscythe.dragonforge.utils.CoreUtils;
import me.quickscythe.dragonforge.utils.chat.Logger;
import me.quickscythe.dragonforge.utils.chat.MessageUtils;
import me.quickscythe.dragonforge.utils.network.discord.WebhookManager;
import me.quickscythe.dragonforge.utils.network.discord.embed.Embed;
import me.quickscythe.dragonforge.utils.storage.DataManager;
import me.quickscythe.paper.ll4el.Initializer;
import me.quickscythe.paper.ll4el.utils.donations.Donation;
import me.quickscythe.paper.ll4el.utils.donations.DonorDriveApi;
import me.quickscythe.paper.ll4el.utils.managers.PlayerManager;
import me.quickscythe.paper.ll4el.utils.managers.loot.LootManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.Objects;

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
        if (e.getAction().isLeftClick()) {
            JSONObject data = new JSONObject();
            data.put("eventID", 554);
            data.put("amount", 10);
            data.put("displayName", "Tyler Olexa");
            data.put("avatarImageURL", "https://donordrivecontent.com/clients/extralife/img/avatar-constituent-default.gif");
            data.put("isRegFee", false);
            data.put("donationID", "5FD34169C6B9ACFA");
            data.put("incentiveID", "96E52B46-FBB0-6EF6-56EA6C7E1BDC6B00");
            data.put("message", "This was donated by the winner of the Olexamas Art competition Jepski! They offered the donation from the cash prize for winning, which was extremely kind <3");
            data.put("participantID", 548110);
            data.put("recipientImageURL", "https://donordrivecontent.com/extralife/images/$avatars$/constituent_6AD3944D-941B-205A-6D97DA944FDE4EAE.jpg");
            data.put("createdDateUTC", "2024-11-29T16:17:52.77+0000");
            data.put("teamID", 68860);
            data.put("recipientName", "Olexamas 2024");
            data.put("donorID", "D828B0CA974D38C6");
            JSONObject links = new JSONObject();
            links.put("recipient", "https://www.extra-life.org/index.cfm?fuseaction=donorDrive.participant&participantID=547496");
            links.put("donate", "https://www.extra-life.org/index.cfm?fuseaction=donorDrive.participant&participantID=547496#donate");
            data.put("links", links);

            Donation donation = new Donation(data);
            DonorDriveApi.queueDonation(donation);
            LootManager lootManager = DataManager.getConfigManager("loot", LootManager.class);
            if (lootManager.isEditing(e.getPlayer())) {
                lootManager.createDrop(lootManager.getEditingLocation(e.getPlayer()), Objects.requireNonNull(e.getClickedBlock()).getLocation());
                e.getPlayer().sendMessage(MessageUtils.getMessage("loot.create", lootManager.getEditingLocation(e.getPlayer())));
                lootManager.finishEditing(e.getPlayer());
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        Player player = e.getEntity();
        boolean boogieKill = false;
        boolean elimination = false;
        Component msg = e.deathMessage();
//        String msg = ChatColor.stripColor(e.getDeathMessage());
        PlayerManager playerManager = (PlayerManager) DataManager.getConfigManager("players");
        if (player.getKiller() != null && playerManager.isBoogie(player.getKiller()) && !player.equals(player.getKiller())) {
            playerManager.removeBoogie(player.getKiller());
            boogieKill = true;
        }
        if (playerManager.getLives(player) == 1) {
            e.setCancelled(true);
            player.setGameMode(GameMode.SPECTATOR);
            player.getWorld().strikeLightningEffect(player.getLocation());
            msg = MessageUtils.getMessage("action.elimination", player.getName(), (player.hasMetadata("last_damager") ? " by " + ((Player) Objects.requireNonNull(player.getMetadata("last_damager").getFirst().value())).getName() : ""));
            e.getEntity().getServer().broadcast(msg);
            elimination = true;
            playerManager.removeBoogie(player);
            playerManager.setParty(player, "none");
        }
        playerManager.removeLife(player);
        try {
            sendDeathEmbed(MessageUtils.plainText(msg), player, boogieKill, elimination);
        } catch (QuickException quickException) {
            CoreUtils.logger().log(Logger.LogLevel.ERROR, "DeathManager", quickException);
        }
    }

    private void sendDeathEmbed(String msg, Player player, boolean boogieKill, boolean elimination) throws QuickException {
//        JSONObject embed = new JSONObject();
        PlayerManager playerManager = (PlayerManager) DataManager.getConfigManager("players");
        Embed embed = new Embed();
        embed.title(player.getName() + " has died!");
        embed.description(msg);
        embed.addField("Lives", String.valueOf(playerManager.getLives(player)), true);
        embed.addField("Boogie Kill", String.valueOf(boogieKill), true);
        embed.addField("Elimination", String.valueOf(elimination), true);
        embed.color(TextColor.color(0xE9334B).value());

        DataManager.getConfigManager("webhooks", WebhookManager.class).send("deaths", embed);


    }
}
