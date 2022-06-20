package design.aeonic.makeshift.content.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

public class TooltipBlockItem extends BlockItem {

    protected final Component[] tooltip;

    public TooltipBlockItem(Block block, Properties props, Component... tooltip) {
        super(block, props);
        this.tooltip = tooltip;
    }

    @Override
    public void appendHoverText(ItemStack $$0, @Nullable Level $$1, List<Component> $$2, TooltipFlag $$3) {
        super.appendHoverText($$0, $$1, $$2, $$3);

        Collections.addAll($$2, tooltip);
    }
}
