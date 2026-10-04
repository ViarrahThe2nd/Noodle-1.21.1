package de.viarrah.noodle.datagen;

import de.viarrah.noodle.NoodleMod;
import de.viarrah.noodle.item.ModItems;
import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, NoodleMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        basicItem(ModItems.NOODLUM.get());
        basicItem(ModItems.COOKED_NOODLUM.get());
        basicItem(ModItems.MAGIC_ASHES.get());
        basicItem(ModItems.MAGIC_FUEL.get());
        basicItem(ModItems.CHISEL.get());
    }
}
