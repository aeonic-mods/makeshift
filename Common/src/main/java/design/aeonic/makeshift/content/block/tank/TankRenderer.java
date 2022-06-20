package design.aeonic.makeshift.content.block.tank;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import com.mojang.math.Matrix4f;
import design.aeonic.nifty.Nifty;
import design.aeonic.nifty.api.client.RenderHelper;
import design.aeonic.nifty.api.fluid.FluidStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.apache.commons.lang3.tuple.Pair;

public class TankRenderer implements BlockEntityRenderer<TankBlockEntity> {

    protected final Minecraft minecraft;
    protected final BlockRenderDispatcher blockRenderDispatcher;
    protected final BlockEntityRenderDispatcher blockEntityRenderDispatcher;

    public TankRenderer(BlockEntityRendererProvider.Context ctx) {
        minecraft = Minecraft.getInstance();
        blockRenderDispatcher = ctx.getBlockRenderDispatcher();
        blockEntityRenderDispatcher = ctx.getBlockEntityRenderDispatcher();
    }

    @Override
    public void render(TankBlockEntity blockEntity, float partialTick, PoseStack stack, MultiBufferSource bufferSource, int light, int overlay) {
        FluidStack fluidStack = blockEntity.tank.get(0);
        if (fluidStack.isEmpty()) return;

        var fluidInfo = Nifty.RENDER_HELPER.getFluidSpriteAndColor(fluidStack.getFluid().defaultFluidState());
        float fillLevel = blockEntity.tankSlot.getFillLevel();
        drawFluidBlock(stack, fluidInfo, fillLevel);
    }

    public static void drawFluidBlock(PoseStack stack, Pair<TextureAtlasSprite, Integer> fluidInfo, float height) {
        TextureAtlasSprite sprite = fluidInfo.getLeft();
        float[] color = RenderHelper.unpackARGB(fluidInfo.getRight());

        RenderSystem.setShaderColor(color[1], color[2], color[3], 1f);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, sprite.atlas().location());

        float u0 = sprite.getU0();
        float u1 = sprite.getU1();
        float v0 = sprite.getV0();
        float v1 = sprite.getV(height * .98f * sprite.getHeight());

        stack.pushPose();
        Matrix4f pose = stack.last().pose();
        BufferBuilder builder = Tesselator.getInstance().getBuilder();
        builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);

        builder.vertex(pose, 0, height * .98f, .02f).uv(u0, v1).endVertex();
        builder.vertex(pose, 1, height * .98f, .02f).uv(u1, v1).endVertex();
        builder.vertex(pose, 1, 0, .02f).uv(u1, v0).endVertex();
        builder.vertex(pose, 0, 0, .02f).uv(u0, v0).endVertex();

        builder.vertex(pose, 1, height * .98f, .98f).uv(u0, v1).endVertex();
        builder.vertex(pose, 0, height * .98f, .98f).uv(u1, v1).endVertex();
        builder.vertex(pose, 0, 0, .98f).uv(u1, v0).endVertex();
        builder.vertex(pose, 1, 0, .98f).uv(u0, v0).endVertex();

        builder.vertex(pose, .98f, height * .98f, 0).uv(u0, v1).endVertex();
        builder.vertex(pose, .98f, height * .98f, 1).uv(u1, v1).endVertex();
        builder.vertex(pose, .98f, 0, 1).uv(u1, v0).endVertex();
        builder.vertex(pose, .98f, 0, 0).uv(u0, v0).endVertex();

        builder.vertex(pose, .02f, height * .98f, 1).uv(u0, v1).endVertex();
        builder.vertex(pose, .02f, height * .98f, 0).uv(u1, v1).endVertex();
        builder.vertex(pose, .02f, 0, 0).uv(u1, v0).endVertex();
        builder.vertex(pose, .02f, 0, 1).uv(u0, v0).endVertex();

        builder.vertex(pose, 0, height * .98f, 1).uv(u0, sprite.getV1()).endVertex();
        builder.vertex(pose, 1, height * .98f, 1).uv(u1, sprite.getV1()).endVertex();
        builder.vertex(pose, 1, height * .98f, 0).uv(u1, v0).endVertex();
        builder.vertex(pose, 0, height * .98f, 0).uv(u0, v0).endVertex();

        builder.end();
        RenderSystem.enableDepthTest();
        BufferUploader.end(builder);
        stack.popPose();

        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }

}
