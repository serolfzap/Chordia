package net.vairosh.chordia;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.resources.ResourceLocation;

import net.vairosh.chordia.block.CDBlocks;
import net.vairosh.chordia.block.CDItems;
import net.vairosh.chordia.event.CDCommandRegistrationCallback;
import net.vairosh.chordia.symphony.Symphonies;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Chordia implements ModInitializer {
	public static final String MOD_ID = "chordia";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
        CDBlocks.register();
        CDItems.register();
        Symphonies.register();

        CommandRegistrationCallback.EVENT.register(new CDCommandRegistrationCallback());
    }

	public static ResourceLocation id(String path) {
		return new ResourceLocation(MOD_ID, path);
	}

    public static void info(String info){
        LOGGER.info(info);
    }
}
