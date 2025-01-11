package me.quickscythe.paper.ll4el.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import me.quickscythe.dragonforge.commands.CommandExecutor;
import me.quickscythe.dragonforge.utils.chat.MessageUtils;
import me.quickscythe.dragonforge.utils.items.ItemUtils;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

import static io.papermc.paper.command.brigadier.Commands.argument;
import static io.papermc.paper.command.brigadier.Commands.literal;

public class TaskCommand extends CommandExecutor {
    public TaskCommand(JavaPlugin plugin) {
        super(plugin, "weeklytask");
    }


    @Override
    public LiteralCommandNode<CommandSourceStack> execute() {
        return literal(getName()).executes(context -> showUsage(context, "lastlife.task.admin")).then(argument("advancement", StringArgumentType.string()).executes(context -> showUsage(context, "lastlife.task.admin")).suggests((context, builder) -> {
            if (!context.getSource().getSender().hasPermission("lastlife.task.admin")) return builder.buildFuture();
            for (@NotNull Iterator<Advancement> it = Bukkit.getServer().advancementIterator(); it.hasNext(); ) {
                Advancement advancement = it.next();
                builder.suggest("\"" + advancement.getKey().getKey() + "\"");
            }
            return builder.buildFuture();
        }).then(argument("reward", StringArgumentType.greedyString()).executes(context -> {
                    String[] args = new String[]{context.getArgument("advancement", String.class), context.getArgument("reward", String.class)};
                    CommandSender sender = context.getSource().getSender();
                    NamespacedKey advancementKey = NamespacedKey.minecraft(args[0]);
                    Advancement advancement = sender.getServer().getAdvancement(advancementKey);
                    ItemStack reward = ItemUtils.deserializeVanillaString(args[1]);
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        AdvancementProgress progress = player.getAdvancementProgress(advancement);
                        if (progress.isDone()) {
                            player.sendMessage(MessageUtils.getMessage("message.task.complete", MessageUtils.plainText(reward.displayName())));
                            if (!ItemUtils.inventoryHasRoom(player.getInventory(), reward))
                                player.getWorld().dropItem(player.getLocation(), reward);
                            else player.getInventory().addItem(reward);
                        }
                    }
                    return 1;
                }).suggests((context, builder) -> {
                    if (!context.getSource().getSender().hasPermission("lastlife.task.admin")) return builder.buildFuture();
                    RegistryAccess.registryAccess().getRegistry(RegistryKey.ITEM).forEach(item -> {
                        builder.suggest(item.getKey().asString());
                    });
                    return builder.buildFuture();
                })

        )).build();
    }
}
