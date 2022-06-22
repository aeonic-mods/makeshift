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

    private static final ResourceLocation SURFACE = Makeshift.location("surface_ore_node");
    private static final ResourceLocation CAVE = Makeshift.location("cave_ore_node");

    public static final GameObject<StructureFeature<NoneFeatureConfiguration>> SURFACE_ORE_NODE = Nifty.REGISTRY.register(
            Registry.STRUCTURE_FEATURE, SURFACE, () -> new OreNodeFeature(SURFACE, OreNodeFeature.Type.SURFACE));

    public static final GameObject<StructureFeature<NoneFeatureConfiguration>> CAVE_ORE_NODE = Nifty.REGISTRY.register(
            Registry.STRUCTURE_FEATURE, CAVE, () -> new OreNodeFeature(CAVE, OreNodeFeature.Type.CAVE));

    public static void init() {}

    public static class Configured {

        public static final GameObject<ConfiguredStructureFeature<?, ?>> SURFACE_ORE_NODE = Nifty.REGISTRY.register(
                BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, SURFACE, () -> MkStructureFeatures.SURFACE_ORE_NODE.get().configured(NoneFeatureConfiguration.INSTANCE, MkTags.Biomes.HAS_SURACE_ORE_NODE, true));

        public static final GameObject<ConfiguredStructureFeature<?, ?>> CAVE_ORE_NODE = Nifty.REGISTRY.register(
                BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, CAVE, () -> MkStructureFeatures.CAVE_ORE_NODE.get().configured(NoneFeatureConfiguration.INSTANCE, MkTags.Biomes.HAS_CAVE_ORE_NODE, true));

        public static void init() {}

    }

}
