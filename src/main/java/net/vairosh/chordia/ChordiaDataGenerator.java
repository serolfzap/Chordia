package net.vairosh.chordia;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.vairosh.chordia.datagen.CDBlockTagProvider;
import net.vairosh.chordia.datagen.CDDynamicRegistryProvider;
import net.vairosh.chordia.datagen.CDItemTagProvider;
import net.vairosh.chordia.datagen.CDModelProvider;

public class ChordiaDataGenerator implements DataGeneratorEntrypoint {
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

        pack.addProvider(CDDynamicRegistryProvider::new);
        pack.addProvider(CDModelProvider::new);
        pack.addProvider(CDBlockTagProvider::new);
        pack.addProvider(CDItemTagProvider::new);

    }

    @Override
    public void buildRegistry(RegistrySetBuilder registryBuilder) {
        //registryBuilder.add(Registries.CONFIGURED_FEATURE, );
        //registryBuilder.add(Registries.CONFIGURED_FEATURE, );
    }
}
