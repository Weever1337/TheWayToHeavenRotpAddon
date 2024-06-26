package it.hurts.weever.rotp_waytoheaven.action.stand;

import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.capability.entity.EntityUtilCapProvider;
import com.github.standobyte.jojo.client.ClientUtil;
import com.github.standobyte.jojo.client.ui.actionshud.ActionsOverlayGui;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandManifestation;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;

import com.github.standobyte.jojo.util.mc.MCUtil;
import com.weever.rotp_cm.entity.CMEntity;
import it.hurts.weever.rotp_waytoheaven.init.InitItems;
import it.hurts.weever.rotp_waytoheaven.init.InitStands;
import it.hurts.weever.rotp_waytoheaven.item.BoneItem;
import it.hurts.weever.rotp_waytoheaven.util.GameplayUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import java.util.OptionalInt;
import java.util.Random;

public class CMHeaven extends StandEntityAction {
    public CMHeaven(StandEntityAction.Builder builder) {
        super(builder);
    }

    protected ActionConditionResult checkSpecificConditions(LivingEntity user, IStandPower power, ActionTarget target) {
        if (InitStands.MADE_IN_HEAVEN.get() == null) {
            return conditionMessage("no_madeinheaven");
        }
        if (checkCoordinates((PlayerEntity) user)) {
            return ActionConditionResult.POSITIVE;
        }
        return conditionMessage("cm_heaven");
    }

    @Override
    public void holdTick(World world, LivingEntity user, IStandPower userPower, int ticksHeld, ActionTarget target, boolean requirementsFulfilled) {
        if (checkCoordinates((PlayerEntity) user)) {
            if (!world.isClientSide()) {
                double x = user.getX();
                double y = user.getY() + 1;
                double z = user.getZ();
                user.addEffect(new EffectInstance(Effects.LEVITATION, 10, 3, false, false, false));
                MCUtil.runCommand(user, "particle rotp_mih:cum "+x+" "+y+" "+z+" .5 .5 .5 1 30");
            }
        }
    }


    private static boolean checkCoordinates(PlayerEntity player){
        if (!GameplayUtil.getHeavenPosition().containsKey(player)){
            player.addTag("heaven");
        } else {
            double x = player.getX();
            double z = player.getZ();
            double needX = GameplayUtil.getHeavenPosition().get(player).x;
            double needZ = GameplayUtil.getHeavenPosition().get(player).z;
            if(Math.abs(x - needX) <= 2 && Math.abs(z - needZ) <= 2){
                return true;
            }
        }
        return false;
    }

    @Override
    public IFormattableTextComponent getTranslatedName(IStandPower power, String key){
        if(power.getUser() instanceof PlayerEntity){
            PlayerEntity player = (PlayerEntity) power.getUser();
            if(GameplayUtil.getHeavenPosition().containsKey(player)) {
                player.addTag("heaven_message");
                Vector3d pos = GameplayUtil.getHeavenPosition().get(player);
                int roundedX = Math.round((float) pos.x);
                int roundedY = Math.round((float) pos.y);
                int roundedZ = Math.round((float) pos.z);
                if (player.getTags().contains("heaven_message") && !player.getTags().contains("sended_or_not")) {
                    player.displayClientMessage(new TranslationTextComponent("heaven.chat.quest", roundedX, roundedY, roundedZ).withStyle(TextFormatting.ITALIC).withStyle(TextFormatting.GOLD), false);
                    player.addTag("sended_or_not");
                }
                return new TranslationTextComponent(key + ".param", roundedX, roundedY, roundedZ);
            }
        }
        return super.getTranslatedName(power, key);
    }
}
