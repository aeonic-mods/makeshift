package design.aeonic.makeshift.api.machine;

import design.aeonic.makeshift.api.client.ui.MkUiSets;
import design.aeonic.nifty.api.client.ui.template.StaticUiElementTemplate;

public enum MachineTier {

    CRUDE(MkUiSets.Crude.BACKGROUND),
    KINETIC(MkUiSets.Kinetic.BACKGROUND),
    DIGITAL(MkUiSets.Digital.BACKGROUND),
    VANGUARD(MkUiSets.Vanguard.BACKGROUND),
    CREATIVE(MkUiSets.Vanguard.BACKGROUND);

    public final StaticUiElementTemplate defaultGuiBackground;

    MachineTier(StaticUiElementTemplate defaultGuiBackground) {
        this.defaultGuiBackground = defaultGuiBackground;
    }

}
