package design.aeonic.makeshift.content.block.tank;

import design.aeonic.makeshift.api.machine.block.MachineMenu;
import design.aeonic.makeshift.registry.MkMenus;
import design.aeonic.nifty.api.fluid.AbstractTank;
import design.aeonic.nifty.api.fluid.FluidStack;
import design.aeonic.nifty.api.network.container.ContainerFields;
import design.aeonic.nifty.api.network.container.DataField;
import net.minecraft.core.Registry;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.material.Fluid;

public class TankMenu extends MachineMenu {

    protected final DummyTank tank;

    public TankMenu(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new ContainerFields(
                new DataField.ShortField(), new DataField.IntField(), new DataField.IntField()
        ));
    }

    public TankMenu(int syncId, Inventory playerInventory, ContainerData containerData) {
        super(MkMenus.TANK.get(), 0, containerData.getCount(), syncId, playerInventory, null, containerData);
        this.tank = new DummyTank();
    }

    public AbstractTank getTank() {
        updateTank();
        return tank;
    }

    void updateTank() {
        var data = (ContainerFields) containerData;

        FluidStack old = tank.get();
        Fluid fluid = Registry.FLUID.byId((short) data.getField(0));
        if (!old.is(fluid)) tank.set(FluidStack.of(fluid, old.getAmount() == 0 ? 1 : old.getAmount(), old.getTag()));

        tank.get().setAmount(data.getField(1));
        tank.capacity = data.getField(2);
    }

    @Override
    protected void addContainerSlots() {}

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
