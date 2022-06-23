package design.aeonic.makeshift.registry;

import design.aeonic.makeshift.Makeshift;
import design.aeonic.makeshift.data.node.OreNodePurityProvider;
import design.aeonic.makeshift.data.node.OreNodePurityRandom;
import design.aeonic.nifty.Nifty;
import design.aeonic.nifty.api.registry.GameObject;
import net.minecraft.core.Registry;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;

public class MkLoot {

    public static final LootContextParam<Float> ORE_NODE_PURITY_PARAM = new LootContextParam<>(Makeshift.location("ore_node_purity"));

    public static final LootContextParamSet ORE_NODE_PARAM_SET = LootContextParamSet.builder()
            .required(LootContextParams.BLOCK_STATE)
            .required(LootContextParams.ORIGIN)
            .required(LootContextParams.TOOL)
            .required(LootContextParams.BLOCK_ENTITY)
            .required(ORE_NODE_PURITY_PARAM)
            .build();

    public static final GameObject<LootNumberProviderType> ORE_NODE_PURITY_PROVIDER = Nifty.REGISTRY.register(
            Registry.LOOT_NUMBER_PROVIDER_TYPE, Makeshift.location("ore_node_purity"), () -> new LootNumberProviderType(new OreNodePurityProvider.Serializer()));

    public static final GameObject<LootNumberProviderType> ORE_NODE_RANDOM_PROVIDER = Nifty.REGISTRY.register(
            Registry.LOOT_NUMBER_PROVIDER_TYPE, Makeshift.location("ore_node_purity_random"), () -> new LootNumberProviderType(new OreNodePurityRandom.Serializer()));

    public static void init() {}

}
