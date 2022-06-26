package design.aeonic.makeshift.api.machine.block;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class MachineMenu extends AbstractContainerMenu {

    protected final int containerCount;
    protected final int containerDataCount;

    protected final Inventory playerInventory;
    protected final @Nullable
    Container container;
    protected final @Nullable
    ContainerData containerData;

    /**
     * Client menu constructor.
     *
     * @param menuType           the menu type
     * @param containerCount     the container slot count; if 0, the menu has no container
     * @param containerDataCount the data slot count; if 0, the menu has no container data
     * @param syncId             the sync id
     * @param playerInventory    the player inventory
     */
    public MachineMenu(@Nullable MenuType<?> menuType, int containerCount, int containerDataCount, int syncId, Inventory playerInventory) {
        this(menuType, containerCount, containerDataCount, syncId, playerInventory, new SimpleContainer(containerCount), new SimpleContainerData(containerDataCount));
    }

    /**
     * Server menu constructor.
     *
     * @param menuType           the menu type
     * @param containerCount     the container slot count; if 0, the menu has no container
     * @param containerDataCount the data slot count; if 0, the menu has no container data
     * @param syncId             the sync id
     * @param playerInventory    the player inventory
     * @param container          the container, or null if this menu has none
     * @param containerData      the container data, or null if this menu has none
     */
    public MachineMenu(@Nullable MenuType<?> menuType, int containerCount, int containerDataCount, int syncId, Inventory playerInventory, @Nullable Container container, @Nullable ContainerData containerData) {
        super(menuType, syncId);

        this.containerCount = containerCount;
        this.containerDataCount = containerDataCount;

        this.playerInventory = playerInventory;
        this.container = container;
        this.containerData = containerData;

        if (hasContainer()) {
            checkContainerSize(container(), containerCount);
            addContainerSlots();
        }

        addPlayerSlots();

        if (hasContainerData()) {
            checkContainerDataCount(containerData(), containerDataCount);
            addDataSlots(containerData());
        }
    }

    protected abstract void addContainerSlots();

    void addPlayerSlots() {
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

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack ret = ItemStack.EMPTY;
        Slot slot = slots.get(index);
        if (slot.hasItem()) {
            ItemStack movingStack = slot.getItem();
            ret = movingStack.copy();
            if (index >= containerCount) {
                boolean triedMove = false;
                for (int i = 0; i < containerCount; i++) {
                    if (slots.get(i).mayPlace(movingStack)) {
                        if (!moveItemStackTo(movingStack, i, i + 1, false))
                            return ItemStack.EMPTY;
                        triedMove = true;
                    }
                }
                if (!triedMove) {
                    if (index < 27 + containerCount) {
                        if (!moveItemStackTo(movingStack, 31, 40, false))
                            return ItemStack.EMPTY;
                    } else if (index >= 27 + containerCount && index < 36 + containerCount && !moveItemStackTo(movingStack, containerCount, 27 + containerCount, false))
                        return ItemStack.EMPTY;
                }
            } else if (!moveItemStackTo(movingStack, containerCount, 36 + containerCount, false))
                return ItemStack.EMPTY;

            if (movingStack.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();

            if (movingStack.getCount() == ret.getCount()) return ItemStack.EMPTY;

            slot.onTake(player, movingStack);
        }

        return ret;
    }

    public @Nonnull
    Container container() {
        if (!hasContainer()) throw new AssertionError("Attempted to access a container from a menu without one!");
        return container;
    }

    public @Nonnull
    ContainerData containerData() {
        if (!hasContainerData())
            throw new AssertionError("Attempted to access container data from a menu without any!");
        return containerData;
    }

    boolean hasContainer() {
        return containerCount > 0;
    }

    boolean hasContainerData() {
        return containerDataCount > 0;
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }

    @FunctionalInterface
    public interface Constructor<M extends MachineMenu> {
        M apply(int syncId, Inventory playerInventory, @Nullable Container container, @Nullable ContainerData containerData);
    }

}
