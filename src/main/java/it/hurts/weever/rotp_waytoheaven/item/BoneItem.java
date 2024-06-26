package it.hurts.weever.rotp_waytoheaven.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class BoneItem extends Item {
    public BoneItem(Properties p_i48487_1_) {
        super(p_i48487_1_);
    }

    public static final int MAX_SINNERS = 36;
    private static int getSinners(ItemStack bone) {
        return bone.getOrCreateTag().getInt("sinners");
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return getSinners(stack) < MAX_SINNERS;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return 1 - ((double) getSinners(stack) / (double) MAX_SINNERS);
    }

}
