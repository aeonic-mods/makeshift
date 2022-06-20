package design.aeonic.makeshift.content.block.miner;

import design.aeonic.makeshift.registry.MkMenus;
import design.aeonic.makeshift.util.container.ContainerSlot;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class PortableMinerMenu extends AbstractContainerMenu {

    protected final Inventory playerInventory;
    protected final Container container;
    protected final ContainerData containerData;

    public PortableMinerMenu(int syncId, Inventory playerInventory) {
        this(syncId, playerInventory, new SimpleContainer(4), new SimpleContainerData(4));
    }

    public PortableMinerMenu(int syncId, Inventory playerInventory, Container container, ContainerData containerData) {
        super(MkMenus.PORTABLE_MINER.get(), syncId);
        this.playerInventory = playerInventory;

        checkContainerSize(container, 4);
        checkContainerDataCount(containerData, 4);
        this.container = container;
        this.containerData = containerData;

        addDataSlots(containerData);

        addSlot(new ContainerSlot.FuelSlot(container, 0, 35, 35));
        addSlot(new ContainerSlot.OutputSlot(container, 1, 89, 35));
        addSlot(new ContainerSlot.OutputSlot(container, 2, 107, 35));
        addSlot(new ContainerSlot.OutputSlot(container, 3, 125, 35));

        int i;
        for (i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        for (i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
        }
    }

    int getLitTime() {
        return containerData.get(0);
    }

    int getLitDuration() {
        return containerData.get(1);
    }

    int getExtractTime() {
        return containerData.get(2);
    }

    int getExtractDuration() {
        return containerData.get(3);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

}
