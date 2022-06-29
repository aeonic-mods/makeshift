package design.aeonic.makeshift.registry;

import design.aeonic.makeshift.Makeshift;
import design.aeonic.makeshift.content.block.miner.PortableMinerBlockEntity;
import design.aeonic.makeshift.content.block.node.OreNodeBlockEntity;
import design.aeonic.makeshift.content.block.tank.TankBlock;
import design.aeonic.makeshift.content.block.tank.TankBlockEntity;
import design.aeonic.nifty.Nifty;
import design.aeonic.nifty.api.registry.GameObject;
import net.minecraft.core.Registry;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class MkBlockEntities {

    public static final GameObject<BlockEntityType<TankBlockEntity>> TANK = register("tank",
            () -> Nifty.ACCESS.blockEntityType((pos, state) -> new TankBlockEntity(pos, state, ((TankBlock) state.getBlock()).getCapacity()),
                    MkBlocks.BRASS_TANK.get()));

    public static final GameObject<BlockEntityType<OreNodeBlockEntity>> ORE_NODE = register("ore_node",
            () -> Nifty.ACCESS.blockEntityType(OreNodeBlockEntity::new, MkBlocks.ORE_NODE.get()));

    public static final GameObject<BlockEntityType<PortableMinerBlockEntity>> PORTABLE_MINER = register("portable_miner",
            () -> Nifty.ACCESS.blockEntityType(PortableMinerBlockEntity::new, MkBlocks.PORTABLE_MINER.get()));

    public static void init() {}

    static <T extends BlockEntityType<?>> GameObject<T> register(String name, Supplier<T> supplier) {
        return Nifty.REGISTRY.register(Registry.BLOCK_ENTITY_TYPE, Makeshift.location(name), supplier);
    }

}
