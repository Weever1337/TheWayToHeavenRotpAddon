package it.hurts.weever.rotp_waytoheaven.action.stand;

import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mc.MCUtil;
import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

public class CMHeaven extends StandEntityAction {
    public CMHeaven(StandEntityAction.Builder builder) {
        super(builder);
    }

    @Override
    public void holdTick(World level, LivingEntity user, IStandPower userPower, int ticksHeld, ActionTarget target, boolean requirementsFulfilled) {
        if (!level.isClientSide()) {
            double x = user.getX();
            double y = user.getY() + 1;
            double z = user.getZ();
            Vector3d position = new Vector3d(x, 125, z);
            double distance = user.position().distanceTo(position);
            if (distance <= 5) {
                MCUtil.runCommand(user, "stand clear @s");
                MCUtil.runCommand(user, "stand give @s rotp_mih:madeinheaven true");
                user.addTag("immunity");
            } else {
                user.addEffect(new EffectInstance(Effects.LEVITATION, 10, 3, false, false, false));
                MCUtil.runCommand(user, "particle rotp_mih:spark " + x + " " + y + " " + z + " .5 .5 .5 1 30");
            }
        }
    }

    @Override
    public IFormattableTextComponent getTranslatedName(IStandPower power, String key) {
        LivingEntity player = power.getUser();
        Vector3d position = new Vector3d(player.getX(), 125, player.getZ());
        int roundedX = Math.round((float) position.x);
        int roundedY = Math.round((float) position.y);
        int roundedZ = Math.round((float) position.z);
        return new TranslationTextComponent(key + ".param", roundedX, roundedY, roundedZ);
    }
}
