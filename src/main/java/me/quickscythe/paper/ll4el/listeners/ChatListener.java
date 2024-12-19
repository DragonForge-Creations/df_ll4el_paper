package me.quickscythe.paper.ll4el.listeners;

import io.papermc.paper.event.player.AsyncChatEvent;
import me.quickscythe.dragonforge.utils.storage.DataManager;
import me.quickscythe.paper.ll4el.Initializer;
import me.quickscythe.paper.ll4el.utils.managers.LifeManager;
import me.quickscythe.paper.ll4el.utils.managers.party.PartyManager;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import static net.kyori.adventure.text.Component.text;

public class ChatListener implements Listener {
    public ChatListener(Initializer plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerChat(AsyncChatEvent e) {

        PartyManager partyManager = (PartyManager) DataManager.getConfigManager("parties");
        if (partyManager.inPartyChat(e.getPlayer())) {
            partyManager.handleChat(e.getPlayer(), e.message());
            e.setCancelled(true);
            return;
        }
        e.setCancelled(true);
        Component playerFormat = text("<", NamedTextColor.WHITE).append(text(e.getPlayer().getName(), LifeManager.getLifeColor(e.getPlayer()))).append(text("> ", NamedTextColor.WHITE));
        Component msg = playerFormat.append(e.message());
        e.getPlayer().getServer().broadcast(msg);

    }
}
