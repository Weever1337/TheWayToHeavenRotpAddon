package it.hurts.weever.rotp_waytoheaven.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import it.hurts.weever.rotp_waytoheaven.init.InitItems;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TranslationTextComponent;

public class BoneCommand {

    public static void register(CommandDispatcher<CommandSource> pDispatcher) {
        pDispatcher.register(Commands.literal("bone").requires(ctx -> ctx.hasPermission(0))
                .executes(ctx -> giveBone(ctx.getSource()))
        );
    }

    private static int giveBone(CommandSource ctx) throws CommandSyntaxException {
        PlayerEntity entity = (PlayerEntity) ctx.getEntity();
        ItemStack item = new ItemStack(InitItems.BONE_ITEM.get()).copy();
        item.getOrCreateTag().putInt("sinners", 36);
        entity.addItem(item);
        return 1;
    }
}