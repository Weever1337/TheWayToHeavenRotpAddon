package it.hurts.weever.rotp_waytoheaven;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import it.hurts.weever.rotp_waytoheaven.init.*;

import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

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
