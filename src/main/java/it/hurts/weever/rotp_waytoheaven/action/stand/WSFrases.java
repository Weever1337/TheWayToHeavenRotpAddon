package it.hurts.weever.rotp_waytoheaven.action.stand;

import com.github.standobyte.jojo.action.ActionConditionResult;
import com.github.standobyte.jojo.action.ActionTarget;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.github.standobyte.jojo.entity.stand.StandEntityTask;
import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.github.standobyte.jojo.util.mc.MCUtil;
import it.hurts.weever.rotp_waytoheaven.init.InitItems;
import it.hurts.weever.rotp_waytoheaven.item.BoneItem;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.EffectInstance;
import net.minecraft.potion.Effects;
import net.minecraft.world.World;

public class WSFrases extends StandEntityAction {
    public WSFrases(StandEntityAction.Builder builder) {
        super(builder);
    }

    protected ActionConditionResult checkSpecificConditions(LivingEntity user, IStandPower power, ActionTarget target) {
        if (rewritedCheckBone((PlayerEntity) user,false)) {
            return ActionConditionResult.POSITIVE;
        }
        return conditionMessage("no_bone");
    }

    @Override
    public void standPerform(World world, StandEntity standEntity, IStandPower userPower, StandEntityTask task) {
        if (!world.isClientSide()) {
            PlayerEntity user = (PlayerEntity) userPower.getUser();
            rewritedCheckBone(user, true);
            MCUtil.runCommand(user, "stand clear @s");
            MCUtil.runCommand(user, "stand give @s rotp_cm:cmoon true");
        }
    }

    @Override
    public void holdTick(World world, LivingEntity user, IStandPower power, int ticksHeld, ActionTarget target, boolean requirementsFulfilled) {
        if(!world.isClientSide()){
            power.getUser().addEffect(new EffectInstance(Effects.CONFUSION,100,4, false, false, false));
            power.getUser().addEffect(new EffectInstance(Effects.MOVEMENT_SLOWDOWN,10, 1, false, false, false));
        }
    }

    public static boolean checkBone(PlayerEntity player,boolean delete){
        boolean result = false;
        for(int i=0;i<player.inventory.getContainerSize();i++){
            ItemStack stack = player.inventory.getItem(i);
            if(stack.getItem() == InitItems.BONE_ITEM.get()){
                if(stack.hasTag()){
                    result = stack.getTag().getInt("sinners") >= BoneItem.MAX_SINNERS;
                    if(result){
                        i = player.inventory.getContainerSize();
                        if(delete){
                            stack.shrink(stack.getCount());
                        }
                    }
                }
            }
        }
        return result;
    }

    public static boolean rewritedCheckBone(PlayerEntity player, boolean remove) {
        boolean result = false;
        ItemStack itemStack = player.getOffhandItem();
        if (itemStack.getItem() == InitItems.BONE_ITEM.get()) {
            if (itemStack.hasTag()) {
                result = itemStack.getTag().getInt("sinners") >= BoneItem.MAX_SINNERS;
                if (remove) {
                    itemStack.shrink(itemStack.getCount());
                }
            }
        }
        return result;
    }
}
