package net.vairosh.chordia.symphony;

import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

import java.util.List;

public class AbyssalSymphony extends Symphony{
    @Override
    public List<Enchantment> getRequiredEnchantments() {
        return List.of(Enchantments.AQUA_AFFINITY);
    }
}
