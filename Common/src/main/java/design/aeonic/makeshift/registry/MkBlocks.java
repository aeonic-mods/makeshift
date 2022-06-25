package design.aeonic.makeshift.registry;

import design.aeonic.makeshift.Makeshift;
import design.aeonic.makeshift.content.block.miner.PortableMinerBlock;
import design.aeonic.makeshift.content.block.node.OreNodeBlock;
import design.aeonic.makeshift.content.block.tank.TankBlock;
import design.aeonic.nifty.Nifty;
import design.aeonic.nifty.api.fluid.FluidStack;
import design.aeonic.nifty.api.registry.GameObject;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class MkBlocks {

    public static final GameObject<Block> BRASS_BLOCK = Nifty.REGISTRY.register(Registry.BLOCK, Makeshift.location("brass_block"), () ->
            new Block(BlockBehaviour.Properties.of(Material.METAL, MaterialColor.GOLD)
                    .requiresCorrectToolForDrops().strength(4f, 6f).sound(SoundType.COPPER)));

    public static final GameObject<Block> BRASS_CASING = Nifty.REGISTRY.register(Registry.BLOCK, Makeshift.location("brass_casing"), () ->
            new Block(BlockBehaviour.Properties.copy(BRASS_BLOCK.get())
                    .strength(2f, 3f)));

    public static final GameObject<TankBlock> BRASS_TANK = Nifty.REGISTRY.register(Registry.BLOCK, Makeshift.location("brass_tank"), () ->
            new TankBlock(BlockBehaviour.Properties.copy(BRASS_CASING.get())
                    .noOcclusion().isValidSpawn(MkBlocks::never).isRedstoneConductor(MkBlocks::never), FluidStack.BUCKET * 16));

    public static final GameObject<OreNodeBlock> ORE_NODE = Nifty.REGISTRY.register(Registry.BLOCK, Makeshift.location("ore_node"), () ->
            new OreNodeBlock(BlockBehaviour.Properties.copy(Blocks.DEEPSLATE)
                    .strength(3f, 32f).noOcclusion().isValidSpawn(MkBlocks::never).isRedstoneConductor(MkBlocks::never)));

    public static final GameObject<PortableMinerBlock> PORTABLE_MINER = Nifty.REGISTRY.register(Registry.BLOCK, Makeshift.location("portable_miner"), () ->
            new PortableMinerBlock(BlockBehaviour.Properties.copy(BRASS_CASING.get())
                    .noOcclusion().isValidSpawn(MkBlocks::never).isRedstoneConductor(MkBlocks::never).sound(SoundType.METAL)
                    .emissiveRendering((state, getter, pos) -> state.getValue(PortableMinerBlock.POWERED)).lightLevel(state -> state.getValue(PortableMinerBlock.POWERED) ? 13: 0)));

    /**
     * Fake blocks used to load json models.
     */
    public static class Rndr {

        private static Block fakeBlock() {
            return new Block(BlockBehaviour.Properties.of(Material.AIR));
        }

        public static final GameObject<Block> ORE_NODE = Nifty.REGISTRY.register(Registry.BLOCK, Makeshift.location("rndr_ore_node"), Rndr::fakeBlock);

        public static final GameObject<Block> ORE_NODE_MINERAL = Nifty.REGISTRY.register(Registry.BLOCK, Makeshift.location("rndr_ore_node_mineral"), Rndr::fakeBlock);

        public static final GameObject<Block> PORTABLE_MINER_DRILL = Nifty.REGISTRY.register(Registry.BLOCK, Makeshift.location("rndr_portable_miner_drill"), () -> new Block(BlockBehaviour.Properties.of(Material.AIR)));

        public static void init() {}

    }

    public static void init() {
        Rndr.init();
    }

    public static boolean always(BlockState state, BlockGetter level, BlockPos pos) {
        return true;
    }

    public static boolean never(BlockState state, BlockGetter level, BlockPos pos) {
        return false;
    }

    public static <T> boolean never(BlockState state, BlockGetter level, BlockPos pos, T t) {
        return false;
    }

}
