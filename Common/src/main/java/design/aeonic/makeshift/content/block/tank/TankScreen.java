package design.aeonic.makeshift.content.block.tank;

import design.aeonic.nifty.api.client.screen.ModularMenuScreen;
import design.aeonic.nifty.api.client.ui.UiSets;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class TankScreen extends ModularMenuScreen<TankMenu> {

    public TankScreen(TankMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();

        addUiElement(UiSets.Vanilla.TANK_TWO_TWO, menu::getTank, 69, 25);
    }

}
