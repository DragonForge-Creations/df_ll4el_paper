package me.quickscythe.paper.ll4el.listeners;

import me.quickscythe.dragonforge.utils.chat.ChatManager;
import me.quickscythe.dragonforge.utils.chat.MessageUtils;
import me.quickscythe.dragonforge.utils.chat.placeholder.PlaceholderUtils;
import me.quickscythe.dragonforge.utils.storage.DataManager;
import me.quickscythe.paper.ll4el.Initializer;
import me.quickscythe.paper.ll4el.utils.managers.party.PartyManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener implements Listener {
    public ChatListener(Initializer plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e){

        PartyManager partyManager = (PartyManager) DataManager.getConfigManager("parties");
        if(partyManager.inPartyChat(e.getPlayer())){
            partyManager.handleChat(e.getPlayer(), e.getMessage());
            e.setCancelled(true);
            return;
        }
        e.setFormat(MessageUtils.colorize(PlaceholderUtils.replace(e.getPlayer(), ChatManager.getFormat("player"))) + MessageUtils.colorize(ChatManager.getFormat("chat")).replaceAll("%message%", e.getMessage()));

    }
}
