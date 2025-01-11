package me.quickscythe.paper.ll4el.utils.donations;

import json2.JSONArray;
import json2.JSONObject;
import me.quickscythe.dragonforge.exceptions.QuickException;
import me.quickscythe.dragonforge.utils.CoreUtils;
import me.quickscythe.dragonforge.utils.chat.Logger;
import me.quickscythe.dragonforge.utils.config.ConfigFile;
import me.quickscythe.dragonforge.utils.config.ConfigFileManager;
import me.quickscythe.dragonforge.utils.network.NetworkUtils;
import me.quickscythe.dragonforge.utils.network.discord.WebhookManager;
import me.quickscythe.dragonforge.utils.network.discord.embed.Embed;
import me.quickscythe.dragonforge.utils.storage.DataManager;
import me.quickscythe.paper.ll4el.utils.Utils;
import me.quickscythe.paper.ll4el.utils.donations.event.DonationEvent;
import me.quickscythe.paper.ll4el.utils.managers.BoogieManager;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DonorDriveApi {

    private static JSONArray participants;
    private static ConfigFile config;
    private static ConfigFile processedDonations;

    private static int queuedBoogies = 0;

    private static DonationListener donationListener;


    public static void start() {
        CoreUtils.logger().log("DonorDriveAPI", "Starting DonorDriveApi...");
        processedDonations = ConfigFileManager.getFile(Utils.plugin(), "processed_donations");
        config = ConfigFileManager.getFile(Utils.plugin(), "config", "config.json");
        if (processedDonations.getData().isEmpty()) {
            processedDonations.getData().put("donations", new JSONArray());
        }
        updateParticipants();
        donationListener = new DonationListener();
    }

    public static void processDonations() {
        CoreUtils.logger().log("DonationProcessor", "Starting donation processing...");
        JSONArray donations = getTeamDonations();
        JSONArray processed = processedDonations.getData().getJSONArray("donations");
        boolean save = false;
        List<Donation> donationList = new ArrayList<>();
        for (int i = 0; i < donations.length(); i++) {
            JSONObject donation = donations.getJSONObject(i);
            if (!processed.toString().contains(donation.toString())) {
                donationList.add(new Donation(donation));
//                donationListener.donationReceived(new DonationEvent(new Donation(donation)));
                save = true;
                processed.put(donation);
            }
        }
        Bukkit.getScheduler().runTaskLater(CoreUtils.plugin(), () ->{
            for (Donation donation : donationList) {
                donationListener.donationReceived(new DonationEvent(donation));
            }
        }, 1);

        CoreUtils.logger().log("DonationProcessor", "Processed donations.");

        if (save) processedDonations.save();

    }

    public static JSONObject getIncentiveData(long participantId, String incentiveId) {
        String url = config.getData().getString("domain");
        InputStream inputStream = NetworkUtils.downloadFile("https://" + url + "/api/participants/" + participantId + "/incentives");
        JSONArray incentives;
        try {

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String response = reader.lines().collect(Collectors.joining("\n"));
                incentives = new JSONArray(response);
            }
        } catch (IOException ex) {
            CoreUtils.logger().log(Logger.LogLevel.ERROR, "DonationProcessor", "Couldn't reach DonorDrive API. Double check your config.");
            incentives = new JSONArray();
        }
        for (int i = 0; i < incentives.length(); i++) {
            JSONObject incentive = incentives.getJSONObject(i);
            if (incentive.getString("incentiveID").equals(incentiveId)) {
                return incentive;
            }
        }
        return new JSONObject();

    }

    public static void updateParticipants() {
        int teamId = config.getData().getInt("teamId");
        String url = config.getData().getString("domain");
        InputStream inputStream = NetworkUtils.downloadFile("https://" + url + "/api/teams/" + teamId + "/participants");
//        https://www.extra-life.org/api/teams/68921/participants
        try {

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String response = reader.lines().collect(Collectors.joining("\n"));
                DonorDriveApi.participants = new JSONArray(response);
            }
        } catch (IOException ex) {
            CoreUtils.logger().log(Logger.LogLevel.ERROR, "DonationProcessor", "Couldn't reach DonorDrive API. Double check your config.");
            DonorDriveApi.participants = new JSONArray();
        }
    }


    static JSONObject getUsers() {
        if (!config.getData().has("users")) {
            config.getData().put("users", new JSONObject());
        }
        return config.getData().getJSONObject("users");
    }

    public static JSONObject getParticipant(long participantId) {
        for (int i = 0; i < participants.length(); i++) {
            JSONObject participant = participants.getJSONObject(i);
            if (participant.getLong("participantID") == participantId) {
                return participant;
            }
        }
        CoreUtils.logger().log(Logger.LogLevel.ERROR, "DonationProcessor", "Couldn't find participant for " + participantId);
        return null;
    }

    public static void linkParticipant(OfflinePlayer player, long participantId) {
        JSONObject users = getUsers();
        users.put(player.getUniqueId().toString(), participantId);
        config.save();
    }

    public static JSONArray getTeamDonations() {
        long teamId = config.getData().getLong("teamId");
        String url = "https://" + config.getData().getString("domain") + "/api/teams/" + teamId + "/donations";
        InputStream inputStream = NetworkUtils.downloadFile(url);
        try {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String response = reader.lines().collect(Collectors.joining("\n"));
                return new JSONArray(response);
            }
        } catch (IOException ex) {
            CoreUtils.logger().log(Logger.LogLevel.ERROR, "DonationProcessor", "Couldn't reach DonorDrive API. Double check your config.");
            return new JSONArray();
        }
    }

    public static long getDonorTime() {
        return config.getData().getLong("donorDriveTimer");
    }

    public static long getBoogieTimer() {
        return config.getData().getLong("boogieTimer");
    }

    public static void rollBoogies() {
        DataManager.getConfigManager("boogies", BoogieManager.class).rollBoogies(queuedBoogies, true);


        Embed embed = new Embed();
        embed.title("Boogie Time!");
        embed.description(queuedBoogies + " Boogies have been rolled!");
        embed.color(TextColor.color(0xD97E1C).value());
        try {
            DataManager.getConfigManager("webhooks", WebhookManager.class).send("donations", embed);
        } catch (QuickException e) {
            CoreUtils.logger().log(Logger.LogLevel.ERROR, "DonationProcessor", "Couldn't send boogie webhook.");
        }

        queuedBoogies = 0;
    }

    public static void queueBoogie() {
        queueBoogies(1);
    }

    public static void queueBoogies(int amount) {
        queuedBoogies = queuedBoogies + amount;
    }

    public static OfflinePlayer getOfflinePlayer(long participantId) {
        JSONObject users = DonorDriveApi.getUsers();
        OfflinePlayer offlinePlayer = null;
        for (String key : users.keySet()) {
            if (users.getLong(key) == participantId) {
                offlinePlayer = Utils.plugin().getServer().getOfflinePlayer(UUID.fromString(key));
                break;
            }
        }
        return offlinePlayer;
    }

    public static void queueDonation(Donation donation) {
        donationListener.donationReceived(new DonationEvent(donation));
    }

    public enum IncentiveType {

        BOOGIE("ID:77813"), LIFE("ID:77814"), LOOT("ID:77815"), ADMIN_LOOT("ID:77816"), OTHER("*");

        final String id;

        IncentiveType(String id) {
            this.id = id;
        }

        public static IncentiveType fromDescription(String description) {
            for (IncentiveType type : values()) {
                if (description.endsWith(type.id)) {
                    return type;
                }
            }
            return OTHER;
        }

        public String id() {
            return id;
        }
    }

}
