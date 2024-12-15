package me.quickscythe.paper.ll4el.utils.managers.loot;

import me.quickscythe.dragonforge.utils.CoreUtils;
import me.quickscythe.dragonforge.utils.config.ConfigFile;
import me.quickscythe.dragonforge.utils.config.ConfigFileManager;
import me.quickscythe.dragonforge.utils.storage.ConfigManager;
import me.quickscythe.paper.ll4el.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Container;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class LootManager extends ConfigManager {
    static Map<String, LootTable> tables_map = new HashMap<>();

    Map<UUID, String> editing = new HashMap<>();

    public LootManager(JavaPlugin plugin) {
        super(plugin, "loot");
    }

    public void start() {
        super.start();
        ConfigFile tables = ConfigFileManager.getFile(Utils.plugin(), "loot_tables");

        for (String s : tables.getData().keySet()) {
            tables_map.put(s, new LootTable(tables.getData().getJSONObject(s)));
        }
    }

    public LootTable getLootTable(String name) {
        return tables_map.get(name);
    }

    public  void createDrop(String name, Location location) {
        config().getData().put(name, CoreUtils.encryptLocation(location));
    }

    public void dropLoot(String name, LootType type) {
        Location drop = CoreUtils.decryptLocation(config().getData().getString(name));
        switch (type) {
            case DEFAULT -> drop.getBlock().setType(new Random().nextBoolean() ? Material.CHEST : Material.BARREL);
            case SHULKER -> drop.getBlock().setType(Material.SHULKER_BOX);
        }
        Container block = (Container) drop.getBlock().getState();
        Inventory inv = block.getInventory();
        generateLoot(inv, tables_map.get(tables_map.keySet().stream().skip(new Random().nextInt(tables_map.size())).findFirst().get()));

    }

    public void dropLoot(LootType type) {
        dropLoot(config().getData().keySet().stream().skip(new Random().nextInt(config().getData().length())).findFirst().get(), type);

    }

    private void generateLoot(Inventory inv, LootTable table) {
        for (LootItem loot : table.generateItems()) {
            inv.setItem(new Random().nextInt(inv.getSize()), loot.generateItem());
        }
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

    public Location[] getLocations() {
        List<Location> locations = new ArrayList<>();
        config().getData().keySet().forEach(s -> locations.add(CoreUtils.decryptLocation(config().getData().getString(s))));
//        locations.add(CoreUtils.decryptLocation(s)));
        return locations.toArray(new Location[0]);
    }
}
