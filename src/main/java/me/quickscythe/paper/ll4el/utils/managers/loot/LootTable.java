package me.quickscythe.paper.ll4el.utils.managers.loot;


import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import json2.JSONObject;
import org.bukkit.Material;
import org.bukkit.MusicInstrument;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MusicInstrumentMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionType;

import java.util.Collections;

import static net.kyori.adventure.text.Component.text;

public abstract class LootTable {

    private final LootType type;
    private final String name;
    private final JSONObject data;

    public LootTable(String name, JSONObject data, LootType type) {
        this.type = type;
        this.name = name;
        this.data = data;
    }

    public String name() {
        return name;
    }

    public LootType type() {
        return type;
    }

    public JSONObject data() {
        return data;
    }

    public abstract void loadInventory(Inventory inv);

    public ItemStack generateItemFromData(JSONObject itemData) {
        ItemStack itemStack = new ItemStack(Material.valueOf(itemData.getString("item").toUpperCase()));
        itemStack.setAmount(itemData.has("amount") ? itemData.getInt("amount") : 1);
        if (itemData.has("components")) {
            System.out.println(itemData.getJSONObject("components").toString(2));
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
                    case "stored_enchantments":
                        JSONObject levels = ((JSONObject) value).getJSONObject("levels");
                        for (String key : levels.keySet()) {
                            Enchantment enchantment = RegistryAccess.registryAccess().getRegistry(RegistryKey.ENCHANTMENT).get(NamespacedKey.minecraft(key));
                            assert enchantment != null;
                            meta.addEnchant(enchantment, levels.getInt(key), true);
                        }
                        break;
                    case "sound_data":
                        JSONObject sound = ((JSONObject) value).getJSONObject("sound");
                        if (itemStack.getType().equals(Material.GOAT_HORN)) {
                            MusicInstrumentMeta musicMeta = (MusicInstrumentMeta) meta;
                            MusicInstrument instrument = Registry.INSTRUMENT.get(NamespacedKey.minecraft(sound.getString("instrument")));
                            musicMeta.setInstrument(instrument);

                        }
                        break;
                    case "tipped_arrow":
                        JSONObject potionData = ((JSONObject) value).getJSONObject("potion");
                        if (itemStack.getType().equals(Material.TIPPED_ARROW)) {
                            PotionMeta potionMeta = (PotionMeta) meta;
                            PotionType potionType = Registry.POTION.get(NamespacedKey.minecraft(potionData.getString("type")));
                            potionMeta.setBasePotionType(potionType);
                        }
                        break;
                }
            }
            itemStack.setItemMeta(meta);
        }
        return itemStack;
    }


}
