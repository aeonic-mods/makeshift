package design.aeonic.makeshift.content.block.node;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import design.aeonic.makeshift.registry.MkBlocks;
import design.aeonic.makeshift.registry.MkItems;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

public class OreNodeRenderer implements BlockEntityRenderer<OreNodeBlockEntity> {

    protected final Minecraft minecraft;
    protected final BlockRenderDispatcher blockRenderDispatcher;
    protected final BlockEntityRenderDispatcher blockEntityRenderDispatcher;

    public OreNodeRenderer(BlockEntityRendererProvider.Context ctx) {
        minecraft = Minecraft.getInstance();
        blockRenderDispatcher = ctx.getBlockRenderDispatcher();
        blockEntityRenderDispatcher = ctx.getBlockEntityRenderDispatcher();
    }

    @Override
    public void render(OreNodeBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        // FIXME: Ore node rendering with actual mineral types

        // Mineral
        blockRenderDispatcher.renderSingleBlock(MkBlocks.Rndr.ORE_NODE_MINERAL.get().defaultBlockState(), poseStack, multiBufferSource, i, i1);

        // Portable Miner valid locations
        if (minecraft.player != null && (minecraft.player.getMainHandItem().is(MkItems.PORTABLE_MINER.get()) || minecraft.player.getOffhandItem().is(MkItems.PORTABLE_MINER.get()))) {
            poseStack.pushPose();
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            RenderSystem.enableBlend();
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder builder = tesselator.getBuilder();
            builder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
            Matrix4f pose = poseStack.last().pose();

            builder.vertex(pose, -2f, .001f, -2f).color(.2f, .8f, .2f, .4f).endVertex();
            builder.vertex(pose, -2f, .001f, 3f).color(.2f, .8f, .2f, .4f).endVertex();
            builder.vertex(pose, 3f, .001f, 3f).color(.2f, .8f, .2f, .4f).endVertex();
            builder.vertex(pose, 3f, .001f, -2f).color(.2f, .8f, .2f, .4f).endVertex();

            tesselator.end();
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
            poseStack.popPose();
        }
    }

}
