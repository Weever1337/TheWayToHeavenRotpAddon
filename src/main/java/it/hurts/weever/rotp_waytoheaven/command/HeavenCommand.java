package it.hurts.weever.rotp_waytoheaven.command;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collection;
import java.util.Collections;

public class HeavenCommand {

    public static void register(CommandDispatcher<CommandSource> pDispatcher) {
        pDispatcher.register(Commands.literal("heaven").requires(ctx -> ctx.hasPermission(0))
                .executes(ctx -> giveHeavenInfo(
                        ctx.getSource(),
                        Collections.singleton(ctx.getSource().getPlayerOrException()))
                )
                .then(Commands.argument("targets", EntityArgument.players())
                        .executes(ctx -> giveHeavenInfo(
                                ctx.getSource(),
                                EntityArgument.getPlayers(ctx, "targets")
                        ))
                )
        );
    }

    private static int giveHeavenInfo(CommandSource ctx, Collection<ServerPlayerEntity> targets) {
        for (ServerPlayerEntity player : targets) {
            player.sendMessage(new TranslationTextComponent("heaven.chat.command.lib.info")
                    .withStyle(TextFormatting.GRAY)
                    .withStyle(TextFormatting.ITALIC), player.getUUID());
        }
        return 1;
    }
}
