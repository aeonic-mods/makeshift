package design.aeonic.makeshift.content.block.tank;

import design.aeonic.makeshift.registry.MkMenus;
import design.aeonic.nifty.api.core.Constants;
import design.aeonic.nifty.api.fluid.AbstractTank;
import design.aeonic.nifty.api.fluid.FluidStack;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import javax.annotation.Nullable;

public class TankMenu extends AbstractContainerMenu {

    protected @Nullable TankBlockEntity blockEntity;
    protected final Inventory playerInventory;
    protected final ContainerData containerData;
    protected final DummyTank tank;

    public TankMenu(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainerData(5));
    }

    public TankMenu(int syncId, Inventory playerInventory, ContainerData containerData) {
        super(MkMenus.TANK.get(), syncId);
        this.playerInventory = playerInventory;
        this.tank = new DummyTank();
        this.containerData = containerData;

        checkContainerDataCount(containerData, 5);
        addDataSlots(containerData);
    }

    public AbstractTank getTank() {
        updateTank();
        return tank;
    }

    void updateTank() {
        // I have only a vague understanding of ContainerData and it took me an hour to get this cursed thing working.
        // It's bad. But it works, so I'm leaving it as is.
        FluidStack old = tank.get();
        Fluid fluid = Registry.FLUID.byId(containerData.get(0));
        if (!old.is(fluid)) tank.set(FluidStack.of(fluid, old.getAmount() == 0 ? 1 : old.getAmount(), old.getTag()));

        FluidStack stack = tank.get();
        stack.setAmount((stack.getAmount() & 0xffff0000) + (containerData.get(1) & 0xffff));
        stack.setAmount((stack.getAmount() & 0x0000ffff) | (containerData.get(2) << 16));

        tank.capacity = (tank.capacity & 0xffff0000) + (containerData.get(3) & 0xffff);
        tank.capacity = (tank.capacity & 0x0000ffff) | (containerData.get(4) << 16);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    public static class DummyTank extends AbstractTank {
        protected int capacity;

        public DummyTank() {
            capacity = 0;
        }

        @Override
        public boolean allowedInSlot(FluidStack stack) {
            return true;
        }

        @Override
        public int getCapacity() {
            return capacity;
        }

        @Override
        public void onChanged() {}
    }

}
