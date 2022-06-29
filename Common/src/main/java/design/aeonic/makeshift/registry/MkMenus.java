package design.aeonic.makeshift.registry;

import design.aeonic.makeshift.Makeshift;
import design.aeonic.makeshift.content.block.miner.PortableMinerMenu;
import design.aeonic.makeshift.content.block.tank.TankMenu;
import design.aeonic.nifty.Nifty;
import design.aeonic.nifty.api.registry.GameObject;
import net.minecraft.core.Registry;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public class MkMenus {

    public static final GameObject<MenuType<TankMenu>> TANK = register("tank",
            () -> Nifty.ACCESS.menuType(TankMenu::new));

    public static final GameObject<MenuType<PortableMinerMenu>> PORTABLE_MINER = register("portable_miner",
            () -> Nifty.ACCESS.menuType(PortableMinerMenu::new));

    public static void init() {}

    static <T extends MenuType<?>> GameObject<T> register(String name, Supplier<T> supplier) {
        return Nifty.REGISTRY.register(Registry.MENU, Makeshift.location(name), supplier);
    }

 }
