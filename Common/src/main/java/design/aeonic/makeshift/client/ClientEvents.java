package design.aeonic.makeshift.client;

import com.mojang.blaze3d.vertex.PoseStack;
import design.aeonic.makeshift.api.client.BlockHoverData;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;

import java.util.Objects;

public class ClientEvents {

    public static void renderGuiOverlays(PoseStack stack, float partialTick) {
        Minecraft minecraft = Minecraft.getInstance();
        Level level = Objects.requireNonNull(minecraft.level);

        if (minecraft.hitResult instanceof BlockHitResult blockHitResult) {
            if (level.getBlockState(blockHitResult.getBlockPos()).getBlock() instanceof BlockHoverData hoverData) {
                hoverData.drawHoverData(level, blockHitResult.getBlockPos(), stack, partialTick);
            }
        }
    }

}
