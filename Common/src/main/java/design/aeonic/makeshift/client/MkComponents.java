package design.aeonic.makeshift.client;

import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;

import java.util.function.Function;

public class MkComponents {

    public static final Component GUI_TANK = new TranslatableComponent("gui.makeshift.tank");
    public static final Component GUI_PORTABLE_MINER = new TranslatableComponent("gui.makeshift.portable_miner");

    public static final Component TOOLTIP_DOES_NOT_RETAIN = tooltip("makeshift.tooltip.does_not_retain");
    public static final Component TOOLTIP_PORTABLE_MINER_PLACEMENT = tooltip("makeshift.tooltip.portable_miner.placement");
    public static final Component TOOLTIP_PORTABLE_MINER_DESCRIPTION = tooltip("makeshift.tooltip.portable_miner.description");

    public static final Component HOVER_ORE_NODE_TYPE = new TranslatableComponent("makeshift.hover_data.ore_node.type_prefix");
    public static final ComponentTemplate HOVER_ORE_NODE_PURITY = template("makeshift.hover_data.ore_node.purity");

    public static Component tooltip(String key) {
        return new TranslatableComponent(key).withStyle(ChatFormatting.GRAY);
    }

    public static ComponentTemplate template(String key, Function<Component, Component> transformer) {
        return params -> transformer.apply(template(key).resolve(params));
    }

    public static ComponentTemplate template(String key) {
        return params -> new TranslatableComponent(key, params);
    }

    @FunctionalInterface
    public interface ComponentTemplate {
        Component resolve(Object... params);
    }

}
