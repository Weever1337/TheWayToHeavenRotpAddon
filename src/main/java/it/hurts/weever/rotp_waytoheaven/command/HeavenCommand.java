package it.hurts.weever.rotp_waytoheaven.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;

public class HeavenCommand {

    public static void register(CommandDispatcher<CommandSource> pDispatcher) {
        pDispatcher.register(Commands.literal("heaven").requires(ctx -> ctx.hasPermission(0))
                .executes(ctx -> giveHeavenInfo(ctx.getSource()))
        );
    }

    private static int giveHeavenInfo(CommandSource ctx) throws CommandSyntaxException {
        ctx.sendSuccess(new TranslationTextComponent("heaven.chat.command.lib.info").withStyle(TextFormatting.GRAY).withStyle(TextFormatting.ITALIC), false);
        return 1;
    }
}