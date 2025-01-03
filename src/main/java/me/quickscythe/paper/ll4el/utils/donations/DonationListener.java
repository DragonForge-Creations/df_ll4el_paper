package me.quickscythe.paper.ll4el.utils.donations;

import io.papermc.paper.advancement.AdvancementDisplay;
import json2.JSONObject;
import me.quickscythe.dragonforge.exceptions.QuickException;
import me.quickscythe.dragonforge.utils.CoreUtils;
import me.quickscythe.dragonforge.utils.advancements.EphemeralAdvancement;
import me.quickscythe.dragonforge.utils.chat.Logger;
import me.quickscythe.dragonforge.utils.config.ConfigFile;
import me.quickscythe.dragonforge.utils.config.ConfigFileManager;
import me.quickscythe.dragonforge.utils.network.discord.WebhookManager;
import me.quickscythe.dragonforge.utils.network.discord.embed.Embed;
import me.quickscythe.dragonforge.utils.storage.DataManager;
import me.quickscythe.paper.ll4el.utils.Utils;
import me.quickscythe.paper.ll4el.utils.donations.event.DonationEvent;
import me.quickscythe.paper.ll4el.utils.managers.PlayerManager;
import me.quickscythe.paper.ll4el.utils.managers.SettingsManager;
import me.quickscythe.paper.ll4el.utils.managers.loot.LootManager;
import me.quickscythe.paper.ll4el.utils.managers.loot.LootType;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import static net.kyori.adventure.text.Component.text;

public class DonationListener {


    public void donationReceived(DonationEvent e) {
        // Handle donation event
        sendDonationWebhook(e.donation());
        ConfigFile processedDonations = ConfigFileManager.getFile(Utils.plugin(), "processed_donations");
        ConfigFile config = ConfigFileManager.getFile(Utils.plugin(), "config");
        double amount = e.donation().amount();
        if (!processedDonations.getData().has("total")) processedDonations.getData().put("total", 0.0D);
        double total = processedDonations.getData().getDouble("total") + amount;
        processedDonations.getData().put("total", total);
        if (!processedDonations.getData().has("last_drop")) processedDonations.getData().put("last_drop", 0);
        int last_drop = processedDonations.getData().getInt("last_drop");
        double drop_amount = config.getData().getDouble("cumulativeDropTriggerAmount");
        // Calculate the number of loot drops needed
        int newDrops = (int) (total / drop_amount) - last_drop;
        CoreUtils.logger().log("DonationProcessor", "New drops: " + newDrops);
        if (newDrops > 0) {
            for (int j = 0; j < newDrops; j++) {
                // Trigger loot drop
                triggerLootDrop();
            }
            processedDonations.getData().put("last_drop", last_drop + newDrops);
            sendLootDropWebhook(newDrops);
        }

        if (e.player() != null) {
            OfflinePlayer offlinePlayer = e.player();
            if (offlinePlayer == null) {
                CoreUtils.logger().log("DonationProcessor", "Couldn't find player with participant ID " + e.donation().participantId());
                return;
            }
            Player player = offlinePlayer.isOnline() ? offlinePlayer.getPlayer() : null;
            SettingsManager.Settings settings = SettingsManager.getSettings(offlinePlayer);
            if (player != null) {
                if (settings.donation().equalsIgnoreCase("both") || settings.donation().equalsIgnoreCase("toast")) {
                    sendDonationToast(player, e.donation());
                }
                if (settings.donation().equalsIgnoreCase("both") || settings.donation().equalsIgnoreCase("chat")) {
                    Component message = text("Donation: ").color(TextColor.color(0xFAFF18)).append(text("A donation of $" + e.donation().amount() + " was received from " + e.donation().displayName() + "."));
                    player.sendMessage(message);
                }
            }
            if (!e.donation().incentiveId().isEmpty()) {
                CoreUtils.logger().log("DonationProcessor", "Donation has incentive ID");
                JSONObject incentiveData = DonorDriveApi.getIncentiveData(e.donation().participantId(), e.donation().incentiveId());
                if (!incentiveData.getString("description").contains("ID:7781") && !incentiveData.getString("description").contains("ID: 7781"))
                    return;
                //If incentiveData doesn't contain a specific incentive ID, we'll stop processing here.
                if (incentiveData.getString("description").contains("ID: 7781")) {
                    String description = incentiveData.getString("description");
                    description = description.replace("ID: 7781", "ID:7781");
                    incentiveData.put("description", description);
                }

                DonorDriveApi.IncentiveType type = DonorDriveApi.IncentiveType.fromDescription(incentiveData.getString("description"));
                CoreUtils.logger().log("DonationProcessor", "Incentive type: " + type);
                switch (type) {
                    case BOOGIE:
                        DonorDriveApi.queueBoogie();
                        break;
                    case LIFE:
                        PlayerManager playerManager = DataManager.getConfigManager("players", PlayerManager.class);
                        if (playerManager.getLives(offlinePlayer) <= 2) {
                            playerManager.addLife(offlinePlayer);
                        }
                        break;
                    case LOOT:
                        DataManager.getConfigManager("loot", LootManager.class).randomDrop(LootType.SHULKER);
                        break;
                    case ADMIN_LOOT:
                        sendAdminLootWebhook();
                        break;
                    case OTHER:
                        break;
                }
                sendIncentiveWebhook(type, offlinePlayer);
            }
        }
    }

