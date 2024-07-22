package it.hurts.weever.rotp_waytoheaven;

import it.hurts.weever.rotp_waytoheaven.init.InitItems;
import it.hurts.weever.rotp_waytoheaven.init.InitSounds;
import it.hurts.weever.rotp_waytoheaven.init.InitStands;
import it.hurts.weever.rotp_waytoheaven.init.InitTags;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(TheWayToHeavenAddon.MOD_ID)
public class TheWayToHeavenAddon {
    public static final String MOD_ID = "rotp_waytoheaven";
    private static final Logger LOGGER = LogManager.getLogger();

    public TheWayToHeavenAddon() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        InitSounds.SOUNDS.register(modEventBus);
        InitStands.ACTIONS.register(modEventBus);
        InitItems.ITEMS.register(modEventBus);

        InitTags.initTags();
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}
