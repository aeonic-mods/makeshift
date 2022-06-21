package design.aeonic.makeshift.registry;

import design.aeonic.makeshift.Makeshift;
import design.aeonic.makeshift.data.MkTags;
import design.aeonic.makeshift.worldgen.structure.node.OreNodeFeature;
import design.aeonic.nifty.Nifty;
import design.aeonic.nifty.api.registry.GameObject;
import net.minecraft.core.Registry;
import net.minecraft.data.BuiltinRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class MkStructureFeatures {

    private static final ResourceLocation GRASSY = Makeshift.location("grassy_ore_node");
    private static final ResourceLocation SANDY = Makeshift.location("sandy_ore_node");
    private static final ResourceLocation ROCKY = Makeshift.location("rocky_ore_node");

    public static final GameObject<StructureFeature<NoneFeatureConfiguration>> GRASSY_ORE_NODE = Nifty.REGISTRY.register(
            Registry.STRUCTURE_FEATURE, GRASSY, () -> new OreNodeFeature(GRASSY));

    public static final GameObject<StructureFeature<NoneFeatureConfiguration>> SANDY_ORE_NODE = Nifty.REGISTRY.register(
            Registry.STRUCTURE_FEATURE, SANDY, () -> new OreNodeFeature(SANDY));

    public static final GameObject<StructureFeature<NoneFeatureConfiguration>> ROCKY_ORE_NODE = Nifty.REGISTRY.register(
            Registry.STRUCTURE_FEATURE, ROCKY, () -> new OreNodeFeature(ROCKY));

    public static void init() {}

    public static class Configured {

        public static final GameObject<ConfiguredStructureFeature<?, ?>> GRASSY_ORE_NODE = Nifty.REGISTRY.register(
                BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, GRASSY, () -> MkStructureFeatures.GRASSY_ORE_NODE.get().configured(NoneFeatureConfiguration.INSTANCE, MkTags.Biomes.HAS_GRASSY_ORE_NODE, true));

        public static final GameObject<ConfiguredStructureFeature<?, ?>> SANDY_ORE_NODE = Nifty.REGISTRY.register(
                BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, SANDY, () -> MkStructureFeatures.SANDY_ORE_NODE.get().configured(NoneFeatureConfiguration.INSTANCE, MkTags.Biomes.HAS_SANDY_ORE_NODE, true));

        public static final GameObject<ConfiguredStructureFeature<?, ?>> ROCKY_ORE_NODE = Nifty.REGISTRY.register(
                BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, ROCKY, () -> MkStructureFeatures.ROCKY_ORE_NODE.get().configured(NoneFeatureConfiguration.INSTANCE, MkTags.Biomes.HAS_ROCKY_ORE_NODE, true));

        public static void init() {}

    }

}
