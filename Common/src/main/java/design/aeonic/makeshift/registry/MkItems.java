package design.aeonic.makeshift.registry;

import design.aeonic.makeshift.Makeshift;
import design.aeonic.makeshift.client.MkComponents;
import design.aeonic.makeshift.content.item.TooltipBlockItem;
import design.aeonic.nifty.Nifty;
import design.aeonic.nifty.api.registry.GameObject;
import net.minecraft.core.Registry;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class MkItems {

    public static final GameObject<Item> BRASS_INGOT = register("brass_ingot", () ->
            new Item(new Item.Properties().tab(MkTabs.TAB_MAIN)));

    public static final GameObject<Item> BRASS_NUGGET = register("brass_nugget", () ->
            new Item(new Item.Properties().tab(MkTabs.TAB_MAIN)));

    public static final GameObject<BlockItem> BRASS_BLOCK = register("brass_block", () ->
            new BlockItem(MkBlocks.BRASS_BLOCK.get(), new Item.Properties().tab(MkTabs.TAB_MAIN)));

    public static final GameObject<BlockItem> BRASS_CASING = register("brass_casing", () ->
            new BlockItem(MkBlocks.BRASS_CASING.get(), new Item.Properties().tab(MkTabs.TAB_MAIN)));

    public static final GameObject<BlockItem> BRASS_TANK = register("brass_tank", () ->
            new TooltipBlockItem(MkBlocks.BRASS_TANK.get(), new Item.Properties().tab(MkTabs.TAB_MAIN),
                    MkComponents.TOOLTIP_DOES_NOT_RETAIN));

    public static final GameObject<BlockItem> PORTABLE_MINER = register("portable_miner", () ->
            new TooltipBlockItem(MkBlocks.PORTABLE_MINER.get(), new Item.Properties().tab(MkTabs.TAB_MAIN),
                    MkComponents.TOOLTIP_PORTABLE_MINER_PLACEMENT, MkComponents.TOOLTIP_PORTABLE_MINER_DESCRIPTION));

    public static void init() {

    }

    public static <T extends Item> GameObject<T> register(String name, Supplier<T> supplier) {
        return Nifty.REGISTRY.register(Registry.ITEM, Makeshift.location(name), supplier);
    }

}
