package net.vairosh.chordia.registry;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.vairosh.chordia.Chordia;

public class CDItemRegistry {
    public static Item registeItem(String name, Item item) {
        return Registry.register(BuiltInRegistries.ITEM, Chordia.id(name), item);
    }
}
