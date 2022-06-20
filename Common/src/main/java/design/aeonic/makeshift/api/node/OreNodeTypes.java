package design.aeonic.makeshift.api.node;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import design.aeonic.makeshift.Makeshift;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import org.apache.logging.log4j.LogManager;

import javax.annotation.Nullable;
import java.util.Optional;
import java.util.Random;

/**
 * A server registry for ore node types.
 * New types are registered from {@link design.aeonic.makeshift.data.node.OreNodeReloadListener} via datapacks or (once implemented) with KJS.
 */
public class OreNodeTypes {

    private static final BiMap<ResourceLocation, OreNodeType> NODE_TYPES = HashBiMap.create();

    public static synchronized void clearNodeTypes() {
        NODE_TYPES.clear();
    }

    public static synchronized void registerNodeType(ResourceLocation id, OreNodeType type) {
        NODE_TYPES.put(id, type);
    }

    public static synchronized ResourceLocation getKey(OreNodeType nodeType) {
        return NODE_TYPES.inverse().get(nodeType);
    }

    public static synchronized OreNodeType getNodeType(ResourceLocation location) {
        return NODE_TYPES.get(location);
    }

    public static synchronized OreNodeType pickNodeType(Holder<Biome> biomeHolder, BlockPos blockPos, Random random) {
        WeightedRandomList<OreNodeType> weightedList = WeightedRandomList.create(NODE_TYPES.values().stream().filter(type -> isNodeTypeValid(type, blockPos, biomeHolder)).toList());
        Optional<OreNodeType> ret = weightedList.getRandom(random);
        return ret.orElseThrow(() -> new IllegalStateException("No available ore node types for {}! Define a fallback node with no biome tag."));
    }

    private static boolean isNodeTypeValid(OreNodeType type, BlockPos blockPos, Holder<Biome> biomeHolder) {
        if (blockPos.getY() < type.minY() || blockPos.getY() > type.maxY()) return false;
        return type.biomeTag() == null || biomeHolder.is(type.biomeTag());
    }

}
