package design.aeonic.makeshift.data;

import design.aeonic.makeshift.Makeshift;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;


public class MkTags {

    public static class Biomes {
        public static final TagKey<Biome> HAS_GRASSY_ORE_NODE = make(Registry.BIOME_REGISTRY, "has_grassy_ore_node");
        public static final TagKey<Biome> HAS_SANDY_ORE_NODE = make(Registry.BIOME_REGISTRY, "has_sandy_ore_node");
        public static final TagKey<Biome> HAS_ROCKY_ORE_NODE = make(Registry.BIOME_REGISTRY, "has_rocky_ore_node");
    }

    private static <T> TagKey<T> make(ResourceKey<? extends Registry<T>> registry, String path) {
        return TagKey.create(registry, Makeshift.location(path));
    }

}
