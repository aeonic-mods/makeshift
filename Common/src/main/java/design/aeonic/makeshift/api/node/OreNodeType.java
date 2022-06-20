package design.aeonic.makeshift.api.node;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import design.aeonic.makeshift.data.MkTags;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.util.random.Weight;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Describes a type of ore node that can generate in the world.
 *
 * @param displayName     the node type's display name to show on hover on the client, can be a raw string or a localization key
 * @param lootTableId     the ID of the loot table to use for node outputs
 * @param mineralBlock    the ID of the block to mix in to the structure and use as a mineral texture in the model
 * @param selectionWeight the weight of this type (relative to others) when one is being selected for any given ore node block - higher will be chosen more often.
 * @param biomeTag        the biome tag this node can generate in; if null, it can generate anywhere
 * @param minY            the minimum y level this node can generate at
 * @param maxY            the maximum y level this node can generate at
 * @param minPurity       the minimum purity of this node; purity is counted as luck towards the given loot table
 * @param maxPurity       the maximum purity of this node; purity is counted as luck towards the given loot table
 */
public record OreNodeType(String displayName,
                          ResourceLocation lootTableId, ResourceLocation mineralBlock,
                          int selectionWeight,
                          @Nullable TagKey<Biome> biomeTag, int minY, int maxY, float minPurity,
                          float maxPurity) implements WeightedEntry {

    public static final Codec<OreNodeType> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("displayName").forGetter(OreNodeType::displayName),
            ResourceLocation.CODEC.fieldOf("lootTableId").forGetter(OreNodeType::lootTableId),
            ResourceLocation.CODEC.fieldOf("mineralBlock").forGetter(OreNodeType::mineralBlock),
            Codec.intRange(1, Integer.MAX_VALUE).optionalFieldOf("selectionWeight", 1).forGetter(OreNodeType::selectionWeight),
            TagKey.codec(Registry.BIOME_REGISTRY).optionalFieldOf("biomeTag", MkTags.Biomes.HAS_ORE_NODE).forGetter(OreNodeType::biomeTag),
            Codec.intRange(-64, 318).fieldOf("minY").forGetter(OreNodeType::minY),
            Codec.intRange(-64, 318).fieldOf("maxY").forGetter(OreNodeType::maxY),
            Codec.floatRange(0, Float.MAX_VALUE).optionalFieldOf("minPurity", .5f).forGetter(OreNodeType::minPurity),
            Codec.floatRange(0, Float.MAX_VALUE).optionalFieldOf("maxPurity", 2f).forGetter(OreNodeType::maxPurity)
    ).apply(instance, OreNodeType::new));

    public Block getMineralBlock() {
        return Registry.BLOCK.get(mineralBlock);
    }

    @Override
    public Weight getWeight() {
        return Weight.of(selectionWeight);
    }

    public OreNode create(Random random) {
        return new OreNode.SimpleOreNode(this, minPurity + random.nextFloat() * (maxPurity - minPurity));
    }

}
