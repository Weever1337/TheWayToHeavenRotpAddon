package it.hurts.weever.rotp_waytoheaven.init;

import it.hurts.weever.rotp_waytoheaven.TheWayToHeavenAddon;
import net.minecraft.entity.EntityType;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Tags;

public class InitTags {

    public static final Tags.IOptionalNamedTag<EntityType<?>> CRIMINALS = EntityTypeTags.createOptional(new ResourceLocation(TheWayToHeavenAddon.MOD_ID,"criminals"));

    public static void initTags(){}
}
