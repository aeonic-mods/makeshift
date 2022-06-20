package design.aeonic.makeshift;

import ca.weblite.objc.Client;
import design.aeonic.makeshift.client.ClientEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Makeshift.MOD_ID, value = Dist.CLIENT)
public class ForgeClientEvents {

    @SubscribeEvent
    public static void renderGuiOverlays(RenderGameOverlayEvent.Text event) {
        ClientEvents.renderGuiOverlays(event.getMatrixStack(), event.getPartialTicks());
    }

}
