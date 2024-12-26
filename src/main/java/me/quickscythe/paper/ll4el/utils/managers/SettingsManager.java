package me.quickscythe.paper.ll4el.utils.managers;

import json2.JSONObject;
import me.quickscythe.dragonforge.utils.storage.DataManager;
import org.bukkit.OfflinePlayer;

public class SettingsManager {

    public static Settings getSettings(OfflinePlayer player) {
        return new Settings(((PlayerManager) DataManager.getConfigManager("players")).getPlayerData(player).getJSONObject("settings"));
    }

    public static void setSettings(OfflinePlayer player, Settings settings) {
        PlayerManager playerManager = (PlayerManager) DataManager.getConfigManager("players");
        JSONObject pd = playerManager.getPlayerData(player);
        pd.put("settings", settings.json());
        playerManager.setPlayerData(player, pd);
    }

    public static class Settings {
        private final JSONObject settings;

        public Settings(JSONObject settings) {

            if (settings.isEmpty()) this.settings = defaultSettings();
            else this.settings = settings;
        }

        public static JSONObject defaultSettings() {
            JSONObject def = new JSONObject();
            def.put("boogie", "both");
            def.put("life", "both");
            def.put("donation", "toast");
            return def;
        }

        public String boogie() {
            return settings.getString("boogie");
        }

        public void boogie(String boogie) {
            settings.put("boogie", boogie);
        }

        public String life() {
            return settings.getString("life");
        }

        public void life(String life) {
            settings.put("life", life);
        }

        public String donation() {
            return settings.getString("donation");
        }

        public void donation(String donation) {
            settings.put("donation", donation);
        }

        public JSONObject json() {
            return settings;
        }
    }
}
