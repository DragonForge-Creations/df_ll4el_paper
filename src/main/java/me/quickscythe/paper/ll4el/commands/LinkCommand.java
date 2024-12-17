package me.quickscythe.paper.ll4el.commands;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import json2.JSONObject;
import me.quickscythe.dragonforge.commands.CommandExecutor;
import me.quickscythe.dragonforge.utils.chat.MessageUtils;
import me.quickscythe.paper.ll4el.utils.DonorDriveApi;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static io.papermc.paper.command.brigadier.Commands.argument;
import static io.papermc.paper.command.brigadier.Commands.literal;

public class LinkCommand extends CommandExecutor {
    public LinkCommand(JavaPlugin plugin) {
        super(plugin, "link");
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> execute() {
        return literal(getName()).executes(context -> showUsage(context, "")).then(argument("participantId", IntegerArgumentType.integer()).executes(context -> {
            if (!(context.getSource().getSender() instanceof Player player)) {
                return logError(context, MessageUtils.getMessage("cmd.error.player_only"));
            }
            int participantId = IntegerArgumentType.getInteger(context, "participantId");
            JSONObject participant = DonorDriveApi.getParticipant(participantId);
            if (participant == null) {
                return logError(context, "Invalid participant ID");
            }

            DonorDriveApi.linkParticipant(player, participantId);
            player.sendMessage(MessageUtils.getMessage("cmd.link.success", player.getName(), participant.getString("displayName")));
            return 1;
        }).then(argument("playerName", StringArgumentType.string()).executes(context->{
            int participantId = IntegerArgumentType.getInteger(context, "participantId");
            String playerName = StringArgumentType.getString(context, "playerName");

            JSONObject participant = DonorDriveApi.getParticipant(participantId);
            if (participant == null) {
                return logError(context, "Invalid participant ID");
            }

            if(!Bukkit.getOfflinePlayer(playerName).hasPlayedBefore()){
                return logError(context, "Invalid player name");
            }

            OfflinePlayer target = Bukkit.getOfflinePlayer(playerName);
            DonorDriveApi.linkParticipant(target, participantId);
            context.getSource().getSender().sendMessage(MessageUtils.getMessage("cmd.link.success", target.getName(), participant.getString("displayName")));
            return 1;
        }))).build();
    }
}
