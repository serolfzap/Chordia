package net.vairosh.chordia.mixin;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.CrossbowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.vairosh.chordia.symphony.Symphony;
import net.vairosh.chordia.symphony.SymphonyHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.HashSet;
import java.util.Set;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Unique
    private static final EquipmentSlot[] HAND_SLOTS = new EquipmentSlot[]{EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND};

    @Inject(method = "hurt", at = @At(value = "RETURN"))
    private void postHurt(DamageSource damageSource, float f, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) return;

        LivingEntity victim = (LivingEntity) (Object) this;

        Entity directEntity = damageSource.getDirectEntity();
        if (directEntity instanceof AbstractArrow abstractArrow) {
            Entity shooterEntity = abstractArrow.getOwner();
            if (shooterEntity instanceof LivingEntity shooter) {
                if (directEntity instanceof ThrownTrident trident) {
                    ItemStack tridentStack = ((ThrownTridentAccessor) trident).getTridentItem();
                    if (!tridentStack.isEmpty()) {
                        SymphonyHelper.applySymphonies(tridentStack, (symphony, level) -> {
                            symphony.doPostProjectileAttack(shooter, victim, tridentStack, level);
                        });
                    }
                } else {
                    for (EquipmentSlot slot : HAND_SLOTS) {
                        ItemStack stack = shooter.getItemBySlot(slot);
                        Item item = stack.getItem();
                        if (!(item instanceof BowItem || item instanceof CrossbowItem)) continue;
                        System.out.println("haberrrrrrrrrr attack");
                        SymphonyHelper.applySymphonies(stack, (symphony, level) -> {
                            symphony.doPostProjectileAttack(victim, shooter, stack, level);
                    /*if (attacker instanceof ServerPlayer serverPlayer && isChallengeGhost(stack)) {
                        withActiveChallenge(serverPlayer, (challenge, eye, progress) ->
                                challenge.trackPostAttack(serverPlayer, victim, eye, progress));
                    }*/
                        });
                    }
                }

                for (EquipmentSlot slot : HAND_SLOTS) {
                    ItemStack stack = victim.getItemBySlot(slot);
                    Item item = stack.getItem();
                    if (!(item instanceof BowItem || item instanceof CrossbowItem)) continue;
                    System.out.println("haberrrrrrrrrr hurt");
                    SymphonyHelper.applySymphonies(stack, (symphony, level) -> {
                        symphony.doPostProjectileHurt(victim, shooter, stack, level);
                    /*if (attacker instanceof ServerPlayer serverPlayer && isChallengeGhost(stack)) {
                        withActiveChallenge(serverPlayer, (challenge, eye, progress) ->
                                challenge.trackPostAttack(serverPlayer, victim, eye, progress));
                    }*/
                    });
                }
            }
        } else {
            Entity attackerEntity = damageSource.getEntity();
            if (attackerEntity instanceof LivingEntity attacker) {
                if (directEntity == attacker) {
                    for (EquipmentSlot slot : EquipmentSlot.values()) {
                        ItemStack stack = attacker.getItemBySlot(slot);
                        if (stack.isEmpty()) continue;
                        System.out.println("flipante");
                        SymphonyHelper.applySymphonies(stack, (symphony, level) -> {
                            symphony.doPostAttack(attacker, victim, stack, level);
                    /*if (attacker instanceof ServerPlayer serverPlayer && isChallengeGhost(stack)) {
                        withActiveChallenge(serverPlayer, (challenge, eye, progress) ->
                                challenge.trackPostAttack(serverPlayer, victim, eye, progress));
                    }*/
                        });
                    }

                    for (EquipmentSlot slot : EquipmentSlot.values()) {
                        ItemStack stack = victim.getItemBySlot(slot);
                        if (stack.isEmpty()) continue;
                        System.out.println("ostias");
                        SymphonyHelper.applySymphonies(stack, (symphony, level) -> {
                            symphony.doPostHurt(victim, attacker, stack, level);

               /* if (victim instanceof ServerPlayer serverPlayer && isChallengeGhost(stack)) {
                    withActiveChallenge(serverPlayer, (challenge, eye, progress) ->
                            challenge.trackPostHurt(serverPlayer, attackerEntity, eye, progress));
                }*/
                        });
                    }
                }
            }
        }
    }

    @Inject(method = "tick", at = @At("TAIL"))
    private void tick(CallbackInfo ci) {
        LivingEntity entity = (LivingEntity) (Object) this;
        Set<Symphony> symphonySet = new HashSet<>();

        for (EquipmentSlot slot : EquipmentSlot.values()) {
            ItemStack stack = entity.getItemBySlot(slot);
            if (stack.isEmpty()) continue;
            SymphonyHelper.applySymphonies(stack, (symphony, level) -> {
                if (symphonySet.add(symphony)) {
                    symphony.tick(entity, stack, level);
                }
               /* if (victim instanceof ServerPlayer serverPlayer && isChallengeGhost(stack)) {
                    withActiveChallenge(serverPlayer, (challenge, eye, progress) ->
                            challenge.trackPostHurt(serverPlayer, attackerEntity, eye, progress));
                }*/
            });
        }
    }
}
