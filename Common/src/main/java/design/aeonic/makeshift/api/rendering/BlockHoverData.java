package design.aeonic.makeshift.api.rendering;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;

import java.util.List;

public interface BlockHoverData {

    void drawHoverData(Level level, BlockPos pos, PoseStack stack, float partialTick);

    interface SimpleHoverData extends BlockHoverData {

        List<Component> getHoverDataComponents(Level level, BlockPos pos);

        @Override
        default void drawHoverData(Level level, BlockPos pos, PoseStack stack, float partialTick) {
            stack.pushPose();

            Minecraft minecraft = Minecraft.getInstance();
            Font font = minecraft.font;

            List<Component> hoverDataComponents = getHoverDataComponents(level, pos);

            int x = minecraft.getWindow().getGuiScaledWidth() / 2 + 15;
            int y = minecraft.getWindow().getGuiScaledHeight() / 2;

            for (int i = 0; i < hoverDataComponents.size(); i++) {
                Component component = hoverDataComponents.get(i);
                font.draw(stack, component, x, y + font.lineHeight * (i), 0xffffff);
            }

            stack.popPose();
        }

    }

}
