package design.aeonic.makeshift.api.machine;

import design.aeonic.nifty.Nifty;
import design.aeonic.nifty.api.energy.EnergyHandler;
import design.aeonic.nifty.api.item.ItemHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;

import java.util.function.Supplier;

/**
 * Describes a machine's consumption, be it power, items etc. <br><br>
 * Contains compoundtag serialization methods that are called automatically in {@link design.aeonic.makeshift.api.machine.block.MachineBlockEntity}.
 */
public interface MachineConsumption {

    /**
     * Whether the machine can run this tick.
     */
    boolean canRun();

    /**
     * Consumes whatever necessary for the machine to run this tick. Only called if {@link #canRun()} is true.
     */
    void run();

    CompoundTag serialize();

    void deserialize(CompoundTag tag);

    /**
     * Consumes furnace fuels when necessary.
     */
    class FuelItemConsumption implements MachineConsumption {

        private final Supplier<ItemHandler> itemHandler;
        private final int slot;

        private int litTime = 0;
        private int litDuration = 0;

        public FuelItemConsumption(Supplier<ItemHandler> itemHandler, int slot) {
            this.itemHandler = itemHandler;
            this.slot = slot;
        }

        @Override
        public boolean canRun() {
//            Makeshift.LOG.info("handler {} slot {} stack {} burn time {}", itemHandler.get(), );
            return itemHandler.get() != null && (litTime > 0 || Nifty.ACCESS.getBurnTime(itemHandler.get().extract(slot, 1, true)) > 0);
        }

        @Override
        public void run() {
            if (litTime > 0) litTime --;
            else {
                ItemStack stack = itemHandler.get().extract(slot, 1, false);
                if (stack.getItem().hasCraftingRemainingItem()) {
                    itemHandler.get().set(slot, stack.getItem().getCraftingRemainingItem().getDefaultInstance());
                }
                litDuration = Nifty.ACCESS.getBurnTime(stack);
                litTime = litDuration;
            }
        }

        @Override
        public CompoundTag serialize() {
            CompoundTag tag = new CompoundTag();
            tag.putInt("LitTime", litTime);
            tag.putInt("LitDuration", litDuration);
            return tag;
        }

        @Override
        public void deserialize(CompoundTag tag) {
            litTime = tag.getInt("LitTime");
            litDuration = tag.getInt("LitDuration");
        }

        public int getLitTime() {
            return litTime;
        }

        public int getLitDuration() {
            return litDuration;
        }

    }

    /**
     * Consumes a mutable amount of energy per tick.
     */
    class EnergyConsumption implements MachineConsumption {

        private final EnergyHandler energyHandler;
        private long energyPerTick;

        public EnergyConsumption(EnergyHandler energyHandler, long energyPerTick) {
            this.energyHandler = energyHandler;
            this.energyPerTick = energyPerTick;
        }

        public long getEnergyPerTick() {
            return energyPerTick;
        }

        public void setEnergyPerTick(long energyPerTick) {
            this.energyPerTick = energyPerTick;
        }

        @Override
        public boolean canRun() {
            return energyHandler.extract(energyPerTick, true) == energyPerTick;
        }

        @Override
        public void run() {
            energyHandler.extract(energyPerTick, false);
        }

        @Override
        public CompoundTag serialize() {
            CompoundTag tag = new CompoundTag();
            tag.putLong("EnergyPerTick", energyPerTick);
            return tag;
        }

        @Override
        public void deserialize(CompoundTag tag) {
            energyPerTick = tag.getLong("EnergyPerTick");
        }

    }

    class CreativeConsumption implements MachineConsumption {

        @Override
        public boolean canRun() {
            return true;
        }

        @Override
        public void run() {}

        @Override
        public CompoundTag serialize() {
            return new CompoundTag();
        }

        @Override
        public void deserialize(CompoundTag tag) {}

    }

}
