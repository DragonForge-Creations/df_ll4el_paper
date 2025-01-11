package me.quickscythe.paper.ll4el.listeners;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import me.quickscythe.dragonforge.utils.chat.MessageUtils;
import me.quickscythe.paper.ll4el.utils.EntitySkullData;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.UUID;

import static net.kyori.adventure.text.Component.text;

public class EntityListener implements Listener {

    public EntityListener(JavaPlugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        Entity attacker = e.getDamageSource().getCausingEntity();

        if (!(attacker instanceof Player attackingPlayer)) return;
        //todo: do some math magic to calculate the chance of a head drop
        double chance = 0.1;
        if(attackingPlayer.getInventory().getItemInMainHand().containsEnchantment(Enchantment.LOOTING)){
            chance += attackingPlayer.getInventory().getItemInMainHand().getEnchantmentLevel(Enchantment.LOOTING) * 0.05;
        }
        if(e.getEntity() instanceof Player) chance = 1;
        if (Math.random() < chance) {
            EntitySkullData skullData = EntitySkullData.fromEntity(e.getEntity());
            ItemStack skull;
            switch (skullData) {
                case ZOMBIE -> skull = new ItemStack(Material.ZOMBIE_HEAD);
                case SKELETON -> skull = new ItemStack(Material.SKELETON_SKULL);
                case CREEPER -> skull = new ItemStack(Material.CREEPER_HEAD);
                case WITHER_SKELETON -> skull = new ItemStack(Material.WITHER_SKELETON_SKULL);
                case PLAYER -> {
                    Player player = (Player) e.getEntity();
                    skull = new ItemStack(Material.PLAYER_HEAD);
                    SkullMeta meta = (SkullMeta) skull.getItemMeta();
                    meta.setOwningPlayer(player);
                    String name = player.getName();
                    meta.displayName(text(name + (name.endsWith("s") ? "'" : "'s") + " Head", NamedTextColor.YELLOW));
                    meta.lore(Collections.singletonList(text("Killed by " + attackingPlayer.getName(), NamedTextColor.GRAY)));
                    skull.setItemMeta(meta);
                }
                default -> skull = getSkull(skullData);
            }
            if(skull != null && (!(e.getEntity() instanceof Player))){
                SkullMeta meta = (SkullMeta) skull.getItemMeta();
                meta.itemName(text((e.getEntity().customName() == null ? skullData.itemName() : MessageUtils.plainText(e.getEntity().customName())) + "'s Head", NamedTextColor.YELLOW));
                meta.lore(Collections.singletonList(text("Killed by " + attackingPlayer.getName(), NamedTextColor.GRAY)));
                skull.setItemMeta(meta);
            }
            e.getDrops().add(skull);
        }

    }

    public ItemStack getSkull(EntitySkullData data) {
        final ItemStack skull = new ItemStack(Material.PLAYER_HEAD);
        SkullMeta meta = (SkullMeta) skull.getItemMeta();
        assert meta != null;
        GameProfile profile = new GameProfile(UUID.randomUUID(), System.currentTimeMillis() + "L");
        profile.getProperties().put("textures", new Property("textures", data.texture()));
        try {
            Field profileField = meta.getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            profileField.set(meta, profile);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        skull.setItemMeta(meta);
        return skull;
    }
}
