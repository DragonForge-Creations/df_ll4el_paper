package me.quickscythe.paper.ll4el.utils;

import json2.JSONArray;
import json2.JSONObject;
import me.quickscythe.dragonforge.exceptions.QuickException;
import me.quickscythe.dragonforge.utils.CoreUtils;
import me.quickscythe.dragonforge.utils.chat.Logger;
import me.quickscythe.dragonforge.utils.config.ConfigFile;
import me.quickscythe.dragonforge.utils.config.ConfigFileManager;
import me.quickscythe.dragonforge.utils.network.NetworkUtils;
import me.quickscythe.dragonforge.utils.network.WebhookUtils;
import me.quickscythe.dragonforge.utils.storage.DataManager;
import me.quickscythe.paper.ll4el.utils.managers.BoogieManager;
import me.quickscythe.paper.ll4el.utils.managers.PlayerManager;
import me.quickscythe.paper.ll4el.utils.managers.loot.LootManager;
import me.quickscythe.paper.ll4el.utils.managers.loot.LootType;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.OfflinePlayer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import java.util.stream.Collectors;

public class DonorDriveApi {

    private static ConfigFile config;
    private static JSONArray participants;

    private static ConfigFile processedDonations;

    private static int queuedBoogies = 0;

    public static void start() {
        CoreUtils.logger().log("DonorDriveAPI", "Starting DonorDriveApi...");
        DonorDriveApi.config = ConfigFileManager.getFile(Utils.plugin(), "config", "config.json");
        DonorDriveApi.processedDonations = ConfigFileManager.getFile(Utils.plugin(), "processed_donations");
        if (processedDonations.getData().isEmpty()) {
            processedDonations.getData().put("donations", new JSONArray());
        }
        updateParticipants();
    }

    public static void processDonations() {
        CoreUtils.logger().log("DonationProcessor", "Starting donation processing...");
        JSONArray donations = getTeamDonations();
        JSONArray processed = processedDonations.getData().getJSONArray("donations");
        boolean save = false;
        for (int i = 0; i < donations.length(); i++) {
            JSONObject donation = donations.getJSONObject(i);


            if (!processed.toString().contains(donation.toString())) {
                CoreUtils.logger().log("DonationProcessor", "Processing donation: " + donation.getString("donationID"));
                processed.put(donation);
                save = true;
                sendDonationWebhook(donation);
                //process donation here

                double amount = donation.getDouble("amount");
                if(!processedDonations.getData().has("total"))
                    processedDonations.getData().put("total", 0.0D);
                double total = processedDonations.getData().getDouble("total") + amount;
                processedDonations.getData().put("total", total);
                if(!processedDonations.getData().has("last_drop"))
                    processedDonations.getData().put("last_drop", 0);
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
                }
                long participantId = donation.has("participantID") ? donation.getLong("participantID") : 0L;
                if (donation.has("incentiveID") && participantId != 0) {
                    CoreUtils.logger().log("DonationProcessor", "Donation has incentive ID");
                    String incentiveId = donation.getString("incentiveID");

                    JSONObject incentiveData = getIncentiveData(participantId, incentiveId);
                    if(!incentiveData.getString("description").contains("ID:7718")) continue;


                    IncentiveType type = IncentiveType.fromDescription(incentiveData.getString("description"));
                    CoreUtils.logger().log("DonationProcessor", "Incentive type: " + type);
                    switch (type) {
                        case BOOGIE:
                            queuedBoogies = queuedBoogies + 1;
                            break;
                        case LIFE:
                            JSONObject users = getUsers();
                            for (String key : users.keySet()) {
                                if (users.getLong(key) == participantId) {
                                    OfflinePlayer player = Utils.plugin().getServer().getOfflinePlayer(UUID.fromString(key));
                                    PlayerManager playerManager = DataManager.getConfigManager("players", PlayerManager.class);
                                    playerManager.addLife(player);
                                }
                            }
                            break;
                        case LOOT:
                            DataManager.getConfigManager("loot", LootManager.class).randomDrop(LootType.SHULKER);
                            break;
                        case OTHER:
                            break;
                    }
                }
            }
        }

        CoreUtils.logger().log("DonationProcessor", "Processed donations.");

        if (save)
            processedDonations.save();

    }

    private static void sendDonationWebhook(JSONObject donation) {
        JSONObject embed = new JSONObject();
        embed.put("title", "Donation Received");
        embed.put("description", "A donation of $" + donation.getDouble("amount") + " was received.");
        embed.put("color", TextColor.color(0x31D9D9).value());
        embed.put("url", donation.getJSONObject("links").getString("recipient"));
        embed.put("image", new JSONObject().put("url", donation.getString("recipientImageURL")));

        JSONArray fields = new JSONArray();
        if(donation.has("message") && donation.get("message") instanceof String){
            JSONObject f_message = new JSONObject();
            f_message.put("name", "Message");
            f_message.put("value", donation.getString("message"));
            fields.put(f_message);
        }

        JSONObject f_recipient = new JSONObject();
        f_recipient.put("name", "Recipient");
        f_recipient.put("value", donation.getString("recipientName"));
        fields.put(f_recipient);


        embed.put("fields", fields);


        JSONObject send = new JSONObject();
        send.put("embeds", new JSONArray().put(embed));
        try {
            WebhookUtils.send("donations", send);
        } catch (QuickException e) {
            CoreUtils.logger().log(Logger.LogLevel.ERROR, "DonationProcessor", "Couldn't send donation webhook.");
        }
    }

    private static void triggerLootDrop() {
        int drops = config.getData().getInt("cumulativeDrops");
        for(int i = 0; i < drops; i++){
            DataManager.getConfigManager("loot", LootManager.class).randomDrop(LootType.DEFAULT);
        }
    }

    public static JSONObject getIncentiveData(long participantId, String incentiveId){
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
        for(int i = 0; i < incentives.length(); i++){
            JSONObject incentive = incentives.getJSONObject(i);
            if(incentive.getString("incentiveID").equals(incentiveId)){
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


    private static JSONObject getUsers() {
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
    
    public static long getBoogieTimer(){
        return  config.getData().getLong("boogieTimer");
    }

    public static void rollBoogies() {
        DataManager.getConfigManager("boogies", BoogieManager.class).rollBoogies(queuedBoogies, true);
        queuedBoogies = 0;
    }

    public enum IncentiveType {

        BOOGIE("ID:77813"),
        LIFE("ID:77814"),
        LOOT("ID:77815"),
        OTHER("*");

        final String id;

        IncentiveType(String id) {
            this.id = id;
        }

        public static IncentiveType fromDescription(String description) {
            for(IncentiveType type : values()){
                if(description.endsWith(type.id)){
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
