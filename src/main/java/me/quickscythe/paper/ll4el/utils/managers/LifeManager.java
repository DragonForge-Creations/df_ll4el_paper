package me.quickscythe.paper.ll4el.utils.managers;

import me.quickscythe.dragonforge.utils.CoreUtils;
import me.quickscythe.dragonforge.utils.storage.DataManager;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class LifeManager {


    public static void start() {
        setupTeams();
    }

    public static String getLifeColor(OfflinePlayer player) {
        int lives = ((PlayerManager) DataManager.getConfigManager("players")).getLives(player);
        ChatColor color = ChatColor.RESET;
        if (lives > 3) color = ChatColor.DARK_GREEN;
        if (lives == 3) color = ChatColor.GREEN;
        if (lives == 2) color = ChatColor.YELLOW;
        if (lives == 1) color = ChatColor.RED;
        if (lives < 1) color = ChatColor.GRAY;
        return color + "";
    }

    private static void setupTeams() {
        ScoreboardManager manager = CoreUtils.plugin().getServer().getScoreboardManager();
        Scoreboard main = manager.getMainScoreboard();
        if (main.getTeam("red") == null) {
            Team red = main.registerNewTeam("red");
            red.setColor(ChatColor.RED);
        }
        if (main.getTeam("yellow") == null) {
            Team red = main.registerNewTeam("yellow");
            red.setColor(ChatColor.YELLOW);
        }

        if (main.getTeam("lime") == null) {
            Team red = main.registerNewTeam("lime");
            red.setColor(ChatColor.GREEN);
        }
        if (main.getTeam("green") == null) {
            Team red = main.registerNewTeam("green");
            red.setColor(ChatColor.DARK_GREEN);
        }
        if (main.getTeam("gray") == null) {
            Team red = main.registerNewTeam("gray");
            red.setColor(ChatColor.GRAY);
        }

    }


}
