package design.aeonic.makeshift.data.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import design.aeonic.makeshift.registry.MkLoot;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.providers.number.LootNumberProviderType;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;

public class OreNodePurityProvider implements NumberProvider {

    public static final OreNodePurityProvider INSTANCE = new OreNodePurityProvider();

    @Override
    public float getFloat(LootContext lootContext) {
        return lootContext.getParam(MkLoot.ORE_NODE_PURITY_PARAM);
    }

    @Override
    public LootNumberProviderType getType() {
        return MkLoot.ORE_NODE_PURITY_PROVIDER.get();
    }

    public static class Serializer implements net.minecraft.world.level.storage.loot.Serializer<OreNodePurityProvider> {

        @Override
        public void serialize(JsonObject jsonObject, OreNodePurityProvider oreNodePurity, JsonSerializationContext jsonSerializationContext) {}

        @Override
        public OreNodePurityProvider deserialize(JsonObject jsonObject, JsonDeserializationContext jsonDeserializationContext) {
            return INSTANCE;
        }

    }

}
