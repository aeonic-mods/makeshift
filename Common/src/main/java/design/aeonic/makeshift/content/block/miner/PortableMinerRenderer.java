package design.aeonic.makeshift.content.block.miner;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Quaternion;
import com.mojang.math.Vector3f;
import design.aeonic.makeshift.registry.MkBlocks;
import design.aeonic.makeshift.registry.MkSoundEvents;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.Objects;
import java.util.Random;

public class PortableMinerRenderer implements BlockEntityRenderer<PortableMinerBlockEntity> {

    protected final Minecraft minecraft;
    protected final BlockRenderDispatcher blockRenderDispatcher;
    protected final BlockEntityRenderDispatcher blockEntityRenderDispatcher;

    public PortableMinerRenderer(BlockEntityRendererProvider.Context ctx) {
        minecraft = Minecraft.getInstance();
        blockRenderDispatcher = ctx.getBlockRenderDispatcher();
        blockEntityRenderDispatcher = ctx.getBlockEntityRenderDispatcher();
    }

    @Override
    public void render(PortableMinerBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {
        BlockState state = blockEntity.getBlockState();
        Direction facing = state.getValue(PortableMinerBlock.FACING);
        boolean powered = state.getValue(PortableMinerBlock.POWERED);

        // Drill
        poseStack.pushPose();
        long time = Util.getMillis();
        float rot = 0;
        if (powered) {
            blockEntity.drillRenderOffset = Math.min(.0625f, blockEntity.drillRenderOffset + .001f);
            rot = (time * .7f) % 360f;
        } else {
            blockEntity.drillRenderOffset = Math.max(0, blockEntity.drillRenderOffset - .001f);
            rot = blockEntity.drillRenderOffset * -3600f;
        }
        poseStack.translate(.5, .5 - blockEntity.drillRenderOffset, .5);
        poseStack.mulPose(Quaternion.fromXYZDegrees(new Vector3f(0, rot, 0)));
        poseStack.translate(-.5, -.5, -.5);
        blockRenderDispatcher.renderSingleBlock(MkBlocks.Rndr.PORTABLE_MINER_DRILL.get().defaultBlockState(), poseStack, multiBufferSource, i, i1);
        poseStack.popPose();

        // Particles + sound
        if (!minecraft.isPaused()) {
            Level level = Objects.requireNonNull(minecraft.level);
            if (powered) {
                Random random = new Random();
                if (random.nextFloat() < .2f) {
                    Vec3 pos = Vec3.atBottomCenterOf(blockEntity.getBlockPos());
                    double x = random.nextGaussian() * 0.02 + pos.x;
                    double y = random.nextGaussian() * 0.02 + pos.y + .02;
                    double z = random.nextGaussian() * 0.02 + pos.z;
                    level.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, level.getBlockState(blockEntity.getBlockPos().below())), x, y, z, 0, .02, 0);
                }
                if (blockEntity.lastRunSound == -1 || time > blockEntity.lastRunSound + PortableMinerBlockEntity.RUN_SOUND_LENGTH) {
                    BlockPos blockPos = blockEntity.getBlockPos();
                    level.playLocalSound(blockPos.getX() + .5, blockPos.getY() + .5, blockPos.getZ() + .5, MkSoundEvents.PORTABLE_MINER_RUN.get(), SoundSource.BLOCKS, .3f, 1f, false);
                    blockEntity.lastRunSound = time;
                }
            } else {
                if (blockEntity.lastRunSound != -1 && time > blockEntity.lastRunSound + PortableMinerBlockEntity.RUN_SOUND_LENGTH) {
                    BlockPos blockPos = blockEntity.getBlockPos();
                    level.playLocalSound(blockPos.getX() + .5, blockPos.getY() + .5, blockPos.getZ() + .5, MkSoundEvents.PORTABLE_MINER_STOP.get(), SoundSource.BLOCKS, .3f, 1f, false);
                    blockEntity.lastRunSound = -1;
                }
            }
        }
    }

}
