package design.aeonic.makeshift.registry;

import design.aeonic.makeshift.Makeshift;
import design.aeonic.nifty.Nifty;
import design.aeonic.nifty.api.registry.GameObject;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

public class MkSoundEvents {

    public static final GameObject<SoundEvent> PORTABLE_MINER_RUN = register("machine.portable_miner.run");
    public static final GameObject<SoundEvent> PORTABLE_MINER_STOP = register("machine.portable_miner.stop");

    public static void init() {}

    private static GameObject<SoundEvent> register(String name) {
        ResourceLocation key = Makeshift.location(name);
        return Nifty.REGISTRY.register(Registry.SOUND_EVENT, key, () -> new SoundEvent(key));
    }

}
