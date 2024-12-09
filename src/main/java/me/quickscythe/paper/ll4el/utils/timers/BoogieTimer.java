package me.quickscythe.paper.ll4el.utils.timers;

import me.quickscythe.dragonforge.utils.CoreUtils;
import me.quickscythe.dragonforge.utils.chat.MessageUtils;
import me.quickscythe.dragonforge.utils.storage.DataManager;
import me.quickscythe.paper.ll4el.utils.managers.BoogieManager;
import me.quickscythe.paper.ll4el.utils.managers.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Date;

public class BoogieTimer implements Runnable {

    private final long started = new Date().getTime();
    private int stage = 0;
    private int amount;
    private final PlayerManager playerManager;
    private final BoogieManager boogieManager;


    public BoogieTimer(int amount) {
        this.amount = amount;
        this.playerManager = (PlayerManager) DataManager.getConfigManager("players");
        this.boogieManager = (BoogieManager) DataManager.getConfigManager("boogies");
    }

    @Override
    public void run() {

         if (stage == 5) boogieManager.selectBoogies(amount);
        for (Player player : Bukkit.getOnlinePlayers()) {

            if (stage == 0) player.sendTitle("", MessageUtils.getMessage("message.boogie.countdown.4"), 1, 20, 1);
            if (stage == 1) player.sendTitle("", MessageUtils.getMessage("message.boogie.countdown.3"), 1, 20, 1);
            if (stage == 2) player.sendTitle("", MessageUtils.getMessage("message.boogie.countdown.2"), 1, 20, 1);
            if (stage == 3) player.sendTitle("", MessageUtils.getMessage("message.boogie.countdown.1"), 1, 20, 1);
            if (stage == 4) player.sendTitle("", MessageUtils.getMessage("message.boogie.countdown.0"), 1, 20, 1);
            if (stage == 5)
                player.sendTitle("", playerManager.getPlayerData(player).getBoolean("boogie") ? MessageUtils.getMessage("message.boogie.countdown.boogie") : MessageUtils.getMessage("message.boogie.countdown.not"), 1, 20, 1);
        }
        stage = stage + 1;
        if (stage <= 5) Bukkit.getScheduler().runTaskLaterAsynchronously(CoreUtils.plugin(), this, 22);
    }
}
