package it.hurts.weever.rotp_waytoheaven.init;

import com.github.standobyte.jojo.action.Action;
import com.github.standobyte.jojo.action.stand.StandAction;
import com.github.standobyte.jojo.action.stand.StandEntityAction;
import com.github.standobyte.jojo.entity.stand.StandEntityType;
import com.github.standobyte.jojo.init.power.stand.EntityStandRegistryObject;
import com.github.standobyte.jojo.power.impl.stand.StandInstance.StandPart;
import com.github.standobyte.jojo.power.impl.stand.stats.StandStats;
import com.github.standobyte.jojo.power.impl.stand.stats.TimeStopperStandStats;
import com.github.standobyte.jojo.power.impl.stand.type.EntityStandType;
import com.github.standobyte.jojo.power.impl.stand.type.StandType;
import com.silentevermore.rotp_whitesnake.entity.WhitesnakeEntity;
import com.weever.rotp_mih.entity.stand.stands.MihEntity;
import it.hurts.weever.rotp_cm.entity.CMEntity;
import it.hurts.weever.rotp_waytoheaven.TheWayToHeavenAddon;
import it.hurts.weever.rotp_waytoheaven.action.stand.CMHeaven;
import it.hurts.weever.rotp_waytoheaven.action.stand.WSFrases;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.forgespi.language.IConfigurable;
import net.minecraftforge.registries.DeferredRegister;

import javax.annotation.Nonnull;
import java.lang.reflect.Field;
import java.util.Optional;

import static com.silentevermore.rotp_whitesnake.init.InitStands.WHITESNAKE;
import static com.weever.rotp_mih.init.InitStands.MIH;
import static it.hurts.weever.rotp_cm.init.InitStands.STAND_CM;

public class InitStands {
    @SuppressWarnings("unchecked")
    public static final DeferredRegister<Action<?>> ACTIONS = DeferredRegister.create(
            (Class<Action<?>>) ((Class<?>) Action.class), TheWayToHeavenAddon.MOD_ID);

    public static final EntityStandRegistryObject.EntityStandSupplier<EntityStandType<StandStats>, StandEntityType<WhitesnakeEntity>>
            WS = new EntityStandRegistryObject.EntityStandSupplier<>(WHITESNAKE);

    public static final EntityStandRegistryObject.EntityStandSupplier<EntityStandType<TimeStopperStandStats>, StandEntityType<CMEntity>>
            CMOON = new EntityStandRegistryObject.EntityStandSupplier<>(STAND_CM);

    public static final EntityStandRegistryObject.EntityStandSupplier<EntityStandType<StandStats>, StandEntityType<MihEntity>>
            MADEINHEAVEN = new EntityStandRegistryObject.EntityStandSupplier<>(MIH);

    public static final RegistryObject<StandEntityAction> FRASES = ACTIONS.register("whitesnake_frases",
            () -> new WSFrases(new StandEntityAction.Builder().standUserWalkSpeed(.5F).staminaCostTick(1)
                    .resolveLevelToUnlock(4).holdToFire(396, false)
                    .standSound(StandEntityAction.Phase.BUTTON_HOLD, InitSounds.PUCCI_FRASES)
                    .standAutoSummonMode(StandEntityAction.AutoSummonMode.OFF_ARM)
                    .partsRequired(StandPart.ARMS))
    );

    public static final RegistryObject<StandEntityAction> HEAVEN = ACTIONS.register("cmoon_heaven",
            () -> new CMHeaven(new StandEntityAction.Builder()
                    .holdType().standUserWalkSpeed(.5F).staminaCostTick(1)
                    .resolveLevelToUnlock(4)
                    .partsRequired(StandPart.ARMS)
                    .standAutoSummonMode(StandEntityAction.AutoSummonMode.OFF_ARM))
    );

    @Mod.EventBusSubscriber(modid = TheWayToHeavenAddon.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class PizdecRegister {
        private static final Field STAND_TYPE_RMB_HOTBAR = getField(StandType.class, "rightClickHotbar");
        private static final Field STAND_STATS_ARROW_RANDOM_WEIGHT = getField(StandStats.class, "randomWeight");

        @SubscribeEvent(priority = EventPriority.HIGH)
        public static void afterStandsRegister(@Nonnull RegistryEvent.Register<StandType<?>> event) {
            ModList.get().getModContainerById("rotp_whitesnake")
                    .map(ModContainer::getModInfo)
                    .flatMap(modInfo -> modInfo instanceof IConfigurable ? ((IConfigurable) modInfo).getConfigElement("authors") : Optional.empty())
                    .map(authorsString -> authorsString instanceof String ? (String) authorsString : null)
                    .ifPresent(authors -> {
                        if (authors.contains("ArchLunatic")) {
                            throw new RuntimeException("!!!!!!!!!!!!!!!!!!!!!!!USE NEW VERSION OF WHITESNAKE!!!!!!!!!!!!!!!!!!!!!!!");
                        }
                    });
            StandType<?> whitesnake = WS.getStandType();
            StandType<?> cMoon = CMOON.getStandType();
            StandType<?> MiH = MADEINHEAVEN.getStandType();
            try {
                // TODO add the new move in a non-hacky way when the version with that comes out
                initWS(whitesnake);
                // TODO instead replace whitesnake.survivalGameplayPool with StandSurvivalGameplayPool.NON_ARROW when the version with that comes out
                initCM(cMoon);
                initMIH(MiH);
            } catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }

        }

        private static void initWS(StandType<?> whitesnake) throws IllegalAccessException {
            StandAction[] rightClickHotbar = (StandAction[]) STAND_TYPE_RMB_HOTBAR.get(whitesnake);
            StandAction[] edited = new StandAction[rightClickHotbar.length + 1];
            edited[0] = rightClickHotbar[0];
            edited[1] = FRASES.get();
            System.arraycopy(rightClickHotbar, 1, edited, 2, rightClickHotbar.length - 1);
            STAND_TYPE_RMB_HOTBAR.set(whitesnake, edited);
        }

        private static void initCM(StandType<?> cMoon) throws IllegalAccessException {
            StandStats defaultStats = cMoon.getDefaultStats();
            STAND_STATS_ARROW_RANDOM_WEIGHT.setDouble(defaultStats, 0);
            StandAction[] rCH = (StandAction[]) STAND_TYPE_RMB_HOTBAR.get(cMoon);
            StandAction[] edtd = new StandAction[rCH.length + 1];
            edtd[0] = rCH[0];
            edtd[1] = HEAVEN.get();
            System.arraycopy(rCH, 1, edtd, 2, rCH.length - 1);
            STAND_TYPE_RMB_HOTBAR.set(cMoon, edtd);
        }

        private static void initMIH(StandType<?> mih) throws IllegalAccessException {
            StandStats defaultStats = mih.getDefaultStats();
            STAND_STATS_ARROW_RANDOM_WEIGHT.setDouble(defaultStats, 0);
        }

        private static Field getField(Class<?> clazz, String fieldName) {
            try {
                Field f = clazz.getDeclaredField(fieldName);
                f.setAccessible(true);
                return f;
            } catch (Exception e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
    }
}
