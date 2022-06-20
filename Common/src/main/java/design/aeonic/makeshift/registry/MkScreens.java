package design.aeonic.makeshift.registry;

import design.aeonic.makeshift.content.block.miner.PortableMinerScreen;
import design.aeonic.makeshift.content.block.tank.TankScreen;
import design.aeonic.makeshift.registry.MkMenus;
import design.aeonic.nifty.Nifty;

public class MkScreens {

    public static void init() {
        Nifty.ACCESS.registerScreen(MkMenus.TANK.get(), TankScreen::new);
        Nifty.ACCESS.registerScreen(MkMenus.PORTABLE_MINER.get(), PortableMinerScreen::new);
    }

}
