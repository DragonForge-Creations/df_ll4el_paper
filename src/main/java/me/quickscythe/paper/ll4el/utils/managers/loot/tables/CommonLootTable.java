package me.quickscythe.paper.ll4el.utils.managers.loot.tables;

import json2.JSONObject;
import me.quickscythe.paper.ll4el.utils.managers.loot.LootTable;
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
            }
        }
    }

    @Override
    public void loadInventory(Inventory inv) {
        //Randomly shuffle the contents into inv
        inv.clear();
        for(ItemStack master: contents){
            ItemStack item = master.clone();
            item.setAmount((int) (Math.random() * master.getAmount()) + 1);
            int slot = (int) (Math.random() * inv.getSize());
            inv.setItem(slot, item);

        }

    }
}
