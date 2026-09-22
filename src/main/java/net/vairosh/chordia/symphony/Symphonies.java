package net.vairosh.chordia.symphony;

import net.minecraft.core.Registry;
import net.vairosh.chordia.Chordia;
import net.vairosh.chordia.registry.CDBuiltInRegistries;

public class Symphonies {
    public static final Symphony ABYSSAL = registerSymphony("abyssal", new AbyssalSymphony());
    private static Symphony registerSymphony(String name, Symphony symphony) {
        return Registry.register(CDBuiltInRegistries.SYMPHONY, Chordia.id(name), symphony);
    }

    public static void register(){
        Chordia.info("Registering Symphonies, Harmonies and something more?");
    }
}
