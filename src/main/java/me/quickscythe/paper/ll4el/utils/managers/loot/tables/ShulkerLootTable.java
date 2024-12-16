package me.quickscythe.paper.ll4el.utils.managers.loot.tables;

import json2.JSONObject;
import me.quickscythe.dragonforge.utils.gui.GuiInventory;
import me.quickscythe.paper.ll4el.utils.managers.loot.LootTable;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ShulkerLootTable extends LootTable {

    Map<Integer, ItemStack> contents = new HashMap<>();

    public ShulkerLootTable(String name, JSONObject data) {
        super(name, data, LootTableType.SHULKER_BOX);


        // Create a new inventory with the name of the loot table
        JSONObject contents = data.getJSONObject("contents");
        int size = contents.length()*9;
        int rows = size/9;
        StringBuilder contentsLayout = new StringBuilder();
        for(int i = 0; i < rows; i++){
            contentsLayout.append(contents.getString(String.valueOf(i + 1)));
        }
        JSONObject items = data.getJSONObject("items");
        Map<Character, ItemStack> itemMap = new HashMap<>();
        for(String key : items.keySet()){
            char c = key.charAt(0);
            JSONObject itemData = items.getJSONObject(key);
            ItemStack itemStack = generateItemFromData(itemData);
            System.out.println("Item: " + itemStack.getType().name());
            itemMap.put(c,itemStack);

        }

        //set up the inventory

        for(int i=0; i<contentsLayout.length(); i++){
            char c = contentsLayout.charAt(i);
            ItemStack item = itemMap.get(c);
            if(item != null){
                this.contents.put(i, item);
            }
        }
    }



    public void loadInventory(Inventory inv) {
        for(int i : contents.keySet()){
            inv.setItem(i, contents.get(i));
        }

    }
}
