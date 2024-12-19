package me.quickscythe.paper.ll4el.utils.managers;

import me.quickscythe.dragonforge.utils.CoreUtils;
import me.quickscythe.dragonforge.utils.storage.DataManager;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;
import org.bukkit.scoreboard.Team;

public class LifeManager {


    public static void start() {
        setupTeams();
    }

    public static TextColor getLifeColor(OfflinePlayer player) {
        int lives = ((PlayerManager) DataManager.getConfigManager("players")).getLives(player);
        TextColor color = NamedTextColor.WHITE;
        if (lives > 3) color = NamedTextColor.DARK_GREEN;
        if (lives == 3) color = NamedTextColor.GREEN;
        if (lives == 2) color = NamedTextColor.YELLOW;
        if (lives == 1) color = NamedTextColor.RED;
        if (lives < 1) color = NamedTextColor.GRAY;
        return color;
    }

    private static void setupTeams() {
        ScoreboardManager manager = CoreUtils.plugin().getServer().getScoreboardManager();
        Scoreboard main = manager.getMainScoreboard();
        if (main.getTeam("red") == null) {
            Team red = main.registerNewTeam("red");
            red.color(NamedTextColor.RED);
        }
        if (main.getTeam("yellow") == null) {
            Team red = main.registerNewTeam("yellow");
            red.color(NamedTextColor.YELLOW);
        }

        if (main.getTeam("lime") == null) {
            Team red = main.registerNewTeam("lime");
            red.color(NamedTextColor.GREEN);
        }
        if (main.getTeam("green") == null) {
            Team red = main.registerNewTeam("green");
            red.color(NamedTextColor.DARK_GREEN);
        }
        if (main.getTeam("gray") == null) {
            Team red = main.registerNewTeam("gray");
            red.color(NamedTextColor.GRAY);
        }
    }
}
