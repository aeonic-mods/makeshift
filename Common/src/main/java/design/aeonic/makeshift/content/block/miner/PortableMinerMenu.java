package design.aeonic.makeshift.content.block.miner;

import design.aeonic.makeshift.api.machine.block.MachineMenu;
import design.aeonic.makeshift.registry.MkMenus;
import design.aeonic.makeshift.util.container.ContainerSlot;
import design.aeonic.nifty.api.network.container.ContainerFields;
import design.aeonic.nifty.api.network.container.DataField;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;

public class PortableMinerMenu extends MachineMenu {

    public PortableMinerMenu(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(4), new ContainerFields(
                new DataField.IntField(),
                new DataField.IntField(),
                new DataField.IntField()
        ));
    }

    public PortableMinerMenu(int syncId, Inventory playerInventory, Container container, ContainerData containerData) {
        super(MkMenus.PORTABLE_MINER.get(), 4, 6, syncId, playerInventory, container, containerData);
    }

    @Override
    protected void addContainerSlots() {
        addSlot(new ContainerSlot.FuelSlot(container, 0, 35, 35));
        addSlot(new ContainerSlot.OutputSlot(container, 1, 89, 35));
        addSlot(new ContainerSlot.OutputSlot(container, 2, 107, 35));
        addSlot(new ContainerSlot.OutputSlot(container, 3, 125, 35));
    }

    int getLitTime() {
        return ((ContainerFields) containerData()).getField(0);
    }

    int getLitDuration() {
        return ((ContainerFields) containerData()).getField(1);
    }

    int getExtractTime() {
        return ((ContainerFields) containerData()).getField(2);
    }

    int getExtractDuration() {
        return PortableMinerBlockEntity.EXTRACT_TIME;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

}
