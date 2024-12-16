package me.quickscythe.paper.ll4el.utils.managers.loot.tables;

import json2.JSONObject;
import me.quickscythe.paper.ll4el.utils.managers.loot.LootTable;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class CommonLootTable extends LootTable {

    List<ItemStack> contents = new ArrayList<>();

    public CommonLootTable(String name, JSONObject data) {
        super(name, data, LootTableType.COMMON);
        if(data.has("items")){
            for(int i = 0; i < data.getJSONArray("items").length(); i++){
                JSONObject itemData = data.getJSONArray("items").getJSONObject(i);
                contents.add(generateItemFromData(itemData));
                System.out.println(itemData.getString("item"));
            }
        }
    }

    @Override
    public void loadInventory(Inventory inv) {
        //Randomly shuffle the contents into inv
        List<ItemStack> shuffled = new ArrayList<>(contents);
        for(int i = 0; i < inv.getSize(); i++){
            if(shuffled.isEmpty()) break;
            int index = (int) (Math.random() * shuffled.size());
            inv.setItem(i, shuffled.get(index));
            shuffled.remove(index);
        }

    }
}
