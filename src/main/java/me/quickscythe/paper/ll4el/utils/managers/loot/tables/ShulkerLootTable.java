package me.quickscythe.paper.ll4el.utils.managers.loot.tables;

import json2.JSONObject;
import me.quickscythe.paper.ll4el.utils.managers.loot.LootTable;
import me.quickscythe.paper.ll4el.utils.managers.loot.LootType;
import org.bukkit.DyeColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

public class ShulkerLootTable extends LootTable {

    Map<Integer, ItemStack> contents = new HashMap<>();
    DyeColor color = DyeColor.WHITE;

    public ShulkerLootTable(String name, JSONObject data) {
        super(name, data, LootType.SHULKER);

        if (data.has("dye_color")) {
            color = DyeColor.valueOf(data.getString("dye_color").toUpperCase());
        }

        // Create a new inventory with the name of the loot table
        JSONObject contents = data.getJSONObject("contents");
        int size = contents.length() * 9;
        int rows = size / 9;
        StringBuilder contentsLayout = new StringBuilder();
        for (int i = 0; i < rows; i++) {
            contentsLayout.append(contents.getString(String.valueOf(i + 1)));
        }
        JSONObject items = data.getJSONObject("items");
        Map<Character, ItemStack> itemMap = new HashMap<>();
        for (String key : items.keySet()) {
            char c = key.charAt(0);
            JSONObject itemData = items.getJSONObject(key);
            ItemStack itemStack = generateItemFromData(itemData);
            itemMap.put(c, itemStack);

        }

        //set up the inventory
        for (int i = 0; i < contentsLayout.length(); i++) {
            char c = contentsLayout.charAt(i);
            ItemStack item = itemMap.get(c);
            if (item != null) {
                this.contents.put(i, item);
            }
        }
    }

    public DyeColor getColor() {
        return color;
    }

    public void loadInventory(Inventory inv) {
        for (int i : contents.keySet()) {
            inv.setItem(i, contents.get(i));
        }

    }
}
