package design.aeonic.makeshift.registry;

import design.aeonic.makeshift.Makeshift;
import design.aeonic.makeshift.content.block.miner.PortableMinerMenu;
import design.aeonic.makeshift.content.block.tank.TankMenu;
import design.aeonic.nifty.Nifty;
import design.aeonic.nifty.api.registry.GameObject;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;

public class MkMenus {

    public static final GameObject<MenuType<TankMenu>> TANK = Nifty.REGISTRY.register(Registry.MENU, Makeshift.location("tank"),
            () -> Nifty.ACCESS.menuType(TankMenu::new));

    public static final GameObject<MenuType<PortableMinerMenu>> PORTABLE_MINER = Nifty.REGISTRY.register(Registry.MENU, Makeshift.location("portable_miner"),
            () -> Nifty.ACCESS.menuType(PortableMinerMenu::new));

    public static void init() {}

 }
