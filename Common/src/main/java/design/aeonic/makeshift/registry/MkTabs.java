package design.aeonic.makeshift.registry;

import design.aeonic.makeshift.Makeshift;
import design.aeonic.nifty.Nifty;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

public class MkTabs {

    public static final CreativeModeTab TAB_MAIN = Nifty.ACCESS.registerCreativeTab(Makeshift.location("main"), () -> new ItemStack(MkItems.BRASS_CASING.get()));

    public static void init() {}

}
