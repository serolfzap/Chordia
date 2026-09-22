package net.vairosh.chordia.event;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.Dynamic2CommandExceptionType;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.commands.arguments.ResourceArgument;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.vairosh.chordia.registry.CDRegistries;
import net.vairosh.chordia.symphony.Symphony;
import net.vairosh.chordia.symphony.SymphonyHelper;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class CDCommandRegistrationCallback implements CommandRegistrationCallback {
    private static final DynamicCommandExceptionType ERROR_NOT_LIVING_ENTITY = new DynamicCommandExceptionType(object -> Component.translatable("commands.symphony.failed.entity", object));
    private static final DynamicCommandExceptionType ERROR_NO_ITEM = new DynamicCommandExceptionType(object -> Component.translatable("commands.symphony.failed.itemless", object));
    private static final DynamicCommandExceptionType ERROR_INCOMPATIBLE = new DynamicCommandExceptionType(object -> Component.translatable("commands.symphony.failed.incompatible", object));
    private static final SimpleCommandExceptionType ERROR_NOTHING_HAPPENED = new SimpleCommandExceptionType(Component.translatable("commands.symphony.failed"));
    private static final Dynamic2CommandExceptionType ERROR_NOT_REQUIRED_ENCHANTMENT = new Dynamic2CommandExceptionType((object, object2) -> Component.translatable("commands.symphony.failed.required", object, object2));
    private static final DynamicCommandExceptionType ERROR_NO_SYMPHONY = new DynamicCommandExceptionType(object -> Component.translatable("commands.symphony.failed.empty", object));

    @Override
    public void register(CommandDispatcher<CommandSourceStack> commandDispatcher, CommandBuildContext commandBuildContext, Commands.CommandSelection commandSelection) {
        commandDispatcher.register(
                Commands.literal("symphony")
                        .requires(commandSourceStack -> commandSourceStack.hasPermission(2))
                        .then(Commands.argument("targets", EntityArgument.entities())
                                .then(Commands.literal("apply")
                                        .then(Commands.argument("symphony", ResourceArgument.resource(commandBuildContext, CDRegistries.SYMPHONY))
                                                .executes(
                                                        commandContext -> applySymphony(
                                                                commandContext.getSource(), EntityArgument.getEntities(commandContext, "targets"), getSymphony(commandContext, "symphony")
                                                        )
                                                )
                                               ))
                                .then(Commands.literal("remove")
                                        .executes(context ->
                                                removeSymphony(
                                                        context.getSource(), EntityArgument.getEntities(context, "targets"))))


                        )
        );
    }

    private static int applySymphony(CommandSourceStack commandSourceStack, Collection<? extends Entity> collection, Holder<Symphony> holder) throws CommandSyntaxException {
        Symphony symphony = holder.value();
        List<Enchantment> required = symphony.getRequiredEnchantments();
        int j = 0;

        for (Entity entity : collection) {
            if (entity instanceof LivingEntity livingEntity) {
                ItemStack itemStack = livingEntity.getMainHandItem();
                if (!itemStack.isEmpty()) {
                    if (SymphonyHelper.canApplySymphony(itemStack, symphony)) {
                        SymphonyHelper.setSymphony(itemStack, holder, true);
                        j++;
                    } else if (collection.size() == 1) {
                        Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(itemStack);
                        for (Enchantment enchantment : required) {
                            if (!enchantments.containsKey(enchantment)) {
                                int level = EnchantmentHelper.getItemEnchantmentLevel(enchantment, itemStack);
                                throw ERROR_NOT_REQUIRED_ENCHANTMENT.create(itemStack.getItem().getName(itemStack).getString(), enchantment.getFullname(level).getString());
                            }
                        }

                        throw ERROR_INCOMPATIBLE.create(symphony.getFullname().getString());
                    }
                } else if (collection.size() == 1) {
                    throw ERROR_NO_ITEM.create(livingEntity.getName().getString());
                }
            } else if (collection.size() == 1) {
                throw ERROR_NOT_LIVING_ENTITY.create(entity.getName().getString());
            }
        }

        if (j == 0) {
            throw ERROR_NOTHING_HAPPENED.create();
        }

        if (collection.size() == 1) {
            commandSourceStack.sendSuccess(
                    () -> Component.translatable("commands.symphony.success.single", symphony.getFullname(), collection.iterator().next().getDisplayName()), true
            );
        } else {
            commandSourceStack.sendSuccess(() -> Component.translatable("commands.symphony.success.multiple", symphony.getFullname(), collection.size()), true);
        }

        return j;
    }

    private static int removeSymphony(CommandSourceStack commandSourceStack, Collection<? extends Entity> collection) throws CommandSyntaxException {
        int j = 0;

        for (Entity entity : collection) {
            if (entity instanceof LivingEntity livingEntity) {
                ItemStack itemStack = livingEntity.getMainHandItem();
                if (!itemStack.isEmpty()) {
                    if (!SymphonyHelper.getSymphonies(itemStack).isEmpty()) {
                        SymphonyHelper.removeSymphonies(itemStack);
                        j++;
                    } else if (collection.size() == 1) {
                        throw ERROR_NO_SYMPHONY.create(itemStack.getItem().getName(itemStack).getString());
                    }
                } else if (collection.size() == 1) {
                    throw ERROR_NO_ITEM.create(livingEntity.getName().getString());
                }
            } else if (collection.size() == 1) {
                throw ERROR_NOT_LIVING_ENTITY.create(entity.getName().getString());
            }
        }

        if (j == 0) {
            throw ERROR_NOTHING_HAPPENED.create();
        }

        if (collection.size() == 1) {
            commandSourceStack.sendSuccess(
                    () -> Component.translatable("commands.symphony.remove.single", collection.iterator().next().getDisplayName()), true
            );
        } else {
            commandSourceStack.sendSuccess(() -> Component.translatable("commands.symphony.remove.multiple", collection.size()), true);
        }

        return j;
    }

    public static Holder.Reference<Symphony> getSymphony(CommandContext<CommandSourceStack> commandContext, String string) throws CommandSyntaxException {
        return ResourceArgument.getResource(commandContext, string, CDRegistries.SYMPHONY);
    }
}
