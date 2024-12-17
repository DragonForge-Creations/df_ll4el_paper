package me.quickscythe.paper.ll4el.utils;

import json2.JSONArray;
import json2.JSONObject;
import me.quickscythe.dragonforge.utils.CoreUtils;
import me.quickscythe.dragonforge.utils.chat.Logger;
import me.quickscythe.dragonforge.utils.config.ConfigFile;
import me.quickscythe.dragonforge.utils.config.ConfigFileManager;
import me.quickscythe.dragonforge.utils.network.NetworkUtils;
import me.quickscythe.dragonforge.utils.storage.DataManager;
import me.quickscythe.paper.ll4el.utils.managers.BoogieManager;
import me.quickscythe.paper.ll4el.utils.managers.PlayerManager;
import me.quickscythe.paper.ll4el.utils.managers.loot.LootManager;
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

    public static void start() {
        CoreUtils.logger().log("Starting DonorDriveApi...");
        DonorDriveApi.config = ConfigFileManager.getFile(Utils.plugin(), "donordrive", "donordrive.json");
        DonorDriveApi.processedDonations = ConfigFileManager.getFile(Utils.plugin(), "processed_donations");
        if (processedDonations.getData().isEmpty()) {
            processedDonations.getData().put("donations", new JSONArray());
        }
        updateParticipants();
    }

    public static void processDonations() {
        JSONArray donations = getTeamDonations();
        JSONArray processed = processedDonations.getData().getJSONArray("donations");
        boolean save = false;
        int boogies = 0;
        for (int i = 0; i < donations.length(); i++) {
            JSONObject donation = donations.getJSONObject(i);
            if (!processed.toString().contains(donation.toString())) {
                processed.put(donation);
                save = true;
                //process donation here
                double amount = donation.getDouble("amount");
                double total = processedDonations.getData().getDouble("total") + amount;
                processedDonations.getData().put("total", total);
                int last_drop = processedDonations.getData().getInt("last_drop");
                double drop_amount = config.getData().getDouble("cumulative_drop_amount");
                // Calculate the number of loot drops needed
                int newDrops = (int) (total / drop_amount) - last_drop;
                if (newDrops > 0) {
                    for (int j = 0; j < newDrops; j++) {
                        // Trigger loot drop
                        triggerLootDrop();
                    }
                    processedDonations.getData().put("last_drop", last_drop + newDrops);
                }
                if (donation.has("incentiveID")) {
                    String incentiveId = donation.getString("incentiveID");
                    IncentiveType type = IncentiveType.valueOf(incentiveId);
                    switch (type) {
                        case BOOGIE:
                            boogies = boogies + 1;
                            break;
                        case LIFE:
                            long participantId = donation.getLong("participantID");
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
                            DataManager.getConfigManager("loot", LootManager.class).randomDrop();
                            break;
                        case OTHER:
                            break;
                    }
                }
            }
        }
        if (boogies > 0)
            DataManager.getConfigManager("boogies", BoogieManager.class).rollBoogies(boogies, true);

        if (save)
            processedDonations.save();

    }

    private static void triggerLootDrop() {
        //todo
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
            CoreUtils.logger().log(Logger.LogLevel.ERROR, "Couldn't reach DonorDrive API. Double check your config.");
            DonorDriveApi.participants = new JSONArray();
        }
    }

    public static JSONArray getParticipants() {
        return participants;
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
        CoreUtils.logger().log(Logger.LogLevel.ERROR, "Couldn't find participant for " + participantId);
        return null;
    }

    public static JSONObject getParticipant(UUID uid) {
        JSONObject users = getUsers();
        if (users.has(uid.toString())) {
            long participantId = users.getLong(uid.toString());
            for (int i = 0; i < participants.length(); i++) {
                JSONObject participant = participants.getJSONObject(i);
                if (participant.getLong("participantID") == participantId) {
                    return participant;
                }
            }
        }
        CoreUtils.logger().log(Logger.LogLevel.ERROR, "Couldn't find participant for " + uid);
        return null;
    }

    public static void linkParticipant(OfflinePlayer player, long participantId) {
        JSONObject users = getUsers();
        users.put(player.getUniqueId().toString(), participantId);
        config.save();
    }

    public static JSONArray getDonations(long participantId) {
        String url = "https://" + config.getData().getString("domain") + "/api/participants/" + participantId + "/donations";
        InputStream inputStream = NetworkUtils.downloadFile(url);
        try {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                String response = reader.lines().collect(Collectors.joining("\n"));
                return new JSONArray(response);
            }
        } catch (IOException ex) {
            CoreUtils.logger().log(Logger.LogLevel.ERROR, "Couldn't reach DonorDrive API. Double check your config.");
            return new JSONArray();
        }
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
            CoreUtils.logger().log(Logger.LogLevel.ERROR, "Couldn't reach DonorDrive API. Double check your config.");
            return new JSONArray();
        }
    }

    public enum IncentiveType {

        BOOGIE("X_X_X"),
        LIFE(""),
        LOOT(""),
        OTHER("");

        String id;

        IncentiveType(String id) {
            this.id = id;
        }

        public static IncentiveType fromId(String id) {
            for (IncentiveType type : values()) {
                if (type.id().equals(id)) return type;
            }
            return OTHER;
        }

        public String id() {
            return id;
        }
    }

}
