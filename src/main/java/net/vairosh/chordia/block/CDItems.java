package net.vairosh.chordia.block;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.world.item.Item;
import net.vairosh.chordia.Chordia;
import net.vairosh.chordia.registry.CDItemRegistry;

public class CDItems {
    public static final Item ECHO_CODEX = CDItemRegistry.registeItem("echo_codex", new Item(new FabricItemSettings()));

    public static void register() {
        Chordia.info("Registering Items");
    }
}
