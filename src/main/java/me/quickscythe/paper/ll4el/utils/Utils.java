package me.quickscythe.paper.ll4el.utils;

import json2.JSONArray;
import json2.JSONObject;
import me.quickscythe.dragonforge.utils.chat.MessageUtils;
import me.quickscythe.dragonforge.utils.chat.placeholder.PlaceholderUtils;
import me.quickscythe.dragonforge.utils.gui.GuiInventory;
import me.quickscythe.dragonforge.utils.gui.GuiItem;
import me.quickscythe.dragonforge.utils.gui.GuiManager;
import me.quickscythe.dragonforge.utils.network.WebhookUtils;
import me.quickscythe.dragonforge.utils.storage.DataManager;
import me.quickscythe.paper.ll4el.Initializer;
import me.quickscythe.paper.ll4el.utils.managers.*;
import me.quickscythe.paper.ll4el.utils.managers.loot.LootManager;
import me.quickscythe.paper.ll4el.utils.timers.MainTimer;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.Date;

public class Utils {

    private static Initializer plugin;

    public static void init(Initializer plugin) {
        Utils.plugin = plugin;
        DataManager.registerConfigManager(new BoogieManager(plugin));
        DataManager.registerConfigManager(new PartyManager(plugin));
        DataManager.registerConfigManager(new PlayerManager(plugin));
        LifeManager.start();
        DataManager.registerConfigManager(new LootManager(plugin));

        registerPlaceholders();
        registerGuis();
        registerMessages();

        WebhookUtils.add("deaths", "1316979149541343303", "wPGj4xyv05Hwan6xvuJ4k11jmV3SBVGMAG30-3Hyb7jrRm1xSGYZxUrwoFuhJ6pvLIrL");
        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, new MainTimer(), 0);
    }

    private static void registerPlaceholders(){
        PlaceholderUtils.registerPlaceholder("lives_color", LifeManager::getLifeColor);

        PlaceholderUtils.registerPlaceholder("setting_particles", (player)-> SettingsManager.getSettings(player).particles() ? "&aon" : "&coff");
        PlaceholderUtils.registerPlaceholder("setting_icon", (player)-> SettingsManager.getSettings(player).icon() ? "&aon" : "&coff");
        PlaceholderUtils.registerPlaceholder("setting_chat", (player)-> SettingsManager.getSettings(player).chat() ? "&aon" : "&coff");
        PlaceholderUtils.registerPlaceholder("setting_chat", (player)-> SettingsManager.getSettings(player).chat() ? "&aon" : "&coff");
        PlaceholderUtils.registerPlaceholder("party", (player)-> DataManager.getConfigManager("players", PlayerManager.class).getParty(player).equalsIgnoreCase("none") ? "" : DataManager.getConfigManager("players", PlayerManager.class).getParty(player));
        PlaceholderUtils.registerPlaceholder("party_tag", (player)-> DataManager.getConfigManager("players", PlayerManager.class).getParty(player).equalsIgnoreCase("none") ? "&f" : "[" + DataManager.getConfigManager("players", PlayerManager.class).getParty(player) + "]");

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
        MessageUtils.addMessage("cmd.error.player_only","&cSorry, that is a player only command.");
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
    }
    private static void registerGuis() {
        registerSettingsGui();
        registerStatusGui();

    }

    private static void registerStatusGui() {
        GuiInventory inv = new GuiInventory("status", "&e&lStatus Menu", 27, "XXXXXXXXX" + "XAXXBXXCX" + "XXXXXXXXX");
        GuiItem party = new GuiItem("A");
        party.setMaterial(Material.STICK).setCustomModelData(109).setDisplayName("&a&lParty").setLore("&7Search by party");
        party.addAction(new JSONObject().put("action", "command").put("command", "status party"));
        inv.addItem(party);

        GuiItem boogie = new GuiItem("B");
        boogie.setMaterial(Material.STICK).setCustomModelData(110).setDisplayName("&c&lBoogie").setLore("&7Search for boogies");
        boogie.addAction(new JSONObject().put("action", "command").put("command", "status boogie"));
        inv.addItem(boogie);

        GuiItem player = new GuiItem("C");
        player.setMaterial(Material.STICK).setCustomModelData(110).setDisplayName("&e&lPlayer").setLore("&7List all players");
        player.addAction(new JSONObject().put("action", "command").put("command", "status player"));
        inv.addItem(player);

        inv.addItem(new GuiItem("X").setMaterial(Material.AIR));

        GuiManager.registerGui(inv);

    }

    private static void registerSettingsGui() {
        GuiInventory inv = new GuiInventory("settings", "&a&lSettings", 27, "XXXXXXXXXXXAXBXCXXXXXXZXXXX");

        GuiItem partSetting = getParticleItem();
        inv.addItem(partSetting);

        GuiItem iconSetting = getIconItem();
        inv.addItem(iconSetting);

        GuiItem chatSetting = getChatItem();
        inv.addItem(chatSetting);

        GuiItem closeInv = getCloseMenuItem();
        inv.addItem(closeInv);

        GuiItem air = new GuiItem("X");
        air.setMaterial(Material.AIR);
        air.setDisplayName("");
        inv.addItem(air);

        GuiManager.registerGui(inv);
    }

    private static GuiItem getCloseMenuItem() {
        GuiItem close = new GuiItem("Z");
        close.setMaterial(Material.BARRIER);
        close.setDisplayName("&c&lClose Menu");
        JSONArray closeArray = new JSONArray();
        JSONObject closeAction1 = new JSONObject();
        closeAction1.put("action", "close_gui");
        closeArray.put(closeAction1);
        close.setActions(closeArray);
        return close;
    }

    private static GuiItem getChatItem() {
        GuiItem setting = new GuiItem("C");
        setting.setMaterial(Material.STICK);
        setting.setCustomModelData(102);
        setting.setDisplayName("&cToggle the 'Boogie Message'");
        setting.setLore("&7&oCurrent status: %setting_chat%");
        setting.addAction(new JSONObject().put("action", "command").put("command", "settings chat"));
        setting.addAction(new JSONObject().put("action", "command").put("command", "settings"));
        return setting;
    }

    private static GuiItem getIconItem() {
        GuiItem setting = new GuiItem("B");
        setting.setMaterial(Material.STICK);
        setting.setCustomModelData(101);
        setting.setDisplayName("&cToggle Boogie Icon");
        setting.setLore("&7&oCurrent status: %setting_icon%");
        setting.addAction(new JSONObject().put("action", "command").put("command", "settings icon"));
        setting.addAction(new JSONObject().put("action", "command").put("command", "settings"));
        return setting;
    }

    private static GuiItem getParticleItem() {
        GuiItem setting = new GuiItem("A");
        setting.setMaterial(Material.STICK);
        setting.setCustomModelData(100);
        setting.setDisplayName("&aToggle Boogie Particles");
        setting.setLore("&7&oCurrent status: %setting_particles%");
        setting.addAction(new JSONObject().put("action", "command").put("command", "settings particle"));
        setting.addAction(new JSONObject().put("action", "command").put("command", "settings"));
        return setting;
    }

    public static Initializer plugin() {
        return plugin;
    }
}
