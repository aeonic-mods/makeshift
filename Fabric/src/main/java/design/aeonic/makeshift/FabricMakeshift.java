package design.aeonic.makeshift;

import design.aeonic.makeshift.client.ClientEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;

public class FabricMakeshift {

    public void initializeCommon() {
        Makeshift.init();
    }

    public void initializeClient() {
        Makeshift.clientInit();

//        WorldRenderEvents.LAST.register(ctx -> ClientEvents.renderLevelLast(ctx.worldRenderer(), ctx.matrixStack(), ctx.tickDelta()));
        HudRenderCallback.EVENT.register(ClientEvents::renderGuiOverlays);
    }

}
