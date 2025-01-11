package me.quickscythe.paper.ll4el.utils;

import me.quickscythe.dragonforge.utils.CoreUtils;
import me.quickscythe.dragonforge.utils.chat.MessageUtils;
import me.quickscythe.dragonforge.utils.chat.placeholder.PlaceholderUtils;
import me.quickscythe.dragonforge.utils.items.ItemUtils;
import me.quickscythe.dragonforge.utils.sessions.SessionManager;
import me.quickscythe.dragonforge.utils.storage.DataManager;
import me.quickscythe.paper.ll4el.Initializer;
import me.quickscythe.paper.ll4el.utils.donations.DonorDriveApi;
import me.quickscythe.paper.ll4el.utils.managers.BoogieManager;
import me.quickscythe.paper.ll4el.utils.managers.LifeManager;
import me.quickscythe.paper.ll4el.utils.managers.PlayerManager;
import me.quickscythe.paper.ll4el.utils.managers.loot.LootManager;
import me.quickscythe.paper.ll4el.utils.managers.party.PartyManager;
import me.quickscythe.paper.ll4el.utils.timers.MainTimer;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import static net.kyori.adventure.text.Component.text;

public class Utils {

    private static Initializer plugin;

    public static void init(Initializer plugin) {
        Utils.plugin = plugin;
        DataManager.registerConfigManager(new PlayerManager(plugin));
        DataManager.registerConfigManager(new BoogieManager(plugin));
        DataManager.registerConfigManager(new PartyManager(plugin));

        LifeManager.start();
        DataManager.registerConfigManager(new LootManager(plugin));
        DonorDriveApi.start();

        registerPlaceholders();
        registerMessages();
        registerRecipes();

//        CoreUtils.packServer().setUrl("https://ci.vanillaflux.com/view/DragonForge%20Creations/job/df_ll4el_resources/lastSuccessfulBuild/artifact/resources.zip");

//        CoreUtils.packServer().setUrl("https://github.com/DragonForge-Creations/df_ll4el_resources");

        SessionManager.session().end(() -> {
            if (SessionManager.session().task().isPresent() && SessionManager.session().reward().isPresent()) {
                CoreUtils.logger().log("Utils", "Task complete!");
                Advancement advancement = SessionManager.session().task().get();
                ItemStack reward = SessionManager.session().reward().get();
                for (Player player : Bukkit.getOnlinePlayers()) {
                    AdvancementProgress progress = player.getAdvancementProgress(advancement);
                    if (progress.isDone()) {
                        player.sendMessage(MessageUtils.getMessage("message.task.complete", MessageUtils.plainText(reward.displayName())));
                        if (!ItemUtils.inventoryHasRoom(player.getInventory(), reward))
                            player.getWorld().dropItem(player.getLocation(), reward);
                        else player.getInventory().addItem(reward);
                    }
                }
            }
        });

        Bukkit.getScheduler().runTaskLaterAsynchronously(plugin, new MainTimer(), 0);
    }

    private static void registerRecipes() {
        NamespacedKey bundleKey = new NamespacedKey(plugin, "bundle");
        ItemStack bundle = new ItemStack(Material.BUNDLE);

        ShapedRecipe bundleRecipe = new ShapedRecipe(bundleKey, bundle);
        bundleRecipe.shape(" A ", " B ", "   ");
        bundleRecipe.setIngredient('A', Material.STRING);
        bundleRecipe.setIngredient('B', Material.LEATHER);

        plugin.getServer().addRecipe(bundleRecipe);
    }

    private static void registerPlaceholders() {
        PlaceholderUtils.registerPlaceholder("party", (player) -> DataManager.getConfigManager("players", PlayerManager.class).getParty(player).equalsIgnoreCase("none") ? "" : DataManager.getConfigManager("players", PlayerManager.class).getParty(player));
    }

