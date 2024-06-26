package it.hurts.weever.rotp_waytoheaven.events;

import it.hurts.weever.rotp_waytoheaven.TheWayToHeavenAddon;
import it.hurts.weever.rotp_waytoheaven.events.loot.PyramidStructureModifier;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;

@Mod.EventBusSubscriber(modid = TheWayToHeavenAddon.MOD_ID,bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventsBus {
    @SubscribeEvent
    public static void registerModifierSerializers(@Nonnull final RegistryEvent.Register<GlobalLootModifierSerializer<?>> event) {
        event.getRegistry().registerAll(
                new PyramidStructureModifier.Serializer().setRegistryName
                        (new ResourceLocation(TheWayToHeavenAddon.MOD_ID,"pyramid"))
        );
    }
}
