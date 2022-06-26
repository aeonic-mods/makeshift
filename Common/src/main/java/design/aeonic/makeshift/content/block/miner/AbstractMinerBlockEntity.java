package design.aeonic.makeshift.content.block.miner;

import design.aeonic.makeshift.api.machine.MachineConsumption;
import design.aeonic.makeshift.api.machine.block.MachineBlockEntity;
import design.aeonic.makeshift.api.machine.block.MachineMenu;
import design.aeonic.makeshift.api.node.OreNode;
import design.aeonic.nifty.Nifty;
import design.aeonic.nifty.api.aspect.Aspect;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import java.util.List;

public abstract class AbstractMinerBlockEntity<C extends MachineConsumption, M extends MachineMenu> extends MachineBlockEntity<C, M> {

    protected Aspect<OreNode> oreNode = Aspect.empty();
    protected int extractProgress = 0;

    // Used to ensure we're not scanning for ore nodes every tick if one isn't found.
    // Ore nodes should be immovable, so this shouldn't ever be an issue.
    private boolean dontLookForOreNode = false;

    public AbstractMinerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    protected boolean canRun() {
        if (!oreNode.isPresent() && !dontLookForOreNode) {
            oreNode = findOreNode();
            dontLookForOreNode = true;
        }
        return oreNode.isPresent();
    }

    @Override
    protected void runMachine(ServerLevel level, BlockPos pos, BlockState state) {
        if (extractProgress >= extractTime()) {
            oreNode.ifPresent(node -> {
                if (accept(node.yield(level, toolStack(), pos))) {
                    extractProgress = 0;
                }
            });
        } else {
            extractProgress ++;
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        extractProgress = tag.getInt("ExtractProgress");
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        tag.putInt("ExtractProgress", extractProgress);
    }

    /**
     * The time, in ticks, needed to extract resources from an ore node.
     */
    protected abstract int extractTime();

    /**
     * Processes the item stacks obtained from the ore node while running; returns whether they were actually accepted.<br><br>
     * Returns false if, for instance, there is insufficent space in the target inventory.
     */
    protected abstract boolean accept(List<ItemStack> items);

    /**
     * Finds a nearby ore node, or null if there is none in a valid location.
     */
    protected Aspect<OreNode> findOreNode() {
        return Nifty.ASPECTS.query(OreNode.class, level.getBlockEntity(getBlockPos().below()), null);
    }

    /**
     * Gets the itemstack passed as a "tool" to the ore node loot table via {@link OreNode#yield}.
     */
    protected ItemStack toolStack() {
        return getBlockState().getBlock().asItem().getDefaultInstance();
    }

}
