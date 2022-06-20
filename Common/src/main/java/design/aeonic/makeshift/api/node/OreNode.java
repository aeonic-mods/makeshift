package design.aeonic.makeshift.api.node;

import design.aeonic.makeshift.data.MkLoot;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

/**
 * An aspect describing an ore node and its stats.
 */
public interface OreNode {

    OreNodeType getType();

    float getPurity();

    /**
     * Gets the ore node's yields.
     * @param level the level
     * @param tool the tool being used - required for block loot tables
     * @param blockPos the position of the ore node
     * @return a list of yielded items
     */
    default List<ItemStack> yield(ServerLevel level, ItemStack tool, BlockPos blockPos) {
        LootTable table = level.getServer().getLootTables().get(getType().lootTableId());
        return table.getRandomItems(new LootContext.Builder(level)
                .withParameter(LootContextParams.TOOL, tool)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(blockPos))
                .withParameter(LootContextParams.BLOCK_ENTITY, Objects.requireNonNull(level.getBlockEntity(blockPos)))
                .withParameter(LootContextParams.BLOCK_STATE, level.getBlockState(blockPos))
                .withParameter(MkLoot.ORE_NODE_PURITY_PARAM, getPurity())
                .create(MkLoot.ORE_NODE_PARAM_SET));
    }

    default CompoundTag serialize() {
        CompoundTag tag = new CompoundTag();
        tag.putString("Type", OreNodeTypes.getKey(getType()).toString());
        tag.putFloat("Purity", getPurity());
        return tag;
    }

    static @Nullable OreNode deserialize(CompoundTag tag) {
        if (tag.contains("Type", Tag.TAG_STRING) && tag.contains("Purity", Tag.TAG_FLOAT)) {
            OreNodeType type = OreNodeTypes.getNodeType(new ResourceLocation(tag.getString("Type")));
            if (type != null)
                return new SimpleOreNode(type, tag.getFloat("Purity"));
        }
        return null;
    }

    record SimpleOreNode(OreNodeType type, float purity) implements OreNode {

        @Override
        public OreNodeType getType() {
            return type;
        }

        @Override
        public float getPurity() {
            return purity;
        }

    }

}