    private static void registerMessages() {
        MessageUtils.addMessage("message.boogie.chat", "{\"text\":\"You are a Boogie! Kill someone fast to get rid of this effect!\",\"color\":\"red\"}");
        MessageUtils.addMessage("message.boogie.countdown.4", "{\"text\":\"Boogie will be selected in...\",\"bold\":true,\"color\":\"red\"}");
        MessageUtils.addMessage("message.boogie.countdown.3", "{\"text\":\"3...\",\"color\":\"red\",\"bold\":true}");
        MessageUtils.addMessage("message.boogie.countdown.2", "{\"text\":\"2...\",\"color\":\"red\",\"bold\":true}");
        MessageUtils.addMessage("message.boogie.countdown.1", "{\"text\":\"1...\",\"color\":\"red\",\"bold\":true}");
        MessageUtils.addMessage("message.boogie.countdown.0", "{\"text\":\"You are...\",\"color\":\"red\",\"bold\":true}");
        MessageUtils.addMessage("message.boogie.countdown.boogie", "{\"text\":\"a Boogie!\",\"color\":\"red\",\"bold\":true}");
        MessageUtils.addMessage("message.boogie.countdown.not", "{\"text\":\"NOT a Boogie!\",\"color\":\"green\",\"bold\":true}");
        MessageUtils.addMessage("message.boogie.cured", "{\"text\":\"You've been cured!\",\"color\":\"green\"}");
        MessageUtils.addMessage("action.elimination", "{\"text\":\"[0] has been eliminated[1]!\"}");
        MessageUtils.addMessage("message.lives.more", "{\"text\":\"You've gained [0] lives.\",\"color\":\"green\"}");
        MessageUtils.addMessage("cmd.loot.create.success", "{\"text\":\"Successfully created [0] loot drop at [1].\",\"color\":\"green\"}");
        MessageUtils.addMessage("cmd.loot.drop", "{\"text\":\"Dropping [0] loot at [1].\",\"color\":\"green\"}");
        MessageUtils.addMessage("cmd.loot.drop.random", "{\"text\":\"Dropping random loot.\",\"color\":\"green\"}");
        MessageUtils.addMessage("cmd.link.success", "{\"text\":\"Successfully linked [0] to [1].\",\"color\":\"green\"}");
        MessageUtils.addMessage("cmd.life.edit.success", "{\"text\":\"Successfully edited the lives of [0].\",\"color\":\"green\"}");
        MessageUtils.addMessage("cmd.life.add", "{\"text\":\"Added [0] lives to [1].\",\"color\":\"green\"}");
        MessageUtils.addMessage("cmd.life.usage", "{\"text\":\"Usage: /life <set|add|remove> <player> [amount=1]\",\"color\":\"gray\"}");
        MessageUtils.addMessage("cmd.boogie.set.success", "{\"text\":\"[0] is now a boogie.\",\"color\":\"green\"}");
        MessageUtils.addMessage("cmd.boogie.remove.player", "{\"text\":\"[0] is no longer a boogie.\",\"color\":\"green\"}");
        MessageUtils.addMessage("cmd.boogie.roll", "{\"text\":\"Now rolling for [0] boogie(s).\",\"color\":\"green\"}");
        MessageUtils.addMessage("cmd.boogie.remove.all", "{\"text\":\"All boogies have been removed.\",\"color\":\"green\"}");
        MessageUtils.addMessage("cmd.party.create", "{\"text\":\"Successfully created [0] party.\",\"color\":\"green\"}");
        Component settingsSet = text("").append(text("Successfully set ", NamedTextColor.GREEN)).append(text("[0]", NamedTextColor.GRAY)).append(text(" to ", NamedTextColor.GREEN)).append(text("[1]", NamedTextColor.GRAY)).append(text(".", NamedTextColor.GREEN));
        MessageUtils.addMessage("cmd.settings.set", settingsSet);
//        MessageUtils.addMessage("cmd.settings.set", "{\"text\":\"Successfully set [0] to [1].\",\"color\":\"green\"}");
        MessageUtils.addMessage("party.join.success", "{\"text\":\"You have joined the [0] party.\",\"color\":\"green\"}");
        MessageUtils.addMessage("cmd.party.join.other", "{\"text\":\"[0] is now in the [1] party.\",\"color\":\"green\"}");
        MessageUtils.addMessage("party.chat.join", "{\"text\":\"Party chat: \",\"color\":\"gray\",\"extra\":[{\"text\":\"on\",\"color\":\"green\"}]}");
        MessageUtils.addMessage("party.chat.leave", "{\"text\":\"Party chat: \",\"color\":\"gray\",\"extra\":[{\"text\":\"off\",\"color\":\"red\"}]}");
        MessageUtils.addMessage("party.chat.no_party", "{\"text\":\"You aren't in a party.\",\"color\":\"red\"}");
        MessageUtils.addMessage("error.party.no_party", "{\"text\":\"[0] doesn't seem to exist. Check your spelling and try again.\",\"color\":\"red\"}");
        MessageUtils.addMessage("cmd.loot.create", "{\"text\":\"Punch a block to set loot drop location.\",\"color\":\"gray\"}");
        MessageUtils.addMessage("loot.create", "{\"text\":\"Successfully created loot drop for [0].\",\"color\":\"green\"}");
        MessageUtils.addMessage("message.task.complete", text().append(text("Congratulations on completing this week's task! Your reward is ", TextColor.color(0x14CBDB)).append(text("[0]", TextColor.color(0xD95E00)).append(text(".", TextColor.color(0x14CBDB))))).build());
        MessageUtils.addMessage("cmd.weeklytask.usage", text("Usage: /task <advancement> <reward>", TextColor.color(0xDB4630)));
    }

    public static Initializer plugin() {
        return plugin;
    }
}
