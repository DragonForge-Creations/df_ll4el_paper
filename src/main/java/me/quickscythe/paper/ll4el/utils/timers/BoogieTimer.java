package me.quickscythe.paper.ll4el.utils.timers;

import me.quickscythe.dragonforge.utils.CoreUtils;
import me.quickscythe.dragonforge.utils.chat.MessageUtils;
import me.quickscythe.dragonforge.utils.storage.DataManager;
import me.quickscythe.paper.ll4el.utils.managers.BoogieManager;
import me.quickscythe.paper.ll4el.utils.managers.PlayerManager;
import net.kyori.adventure.title.Title;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.time.Duration;
import java.util.Date;

import static net.kyori.adventure.text.Component.text;

public class BoogieTimer implements Runnable {

    private final long started = new Date().getTime();
    private final int amount;
    private final PlayerManager playerManager;
    private final BoogieManager boogieManager;
    private int stage = 0;


    public BoogieTimer(int amount) {
        this.amount = amount;
        this.playerManager = (PlayerManager) DataManager.getConfigManager("players");
        this.boogieManager = (BoogieManager) DataManager.getConfigManager("boogies");
    }

    @Override
    public void run() {

        if (stage == 5) boogieManager.selectBoogies(amount);
        for (Player player : Bukkit.getOnlinePlayers()) {

            if (stage == 5)
                player.showTitle(Title.title(text(""), playerManager.getPlayerData(player).getBoolean("boogie") ? MessageUtils.getMessage("message.boogie.countdown.boogie") : MessageUtils.getMessage("message.boogie.countdown.not"), Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(20), Duration.ofSeconds(1))));
            else
                player.showTitle(Title.title(text(""), MessageUtils.getMessage("message.boogie.countdown." + (4 - stage)), Title.Times.times(Duration.ofSeconds(1), Duration.ofSeconds(20), Duration.ofSeconds(1))));
        }
        stage = stage + 1;
        if (stage <= 5) Bukkit.getScheduler().runTaskLaterAsynchronously(CoreUtils.plugin(), this, 22);
    }
}
