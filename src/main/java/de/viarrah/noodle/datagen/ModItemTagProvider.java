package de.viarrah.noodle.datagen;

import de.viarrah.noodle.NoodleMod;
import de.viarrah.noodle.block.ModBlocks;
import de.viarrah.noodle.item.ModItems;
import de.viarrah.noodle.util.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {

    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                              CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, NoodleMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModTags.Items.TRANSFORMABLE_ITEMS)
                .add(Item.byBlock(ModBlocks.NOODLUM_BLOCK.get()))
                .add(Item.byBlock(Blocks.DIRT))
                .add(ModItems.NOODLUM.get())
                .add(Items.ENDER_PEARL);

        tag(ItemTags.SWORDS)
                .add(ModItems.NOODLUM_SWORD.get());
        tag(ItemTags.AXES)
                .add(ModItems.NOODLUM_AXE.get());
        tag(ItemTags.PICKAXES)
                .add(ModItems.NOODLUM_PICKAXE.get())
                .add(ModItems.NOODLUM_HAMMER.get());
        tag(ItemTags.SHOVELS)
                .add(ModItems.NOODLUM_SHOVEL.get());
        tag(ItemTags.HOES)
                .add(ModItems.NOODLUM_HOE.get());
    }
}
