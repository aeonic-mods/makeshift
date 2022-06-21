package design.aeonic.makeshift.content.block.node;

import design.aeonic.makeshift.api.rendering.BlockHoverData;
import design.aeonic.makeshift.client.MkComponents;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class OreNodeBlock extends BaseEntityBlock {//implements BlockHoverData.SimpleHoverData {

    public OreNodeBlock(Properties props) {
        super(props);
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public VoxelShape getVisualShape(BlockState state, BlockGetter blockGetter, BlockPos blockPos, CollisionContext ctx) {
        return Shapes.empty();
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new OreNodeBlockEntity(blockPos, blockState);
    }

    // TODO: Separate scanner item so we can sync when necessary from the server
//    @Override
//    public List<Component> getHoverDataComponents(Level level, BlockPos pos) {
//        List<Component> hoverData = new ArrayList<>();
//        if (level.getBlockEntity(pos) instanceof OreNodeBlockEntity be && be.getOreNode() != null) {
//            hoverData.add(MkComponents.HOVER_ORE_NODE_TYPE.copy().append(new TranslatableComponent(be.getOreNode().getType().displayName())));
//            hoverData.add(MkComponents.HOVER_ORE_NODE_PURITY.resolve(Math.round(be.getOreNode().getPurity() * 100d) / 100d));
//        }
//        return hoverData;
//    }

}
