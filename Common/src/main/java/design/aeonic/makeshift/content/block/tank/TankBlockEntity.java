package design.aeonic.makeshift.content.block.tank;

import design.aeonic.makeshift.client.MkComponents;
import design.aeonic.makeshift.registry.MkBlockEntities;
import design.aeonic.nifty.Nifty;
import design.aeonic.nifty.api.fluid.SimpleFluidHandler;
import design.aeonic.nifty.api.fluid.SimpleTank;
import design.aeonic.nifty.api.item.FluidHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

public class TankBlockEntity extends BlockEntity implements MenuProvider {

    public final int capacity;

    public final FluidHandler tank;
    public final SimpleTank tankSlot;

    protected final ContainerData containerData = new ContainerData() {
        @Override
        public int get(int i) {
            BlockPos pos = getBlockPos();
            return switch (i) {
                case 0 -> Registry.FLUID.getId(tankSlot.get().getFluid());
                // Container data only stores 16 bits
                case 1 -> tankSlot.get().getAmount() & 0xffff;
                case 2 -> (tankSlot.get().getAmount() >> 16) & 0xffff;
                case 3 -> capacity  & 0xffff;
                case 4 -> (capacity >> 16) % 0xffff;
                default -> throw new IllegalStateException();
            };
        }

        @Override
        public void set(int i, int value) {}

        @Override
        public int getCount() {
            return 5;
        }
    };

    public TankBlockEntity(BlockPos pos, BlockState state, int capacity) {
        super(MkBlockEntities.TANK.get(), pos, state);
        this.capacity = capacity;
        tankSlot = new SimpleTank(this::setChanged, capacity);
        tank = Nifty.WRAPPERS.fluidHandler(new SimpleFluidHandler(tankSlot));
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level != null) {
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        tank.deserialize(tag.getCompound("Tank"));
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.put("Tank", tank.serialize());
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public Component getDisplayName() {
        return MkComponents.GUI_TANK;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int syncId, Inventory inventory, Player player) {
        return new TankMenu(syncId, inventory, containerData);
    }

}
