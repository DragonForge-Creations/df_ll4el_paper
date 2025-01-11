package me.quickscythe.paper.ll4el.commands.arguments;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import me.quickscythe.dragonforge.utils.storage.DataManager;
import me.quickscythe.paper.ll4el.utils.managers.party.Party;
import me.quickscythe.paper.ll4el.utils.managers.party.PartyManager;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.CompletableFuture;

public class PartyArgumentType implements CustomArgumentType.Converted<Party, String> {

    @Override
    public Party convert(@NotNull String nativeType) {
        return DataManager.getConfigManager("parties", PartyManager.class).getParty(nativeType);
    }

    @Override
    public @NotNull ArgumentType<String> getNativeType() {
        return StringArgumentType.word();
    }

    @Override
    public <S> @NotNull CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {

        for(String name : DataManager.getConfigManager("parties", PartyManager.class).getParties())
            builder.suggest(name);

        return builder.buildFuture();
    }
}
