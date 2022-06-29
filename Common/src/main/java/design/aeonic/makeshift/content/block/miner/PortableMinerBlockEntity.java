package design.aeonic.makeshift.content.block.miner;

import design.aeonic.makeshift.api.machine.MachineConsumption;
import design.aeonic.makeshift.api.machine.MachineTier;
import design.aeonic.makeshift.api.node.OreNode;
import design.aeonic.makeshift.client.MkComponents;
import design.aeonic.makeshift.registry.MkBlockEntities;
import design.aeonic.nifty.Nifty;
import design.aeonic.nifty.api.aspect.Aspect;
import design.aeonic.nifty.api.item.SimpleItemHandler;
import design.aeonic.nifty.api.item.SimpleSlot;
import design.aeonic.nifty.api.network.container.ContainerFields;
import design.aeonic.nifty.api.network.container.field.BooleanField;
import design.aeonic.nifty.api.network.container.field.IntField;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

public class PortableMinerBlockEntity extends AbstractMinerBlockEntity<MachineConsumption.FuelItemConsumption, PortableMinerMenu> {

    // The length of the run sound effect in milliseconds
    protected static final int RUN_SOUND_LENGTH = 1341;
    protected static final int EXTRACT_TIME = 30 * SharedConstants.TICKS_PER_SECOND;

    // Client rendering data; doesn't need to persist.
    public float drillRenderOffset = 0;
    public long lastRunSound = -1;

    protected final SimpleItemHandler itemHandler = new SimpleItemHandler(
            new SimpleSlot(this::setChanged, $ -> false),
            new SimpleSlot(this::setChanged),
            new SimpleSlot(this::setChanged),
            new SimpleSlot(this::setChanged));

    public PortableMinerBlockEntity(BlockPos pos, BlockState state) {
        super(MkBlockEntities.PORTABLE_MINER.get(), pos, state);
    }

    @Override
    protected void onStartRunning(ServerLevel level, BlockPos pos, BlockState state) {
        level.setBlock(pos, getBlockState().setValue(PortableMinerBlock.POWERED, true), Block.UPDATE_ALL);
    }

    @Override
    protected void onStopRunning(ServerLevel level, BlockPos pos, BlockState state) {
        level.setBlock(pos, getBlockState().setValue(PortableMinerBlock.POWERED, false), Block.UPDATE_ALL);
    }

    @Override
    protected MachineConsumption.FuelItemConsumption initMachineConsumption() {
        return new MachineConsumption.FuelItemConsumption(() -> itemHandler, 0);
    }

    @Override
    protected boolean accept(List<ItemStack> items) {
        for (ItemStack stack : items) {
            if (!(itemHandler.insert(stack, true).getCount() == stack.getCount())) {
                for (ItemStack stack1 : items) {
                    itemHandler.insert(stack, false);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    protected Aspect<OreNode> findOreNode() {
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        BlockEntity be;

        pos.setY(getBlockPos().getY());
        for (int x = -2; x < 3; x++) {
            pos.setX(getBlockPos().getX() + x);
            for (int z = -2; z < 3; z++) {
                pos.setZ(getBlockPos().getZ() + z);
                if ((be = level.getBlockEntity(pos)) != null) {
                    Aspect<OreNode> aspect = Nifty.ASPECTS.query(OreNode.class, be, null);
                    if (aspect.isPresent()) {
                        return aspect;
                    }
                }
            }
        }

        return Aspect.empty();
    }

    @Override
    protected int extractTime() {
        return EXTRACT_TIME;
    }

    @Override
    public MachineTier machineTier() {
        return MachineTier.CRUDE;
    }

    @Override
    public Component getDisplayName() {
        return MkComponents.GUI_PORTABLE_MINER;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new PortableMinerMenu(i, inventory, itemHandler.asContainer(), new ContainerFields(
                new IntField(machineConsumption::getLitTime),
                new IntField(machineConsumption::getLitDuration),
                new IntField(() -> extractProgress),
                new BooleanField(this::canRun)
        ));
    }

}
