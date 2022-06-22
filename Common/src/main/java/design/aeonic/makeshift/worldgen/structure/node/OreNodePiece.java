package design.aeonic.makeshift.worldgen.structure.node;

import design.aeonic.makeshift.api.node.OreNodeType;
import design.aeonic.makeshift.api.node.OreNodeTypes;
import design.aeonic.makeshift.content.block.node.OreNodeBlockEntity;
import design.aeonic.makeshift.registry.MkStructurePieces;
import design.aeonic.makeshift.worldgen.processor.BlockReplaceProcessor;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;

import java.util.Random;

public class OreNodePiece extends TemplateStructurePiece {

    protected OreNodeType nodeType;

    public OreNodePiece(StructureManager structureManager, CompoundTag tag) {
        this(structureManager, tag, OreNodeTypes.getNodeType(new ResourceLocation(tag.getString("NodeType"))));
    }

    public OreNodePiece(StructureManager structureManager, CompoundTag tag, OreNodeType nodeType) {
        super(MkStructurePieces.ORE_NODE.get(), tag, structureManager, key -> settings(nodeType));
        this.nodeType = nodeType;
    }

    public OreNodePiece(StructureManager structureManager, ResourceLocation key, String str, BlockPos pos, OreNodeType nodeType) {
        super(MkStructurePieces.ORE_NODE.get(), 0, structureManager, key, str, settings(nodeType), pos);
        this.nodeType = nodeType;
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext ctx, CompoundTag tag) {
        super.addAdditionalSaveData(ctx, tag);
        tag.putString("NodeType", OreNodeTypes.getKey(nodeType).toString());
    }

    @Override
    protected void handleDataMarker(String s, BlockPos blockPos, ServerLevelAccessor serverLevelAccessor, Random random, BoundingBox boundingBox) {
        if (s.equals("OreNode")) {
            if (serverLevelAccessor.getBlockEntity(blockPos.below()) instanceof OreNodeBlockEntity be) {
                be.setNodeType(nodeType, new Random());
            }
        }
    }

    private static StructurePlaceSettings settings(OreNodeType type) {
        return new StructurePlaceSettings().addProcessor(new BlockReplaceProcessor(Blocks.BEDROCK, type.getMineralBlock().defaultBlockState())).addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK);
    }

}
