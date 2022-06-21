package design.aeonic.makeshift.worldgen.processor;

import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import design.aeonic.makeshift.registry.MkStructureProcessors;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import javax.annotation.Nullable;
import java.util.List;

public class BlockReplaceProcessor extends StructureProcessor {

    public static final Codec<BlockReplaceProcessor> CODEC = Codec.pair(BlockState.CODEC.xmap(BlockState::getBlock, Block::defaultBlockState), BlockState.CODEC)
            .listOf().fieldOf("replacements").xmap(BlockReplaceProcessor::new, BlockReplaceProcessor::getReplacements).codec();

    protected final List<Pair<Block, BlockState>> replacements;

    public BlockReplaceProcessor(Block target, BlockState replaceWith) {
        this(List.of(Pair.of(target, replaceWith)));
    }

    public BlockReplaceProcessor(List<Pair<Block, BlockState>> replacements) {
        this.replacements = replacements;
    }

    public List<Pair<Block, BlockState>> getReplacements() {
        return replacements;
    }

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader, BlockPos blockPos, BlockPos blockPos1, StructureTemplate.StructureBlockInfo structureBlockInfo, StructureTemplate.StructureBlockInfo structureBlockInfo1, StructurePlaceSettings structurePlaceSettings) {
        for (Pair<Block, BlockState> pair: replacements) {
            if (structureBlockInfo1.state.is(pair.getFirst())) {
                return new StructureTemplate.StructureBlockInfo(structureBlockInfo1.pos, pair.getSecond(), structureBlockInfo1.nbt);
            }
        }
        return structureBlockInfo1;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return MkStructureProcessors.BLOCK_REPLACE_PROCESSOR.get();
    }

}
