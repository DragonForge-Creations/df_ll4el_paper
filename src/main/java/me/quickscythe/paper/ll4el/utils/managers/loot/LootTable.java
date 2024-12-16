package me.quickscythe.paper.ll4el.utils.managers.loot;


import json2.JSONObject;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;

import static net.kyori.adventure.text.Component.text;

public abstract class LootTable {

    private final LootTableType type;
    private final String name;
    private final JSONObject data;

    public LootTable(String name, JSONObject data, LootTableType type) {
        this.type = type;
        this.name = name;
        this.data = data;
    }

    public String name() {
        return name;
    }

    public LootTableType type() {
        return type;
    }

    public JSONObject data() {
        return data;
    }

    public abstract void loadInventory(Inventory inv);

    public ItemStack generateItemFromData(JSONObject itemData) {
        System.out.println("Getting item from data: \n" + itemData.toString(2));
        ItemStack itemStack = new ItemStack(Material.valueOf(itemData.getString("item").toUpperCase()));
        itemStack.setAmount(itemData.has("amount") ? itemData.getInt("amount") : 1);
        if (itemData.has("components")) {
            ItemMeta meta = itemStack.getItemMeta();
            for (String component : itemData.getJSONObject("components").keySet()) {
                Object value = itemData.getJSONObject("components").get(component);
                switch (component.toLowerCase()) {
                    case "name":
                        meta.displayName(text((String) value));
                        break;
                    case "lore":
                        meta.lore(Collections.singletonList(text((String) value)));
                        break;
                    case "custom_model_data":
                        meta.setCustomModelData((int) value);
                        break;
                }
            }
            itemStack.setItemMeta(meta);
        }
        return itemStack;
    }

    public enum LootTableType {
        SHULKER_BOX, COMMON, UNCOMMON, RARE, EPIC, LEGENDARY, BOSS
    }


}