    private void sendAdminLootWebhook() {
        Embed embed = new Embed();
        embed.title("Secret Time!");
        embed.description("A donation has triggered Admin Loot Location incentive. It's time to reveal one of the Well's locations'!");
        embed.color(TextColor.color(0xD9CB11).value());
        try {
            DataManager.getConfigManager("webhooks", WebhookManager.class).send("donations", embed);
        } catch (QuickException e) {
            CoreUtils.logger().log(Logger.LogLevel.ERROR, "DonationProcessor", "Couldn't send incentive webhook.");
        }
    }

    private void sendDonationToast(Player player, Donation donation) {
        String displayName = donation.displayName();
        double amount = donation.amount();
        String message = donation.message();
        EphemeralAdvancement.Builder builder = new EphemeralAdvancement.Builder(CoreUtils.plugin());
//        builder.icon(Material.CHEST);
        ItemStack gift = new ItemStack(Material.STICK);
        ItemMeta meta = gift.getItemMeta();
        meta.setCustomModelData(107);
        gift.setItemMeta(meta);
        builder.icon(gift);
        builder.frame(AdvancementDisplay.Frame.GOAL);
        builder.title("A donation of $" + amount + " was received from '" + displayName + "'.");
        builder.description(message);
        EphemeralAdvancement adv = builder.build();
        adv.send(player.getPlayer());

    }

    private void sendIncentiveWebhook(DonorDriveApi.IncentiveType type, OfflinePlayer player) {
        Embed embed = new Embed();
        embed.title("Incentive Triggered");
        embed.description("A donation has triggered the " + type.name().toLowerCase() + " incentive.");
        embed.color(TextColor.color(0xCD25D9).value());
        if (player != null) {
            embed.addField("Player", player.getName(), false);
        }
        try {
            DataManager.getConfigManager("webhooks", WebhookManager.class).send("donations", embed);
        } catch (QuickException e) {
            CoreUtils.logger().log(Logger.LogLevel.ERROR, "DonationProcessor", "Couldn't send incentive webhook.");
        }
    }

    private void sendLootDropWebhook(int drops) {
        Embed embed = new Embed();
        embed.title("Loot Drop Triggered");
        embed.description("A donation has triggered " + drops + " cumulative loot drop(s).");
        embed.color(TextColor.color(0x63D92A).value());
        try {
            DataManager.getConfigManager("webhooks", WebhookManager.class).send("donations", embed);
        } catch (QuickException e) {
            CoreUtils.logger().log(Logger.LogLevel.ERROR, "DonationProcessor", "Couldn't send loot drop webhook.");
        }
    }

    private void sendDonationWebhook(Donation donation) {

        Embed embed = new Embed();
        embed.title("Donation Received");
        embed.description("A donation of $" + donation.amount() + " was received.");
        embed.color(TextColor.color(0x31D9D9).value());
        embed.url(donation.links().getString("recipient"));
        embed.image(donation.recipientImageUrl());

        embed.addField("Message", donation.message(), false);
        embed.addField("Recipient", donation.recipientName(), false);

        try {
            DataManager.getConfigManager("webhooks", WebhookManager.class).send("donations", embed);
        } catch (QuickException e) {
            CoreUtils.logger().log(Logger.LogLevel.ERROR, "DonationProcessor", "Couldn't send donation webhook.");
        }
    }

    private void triggerLootDrop() {
        ConfigFile config = ConfigFileManager.getFile(Utils.plugin(), "config");
        int drops = config.getData().getInt("cumulativeDrops");
        for (int i = 0; i < drops; i++) {
            DataManager.getConfigManager("loot", LootManager.class).randomDrop(LootType.DEFAULT);
        }
    }
}
