package it.hurts.weever.rotp_waytoheaven.init;

import com.github.standobyte.jojo.init.ModItems;
import it.hurts.weever.rotp_waytoheaven.TheWayToHeavenAddon;
import it.hurts.weever.rotp_waytoheaven.item.BoneItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class InitItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, TheWayToHeavenAddon.MOD_ID);

    public static final RegistryObject<BoneItem> BONE_ITEM = ITEMS.register("bone",
            ()->new BoneItem(new Item.Properties().tab(ModItems.MAIN_TAB).stacksTo(1)));
}
