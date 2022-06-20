package design.aeonic.makeshift.util.container;

import design.aeonic.nifty.Nifty;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.function.Predicate;

public class ContainerSlot extends Slot {

    protected final Predicate<ItemStack> mayPlace;

    public ContainerSlot(Container container, int index, int x, int y, Predicate<ItemStack> mayPlace) {
        super(container, index, x, y);
        this.mayPlace = mayPlace;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return mayPlace.test(stack);
    }

    public static class FuelSlot extends ContainerSlot {
        public FuelSlot(Container container, int index, int x, int y) {
            super(container, index, x, y, stack -> Nifty.ACCESS.getBurnTime(stack) > 0);
        }
    }

    public static class OutputSlot extends ContainerSlot {
        public OutputSlot(Container container, int index, int x, int y) {
            super(container, index, x, y, $ -> false);
        }
    }

}
