package me.quickscythe.paper.ll4el.commands;

import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import json2.JSONObject;
import me.quickscythe.dragonforge.commands.CommandExecutor;
import me.quickscythe.paper.ll4el.utils.donations.Donation;
import me.quickscythe.paper.ll4el.utils.donations.DonorDriveApi;
import org.bukkit.plugin.java.JavaPlugin;

import static io.papermc.paper.command.brigadier.Commands.argument;
import static io.papermc.paper.command.brigadier.Commands.literal;

public class DonationCommand extends CommandExecutor {

    public DonationCommand(JavaPlugin plugin) {
        super(plugin, "donation");
    }

    @Override
    public LiteralCommandNode<CommandSourceStack> execute() {
        return literal(getName()).executes(context -> showUsage(context, "admin"))
                .then(argument("player", ArgumentTypes.players())
                        .executes(context -> {
                            PlayerSelectorArgumentResolver targetSelector = context.getArgument("player", PlayerSelectorArgumentResolver.class);
                            targetSelector.resolve(context.getSource()).forEach(target -> {
                                JSONObject data = new JSONObject();
                                data.put("displayName", "Friendly Donor");
                                data.put("participantID", 548514);
                                data.put("amount", 10);
                                data.put("donorID", "EB8610A3FC435D58");
                                data.put("avatarImageURL", "https://static.donordrive.com/clients/try/img/avatar-constituent-default.gif");
                                data.put("createdDateUTC", "2019-10-30T18:01:18.513+0000");
                                data.put("eventID", 581);
                                data.put("teamID", 5074);
                                data.put("donationID", "DF4E676D0828A8D5");
                                data.put("isRegFee", false);
                                data.put("recipientName", "Jane Participant");
                                data.put("recipientImageURL", "https://testdrive.donordrivecontent.com/try/images/$avatars$/constituent_8672DB7B-CE87-F677-6260FF8F15074828.jpg");
                                data.put("message", "Test message");
                                data.put("activityPledgeUnitAmount", 10.00);
                                data.put("activityPledgeMaxAmount", 1000.00);
                                data.put("incentiveID", "67630A91-AD4A-2AF2-1C5D516A0B89CFE5");
                                data.put("links", new JSONObject()
                                        .put("recipient", "https://testdrive.donordrive.com/index.cfm?fuseaction=donorDrive.participant&participantID=4024")
                                        .put("donate", "https://testdrive.donordrive.com/index.cfm?fuseaction=donorDrive.donate&participantID=4024"));
//                                {
//                                    "displayName": "Friendly Donor",
//                                        "participantID": 4024,
//                                        "amount": 10,
//                                        "donorID": "EB8610A3FC435D58",
//                                        "avatarImageURL": "https://static.donordrive.com/clients/try/img/avatar-constituent-default.gif",
//                                        "createdDateUTC": "2019-10-30T18:01:18.513+0000",
//                                        "eventID": 581,
//                                        "teamID": 5074,
//                                        "donationID": "DF4E676D0828A8D5",
//                                        "links": {
//                                    "recipient": "https://testdrive.donordrive.com/index.cfm?fuseaction=donorDrive.participant&participantID=4024",
//                                            "donate": "https://testdrive.donordrive.com/index.cfm?fuseaction=donorDrive.donate&participantID=4024"
//                                },
//                                    "isRegFee": false,
//                                        "recipientName": "Jane Participant",
//                                        "recipientImageURL": "https://testdrive.donordrivecontent.com/try/images/$avatars$/constituent_8672DB7B-CE87-F677-6260FF8F15074828.jpg",
//                                        "message": null,
//                                        "activityPledgeUnitAmount": 10.00,
//                                        "activityPledgeMaxAmount": 1000.00
//                                }
                                Donation donation = new Donation(data);
                                DonorDriveApi.queueDonation(donation);
                            });
                            return 1;
                        })).build();
    }

}
