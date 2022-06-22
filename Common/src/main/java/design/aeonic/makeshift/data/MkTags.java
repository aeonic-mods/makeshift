package design.aeonic.makeshift.data;

import design.aeonic.makeshift.Makeshift;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;


public class MkTags {

    public static class Biomes {
        public static final TagKey<Biome> HAS_SURACE_ORE_NODE = make(Registry.BIOME_REGISTRY, "has_surface_ore_node");
        public static final TagKey<Biome> HAS_CAVE_ORE_NODE = make(Registry.BIOME_REGISTRY, "has_cave_ore_node");

        public static final TagKey<Biome> HAS_ORE_NODE = make(Registry.BIOME_REGISTRY, "has_ore_node");
    }

    private static <T> TagKey<T> make(ResourceKey<? extends Registry<T>> registry, String path) {
        return TagKey.create(registry, Makeshift.location(path));
    }

}
