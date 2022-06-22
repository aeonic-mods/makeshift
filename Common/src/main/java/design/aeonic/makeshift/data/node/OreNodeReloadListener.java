package design.aeonic.makeshift.data.node;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.mojang.serialization.JsonOps;
import design.aeonic.makeshift.Makeshift;
import design.aeonic.makeshift.api.node.OreNodeType;
import design.aeonic.makeshift.api.node.OreNodeTypes;
import design.aeonic.nifty.Nifty;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.Map;

public class OreNodeReloadListener extends SimpleJsonResourceReloadListener {

    public static final OreNodeReloadListener INSTANCE = new OreNodeReloadListener("ore_node_types");

    public OreNodeReloadListener(String folder) {
        super(new GsonBuilder().create(), folder);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> resourceLocationJsonElementMap, ResourceManager resourceManager, ProfilerFiller profilerFiller) {
        OreNodeTypes.clearNodeTypes();
        resourceLocationJsonElementMap.forEach((key, json) -> {
            try {
                OreNodeType.CODEC.decode(JsonOps.INSTANCE, json).get()
                        .ifLeft(pair -> {
                            String modDep = pair.getFirst().requiresMod();
                            if (modDep.equals(Makeshift.MOD_ID) || Nifty.PLATFORM.isModLoaded(modDep)) OreNodeTypes.registerNodeType(key, pair.getFirst());
                        })
                        .ifRight(partial -> Makeshift.LOG.error("Failed to parse node type {}: {}", key, partial.message()));
            } catch (Exception e) {
                Makeshift.LOG.error("Error loading node type {}", key);
                e.printStackTrace();
            }
        });
    }

}
