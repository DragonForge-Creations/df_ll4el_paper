package me.quickscythe.paper.ll4el.utils.managers.loot;

import json2.JSONObject;
import me.quickscythe.dragonforge.utils.CoreUtils;
import me.quickscythe.dragonforge.utils.config.ConfigFile;
import me.quickscythe.dragonforge.utils.config.ConfigFileManager;
import me.quickscythe.dragonforge.utils.storage.ConfigManager;
import me.quickscythe.paper.ll4el.utils.Utils;
import me.quickscythe.paper.ll4el.utils.managers.loot.tables.CommonLootTable;
import me.quickscythe.paper.ll4el.utils.managers.loot.tables.ShulkerLootTable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class LootManager extends ConfigManager {

    private final Map<String, LootTable> tables_map = new HashMap<>();
    private final Map<UUID, String> editing = new HashMap<>();
    private String last_drop = null;

    public LootManager(JavaPlugin plugin) {
        super(plugin, "loot");
    }

    public void start() {
        super.start();
        ConfigFile tables = ConfigFileManager.getFile(Utils.plugin(), "loot_tables");
        if (tables.getData().has("shulkers")) {
            JSONObject shulkers = tables.getData().getJSONObject("shulkers");
            for (String s : shulkers.keySet()) {
                tables_map.put(s, new ShulkerLootTable(s, shulkers.getJSONObject(s)));
            }
        }
        for (String s : tables.getData().keySet()) {
            if (s.equalsIgnoreCase("shulkers")) continue;
            tables_map.put(s, new CommonLootTable(s, tables.getData().getJSONObject(s)));
        }
    }

    public void createDrop(String name, Location location) {
        config().getData().put(name, CoreUtils.encryptLocation(location));
    }

    public LootTable dropLoot(String locationName, LootType type) {
        Location drop = CoreUtils.decryptLocation(config().getData().getString(locationName));
        if (!drop.isChunkLoaded()) {
            drop.getChunk().load();
        }
        if (type.equals(LootType.SHULKER)) {
            String table_name = getRandomTableName();
            while (table_name.equalsIgnoreCase("common")) table_name = getRandomTableName();
            ShulkerLootTable table = (ShulkerLootTable) tables_map.get(table_name);
            drop.getBlock().setType(Material.valueOf(table.getColor().name() + "_SHULKER_BOX"));
            ShulkerBox box = (ShulkerBox) drop.getBlock().getState();
            Inventory inv = box.getInventory();
            table.loadInventory(inv);
            CoreUtils.logger().log("LootManager", "Dropped " + table_name + " loot at " + locationName);
            return table;
        }
        int r = 25;

        for (int x = 0; x < r; x++) {
            for (int z = 0; z < r; z++) {
                for (int y = 0; y < r; y++) {
                    Location loc = drop.clone().add(x - r / 2D, y - r / 2D, z - r / 2D);
                    if (loc.getBlock().getState() instanceof Container container) {
                        if (container instanceof ShulkerBox) continue;
                        tables_map.get("common").loadInventory(container.getInventory());
                        CoreUtils.logger().log("LootManager", "Dropped common loot at " + locationName);
                    }
                }
            }
        }
        return tables_map.get("common");
    }

    public void startEditing(Player player, String location) {
        editing.put(player.getUniqueId(), location);
    }

    public void finishEditing(Player player) {
        editing.remove(player.getUniqueId());
    }

    public boolean isEditing(Player player) {
        return editing.containsKey(player.getUniqueId());
    }

    public String getEditingLocation(Player player) {

        return editing.getOrDefault(player.getUniqueId(), null);
    }

    public void randomDrop(LootType type) {
        String locationName = getRandomLocationName();
        int i = 0;
        while (locationName.equals(last_drop)) {
            locationName = getRandomLocationName();
            i = i + 1;
            if (i > 5) break;
        }
        last_drop = locationName;
        dropLoot(locationName, type);
    }

    private String getRandomTableName() {
        return tables_map.keySet().stream().skip(new Random().nextInt(tables_map.size())).findFirst().get();
    }

    private String getRandomLocationName() {
        return new ArrayList<>(config().getData().keySet()).get(new Random().nextInt(config().getData().keySet().size()));
    }
}
