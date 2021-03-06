package design.aeonic.makeshift.registry;

import design.aeonic.makeshift.Makeshift;
import design.aeonic.makeshift.worldgen.processor.BlockReplaceProcessor;
import design.aeonic.nifty.Nifty;
import design.aeonic.nifty.api.registry.GameObject;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;

import java.util.function.Supplier;

public class MkStructureProcessors {

    public static final GameObject<StructureProcessorType<BlockReplaceProcessor>> BLOCK_REPLACE_PROCESSOR = register("block_replace", () -> () -> BlockReplaceProcessor.CODEC);

    public static void init() {}

    private static <T extends StructureProcessorType<?>> GameObject<T> register(String name, Supplier<T> supplier) {
        return Nifty.REGISTRY.register(Registry.STRUCTURE_PROCESSOR, Makeshift.location(name), supplier);
    }

}
