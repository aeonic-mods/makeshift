package design.aeonic.makeshift;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Makeshift.MOD_ID)
@Mod.EventBusSubscriber(modid = Makeshift.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ForgeMakeshift {

    public ForgeMakeshift() {
        Makeshift.init();
        FMLJavaModLoadingContext.get().getModEventBus().addListener((FMLClientSetupEvent event) -> Makeshift.clientInit());
    }

}
