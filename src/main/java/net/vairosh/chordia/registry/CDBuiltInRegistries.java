package net.vairosh.chordia.registry;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.minecraft.core.Registry;
import net.vairosh.chordia.symphony.Symphony;

public class CDBuiltInRegistries {
    public static final Registry<Symphony> SYMPHONY = FabricRegistryBuilder.createSimple(CDRegistries.SYMPHONY).attribute(RegistryAttribute.SYNCED).attribute(RegistryAttribute.PERSISTED).buildAndRegister();
}
