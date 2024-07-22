package it.hurts.weever.rotp_waytoheaven.util;

import com.github.standobyte.jojo.entity.stand.StandEntity;
import com.mojang.brigadier.CommandDispatcher;
import it.hurts.weever.rotp_waytoheaven.TheWayToHeavenAddon;
import it.hurts.weever.rotp_waytoheaven.command.BoneCommand;
import it.hurts.weever.rotp_waytoheaven.command.HeavenCommand;
import it.hurts.weever.rotp_waytoheaven.init.InitItems;
import it.hurts.weever.rotp_waytoheaven.init.InitTags;
import it.hurts.weever.rotp_waytoheaven.item.BoneItem;
import net.minecraft.command.CommandSource;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = TheWayToHeavenAddon.MOD_ID)
public class GameplayUtil {
//    private static final Map<PlayerEntity,Vector3d> heavenPosition = new HashMap<>();
//    public static Map<PlayerEntity, Vector3d> getHeavenPosition(){
//        return heavenPosition;
//    }

//    @SubscribeEvent(priority = EventPriority.HIGH)
//    public static void onPlayerTickEvent(TickEvent.PlayerTickEvent event) {
//        PlayerEntity player = event.player;
//        double plrPosX = player.getX();
//        double plrPosY = player.getY();
//        double plrPosZ = player.getZ();
//        Vector3d position = player.getCapability(PlayerUtilCapProvider.CAPABILITY).map(cap -> cap.getPosition()).orElse(null);
//        if (position == null) {
//            System.out.println(player.getName().getString() + " " + position);
//            Random random = new Random();
//            Vector3d vec = new Vector3d(Math.round(plrPosX + (random.nextDouble() * 200 - 100)), 125, Math.round(plrPosZ + (random.nextDouble() * 200 - 100)));
//            player.getCapability(PlayerUtilCapProvider.CAPABILITY).ifPresent(cap -> cap.setPosition(vec));
//        }
//        if (!heavenPosition.containsKey(player)) {
//            Random random = new Random();
//            Vector3d vec = new Vector3d(Math.round(plrPosX + (random.nextDouble() * 200 - 100)), 125, Math.round(plrPosZ + (random.nextDouble() * 200 - 100)));
//            heavenPosition.put(player, vec);
//        }
//        } else {
//            double distance = heavenPosition.get(player).distanceTo(new Vector3d(plrPosX, plrPosY, plrPosZ));
//            IStandPower.getStandPowerOptional(player).ifPresent(power -> {
//                if (distance <= 5 && power.getType() == InitStands.CMOON.getStandType()) {
//                    if (!player.level.isClientSide()) {
//                        System.out.println("SERVER SIDE FOR HEAVEN SYSTEM");
//                        power.clear();
//                        power.givePower(InitStands.MADEINHEAVEN.getStandType());
//                    } else {
//                        System.out.println("CLIENT SIDE FOR HEAVEN SYSTEM");
//                        PacketManager.sendToServer(new ClearAndGivePowerWithCommandPacket(player.getId()));
//                    }
//                    heavenPosition.remove(player);
//                    player.addTag("immunity");
//                }
//            });
//        }
//    }

//    @SubscribeEvent(priority = EventPriority.LOW)
//    public static void onServerPlayerTick(TickEvent.PlayerTickEvent event){
//        PlayerEntity player = event.player;
//        if(!player.level.isClientSide){
//            IStandPower.getStandPowerOptional(player).ifPresent(power -> {
//                if (isChange(player)){
//                    double plrPosX = player.getX();
//                    double plrPosY = player.getY();
//                    double plrPosZ = player.getZ();
//                    Random random = new Random();
//                    heavenPosition.putIfAbsent(player,
//                            new Vector3d(Math.round(plrPosX + (random.nextDouble() * 200 - 100)),125,Math.round(plrPosZ + (random.nextDouble() * 200 - 100))));
//                    System.out.println(getHeavenPosition().get(player));
//                    if(heavenPosition.containsKey(player)){
//                        double distance = heavenPosition.get(player).distanceTo(new Vector3d(plrPosX,plrPosY,plrPosZ));
//                        if(distance <= 5){
//                            power.clear();
//                            power.fullStandClear();
//                            power.givePower(InitStands.MADE_IN_HEAVEN.get());
//                            heavenPosition.remove(player);
//                            removeChange(player);
//                            player.addTag("immunity");
//                            player.removeTag("heaven_message");
//                            player.removeTag("sended_or_not");
//                        }
//                    }
//                }
//            });
//        }
//    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public static void onFallDamage(LivingHurtEvent event){
        LivingEntity entity = event.getEntityLiving();
        if (entity == null || !entity.getTags().contains("immunity")) return;
        boolean isPlayer = entity instanceof PlayerEntity;
        if (isPlayer) {
            if (event.getSource() == DamageSource.FALL) {
                entity.removeTag("immunity");
                event.setAmount(0);
                event.setCanceled(true);
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
            if (event.getSource().getEntity() instanceof StandEntity) {
                StandEntity stand = (StandEntity) event.getSource().getEntity();
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
