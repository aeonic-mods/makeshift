package design.aeonic.makeshift.api.machine.block;

import design.aeonic.makeshift.api.machine.MachineConsumption;
import design.aeonic.makeshift.api.machine.MachineTier;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class MachineBlockEntity<C extends MachineConsumption, M extends MachineMenu> extends BlockEntity implements MenuProvider {

    private boolean ranLastTick = false;

    protected final C machineConsumption;

    public MachineBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
        this.machineConsumption = initMachineConsumption();
    }

    /**
     * Creates a new machine consumption instance for this machine; necessary to avoid early calls in the constructor.
     */
    protected abstract C initMachineConsumption();

    public abstract MachineTier machineTier();

    /**
     * Run whatever functionality requires consumption.<br><br>
     * Called only if {@link #machineConsumption} can run, after it has consumed whatever necessary.
     */
    protected abstract void runMachine(ServerLevel level, BlockPos pos, BlockState state);

    protected void onStartRunning(ServerLevel level, BlockPos pos, BlockState state) {}

    protected void onStopRunning(ServerLevel level, BlockPos pos, BlockState state) {}

    protected boolean canRun() {
        return true;
    }

    public void serverTick(ServerLevel level, BlockPos pos, BlockState state) {
        if (canRun() && machineConsumption.canRun()) {
            machineConsumption.run();
            if (!ranLastTick) {
                ranLastTick = true;
                onStartRunning(level, pos, state);
            }
            runMachine(level, pos, state);
        } else if (ranLastTick) {
            ranLastTick = false;
            onStopRunning(level, pos, state);
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        machineConsumption.deserialize(tag.getCompound("Consumption"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        tag.put("Consumption", machineConsumption.serialize());
    }

}
