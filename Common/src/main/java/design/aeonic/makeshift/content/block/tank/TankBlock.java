package design.aeonic.makeshift.content.block.tank;

import design.aeonic.nifty.api.core.Constants;
import design.aeonic.nifty.api.fluid.FluidStack;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.LiquidBlockContainer;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public class TankBlock extends BaseEntityBlock implements LiquidBlockContainer {

    protected final int capacity;

    public TankBlock(Properties $$0, int capacity) {
        super($$0);
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult res) {
        var blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof TankBlockEntity be) {
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
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TankBlockEntity(blockPos, blockState, capacity);
    }

    @Override
    public RenderShape getRenderShape(BlockState $$0) {
        return RenderShape.MODEL;
    }

    @Override
    public boolean canPlaceLiquid(BlockGetter blockGetter, BlockPos blockPos, BlockState blockState, Fluid fluid) {
        Constants.LOGGER.info(getBlockEntity(blockGetter, blockPos).tank.insert(FluidStack.of(fluid), true).isEmpty());
        return getBlockEntity(blockGetter, blockPos).tank.insert(FluidStack.of(fluid), true).isEmpty();
    }

    @Override
    public boolean placeLiquid(LevelAccessor levelAccessor, BlockPos blockPos, BlockState blockState, FluidState fluidState) {
        Constants.LOGGER.info(getBlockEntity(levelAccessor, blockPos).tank.insert(FluidStack.of(fluidState.getType(), fluidState.getAmount() * FluidStack.BUCKET / 8), false).isEmpty());
        return getBlockEntity(levelAccessor, blockPos).tank.insert(FluidStack.of(fluidState.getType(), fluidState.getAmount() * FluidStack.BUCKET / 8), false).isEmpty();
    }

    public TankBlockEntity getBlockEntity(BlockGetter blockGetter, BlockPos blockPos) {
        return (TankBlockEntity) blockGetter.getBlockEntity(blockPos);
    }

}
