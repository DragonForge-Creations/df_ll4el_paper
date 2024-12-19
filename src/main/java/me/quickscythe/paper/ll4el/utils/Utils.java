package me.quickscythe.paper.ll4el.utils;

import me.quickscythe.dragonforge.utils.chat.MessageUtils;
import me.quickscythe.dragonforge.utils.chat.placeholder.PlaceholderUtils;
import me.quickscythe.dragonforge.utils.storage.DataManager;
import me.quickscythe.paper.ll4el.Initializer;
import me.quickscythe.paper.ll4el.utils.managers.*;
import me.quickscythe.paper.ll4el.utils.managers.loot.LootManager;
import me.quickscythe.paper.ll4el.utils.managers.party.Party;
import me.quickscythe.paper.ll4el.utils.managers.party.PartyManager;
import me.quickscythe.paper.ll4el.utils.timers.MainTimer;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

public class Utils {

    private static Initializer plugin;

    public static void init(Initializer plugin) {
        Utils.plugin = plugin;
        DataManager.registerConfigManager(new BoogieManager(plugin));
        DataManager.registerConfigManager(new PartyManager(plugin));
        DataManager.registerConfigManager(new PlayerManager(plugin));
        LifeManager.start();
        DataManager.registerConfigManager(new LootManager(plugin));
        DataManager.registerConfigManager(new WebhookManager(plugin));
        DonorDriveApi.start();

        registerPlaceholders();
        registerMessages();

        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, new MainTimer(), 0);
    }

    private static void registerPlaceholders() {
        PlaceholderUtils.registerPlaceholder("lives_color", LifeManager::getLifeColor);

        PlaceholderUtils.registerPlaceholder("setting_particles", (player) -> SettingsManager.getSettings(player).particles() ? "&aon" : "&coff");
        PlaceholderUtils.registerPlaceholder("setting_icon", (player) -> SettingsManager.getSettings(player).icon() ? "&aon" : "&coff");
        PlaceholderUtils.registerPlaceholder("setting_chat", (player) -> SettingsManager.getSettings(player).chat() ? "&aon" : "&coff");
        PlaceholderUtils.registerPlaceholder("setting_chat", (player) -> SettingsManager.getSettings(player).chat() ? "&aon" : "&coff");
        PlaceholderUtils.registerPlaceholder("party", (player) -> DataManager.getConfigManager("players", PlayerManager.class).getParty(player).equalsIgnoreCase("none") ? "" : DataManager.getConfigManager("players", PlayerManager.class).getParty(player));
        PlaceholderUtils.registerPlaceholder("party_tag", (player) -> DataManager.getConfigManager("players", PlayerManager.class).getParty(player).equalsIgnoreCase("none") ? "&f" : "[" + DataManager.getConfigManager("players", PlayerManager.class).getParty(player) + "]");
        PlaceholderUtils.registerPlaceholder("party_color", (player) -> {
            PlayerManager playerManager = DataManager.getConfigManager("players", PlayerManager.class);
            PartyManager partyManager = DataManager.getConfigManager("parties", PartyManager.class);
            Party party = partyManager.getParty(playerManager.getParty(player));
            return playerManager.getParty(player).equalsIgnoreCase("none") ? "&f" : "" + ChatColor.of(party.getColor());
        });

    }

    private static void registerMessages() {
        MessageUtils.addMessage("message.boogie.chat", "&cYou are a Boogie! Kill someone fast to get rid of this effect!");
        MessageUtils.addMessage("message.boogie.countdown.4", "&c&lBoogie will be selected in...");
        MessageUtils.addMessage("message.boogie.countdown.3", "&c&l3...");
        MessageUtils.addMessage("message.boogie.countdown.2", "&c&l2...");
        MessageUtils.addMessage("message.boogie.countdown.1", "&c&l1...");
        MessageUtils.addMessage("message.boogie.countdown.0", "&c&lYou are...");
        MessageUtils.addMessage("message.boogie.countdown.boogie", "&c&la Boogie!");
        MessageUtils.addMessage("message.boogie.countdown.not", "&a&lNOT a Boogie!");
        MessageUtils.addMessage("message.boogie.cured", "&aYou've been cured!");
        MessageUtils.addMessage("action.elimination", "[0] has been eliminated[1]!");
        MessageUtils.addMessage("cmd.error.player_only", "&cSorry, that is a player only command.");
        MessageUtils.addMessage("message.lives.more", "&aYou've gained [0] lives.");
        MessageUtils.addMessage("cmd.loot.create.success", "&aSuccessfully created [0] loot drop at [1].");
        MessageUtils.addMessage("cmd.life.edit.success", "&aSuccessfully edited the lives of [0].");
        MessageUtils.addMessage("cmd.boogie.set.success", "&a[0] is now a boogie.");
        MessageUtils.addMessage("cmd.boogie.roll", "&aNow rolling for [0] boogie(s).");
        MessageUtils.addMessage("cmd.boogie.remove.success", "&a[0] is no longer a boogie.");
        MessageUtils.addMessage("cmd.party.join.other", "&a[0] is now in the [1] party.");
        MessageUtils.addMessage("party.join.success", "&aYou have joined the [0] party.");
        MessageUtils.addMessage("cmd.party.create", "&aSuccessfully created [0] party.");
        MessageUtils.addMessage("party.chat.join", "&7Party chat: &aon&7.");
        MessageUtils.addMessage("party.chat.leave", "&7Party chat: &coff&7.");
        MessageUtils.addMessage("party.chat.no_party", "&cYou aren't in a party.");
        MessageUtils.addMessage("error.party.no_party", "&c\"[0]\" doesn't seem to exist. Check your spelling and try again.");
        MessageUtils.addMessage("cmd.boogie.remove.player", "&a[0] is no longer a boogie.");
        MessageUtils.addMessage("cmd.loot.create", "&7Punch a block to set loot drop location.");
        MessageUtils.addMessage("cmd.boogie.remove.all", "&aAll boogies have been removed.");
        MessageUtils.addMessage("loot.create", "&aSuccessfully created loot drop for [0].");
        MessageUtils.addMessage("cmd.link.success", "&aSuccessfully linked [0] to [1].");
        MessageUtils.addMessage("cmd.loot.drop", "&aDropping [0] loot at [1].");
        MessageUtils.addMessage("cmd.loot.drop.random", "&aDropping random loot.");

    }




    public static Initializer plugin() {
        return plugin;
    }
}
