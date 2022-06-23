package design.aeonic.makeshift.data.node;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import design.aeonic.makeshift.registry.MkLoot;
import net.minecraft.util.Mth;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public class OreNodePurityRandom implements NumberProvider {

    public static final OreNodePurityRandom INSTANCE = new OreNodePurityRandom();

    @Override
    public float getFloat(LootContext lootContext) {
        float purity = lootContext.getParam(MkLoot.ORE_NODE_PURITY_PARAM);
        int floor = Mth.floor(purity);
        float remainder = purity - floor;
        return (floor + ((lootContext.getRandom().nextFloat() < remainder) ? 1 : 0));
    }

    @Override
    public LootNumberProviderType getType() {
        return MkLoot.ORE_NODE_RANDOM_PROVIDER.get();
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<OreNodePurityRandom> {

        @Override
        public void serialize(JsonObject jsonObject, OreNodePurityRandom oreNodePurity, JsonSerializationContext jsonSerializationContext) {}

        @Override
        public OreNodePurityRandom deserialize(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
            return INSTANCE;
        }

    }

}
