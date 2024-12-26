package me.quickscythe.paper.ll4el.commands;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import json2.JSONObject;
import me.quickscythe.dragonforge.commands.CommandExecutor;
import me.quickscythe.dragonforge.utils.chat.MessageUtils;
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
    public LiteralCommandNode<CommandSourceStack> execute() {
        return literal(getName()).executes(context->showUsage(context, ""))
                .then(argument("setting", StringArgumentType.word())
                        .executes(context->{
                            //todo toggle setting between all options (test, this was ai generated)
                            CommandSender sender = context.getSource().getSender();
                            String setting = context.getArgument("setting", String.class);
                            if(!(sender instanceof Player player))
                                return logError(sender, MessageUtils.getMessage("cmd.error.player_only"));
                            JSONObject settings = SettingsManager.getSettings(player).json();
                            EffectSetting effectSetting = EffectSetting.valueOf(setting.toUpperCase());
                            String current = settings.getString(setting);
                            String next = null;
                            int i = 0;
                            for(String option : effectSetting.options()){
                                i=i+1;
                                //loop until we find the current setting
                                if(!option.equals(current)) continue;
                                // if we are at the end of the list, go back to the start
                                if(i==effectSetting.options().length){
                                    next = effectSetting.options()[0];
                                    break;
                                }
                                // otherwise, go to the next option
                                next = effectSetting.options()[i];
                                break;
                            }
                            settings.put(setting, next);
                            player.sendMessage(MessageUtils.getMessage("cmd.settings.set", setting, next));
                            return 1;
                        })
                        .suggests((context,builder)->{
                            for(EffectSetting setting : EffectSetting.values()){
                                builder.suggest(setting.name().toLowerCase());
                            }
                            return builder.buildFuture();
                        })
                        .then(argument("option", StringArgumentType.string())
                                .executes(context->{
                                    //todo handle setting option. Make sure both setting and option are valid
                                    CommandSender sender = context.getSource().getSender();
                                    String setting = context.getArgument("setting", String.class);
                                    String option = context.getArgument("option", String.class);
                                    if(!(sender instanceof Player player))
                                        return logError(sender, MessageUtils.getMessage("cmd.error.player_only"));
                                    JSONObject settings = SettingsManager.getSettings(player).json();
                                    settings.put(setting, option);
                                    player.sendMessage(MessageUtils.getMessage("cmd.settings.set", setting, option));
                                    return 1;
                                })
                                .suggests((context, builder)->{
                                    String[] input = context.getInput().split(" ");
                                    EffectSetting setting = EffectSetting.valueOf(input[1].toUpperCase());
                                    for(String option : setting.options()){
                                        builder.suggest(option);
                                    }
                                    return builder.buildFuture();
                                }))).build();
    }

    enum EffectSetting {
        LIFE("totem", "toast", "both", "none"), BOOGIE("particle", "icon", "both", "none"), DONATION("chat", "toast", "both", "none");

        final String[] options;

        EffectSetting(String... options) {
            this.options = options;
        }

        public String[] options() {
            return options;
        }
    }
}
