package design.aeonic.makeshift.content.block.node;

import design.aeonic.makeshift.api.node.OreNode;
import design.aeonic.makeshift.api.node.OreNodeTypes;
import design.aeonic.makeshift.registry.MkBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Random;

public class OreNodeBlockEntity extends BlockEntity {

    // TODO: Rewrite node picking placement, organize these method overrides

    private OreNode oreNode;

    public OreNodeBlockEntity(BlockPos pos, BlockState state) {
        super(MkBlockEntities.ORE_NODE.get(), pos, state);
    }

    @Override
    public void setLevel(Level $$0) {
        super.setLevel($$0);

        getOreNode();
    }

    public OreNode getOreNode() {
        if (oreNode == null) pickNode();
        return oreNode;
    }

    private void pickNode() {
        Random random = new Random();
        if (level instanceof ServerLevel level) {
            oreNode = OreNodeTypes.pickNodeType(level.getBiome(getBlockPos()), getBlockPos(), random).create(random);
            BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
            pos.setY(getBlockPos().getY());
            for (int x = -3; x < 4; x++) {
                pos.setX(getBlockPos().getX() + x);
                for (int z = -3; x < 4; x++) {
                    pos.setZ(getBlockPos().getZ() + z);
                    if (level.getBlockState(pos).is(Blocks.DEEPSLATE)) {
                        level.setBlock(pos, oreNode.getType().getMineralBlock().defaultBlockState(), Block.UPDATE_CLIENTS);
                    }
                }
            }
            setChanged();
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        boolean flag = oreNode == null;
        oreNode = OreNode.deserialize(tag.getCompound("Node"));
        if (oreNode == null && hasLevel()) pickNode();
        if (flag && level != null) level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        if (oreNode != null) tag.put("Node", oreNode.serialize());
    }

    @Override
    public void setChanged() {
        super.setChanged();
        if (level != null)
            level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = new CompoundTag();
        saveAdditional(tag);
        return tag;
    }

}
