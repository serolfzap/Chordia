package net.vairosh.chordia.symphony;

import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.vairosh.chordia.registry.CDBuiltInRegistries;

import java.util.*;
import java.util.function.BiConsumer;

public class SymphonyHelper {
    private static final String TAG_SYMPHONIES = "Symphonies";
    private static final String TAG_ACOUSTIC_WEAR = "AcousticWear";

    public static Set<Symphony> getSymphonies(ItemStack stack) {
        if (!stack.hasTag()) return Collections.emptySet();

        ListTag list = stack.getTag().getList(TAG_SYMPHONIES, ListTag.TAG_COMPOUND);
        if (list.isEmpty()) return Collections.emptySet();

        return deserializeSymphonies(list);
    }

    private static Set<Symphony> deserializeSymphonies(ListTag list) {
        Set<Symphony> set = new LinkedHashSet<>();

        for (int i = 0; i < list.size(); i++) {
            CompoundTag tag = list.getCompound(i);
            ResourceLocation id = ResourceLocation.tryParse(tag.getString("id"));

            if (id != null) {
                Symphony symphony = CDBuiltInRegistries.SYMPHONY.get(id);
                if (symphony != null) set.add(symphony);
            }
        }

        return set;
    }

    public static void applySymphonies(ItemStack stack, BiConsumer<Symphony, Integer> consumer) {
        CompoundTag tag = stack.getOrCreateTag().getCompound("ChordiaGhostLink");

        if (hasAcousticWear(stack) || tag.getBoolean("Active")) {
            for (Symphony symphony : getSymphonies(stack)) {
                int level = getEnchantmentLevel(symphony, stack);
                if (level > 0) consumer.accept(symphony, level);
            }
        }
    }

    public static boolean canApplySymphony(ItemStack stack, Symphony newSymphony) {
        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);

        List<Enchantment> required = newSymphony.getRequiredEnchantments();
        for (Enchantment enchantment : required) {
            if (!enchantments.containsKey(enchantment)) {
                return false;
            }
        }

        for (Symphony symphony : getSymphonies(stack)) {
            if (!newSymphony.isCompatibleWith(symphony)) {
                return false;
            }
        }

        return true;
    }

    public static void setSymphony(ItemStack stack, Holder<Symphony> symphonyHolder, boolean acousticWear) {
        Symphony symphony = symphonyHolder.value();
        CompoundTag tag = stack.getOrCreateTag();
        ListTag list = tag.contains(TAG_SYMPHONIES) ? tag.getList(TAG_SYMPHONIES, 10) : new ListTag();

        ResourceLocation symphonyId = CDBuiltInRegistries.SYMPHONY.getKey(symphony);
        if (symphonyId == null) return;

        CompoundTag symphonyTag = new CompoundTag();
        symphonyTag.putString("id", symphonyId.toString());
        list.add(symphonyTag);

        tag.put(TAG_SYMPHONIES, list);

        if (acousticWear){
            setAcousticWear(stack, 250);
        }
    }

    public static void removeSymphonies(ItemStack stack) {
        if (stack.hasTag()) {
            stack.getTag().remove(TAG_SYMPHONIES);
        }
    }


    public static int getEnchantmentLevel(Symphony symphony, ItemStack stack) {
        List<Enchantment> required = symphony.getRequiredEnchantments();
        if (required.isEmpty()) return 0;

        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(stack);
        /*if (required.size() > 1) {
            return getEnchantmentHarmonyLevel(required, enchantments);
        }*/

        return getEnchantmentSymphonyLevel(required.get(0), enchantments);
    }

    private static int getEnchantmentSymphonyLevel (Enchantment required, Map<Enchantment, Integer> enchantments) {
        return enchantments.getOrDefault(required, 0);
    }

    private static int getEnchantmentHarmonyLevel (List<Enchantment> required, Map<Enchantment, Integer> enchantments) {
        for (Enchantment enchantment : required) {
            if (!enchantments.containsKey(enchantment)) return 0;
        }

        return 1;
    }

    public static boolean hasSymphony(Symphony symphony, ItemStack stack) {
        return getSymphonies(stack).contains(symphony);
    }

    public static boolean hasSymphony(LivingEntity entity, Symphony symphony) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = entity.getItemBySlot(slot);
            if (hasSymphony(symphony, stack)) return true;
        }
        return false;
    }

    public static int getAcousticWear(ItemStack stack) {
        if (!stack.hasTag()) return 0;
        return stack.getTag().getInt(TAG_ACOUSTIC_WEAR);
    }

    public static void setAcousticWear(ItemStack stack, int value) {
        CompoundTag tag = stack.getOrCreateTag();
        tag.putInt(TAG_ACOUSTIC_WEAR, Math.max(0, value));
    }

    public static void decreaseAcousticWear(ItemStack stack, int amount) {
        if (amount <= 0 || !stack.hasTag()) return;

        int currentWear = getAcousticWear(stack);
        int newWear = Math.max(0, currentWear - amount);
        setAcousticWear(stack, newWear);
    }

    public static boolean hasAcousticWear(ItemStack stack) {
        return getAcousticWear(stack) > 0;
    }
}
