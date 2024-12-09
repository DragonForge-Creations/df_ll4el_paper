package me.quickscythe.paper.ll4el.utils;

import json2.JSONArray;
import json2.JSONObject;
import me.quickscythe.dragonforge.utils.gui.GuiInventory;
import me.quickscythe.dragonforge.utils.gui.GuiItem;
import me.quickscythe.dragonforge.utils.gui.GuiManager;
import me.quickscythe.dragonforge.utils.storage.DataManager;
import me.quickscythe.paper.ll4el.Initializer;
import me.quickscythe.paper.ll4el.utils.managers.BoogieManager;
import me.quickscythe.paper.ll4el.utils.managers.LifeManager;
import me.quickscythe.paper.ll4el.utils.managers.PartyManager;
import me.quickscythe.paper.ll4el.utils.managers.PlayerManager;
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

        registerGuis();

        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, new MainTimer(), 0);
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
