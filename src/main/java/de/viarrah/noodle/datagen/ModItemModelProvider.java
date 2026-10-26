package de.viarrah.noodle.datagen;

import de.viarrah.noodle.NoodleMod;
import de.viarrah.noodle.block.ModBlocks;
import de.viarrah.noodle.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

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

        // handhelditems

        handheldItem(ModItems.NOODLUM_SWORD.get());
        handheldItem(ModItems.NOODLUM_PICKAXE.get());
        handheldItem(ModItems.NOODLUM_AXE.get());
        handheldItem(ModItems.NOODLUM_SHOVEL.get());
        handheldItem(ModItems.NOODLUM_HOE.get());

        handheldItem(ModItems.NOODLUM_HAMMER.get());

        // Noodlum family, only these 4 because only these aren't cubeAll items, they require specials or textures.
        buttonItem(ModBlocks.NOODLUM_BUTTON, ModBlocks.NOODLUM_BLOCK);
        fenceItem(ModBlocks.NOODLUM_FENCE, ModBlocks.NOODLUM_BLOCK);
        wallItem(ModBlocks.NOODLUM_WALL, ModBlocks.NOODLUM_BLOCK);
        basicItem(ModBlocks.NOODLUM_DOOR.asItem());
    }

    // pass in final block first, then base block to take identity from (same for other thingies)
    public void buttonItem(DeferredBlock<?> block, DeferredBlock<Block> baseBlock) {
        this.withExistingParent(block.getId().getPath(), mcLoc("block/button_inventory"))
                .texture("texture", ResourceLocation.fromNamespaceAndPath(NoodleMod.MOD_ID,
                        "block/" + baseBlock.getId().getPath()));

    }

    public void fenceItem(DeferredBlock<?> block, DeferredBlock<Block> baseBlock) {
        this.withExistingParent(block.getId().getPath(), mcLoc("block/fence_inventory"))
                .texture("texture", ResourceLocation.fromNamespaceAndPath(NoodleMod.MOD_ID,
                        "block/" + baseBlock.getId().getPath()));

    }

    public void wallItem(DeferredBlock<?> block, DeferredBlock<Block> baseBlock) {
        this.withExistingParent(block.getId().getPath(), mcLoc("block/wall_inventory"))
                .texture("wall", ResourceLocation.fromNamespaceAndPath(NoodleMod.MOD_ID,
                        "block/" + baseBlock.getId().getPath()));

    }
}
