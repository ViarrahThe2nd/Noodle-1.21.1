package de.viarrah.noodle.datagen;

import de.viarrah.noodle.NoodleMod;
import de.viarrah.noodle.block.ModBlocks;
import de.viarrah.noodle.block.custom.NoodleLampBlock;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.registries.DeferredBlock;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, NoodleMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(ModBlocks.NOODLUM_BLOCK);
        blockWithItem(ModBlocks.NOODLUM_BOX);

        blockWithItem(ModBlocks.NOODLUM_ORE);
        blockWithItem(ModBlocks.DEEPSLATE_NOODLUM_ORE);

        blockWithItem(ModBlocks.MAGIC_BLOCK);

        // Noodlum family -----------------------------------------------------------------
        stairsBlock(ModBlocks.NOODLUM_STAIRS.get(),
                blockTexture(ModBlocks.NOODLUM_BLOCK.get()));
        blockItem(ModBlocks.NOODLUM_STAIRS); // no .get() (its in the method blockItem)

        slabBlock(ModBlocks.NOODLUM_SLAB.get(),
                blockTexture(ModBlocks.NOODLUM_BLOCK.get()), blockTexture(ModBlocks.NOODLUM_BLOCK.get()));
        blockItem(ModBlocks.NOODLUM_SLAB);

        buttonBlock(ModBlocks.NOODLUM_BUTTON.get(),
                blockTexture(ModBlocks.NOODLUM_BLOCK.get()));

        pressurePlateBlock(ModBlocks.NOODLUM_PRESSURE_PLATE.get(),
                blockTexture(ModBlocks.NOODLUM_BLOCK.get()));
        blockItem(ModBlocks.NOODLUM_PRESSURE_PLATE);

        wallBlock(ModBlocks.NOODLUM_WALL.get(),
                blockTexture(ModBlocks.NOODLUM_BLOCK.get()));

        fenceBlock(ModBlocks.NOODLUM_FENCE.get(),
                blockTexture(ModBlocks.NOODLUM_BLOCK.get()));

        fenceGateBlock(ModBlocks.NOODLUM_FENCE_GATE.get(),
                blockTexture(ModBlocks.NOODLUM_BLOCK.get()));
        blockItem(ModBlocks.NOODLUM_FENCE_GATE);

        doorBlockWithRenderType(ModBlocks.NOODLUM_DOOR.get(), modLoc("block/noodlum_door_bottom"), modLoc("block/noodlum_door_top"), "cutout");

        trapdoorBlockWithRenderType(ModBlocks.NOODLUM_TRAPDOOR.get(),
                modLoc("block/noodlum_trapdoor"), true, "cutout");
        blockItem(ModBlocks.NOODLUM_TRAPDOOR, "_bottom");
        //------------------------------------------------------------------------------------

        // Fog Block
        simpleBlockWithItem(
                ModBlocks.FOG_BLOCK.get(),
                new ModelFile.UncheckedModelFile(modLoc("block/fog_block"))
        );

        noodlumLamp();
    }

    //this is hardcoded, change for more lamps
    private void noodlumLamp() {
        getVariantBuilder(ModBlocks.NOODLUM_LAMP.get()).forAllStates(blockState -> {
            if (blockState.getValue(NoodleLampBlock.ACTIVE)) {
                return new ConfiguredModel[]{new ConfiguredModel(models().cubeAll("noodlum_lamp_on",
                        ResourceLocation.fromNamespaceAndPath(NoodleMod.MOD_ID, "block/" + "noodlum_lamp_on")))};
            } else {
                return new ConfiguredModel[]{new ConfiguredModel(models().cubeAll("noodlum_lamp_off",
                        ResourceLocation.fromNamespaceAndPath(NoodleMod.MOD_ID, "block/" + "noodlum_lamp_off")))};
            }
        });
        simpleBlockItem(ModBlocks.NOODLUM_LAMP.get(), models().cubeAll("noodlum_lamp_on",
                ResourceLocation.fromNamespaceAndPath(NoodleMod.MOD_ID, "block/" + "noodlum_lamp_on")));
    }

    private void blockWithItem(DeferredBlock<?> deferredBlock) {
        simpleBlockWithItem(deferredBlock.get(), cubeAll(deferredBlock.get()));
    }

    private void blockItem(DeferredBlock<?> deferredBlock) {
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile("noodlemod:block/" + deferredBlock.getId().getPath()));
    }

    private void blockItem(DeferredBlock<?> deferredBlock, String appendix) {
        simpleBlockItem(deferredBlock.get(), new ModelFile.UncheckedModelFile("noodlemod:block/" + deferredBlock.getId().getPath() + appendix));
    }
}
