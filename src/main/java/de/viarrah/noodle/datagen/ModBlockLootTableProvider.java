package de.viarrah.noodle.datagen;

import de.viarrah.noodle.block.ModBlocks;
import de.viarrah.noodle.item.ModItems;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.neoforged.fml.common.Mod;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {


    protected ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.NOODLUM_BLOCK.get());
        dropSelf(ModBlocks.NOODLUM_BOX.get());
        // dropSelf(ModBlocks.MAGIC_BLOCK.get()); # .noLootTable is set in ModBlocks class

        add(ModBlocks.NOODLUM_ORE.get(),
                block -> createMultipleOreDrops(ModBlocks.NOODLUM_ORE.get(), ModItems.NOODLUM.get(), 2, 5));

        add(ModBlocks.DEEPSLATE_NOODLUM_ORE.get(),
                block -> createMultipleOreDrops(ModBlocks.DEEPSLATE_NOODLUM_ORE.get(), ModItems.NOODLUM.get(), 3, 6));

        // Noodlum family
        dropSelf(ModBlocks.NOODLUM_STAIRS.get());
        add(ModBlocks.NOODLUM_SLAB.get(),
                block -> createSlabItemTable(ModBlocks.NOODLUM_SLAB.get()));
        dropSelf(ModBlocks.NOODLUM_PRESSURE_PLATE.get());
        dropSelf(ModBlocks.NOODLUM_BUTTON.get());
        dropSelf(ModBlocks.NOODLUM_FENCE.get());
        dropSelf(ModBlocks.NOODLUM_FENCE_GATE.get());
        dropSelf(ModBlocks.NOODLUM_WALL.get());
        dropSelf(ModBlocks.NOODLUM_TRAPDOOR.get());
        add(ModBlocks.NOODLUM_DOOR.get(),
                block -> createDoorTable(ModBlocks.NOODLUM_DOOR.get()));
    }

    // Multiple Ore Drops                               ST Drop         ItemDrop    Min             Max
    protected LootTable.Builder createMultipleOreDrops(Block pBlock, Item item, float minDrops, float maxDrops) {
        HolderLookup.RegistryLookup<Enchantment> registryLookup = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createSilkTouchDispatchTable(pBlock,
                this.applyExplosionDecay(pBlock, LootItem.lootTableItem(item)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(minDrops, maxDrops)))
                        .apply(ApplyBonusCount.addOreBonusCount(registryLookup.getOrThrow(Enchantments.FORTUNE)))));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}
