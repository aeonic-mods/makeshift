package design.aeonic.makeshift;

import design.aeonic.makeshift.api.node.OreNode;
import design.aeonic.makeshift.content.block.node.OreNodeRenderer;
import design.aeonic.makeshift.data.node.OreNodeReloadListener;
import design.aeonic.makeshift.registry.MkScreens;
import design.aeonic.makeshift.content.block.node.OreNodeBlockEntity;
import design.aeonic.makeshift.content.block.miner.PortableMinerRenderer;
import design.aeonic.makeshift.content.block.tank.TankBlockEntity;
import design.aeonic.makeshift.content.block.tank.TankRenderer;
import design.aeonic.makeshift.registry.*;
import design.aeonic.nifty.Nifty;
import design.aeonic.nifty.api.item.FluidHandler;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Makeshift {

    public static final String MOD_ID = "makeshift";
    public static final Logger LOG = LogManager.getLogger();

    public static void init() {
        // Creative tabs, blocks, items
        MkTabs.init();
        MkBlocks.init();
        MkItems.init();

        // Other registries
        MkBlockEntities.init();
        MkSoundEvents.init();
        MkLoot.init();
        MkStructureProcessors.init();
        MkStructurePieces.init();
        MkStructureFeatures.init();
        MkStructureFeatures.Configured.init();

        // Aspects
        registerAspects();
        registerAspectLookups();

        // Data
        Nifty.ACCESS.registerReloadListener(PackType.SERVER_DATA, location("ore_nodes"), OreNodeReloadListener.INSTANCE);
    }

    public static void registerAspects() {
        Nifty.ASPECTS.registerAspect(location("ore_node"), OreNode.class);
    }

    public static void registerAspectLookups() {
        // Fluids
        Nifty.ASPECTS.registerLookup(FluidHandler.class, (be, dir) -> be instanceof TankBlockEntity blockEntity ? blockEntity.tank : null);
        // Other
        Nifty.ASPECTS.registerLookup(OreNode.class, (be, dir) -> be instanceof OreNodeBlockEntity blockEntity ? blockEntity.getOreNode() : null);
    }

    public static void clientInit() {
        // Client registries
        MkScreens.init();

        // Rendering
        Nifty.ACCESS.registerBlockEntityRenderer(MkBlockEntities.TANK.get(), TankRenderer::new);
        Nifty.ACCESS.registerBlockEntityRenderer(MkBlockEntities.PORTABLE_MINER.get(), PortableMinerRenderer::new);
        Nifty.ACCESS.registerBlockEntityRenderer(MkBlockEntities.ORE_NODE.get(), OreNodeRenderer::new);
        Nifty.ACCESS.setRenderLayer(RenderType.translucent(), MkBlocks.BRASS_TANK.get());
    }

    public static ResourceLocation location(String... path) {
        return new ResourceLocation(MOD_ID, StringUtils.join(path, '/'));
    }

}
