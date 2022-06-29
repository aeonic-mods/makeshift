package design.aeonic.makeshift.content.block.miner;

import design.aeonic.makeshift.api.client.ui.MkUiSets;
import design.aeonic.makeshift.api.machine.block.MachineScreen;
import design.aeonic.makeshift.client.MkComponents;
import design.aeonic.nifty.api.client.ui.UiSets;
import design.aeonic.nifty.api.client.ui.template.BooleanUiElementTemplate;
import design.aeonic.nifty.api.client.ui.template.FillingUiElementTemplate;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

import java.util.Collections;
import java.util.List;

public class PortableMinerScreen extends MachineScreen<PortableMinerMenu> {

    private static final List<Component> INVALID_LOCATION = List.of(MkComponents.TOOLTIP_PORTABLE_MINER_PLACEMENT);

    public PortableMinerScreen(PortableMinerMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
    }

    @Override
    protected void init() {
        super.init();

        setBackground(MkUiSets.Crude.BACKGROUND);

        addSlot(0, UiSets.Vanilla.ITEM_SLOT_NORMAL);
        addSlot(1, UiSets.Vanilla.ITEM_SLOT_NORMAL);
        addSlot(2, UiSets.Vanilla.ITEM_SLOT_NORMAL);
        addSlot(3, UiSets.Vanilla.ITEM_SLOT_NORMAL);

        BooleanUiElementTemplate.Context<FillingUiElementTemplate.FillLevel, Void> arrowContext = new BooleanUiElementTemplate.SimpleContext<>(
                menu::canRun, () -> this::getExtractProgress, () -> null);

        addUiElement(UiSets.Vanilla.RECIPE_ARROW_FILLING_TOGGLABLE, () -> arrowContext, 59, 35)
                .setTooltip(() -> menu.canRun() ? Collections.emptyList() : INVALID_LOCATION);

        addUiElement(UiSets.Vanilla.BURN_TIME_INDICATOR, () -> this::getRemainingBurnTime, 36, 55);
    }

    protected float getExtractProgress() {
        return menu.getExtractTime() / (float) menu.getExtractDuration();
    }

    protected float getRemainingBurnTime() {
        return menu.getLitTime() / (float) menu.getLitDuration();
    }

}
