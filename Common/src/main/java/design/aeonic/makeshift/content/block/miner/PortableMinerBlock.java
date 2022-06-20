package design.aeonic.makeshift.content.block.miner;

import design.aeonic.makeshift.registry.MkBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import java.util.Random;

public class PortableMinerBlock extends BaseEntityBlock {

    private static final VoxelShape SHAPE_NORTH_SOUTH;
    private static final VoxelShape SHAPE_EAST_WEST;

    static {
        SHAPE_NORTH_SOUTH = Shapes.or(
                Shapes.box(0.25, 0, 0.25, 0.75, 1, 0.75));

        SHAPE_EAST_WEST = Shapes.or(
                Shapes.box(0.25, 0, 0.25, 0.75, 1, 0.75));
    }

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public PortableMinerBlock(Properties props) {
        super(props);
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult res) {
        var blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof PortableMinerBlockEntity be) {
            if (!level.isClientSide()) {
                player.openMenu(be);
                return InteractionResult.CONSUME;
            }
            return InteractionResult.SUCCESS;
        }
        return super.use(state, level, pos, player, hand, res);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> type) {
        return type == MkBlockEntities.PORTABLE_MINER.get() ? (lvl, pos, state, be) -> {
            if (lvl instanceof ServerLevel serverLvl) ((PortableMinerBlockEntity) be).serverTick(serverLvl, pos, state);
        } : null;
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, Random random) {
        if (blockState.getValue(POWERED) && random.nextDouble() < .2d) {
            level.playLocalSound(blockPos.getX() + .5, blockPos.getY() + .5, blockPos.getZ() + .5, SoundEvents.FURNACE_FIRE_CRACKLE, SoundSource.BLOCKS, 1f, 1f, false);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        return defaultBlockState().setValue(FACING, ctx.getHorizontalDirection().getOpposite()).setValue(POWERED, false);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(POWERED, FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter blockGetter, BlockPos blockPos, CollisionContext ctx) {
        return state.getValue(FACING).getAxis() == Direction.Axis.X ? SHAPE_EAST_WEST : SHAPE_NORTH_SOUTH;
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
        return new PortableMinerBlockEntity(blockPos, blockState);
    }

}
