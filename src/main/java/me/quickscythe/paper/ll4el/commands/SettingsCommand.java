package me.quickscythe.paper.ll4el.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.quickscythe.dragonforge.commands.CommandExecutor;
import me.quickscythe.dragonforge.utils.gui.GuiManager;
import me.quickscythe.paper.ll4el.utils.managers.SettingsManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static io.papermc.paper.command.brigadier.Commands.argument;
import static io.papermc.paper.command.brigadier.Commands.literal;

public class SettingsCommand extends CommandExecutor {
    public SettingsCommand(JavaPlugin plugin) {
        super(plugin, "settings");
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> getNode() {
        return literal(getName())
                .executes(context -> {
                    CommandSender sender = context.getSource().getSender();
                    if(!(sender instanceof Player player)){
                        return logError(sender, "You must be a player to use this command.");
                    }
                    GuiManager.openGui(player, GuiManager.getGui("settings"));
                    return Command.SINGLE_SUCCESS;
                }).then(argument("setting", StringArgumentType.string())
                        .suggests((context, builder) -> {
                            builder.suggest("icon");
                            builder.suggest("particles");
                            builder.suggest("chat");
                            return builder.buildFuture();
                        })
                        .executes(context -> {
                            String setting = StringArgumentType.getString(context, "setting");
                            CommandSender sender = context.getSource().getSender();
                            if(!(sender instanceof Player player)){
                                return logError(sender, "You must be a player to use this command.");
                            }
                            if(setting.equalsIgnoreCase("particles")){
                                SettingsManager.Settings set = SettingsManager.getSettings(player);
                                set.particles(!set.particles());
                                SettingsManager.setSettings(player, set);
                            }
                            if(setting.equalsIgnoreCase("icon")){
                                SettingsManager.Settings set = SettingsManager.getSettings(player);
                                set.icon(!set.icon());
                                SettingsManager.setSettings(player, set);
                            }
                            if(setting.equalsIgnoreCase("chat")){
                                SettingsManager.Settings set = SettingsManager.getSettings(player);
                                set.chat(!set.chat());
                                SettingsManager.setSettings(player, set);
                            }

                            return Command.SINGLE_SUCCESS;
                        })).build();
    }
}
