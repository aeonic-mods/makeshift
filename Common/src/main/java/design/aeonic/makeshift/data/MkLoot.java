package design.aeonic.makeshift.data;

import design.aeonic.makeshift.Makeshift;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class MkLoot {

    public static final LootContextParam<Float> ORE_NODE_PURITY_PARAM = new LootContextParam<>(Makeshift.location("ore_node_purity"));

    public static final LootContextParamSet ORE_NODE_PARAM_SET = LootContextParamSet.builder()
            .required(LootContextParams.BLOCK_STATE)
            .required(LootContextParams.ORIGIN)
            .required(LootContextParams.TOOL)
            .required(LootContextParams.BLOCK_ENTITY)
            .required(ORE_NODE_PURITY_PARAM)
            .build();

}
