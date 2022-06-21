package design.aeonic.makeshift.content.block.node;

import design.aeonic.makeshift.api.node.OreNode;
import design.aeonic.makeshift.api.node.OreNodeType;
import design.aeonic.makeshift.registry.MkBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import java.util.Random;

public class OreNodeBlockEntity extends BlockEntity {

    private OreNode oreNode;

    public OreNodeBlockEntity(BlockPos pos, BlockState state) {
        super(MkBlockEntities.ORE_NODE.get(), pos, state);
    }

    // Node type is assigned during structure generation
    public void setNodeType(OreNodeType type, Random random) {
        oreNode = type.create(random);
        setChanged();
    }

    @Override
    public void setChanged() {
        if (level != null) level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
    }

    public OreNode getOreNode() {
        return oreNode;
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        oreNode = OreNode.deserialize(tag.getCompound("Node"));
        if (level != null) level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), Block.UPDATE_CLIENTS);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);

        if (oreNode != null) tag.put("Node", oreNode.serialize());
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        CompoundTag tag = super.getUpdateTag();
        saveAdditional(tag);
        return tag;
    }

}
