package me.quickscythe.paper.ll4el.utils.managers;

import json2.JSONObject;
import org.bukkit.OfflinePlayer;

public class SettingsManager {

    public static Settings getSettings(OfflinePlayer player){
        return new Settings(PlayerManager.getPlayerData(player).getJSONObject("settings"));
    }

    public static void setSettings(OfflinePlayer player, Settings settings){
        JSONObject pd = PlayerManager.getPlayerData(player);
        pd.put("settings", settings.json());
        PlayerManager.setPlayerData(player,pd);

    }

    public static class Settings {
        private final JSONObject settings;

        public Settings(JSONObject settings){
            this.settings = settings;
        }

        public static JSONObject defaultSettings() {
            JSONObject def = new JSONObject();
            def.put("particles",true);
            def.put("icon", true);
            def.put("chat",true);
            return def;
        }

        public boolean particles(){
            return settings.getBoolean("particles");
        }

        public void particles(boolean particles){
            settings.put("particles", particles);
        }

        public boolean icon(){
            return settings.getBoolean("icon");
        }

        public void icon(boolean b) {
            settings.put("icon", b);
        }

        public boolean chat(){
            return settings.getBoolean("chat");
        }

        public void chat(boolean b) {
            settings.put("chat", b);
        }

        public JSONObject json(){
            return settings;
        }
    }
}
