package it.hurts.weever.rotp_waytoheaven.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import it.hurts.weever.rotp_waytoheaven.init.InitItems;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;

import java.util.Collection;
import java.util.Collections;

public class BoneCommand {

    public static void register(CommandDispatcher<CommandSource> pDispatcher) {
        pDispatcher.register(Commands.literal("bone").requires(ctx -> ctx.hasPermission(2))
                .then(Commands.argument("full", BoolArgumentType.bool())
                        .executes(ctx -> giveBone(
                                ctx.getSource(),
                                Collections.singleton(ctx.getSource().getPlayerOrException()),
                                BoolArgumentType.getBool(ctx, "full"))
                        ))
                .then(Commands.argument("targets", EntityArgument.players())
                        .then(Commands.argument("full", BoolArgumentType.bool())
                                .executes(ctx -> giveBone(
                                        ctx.getSource(),
                                        EntityArgument.getPlayers(ctx, "targets"),
                                        BoolArgumentType.getBool(ctx, "full"))
                                )))
        );
    }

    private static int giveBone(CommandSource ctx, Collection<ServerPlayerEntity> targets, boolean full) {
        ItemStack item = new ItemStack(InitItems.BONE_ITEM.get()).copy();
        item.getOrCreateTag().putInt("sinners", full ? 36 : 1);

        for (ServerPlayerEntity player : targets) {
            player.addItem(item);
        }

        return 1;
    }
}
