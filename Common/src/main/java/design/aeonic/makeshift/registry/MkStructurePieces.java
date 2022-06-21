package design.aeonic.makeshift.registry;

import design.aeonic.makeshift.Makeshift;
import design.aeonic.makeshift.worldgen.structure.node.OreNodePiece;
import design.aeonic.nifty.Nifty;
import design.aeonic.nifty.api.registry.GameObject;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public class MkStructurePieces {

    public static final GameObject<StructurePieceType.StructureTemplateType> ORE_NODE = Nifty.REGISTRY.register(Registry.STRUCTURE_PIECE,
            Makeshift.location("ore_node"),
            () -> OreNodePiece::new);

    public static void init() {}

}
