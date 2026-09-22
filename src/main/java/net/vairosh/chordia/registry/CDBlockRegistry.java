package net.vairosh.chordia.registry;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.vairosh.chordia.Chordia;

public class CDBlockRegistry {
    public static Block registerBlock(String name, Block block) {
        CDItemRegistry.registeItem(name, new BlockItem(block, new FabricItemSettings()));
        return Registry.register(BuiltInRegistries.BLOCK, Chordia.id(name), block);
    }

    public static Block registerBlockWithoutItem(String name, Block block) {
        return Registry.register(BuiltInRegistries.BLOCK, Chordia.id(name), block);
    }
}
