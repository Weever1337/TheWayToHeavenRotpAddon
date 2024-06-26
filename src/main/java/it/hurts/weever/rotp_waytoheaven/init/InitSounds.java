package it.hurts.weever.rotp_waytoheaven.init;

import it.hurts.weever.rotp_waytoheaven.TheWayToHeavenAddon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, TheWayToHeavenAddon.MOD_ID);

    public static final RegistryObject<SoundEvent> PUCCI_FRASES = SOUNDS.register("pucci_frases",
            () -> new SoundEvent(new ResourceLocation(TheWayToHeavenAddon.MOD_ID, "pucci_frases")));

}
