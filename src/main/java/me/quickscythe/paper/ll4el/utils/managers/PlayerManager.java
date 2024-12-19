package me.quickscythe.paper.ll4el.utils.managers;

import json2.JSONObject;
import me.quickscythe.dragonforge.utils.CoreUtils;
import me.quickscythe.dragonforge.utils.chat.MessageUtils;
import me.quickscythe.dragonforge.utils.storage.ConfigManager;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class PlayerManager extends ConfigManager {

    public PlayerManager(JavaPlugin plugin) {
        super(plugin, "players");
    }

   

    public void checkData(OfflinePlayer player) {
        if (!config().getData().has(String.valueOf(player.getUniqueId()))) {
            JSONObject pd = new JSONObject();
            pd.put("last_selected", 0);
            pd.put("boogie", false);
            pd.put("lives", 3);
            pd.put("name", player.getName());
            pd.put("settings", SettingsManager.Settings.defaultSettings());
            pd.put("party", "none");
            config().getData().put(String.valueOf(player.getUniqueId()), pd);

        }
        JSONObject pd = getPlayerData(player);
        pd.put("name", player.getName());
        setPlayerData(player, pd);
        setScoreboardInfo(player);
    }

    public void end() {
        config().save();
    }


    public UUID getUUID(String lastUsername) {
        for (String sid : config().getData().keySet()) {
            if (config().getData().getJSONObject(sid).get("name").equals(lastUsername)) return UUID.fromString(sid);
        }
        return null;
    }

    public JSONObject getPlayerData(OfflinePlayer player) {
        return config().getData().getJSONObject(String.valueOf(player.getUniqueId()));
    }

    public void setPlayerData(OfflinePlayer player, JSONObject json) {
        config().getData().put(String.valueOf(player.getUniqueId()), json);
    }

    public String getParty(OfflinePlayer player){
        return config().getData().getJSONObject(String.valueOf(player.getUniqueId())).getString("party");
    }

    public void setParty(OfflinePlayer player, String party){
        setPlayerData(player, getPlayerData(player).put("party",party));
        if(player.isOnline() && !party.equalsIgnoreCase("none"))
            Objects.requireNonNull(player.getPlayer()).sendMessage(MessageUtils.getMessage("party.join.success", party));
    }

    public boolean isBoogie(OfflinePlayer player) {
        return getPlayerData(player).getBoolean("boogie");
    }


    public void removeBoogie(OfflinePlayer player) {
        JSONObject pd = getPlayerData(player);
        pd.put("boogie", false);
        pd.put("last_selected", new Date().getTime());
        if (player.isOnline()) Objects.requireNonNull(player.getPlayer()).sendMessage(MessageUtils.getMessage("message.boogie.cured"));
        setPlayerData(player, pd);
    }


    public int getLives(OfflinePlayer player) {
        return getPlayerData(player).getInt("lives");
    }

    public  void removeLife(OfflinePlayer player) {
        editLife(player, -1);
    }


    public void addLife(OfflinePlayer player) {
        editLife(player, 1);
    }

    public void setLife(OfflinePlayer player, int l) {
        JSONObject pd = getPlayerData(player);
        pd.put("lives", l);
        setPlayerData(player, pd);
        setScoreboardInfo(player);
    }

    public void editLife(OfflinePlayer player, int i) {
        editLife(player, i, true);
    }

    public void editLife(OfflinePlayer player, int i, boolean animation) {
        int l = getLives(player) + i;
        setLife(player, l);
        if (i > 0 && player.isOnline()) {
            int cmd = 1000 + i;
            if (animation) CoreUtils.playTotemAnimation(player.getPlayer(), cmd);
            Objects.requireNonNull(player.getPlayer()).sendMessage(MessageUtils.getMessage("message.lives.more", i +""));
        }
    }

    private void setScoreboardInfo(OfflinePlayer player) {
        int l = getLives(player);
        if (l == 0) {
            Objects.requireNonNull(CoreUtils.plugin().getServer().getScoreboardManager().getMainScoreboard().getTeam("red")).removeEntry(Objects.requireNonNull(player.getName()));
            Objects.requireNonNull(CoreUtils.plugin().getServer().getScoreboardManager().getMainScoreboard().getTeam("yellow")).removeEntry(player.getName());
            Objects.requireNonNull(CoreUtils.plugin().getServer().getScoreboardManager().getMainScoreboard().getTeam("lime")).removeEntry(player.getName());
            Objects.requireNonNull(CoreUtils.plugin().getServer().getScoreboardManager().getMainScoreboard().getTeam("green")).removeEntry(player.getName());
            Objects.requireNonNull(CoreUtils.plugin().getServer().getScoreboardManager().getMainScoreboard().getTeam("gray")).addEntry(player.getName());
        }
        if (l == 1) {
            Objects.requireNonNull(CoreUtils.plugin().getServer().getScoreboardManager().getMainScoreboard().getTeam("red")).addEntry(Objects.requireNonNull(player.getName()));
            Objects.requireNonNull(CoreUtils.plugin().getServer().getScoreboardManager().getMainScoreboard().getTeam("yellow")).removeEntry(player.getName());
            Objects.requireNonNull(CoreUtils.plugin().getServer().getScoreboardManager().getMainScoreboard().getTeam("lime")).removeEntry(player.getName());
            Objects.requireNonNull(CoreUtils.plugin().getServer().getScoreboardManager().getMainScoreboard().getTeam("green")).removeEntry(player.getName());
            Objects.requireNonNull(CoreUtils.plugin().getServer().getScoreboardManager().getMainScoreboard().getTeam("gray")).removeEntry(player.getName());
        }
        if (l == 2) {
            Objects.requireNonNull(CoreUtils.plugin().getServer().getScoreboardManager().getMainScoreboard().getTeam("red")).removeEntry(Objects.requireNonNull(player.getName()));
            Objects.requireNonNull(CoreUtils.plugin().getServer().getScoreboardManager().getMainScoreboard().getTeam("yellow")).addEntry(player.getName());
            Objects.requireNonNull(CoreUtils.plugin().getServer().getScoreboardManager().getMainScoreboard().getTeam("lime")).removeEntry(player.getName());
            Objects.requireNonNull(CoreUtils.plugin().getServer().getScoreboardManager().getMainScoreboard().getTeam("green")).removeEntry(player.getName());
            Objects.requireNonNull(CoreUtils.plugin().getServer().getScoreboardManager().getMainScoreboard().getTeam("gray")).removeEntry(player.getName());
        }
        if (l == 3) {
            Objects.requireNonNull(CoreUtils.plugin().getServer().getScoreboardManager().getMainScoreboard().getTeam("red")).removeEntry(Objects.requireNonNull(player.getName()));
            Objects.requireNonNull(CoreUtils.plugin().getServer().getScoreboardManager().getMainScoreboard().getTeam("yellow")).removeEntry(player.getName());
            Objects.requireNonNull(CoreUtils.plugin().getServer().getScoreboardManager().getMainScoreboard().getTeam("lime")).addEntry(player.getName());
            Objects.requireNonNull(CoreUtils.plugin().getServer().getScoreboardManager().getMainScoreboard().getTeam("green")).removeEntry(player.getName());
            Objects.requireNonNull(CoreUtils.plugin().getServer().getScoreboardManager().getMainScoreboard().getTeam("gray")).removeEntry(player.getName());
        }
        if (l > 3) {
            Objects.requireNonNull(CoreUtils.plugin().getServer().getScoreboardManager().getMainScoreboard().getTeam("red")).removeEntry(Objects.requireNonNull(player.getName()));
            Objects.requireNonNull(CoreUtils.plugin().getServer().getScoreboardManager().getMainScoreboard().getTeam("yellow")).removeEntry(player.getName());
            Objects.requireNonNull(CoreUtils.plugin().getServer().getScoreboardManager().getMainScoreboard().getTeam("lime")).removeEntry(player.getName());
            Objects.requireNonNull(CoreUtils.plugin().getServer().getScoreboardManager().getMainScoreboard().getTeam("green")).addEntry(player.getName());
            Objects.requireNonNull(CoreUtils.plugin().getServer().getScoreboardManager().getMainScoreboard().getTeam("gray")).removeEntry(player.getName());
        }
    }

    public void setBoogie(OfflinePlayer player) {
        JSONObject pd = getPlayerData(player);
        pd.put("boogie", true);
        pd.put("last_selected", new Date().getTime());
        setPlayerData(player, pd);
        if(player.isOnline()){
            player.getPlayer().playSound(player.getLocation(), Sound.ENTITY_WITHER_DEATH, SoundCategory.PLAYERS, 10F, 1F);
        }
        if (SettingsManager.getSettings(player).chat() && player.isOnline())
            Objects.requireNonNull(player.getPlayer()).sendMessage(MessageUtils.getMessage("message.boogie.chat"));

    }

    public List<UUID> getPlayers() {
        List<UUID> uids = new ArrayList<>();
        for (String sid : config().getData().keySet()) {
            uids.add(UUID.fromString(sid));
        }
        return uids;

    }
}
