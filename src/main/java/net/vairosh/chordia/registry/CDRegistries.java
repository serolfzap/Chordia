package net.vairosh.chordia.registry;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.vairosh.chordia.Chordia;
import net.vairosh.chordia.symphony.Symphony;

public class CDRegistries {
    public static final ResourceKey<Registry<Symphony>> SYMPHONY = createRegistryKey("symphony");

    private static <T> ResourceKey<Registry<T>> createRegistryKey(String string) {
        return ResourceKey.createRegistryKey(new ResourceLocation(Chordia.MOD_ID, string));
    }
}
