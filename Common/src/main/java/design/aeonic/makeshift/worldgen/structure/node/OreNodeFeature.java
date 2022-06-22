package design.aeonic.makeshift.worldgen.structure.node;

import design.aeonic.makeshift.api.node.OreNodeTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.QuartPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGenerator;
import net.minecraft.world.level.levelgen.structure.pieces.PieceGeneratorSupplier;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

import java.util.Random;

public class OreNodeFeature extends StructureFeature<NoneFeatureConfiguration> {

    protected final Type type;

    public OreNodeFeature(ResourceLocation structureKey, Type type) {
        super(NoneFeatureConfiguration.CODEC, PieceGeneratorSupplier.simple(type == Type.SURFACE ? PieceGeneratorSupplier.checkForBiomeOnTop(Heightmap.Types.WORLD_SURFACE_WG) : ctx -> {
                    // TODO: Rewrite cave structure gen
                    ChunkPos chunkPos = ctx.chunkPos();
                    BlockPos tmp = chunkPos.getMiddleBlockPosition(0);
                    BlockPos blockPos = tmp.atY(ctx.chunkGenerator().getFirstFreeHeight(tmp.getX(), tmp.getZ(), Heightmap.Types.MOTION_BLOCKING, ctx.heightAccessor()));
                    return ctx.validBiome().test(ctx.chunkGenerator().getNoiseBiome(QuartPos.fromBlock(blockPos.getX()), QuartPos.fromBlock(blockPos.getY()), QuartPos.fromBlock(blockPos.getZ())));
                },
                (builder, ctx) -> generatePieces(builder, ctx, structureKey, type)));
        this.type = type;
    }

    private static void generatePieces(StructurePiecesBuilder builder, PieceGenerator.Context<NoneFeatureConfiguration> ctx, ResourceLocation key, Type type) {
        ChunkPos chunkPos = ctx.chunkPos();
        BlockPos tmp = chunkPos.getMiddleBlockPosition(0);
        BlockPos blockPos = tmp.atY(type == Type.CAVE ?
                ctx.chunkGenerator().getFirstFreeHeight(tmp.getX(), tmp.getZ(), Heightmap.Types.MOTION_BLOCKING, ctx.heightAccessor()) :
                ctx.chunkGenerator().getBaseHeight(tmp.getX(), tmp.getZ(), Heightmap.Types.WORLD_SURFACE_WG, ctx.heightAccessor()));

        builder.addPiece(new OreNodePiece(ctx.structureManager(), key, key.toString(), blockPos, OreNodeTypes.pickNodeType(
                ctx.chunkGenerator().getNoiseBiome(QuartPos.fromBlock(blockPos.getX()), QuartPos.fromBlock(blockPos.getY()), QuartPos.fromBlock(blockPos.getZ())), blockPos, new Random())));
    }

    @Override
    public GenerationStep.Decoration step() {
        return type == Type.SURFACE ? GenerationStep.Decoration.SURFACE_STRUCTURES : GenerationStep.Decoration.UNDERGROUND_STRUCTURES;
    }

    public enum Type {
        SURFACE,
        CAVE
    }

}
