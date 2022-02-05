package caittastic.threethreeminer.init;

import caittastic.threethreeminer.init.custom.item.DestructionCatalyst;
import caittastic.threethreeminer.init.custom.item.DestructionCatalystCopy;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

import static caittastic.threethreeminer.ThreeThreeMiner.MOD_ID;

public class ItemInit {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    public static final RegistryObject<Item> DESTRUCTION_CATALYST = ITEMS.register("destruction_catalyst",
            () -> new DestructionCatalystCopy(new Item.Properties().tab(CreativeModeTab.TAB_TOOLS).stacksTo(1)));




    //function that registers items
    private static <T extends Item> RegistryObject<T> register(final String name, final Supplier<T> item) {
        return ITEMS.register(name, item);
    }
}
