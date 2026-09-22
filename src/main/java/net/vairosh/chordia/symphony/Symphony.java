package net.vairosh.chordia.symphony;

import net.minecraft.ChatFormatting;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.vairosh.chordia.registry.CDBuiltInRegistries;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class Symphony {
    @Nullable
    private ResourceLocation id;

    @Nullable
    protected String descriptionId;


    private ResourceLocation getId() {
        if (this.id == null) {
            this.id = CDBuiltInRegistries.SYMPHONY.getKey(this);
        }

        return id;
    }

    protected String getOrCreateDescriptionId() {
        if (this.descriptionId == null) {
            this.descriptionId = Util.makeDescriptionId("symphony", CDBuiltInRegistries.SYMPHONY.getKey(this));
        }
        return this.descriptionId;
    }

    public String getDescriptionId() {
        return this.getOrCreateDescriptionId();
    }

    public Component getFullname() {
        MutableComponent component = Component.translatable(this.getDescriptionId());

        if (isHarmony()) {
            component.withStyle(ChatFormatting.DARK_BLUE);
        } else {
            component.withStyle(ChatFormatting.BLUE);
        }

        return component;
    }

    public List<Enchantment> getRequiredEnchantments() {
        return List.of();
    }

    private boolean isHarmony() {
        return getRequiredEnchantments().size() > 1;
    }

    public final boolean isCompatibleWith(Symphony symphony) {
        return this.checkCompatibility(symphony) && symphony.checkCompatibility(this);
    }

    protected boolean checkCompatibility(Symphony symphony) {
        return this != symphony;
    }

    public void doPostAttack(LivingEntity attacker, Entity target, ItemStack stack, int enchantmentLevel) {}
    public void doPostHurt(LivingEntity user, Entity attacker, ItemStack stack, int enchantmentLevel) {}
    public void doPostProjectileAttack(LivingEntity shooter, Entity target,  ItemStack stack, int enchantmentLevel) {}
    public void doPostProjectileHurt(LivingEntity target, Entity shooter, ItemStack stack, int enchantmentLevel) {}
    public void tick(LivingEntity entity, ItemStack stack, int enchantmentLevel) {}
    public void playerTick(Player user, ItemStack stack, int enchantmentLevel) {}
    public boolean use(Player user, ItemStack stack, int level) {return false;}
}
