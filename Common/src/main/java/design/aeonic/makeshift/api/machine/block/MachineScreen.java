package design.aeonic.makeshift.api.machine.block;

import design.aeonic.nifty.api.client.screen.ModularMenuScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public abstract class MachineScreen<M extends MachineMenu> extends ModularMenuScreen<M> {

    public MachineScreen(M menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);

        titleLabelY = 7;
    }

}
