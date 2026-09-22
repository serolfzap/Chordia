package net.vairosh.chordia.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.vairosh.chordia.Chordia;
import net.vairosh.chordia.registry.CDBlockRegistry;

public class CDBlocks {
    public static final Block RESONITE_ORE = CDBlockRegistry.registerBlock("resonite_ore", new DropExperienceBlock(FabricBlockSettings.copyOf(Blocks.DIAMOND_ORE)));

    public static void register() {
        Chordia.info("Registering Blocks");
    }
}
