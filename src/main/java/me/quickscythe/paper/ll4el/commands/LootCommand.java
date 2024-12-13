package me.quickscythe.paper.ll4el.commands;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import me.quickscythe.dragonforge.commands.CommandExecutor;
import me.quickscythe.dragonforge.utils.chat.MessageUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

import static io.papermc.paper.command.brigadier.Commands.argument;
import static io.papermc.paper.command.brigadier.Commands.literal;

public class LootCommand extends CommandExecutor {
    public LootCommand(JavaPlugin plugin) {
        super(plugin, "loot");
    }

    // /loot drop <type> <location>

    @Override
    public LiteralCommandNode<CommandSourceStack> execute() {
        return literal(getName()).executes(context -> showUsage(context, "lastlife.admin.loot")).then(argument("type", StringArgumentType.string())
                .executes(context->showUsage(context, "lastlife.admin.loot")).then(argument("location", StringArgumentType.string())
                        .executes(context->{
                            CommandSender sender = context.getSource().getSender();
                            String type = context.getArgument("type", String.class);
                            String location = context.getArgument("location", String.class);

                            return 1;
                        }))).build();
    }


}
