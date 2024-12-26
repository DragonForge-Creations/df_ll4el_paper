package me.quickscythe.paper.ll4el.utils.donations;

import json2.JSONObject;
import me.quickscythe.paper.ll4el.utils.Utils;
import org.bukkit.OfflinePlayer;

import java.util.ResourceBundle;
import java.util.UUID;

public class Donation {

    private final JSONObject data;
    private OfflinePlayer player = null;

    public Donation(JSONObject data) {
        this.data = data;
    }

    public long participantId() {
        return data.has("participantID") ? data.getLong("participantID") : 0L;
    }

    public long donationId() {
        return data.getLong("donationID");
    }

    public double amount() {
        return data.getDouble("amount");
    }

    public String incentiveId() {
        return data.has("incentiveID") ? data.getString("incentiveID") : "";
    }

    public String displayName() {
        return data.has("displayName") ? data.getString("displayName") : "Anonymous";
    }

    public String message() {
        return data.has("message") && data.get("message") instanceof String ? data.getString("message") : "Thank you!";
    }

    public JSONObject links() {
        return data.getJSONObject("links");
    }

    public String recipientImageUrl() {
        return data.getString("recipientImageURL");
    }

    public String recipientName() {
        return data.getString("recipientName");
    }

    public OfflinePlayer player() {
        if(player == null) {
            JSONObject users = DonorDriveApi.getUsers();
            for (String key : users.keySet()) {
                if (users.getLong(key) == participantId()) {
                    player = Utils.plugin().getServer().getOfflinePlayer(UUID.fromString(key));
                    break;
                }
            }
        }
        return player;
    }
}
