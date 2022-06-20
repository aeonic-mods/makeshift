package design.aeonic.makeshift.content.block.miner;

import design.aeonic.makeshift.client.MkComponents;
import design.aeonic.makeshift.content.block.node.OreNodeBlockEntity;
import design.aeonic.makeshift.registry.MkBlockEntities;
import design.aeonic.makeshift.registry.MkItems;
import design.aeonic.nifty.Nifty;
import design.aeonic.nifty.api.item.ItemHandler;
import design.aeonic.nifty.api.item.SimpleItemHandler;
import design.aeonic.nifty.api.item.SimpleSlot;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.List;

public class PortableMinerBlockEntity extends BlockEntity implements MenuProvider {

    // FIXME: Rewrite, abstract miner-type blocks

    // The length of the run sound effect in milliseconds
    static final int RUN_SOUND_LENGTH = 1341;

    // Client rendering data; doesn't need to persist.
    public float drillRenderOffset = 0;
    public long lastRunSound = -1;

    protected final ItemHandler itemHandler = new SimpleItemHandler(
            new SimpleSlot(this::setChanged, stack -> Nifty.ACCESS.getBurnTime(stack) > 0),
            new SimpleSlot(this::setChanged),
            new SimpleSlot(this::setChanged),
            new SimpleSlot(this::setChanged));

    protected final Container container = itemHandler.asContainer();

    protected final ContainerData containerData = new ContainerData() {
        @Override
        public int get(int i) {
            return switch (i) {
                case 0 -> litTime;
                case 1 -> litDuration;
                case 2 -> extractTime;
                case 3 -> extractDuration;
                default -> throw new IllegalStateException();
            };
        }

        @Override
        public void set(int i, int i1) {}

        @Override
        public int getCount() {
            return 4;
        }
    };

    protected int litTime = 0;
    protected int litDuration = 0;
    protected int extractTime = 0;
    protected int extractDuration = 0;

    protected @Nullable OreNodeBlockEntity oreNode = null;
    protected boolean invalidPlacement = false;

    public PortableMinerBlockEntity(BlockPos pos, BlockState state) {
        super(MkBlockEntities.PORTABLE_MINER.get(), pos, state);
    }

    public void serverTick(ServerLevel level, BlockPos pos, BlockState state) {
        if (litTime > 0 && --litTime <= 0) {
            level.setBlock(pos, state.setValue(PortableMinerBlock.POWERED, false), Block.UPDATE_ALL);
            setChanged();
        }
        else {
            ItemStack fuelStack = itemHandler.get(0);
            int fuelBurnTime = Nifty.ACCESS.getBurnTime(fuelStack);
            if (fuelBurnTime > 0 && getOreNode() != null) {
                litTime = fuelBurnTime;
                litDuration = fuelBurnTime;
                if (fuelStack.getItem().hasCraftingRemainingItem()) {
                    itemHandler.set(0, fuelStack.getItem().getCraftingRemainingItem().getDefaultInstance());
                } else fuelStack.shrink(1);
                level.setBlock(pos, state.setValue(PortableMinerBlock.POWERED, true), Block.UPDATE_ALL);
                setChanged();
            }
        }

        if (litTime > 0) {
            if (extractDuration == 0) {
                // TODO: Varying extraction time logic
                extractDuration = 20 * 30;
                extractTime = 0;
            } else {
                extractTime = Math.min(extractDuration, extractTime + 1);
                OreNodeBlockEntity node;
                if (extractTime >= extractDuration && (node = getOreNode()) != null) {
                    List<ItemStack> yield = node.getOreNode().yield(level, MkItems.PORTABLE_MINER.get().getDefaultInstance(), getBlockPos());
                    if (!yield.isEmpty()) {
                        int flag = (int) yield.stream().filter(stack -> !itemHandler.insert(stack, true).isEmpty()).count();
                        if (flag < Math.min(yield.size(), itemHandler.getNumSlots())) {
                            yield.forEach(itemStack -> itemHandler.insert(itemStack, false));
                            container.setChanged();
                        }
                        extractTime = 0;
                    }
                }
            }
            setChanged();
        }
    }

    private @Nullable OreNodeBlockEntity getOreNode() {
        // A little janky, but essentially we avoid the iteration if the ore node is actually null - that is, if the blockentity
        // has been initialized but a node was not found, this portable miner will never work until it's initialized again.
        if (level == null || invalidPlacement) return null;
        if (oreNode != null && !oreNode.isRemoved()) return oreNode;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        pos.setY(getBlockPos().getY());
        for (int x = -2; x < 3; x++) {
            pos.setX(getBlockPos().getX() + x);
            for (int z = -2; z < 3; z++) {
                pos.setZ(getBlockPos().getZ() + z);
                if (level.getBlockEntity(pos) instanceof OreNodeBlockEntity be && !be.isRemoved()) {
                    oreNode = be;
                    return be;
                }
            }
        }
        invalidPlacement = true;
        return oreNode = null;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        litTime = tag.getInt("LitTime");
        litDuration = tag.getInt("LitDuration");
        extractTime = tag.getInt("ExtractTime");
        extractDuration = tag.getInt("ExtractDuration");
        itemHandler.deserialize(tag.getCompound("Items"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        tag.putInt("LitTime", litTime);
        tag.putInt("LitDuration", litDuration);
        tag.putInt("ExtractTime", extractTime);
        tag.putInt("ExtractDuration", extractDuration);
        tag.put("Items", itemHandler.serialize());
    }

    @Override
    public Component getDisplayName() {
        return MkComponents.GUI_PORTABLE_MINER;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory playerInventory, Player player) {
        return new PortableMinerMenu(syncId, playerInventory, container, containerData);
    }

}
