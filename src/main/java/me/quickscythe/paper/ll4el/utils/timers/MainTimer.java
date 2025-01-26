package me.quickscythe.paper.ll4el.utils.timers;

import me.quickscythe.dragonforge.utils.sessions.SessionManager;
import me.quickscythe.dragonforge.utils.storage.DataManager;
import me.quickscythe.paper.ll4el.utils.donations.DonorDriveApi;
import me.quickscythe.paper.ll4el.utils.Utils;
import me.quickscythe.paper.ll4el.utils.managers.BoogieManager;
import me.quickscythe.paper.ll4el.utils.managers.PlayerManager;
import me.quickscythe.paper.ll4el.utils.managers.SettingsManager;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static net.kyori.adventure.text.Component.text;

public class MainTimer implements Runnable {

    private final PlayerManager playerManager;
    protected Particle.DustOptions dustoptions = new Particle.DustOptions(Color.RED, 1);
    private long lastApiUpdate = 0;
    private long lastBoogieUpdate = 0;
    private boolean boogieStarted = false;

    private Optional<Long> lastBoogieForceUpdate =Optional.empty();

    public MainTimer() {
        this.playerManager = (PlayerManager) DataManager.getConfigManager("players");

    }

    @Override
    public void run() {
        long now = System.currentTimeMillis();
        lastBoogieForceUpdate.ifPresent(aLong -> lastBoogieUpdate = aLong);
        if (now - lastApiUpdate >= TimeUnit.MILLISECONDS.convert(DonorDriveApi.getDonorTime(), TimeUnit.MINUTES)) {
            lastApiUpdate = now;
            DonorDriveApi.processDonations();
        }

        if(SessionManager.session().started() > 0){
            if(!boogieStarted){
                boogieStarted = true;
                lastBoogieUpdate = now;
            } else {
                if (now - lastBoogieUpdate >= TimeUnit.MILLISECONDS.convert(DonorDriveApi.getBoogieTimer(), TimeUnit.MINUTES)) {
                    lastBoogieUpdate = now;
                    Bukkit.getScheduler().runTaskLater(Utils.plugin(), DonorDriveApi::rollBoogies, 0);
                }
            }
        }

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (playerManager.isBoogie(player)) {
                SettingsManager.Settings settings = SettingsManager.getSettings(player);
                if (settings.boogie().equalsIgnoreCase("both") || settings.boogie().equalsIgnoreCase("particle"))
                    player.spawnParticle(Particle.DUST, player.getLocation(), 1, dustoptions);
                if (settings.boogie().equalsIgnoreCase("both") || settings.boogie().equalsIgnoreCase("icon"))
                    player.sendActionBar(text("                                                                                          \ue001"));
                if (playerManager.getLives(player) < 1) playerManager.removeBoogie(player);
            }
        }
        Bukkit.getScheduler().runTaskLaterAsynchronously(Utils.plugin(), this, 0);
    }

    public void forceUpdateBoogies(long time){
        lastBoogieForceUpdate = Optional.of(time);
    }
}
