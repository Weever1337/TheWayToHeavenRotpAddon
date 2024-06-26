package it.hurts.weever.rotp_waytoheaven.util;

import com.archlunatic1057.rotp_whitesnake.entity.stand.stands.WhitesnakeEntity;

import com.github.standobyte.jojo.power.impl.stand.IStandPower;
import com.mojang.brigadier.CommandDispatcher;
import it.hurts.weever.rotp_waytoheaven.TheWayToHeavenAddon;
import it.hurts.weever.rotp_waytoheaven.command.BoneCommand;
import it.hurts.weever.rotp_waytoheaven.command.HeavenCommand;
import it.hurts.weever.rotp_waytoheaven.init.InitItems;
import it.hurts.weever.rotp_waytoheaven.init.InitStands;
import it.hurts.weever.rotp_waytoheaven.init.InitTags;
import it.hurts.weever.rotp_waytoheaven.item.BoneItem;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Mod.EventBusSubscriber(modid = TheWayToHeavenAddon.MOD_ID)
public class GameplayUtil {
    public static boolean isChange(PlayerEntity player) { return player.getTags().contains("heaven"); }
    public static void removeChange(PlayerEntity player) { player.removeTag("heaven"); }


    private static final Map<PlayerEntity,Vector3d> heavenPosition = new HashMap<>();
    public static Map<PlayerEntity, Vector3d> getHeavenPosition(){
        return heavenPosition;
    }


    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onServerPlayerTick(TickEvent.PlayerTickEvent event){
        PlayerEntity player = event.player;
        if(!player.level.isClientSide){
            IStandPower.getStandPowerOptional(player).ifPresent(power -> {
                if (isChange(player)){
                    double plrPosX = player.getX();
                    double plrPosY = player.getY();
                    double plrPosZ = player.getZ();
                    Random random = new Random();
                    heavenPosition.putIfAbsent(player,
                            new Vector3d(Math.round(plrPosX + (random.nextDouble() * 200 - 100)),125,Math.round(plrPosZ + (random.nextDouble() * 200 - 100))));
                    System.out.println(getHeavenPosition().get(player));
                    if(heavenPosition.containsKey(player)){
                        double distance = heavenPosition.get(player).distanceTo(new Vector3d(plrPosX,plrPosY,plrPosZ));
                        if(distance <= 5){
                            power.clear();
                            power.fullStandClear();
                            power.givePower(InitStands.MADE_IN_HEAVEN.get());
                            heavenPosition.remove(player);
                            removeChange(player);
                            player.addTag("immunity");
                            player.removeTag("heaven_message");
                            player.removeTag("sended_or_not");
                        }
                    }
                }
            });
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onFallDamage(LivingHurtEvent event){
        LivingEntity entity = event.getEntityLiving();
        if (entity == null || !entity.getTags().contains("immunity")) return;
        boolean isPlayer = entity instanceof PlayerEntity;
        if (isPlayer) {
            if (event.getSource() == DamageSource.FALL) {
                entity.removeTag("immunity");
                event.setAmount(0);
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onKilled(LivingDeathEvent event){
        LivingEntity entity = event.getEntityLiving();
        if (InitTags.CRIMINALS.contains(entity.getType())){
            if (event.getSource().getEntity() instanceof PlayerEntity) {
                PlayerEntity player = (PlayerEntity) event.getSource().getEntity();
                checkBone(player);
            }
            if (event.getSource().getEntity() instanceof WhitesnakeEntity) {
                WhitesnakeEntity stand = (WhitesnakeEntity) event.getSource().getEntity();
                if (stand.getUser() instanceof PlayerEntity) {
                    PlayerEntity player = (PlayerEntity) stand.getUser();
                    checkBone(player);
                }
            }
        }
    }

    public static void checkBone(PlayerEntity player){
        for(int i = 0; i < player.inventory.getContainerSize(); i++){
            ItemStack stack = player.inventory.getItem(i);
            if (stack.getItem() == InitItems.BONE_ITEM.get()){
                if (!stack.hasTag()) {
                    CompoundNBT nbt = new CompoundNBT();
                    stack.setTag(nbt);
                    nbt.putInt("sinners", 1);
                } else {
                    int quantum = stack.getTag().getInt("sinners");
                    if (quantum < BoneItem.MAX_SINNERS){
                        stack.getTag().putInt("sinners", quantum+1);
                    }
                }
                break;
            }
        }
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSource> dispatcher = event.getDispatcher();
        BoneCommand.register(dispatcher);
        HeavenCommand.register(dispatcher);
    }
}
