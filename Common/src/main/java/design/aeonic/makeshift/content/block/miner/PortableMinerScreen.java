package design.aeonic.makeshift.content.block.miner;

import design.aeonic.nifty.api.client.screen.ModularMenuScreen;
import design.aeonic.nifty.api.client.ui.UiSets;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

public class PortableMinerScreen extends ModularMenuScreen<PortableMinerMenu> {

    public PortableMinerScreen(PortableMinerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();

        addUiElement(UiSets.Vanilla.ITEM_SLOT_NORMAL, () -> null, 34, 34);
        addUiElement(UiSets.Vanilla.ITEM_SLOT_NORMAL, () -> null, 88, 34);
        addUiElement(UiSets.Vanilla.ITEM_SLOT_NORMAL, () -> null, 106, 34);
        addUiElement(UiSets.Vanilla.ITEM_SLOT_NORMAL, () -> null, 124, 34);

        addUiElement(UiSets.Vanilla.BURN_TIME_INDICATOR,
                () -> this::getRemainingBurnTime, 36, 55);

        addUiElement(UiSets.Vanilla.RECIPE_ARROW_FILLING,
                () -> this::getExtractProgress, 59, 35);
    }

    protected float getExtractProgress() {
        return menu.getExtractTime() / (float) menu.getExtractDuration();
    }

    protected float getRemainingBurnTime() {
        return menu.getLitTime() / (float) menu.getLitDuration();
    }

}
