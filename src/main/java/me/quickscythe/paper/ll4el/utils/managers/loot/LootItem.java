package me.quickscythe.paper.ll4el.utils.managers.loot;

import json2.JSONObject;
import me.quickscythe.dragonforge.utils.chat.MessageUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Random;

public class LootItem {

    JSONObject data;
    public LootItem(JSONObject data) {
        this.data = data;
    }

    public JSONObject getData() {
        return data;
    }

    public ItemStack generateItem() {
        ItemStack item = new ItemStack(Material.valueOf(data.getString("item")));
        ItemMeta meta = item.getItemMeta();
        String amount = data.has("amount") ? (data.get("amount") instanceof Integer ? data.getInt("amount") + "" : data.getString("amount")) : "1-3";
        int min = 0;
        int max = 0;
        if(amount.contains("-")){
            min = Integer.parseInt(amount.split("-")[0]);
            max = Integer.parseInt(amount.split("-")[1]);
        } else {
            min = Integer.parseInt(amount);
            max = min;
        }
        int goal = (min == max ? min : new Random().nextInt(max-min)+min);
        item.setAmount(goal);
        if(data.has("name"))
            meta.displayName(MessageUtils.deserialize(data.getString("name")));
        if(data.has("custom_model_data"))
            meta.setCustomModelData(data.getInt("custom_model_data"));


        item.setItemMeta(meta);
        return item;
    }
}
