package de.viarrah.noodle.datagen;

import de.viarrah.noodle.NoodleMod;
import de.viarrah.noodle.block.ModBlocks;
import de.viarrah.noodle.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagProvider extends BlockTagsProvider {
    public ModBlockTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, NoodleMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {

        //AXE/PICKAXE
        tag(BlockTags.MINEABLE_WITH_AXE)
                .add(ModBlocks.MAGIC_BLOCK.get())
                .add(ModBlocks.NOODLUM_BLOCK.get())
                .add(ModBlocks.NOODLUM_BOX.get())

                .add(ModBlocks.NOODLUM_DOOR.get())
                .add(ModBlocks.NOODLUM_TRAPDOOR.get())
                .add(ModBlocks.NOODLUM_FENCE.get())
                .add(ModBlocks.NOODLUM_FENCE_GATE.get())
                .add(ModBlocks.NOODLUM_WALL.get())
                .add(ModBlocks.NOODLUM_PRESSURE_PLATE.get())
                .add(ModBlocks.NOODLUM_BUTTON.get())
                .add(ModBlocks.NOODLUM_SLAB.get())
                .add(ModBlocks.NOODLUM_STAIRS.get());


        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.DEEPSLATE_NOODLUM_ORE.get())
                .add(ModBlocks.NOODLUM_ORE.get());

        //CONNECTING BLOCKS
        tag(BlockTags.FENCES)
                .add(ModBlocks.NOODLUM_FENCE.get());

        tag(BlockTags.FENCE_GATES)
                .add(ModBlocks.NOODLUM_FENCE_GATE.get());

        tag(BlockTags.WALLS)
                .add(ModBlocks.NOODLUM_WALL.get());

        //TOOLTIER
        tag(BlockTags.NEEDS_STONE_TOOL)
                .add(ModBlocks.NOODLUM_ORE.get());

        tag(BlockTags.NEEDS_IRON_TOOL)
                .add(ModBlocks.DEEPSLATE_NOODLUM_ORE.get());

        tag(BlockTags.NEEDS_DIAMOND_TOOL)
                .add(ModBlocks.MAGIC_BLOCK.get());

        tag(ModTags.Blocks.NEEDS_NOODLUM_TOOL)
                .add(ModBlocks.NOODLUM_LAMP.get())
                .addTag(BlockTags.NEEDS_IRON_TOOL);

        tag(ModTags.Blocks.INCORRECT_FOR_NOODLUM_TOOL)
                .addTag(BlockTags.INCORRECT_FOR_IRON_TOOL)
                .remove(ModTags.Blocks.NEEDS_NOODLUM_TOOL);

    }
}
